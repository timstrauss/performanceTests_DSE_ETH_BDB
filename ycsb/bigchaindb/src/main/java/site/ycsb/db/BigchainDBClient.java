package site.ycsb.db;

import com.bigchaindb.builders.BigchainDbConfigBuilder;
import com.bigchaindb.builders.BigchainDbTransactionBuilder;
import com.bigchaindb.constants.BigchainDbApi;
import com.bigchaindb.constants.Operations;
import com.bigchaindb.model.*;
import com.bigchaindb.util.NetworkUtils;
import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import net.i2p.crypto.eddsa.EdDSAPrivateKey;
import net.i2p.crypto.eddsa.EdDSAPublicKey;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.bson.Document;
import org.bson.conversions.Bson;
import site.ycsb.*;

import javax.print.Doc;
import java.io.IOException;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;

import static java.lang.Thread.sleep;

public class BigchainDBClient extends DB {
  protected static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
  private MongoClient mongoClient = new MongoClient("172.20.8.223");
  private static final boolean retry = false;
  private static String priv = "302e020100300506032b6570042204206f6b0cd095f1e83fc5f08bffb79c7c8a30e77a3ab65f4bc659026b76394fcea8";
  private static String pub = "302a300506032b657003210033c43dc2180936a2a9138a05f06c892d2fb1cfda4562cbc35373bf13cd8ed373";
  private KeyPair keyPair;
  MongoDatabase db;
  MongoCollection<Document> assetCollection;
  MongoCollection<Document> transactionsCollection;
  MongoCollection<Document> metadataCollection;
  private static final AtomicInteger INIT_COUNT = new AtomicInteger(0);

  private class CallBackBDB implements GenericCallback {
    Function<Status, Void> function;

    public CallBackBDB( Function<Status, Void> function) {
      this.function = function;
    }

    @Override
    public void pushedSuccessfully(Response response) {
      function.apply(Status.OK);
    }

    @Override
    public void transactionMalformed(Response response) {
      String message = "";
      try {
        message = response.body().string();
      } catch (IOException e) {
        e.printStackTrace();
      }
      Status status = Status.ERROR;
      if (message.contains("Tx already exists in cache")) {
        status = new Status("ALREADY_IN_CACHE", "Tx already exists in cache");
      } else if (message.contains("DoubleSpend")) {
        status = new Status("DOUBLE_SPEND", "Latest id was spend");
      }
      function.apply(status);
    }

    @Override
    public void otherError(Response response) {
      String message = "";
      try {
        message = response.body().string();
      } catch (IOException e) {
        e.printStackTrace();
      }
      Status status = Status.ERROR;
      if (message.contains("Tx already exists in cache")) {
        status = new Status("ALREADY_IN_CACHE", "Tx already exists in cache");
      } else if (message.contains("DoubleSpend")) {
        status = new Status("DOUBLE_SPEND", "Latest id was spend");
      }
      function.apply(status);
    }
  }

  @Override
  public void init() throws DBException {
    INIT_COUNT.incrementAndGet();
    synchronized (INIT_COUNT) {
      try {
        PrivateKey s = Account.privateKeyFromHex(priv);
        PublicKey p = Account.publicKeyFromHex(pub);
        keyPair = new KeyPair(p, s);
        BigchainDbConfigBuilder.baseUrl("http://172.20.8.223:9984").addToken("app_id", "2bbaf3ff")
            .addToken("app_key", "c929b708177dcc8b9d58180082029b8d")
            .setup();
      } catch (Exception e) {
        throw new DBException();
      }
      db =  mongoClient.getDatabase("bigchain");
      assetCollection =  db.getCollection("assets");
      transactionsCollection = db.getCollection("transactions");
      metadataCollection = db.getCollection("metadata");
    }
  }

  @Override
  public Status read(String table, String key, Set<String> fields, Map<String, ByteIterator> result) {
    try {
      ArrayList<Bson> filters = new ArrayList<Bson>();
      MongoCursor<Document> docs;
      if (fields != null) {
        for (String field : fields) {
          filters.add(new BasicDBObject("data.property", field));
        }
        docs = assetCollection.find(
            Filters.and(
                new BasicDBObject("data.key", key),
                Filters.or(
                    filters
                )
            )).iterator();
      } else {
        docs = assetCollection.find(new BasicDBObject("data.key", key)).iterator();
      }
      while (docs.hasNext()) {
        Document doc = docs.next();
        String propertyAssetId = doc.getString("id");
        MongoCursor<Document> latest = transactionsCollection.find(
            new BasicDBObject(
                "asset.id", propertyAssetId
            )
        ).sort(new BasicDBObject("$natural", -1)).limit(1).cursor();
        String latestId;
        if (!latest.hasNext()) {
          latestId = propertyAssetId;
        } else {
          latestId = latest.next().getString("id");
        }
        String value = ((Document) metadataCollection.find(new BasicDBObject("id", latestId)).first().get("metadata")).getString("value");
        result.put(((Document) doc.get("data")).getString("property"), new StringByteIterator(value));
      }

      return Status.OK;
    } catch (Exception e) {
      e.printStackTrace();
      return Status.ERROR;
    }
  }

  @Override
  public Status scan(String table, String startkey, int recordcount, Set<String> fields, Vector<HashMap<String, ByteIterator>> result) {
    return Status.OK;
  }

  @Override
  public Status update(String table, String key, Map<String, ByteIterator> values) {
    try {
      ArrayList<Bson> filters = new ArrayList<Bson>();
      for (String field : values.keySet()) {
        filters.add(new BasicDBObject("data.property", field));
      }
      MongoCursor<Document> docs = assetCollection.find(
          Filters.and(
              new BasicDBObject("data.key", key),
              Filters.or(
                  filters
              )
          )).iterator();
      List<WriteThread> threads = new ArrayList<>();
      while (docs.hasNext()) {
        Document doc = docs.next();

        WriteThread thread = new WriteThread(key, ((Document )doc.get("data")).getString("property"), values.get(((Document )doc.get("data")).getString("property")).toString(), false, retry, doc.getString("id"));
        thread.start();

        threads.add(thread);
      }
      for (WriteThread thread : threads) {
        thread.join();
        if (thread.getStatus() != Status.OK) {
          return thread.getStatus();
        }
      }
      return Status.OK;
    } catch (Exception e) {
      return Status.ERROR;
    }
  }

  @Override
  public Status insert(String table, String key, Map<String, ByteIterator> values) {
    try {
      List<WriteThread> threads = new ArrayList<>();
      for (String property : values.keySet()) {
        WriteThread thread = new WriteThread(key, property, values.get(property).toString(), true, retry);
        thread.start();
        threads.add(thread);
      }
      for (WriteThread thread : threads) {
        thread.join();
        if (thread.getStatus() != Status.OK) {
          return thread.getStatus();
        }
      }
      return Status.OK;
    } catch (Exception e) {
      e.printStackTrace();
      return Status.ERROR;
    }
  }

  @Override
  public Status delete(String table, String key) {
    return null;
  }

  class WriteThread extends Thread {
    String key;
    String property;
    String value;
    boolean createOrUpdate; //true = create
    boolean retry;
    Status status;
    String propertyAssetId;
    public WriteThread(String key, String property, String value, boolean createOrUpdate, boolean retry) {
      this.key = key;
      this.property = property;
      this.value = value;
      this.createOrUpdate = createOrUpdate;
      this.retry = retry;
    }

    public WriteThread(String key, String property, String value, boolean createOrUpdate, boolean retry, String propertyAssetId) {
      this.key = key;
      this.property = property;
      this.value = value;
      this.createOrUpdate = createOrUpdate;
      this.retry = retry;
      this.propertyAssetId = propertyAssetId;
    }

    public Status getStatus() {
      return status;
    }

    @Override
    public void run() {
      status = Status.ERROR;
      boolean first = true;
      while ((status != Status.OK && retry) || first) {
        first = false;
        try {
          Transaction transaction;
          AtomicReference<Status> wasSuccess = new AtomicReference<>(null);
          if (createOrUpdate) {
            TreeMap<String, String> assetData = new TreeMap<>();
            assetData.put("key", key);
            assetData.put("property", property);
            MetaData metaData = new MetaData();
            metaData.setMetaData("value", value);
            transaction = BigchainDbTransactionBuilder
                .init()
                .addAssets(assetData, TreeMap.class)
                .addMetaData(metaData)
                .operation(Operations.CREATE)
                .buildAndSignOnly((EdDSAPublicKey) Account.publicKeyFromHex(pub), (EdDSAPrivateKey) Account.privateKeyFromHex(priv));
          } else {
            MongoCursor<Document> latest = transactionsCollection.find(
                new BasicDBObject(
                    "asset",
                    new BasicDBObject("id", propertyAssetId)
                )
            ).sort(new BasicDBObject("$natural", -1)).limit(1).cursor();
            String latestId;
            if (!latest.hasNext()) {
              latestId = propertyAssetId;
            } else {
              latestId = latest.next().getString("id");
            }
            String newValue = value;
            FulFill fulFill = new FulFill();
            fulFill.setOutputIndex(0);
            fulFill.setTransactionId(latestId);
            MetaData metaData = new MetaData();
            metaData.setMetaData("value", newValue);
            transaction = BigchainDbTransactionBuilder
                .init()
                .addMetaData(metaData)
                .addAssets(propertyAssetId, String.class)
                .addInput(null, fulFill, (EdDSAPublicKey) Account.publicKeyFromHex(pub))
                .addOutput("1", (EdDSAPublicKey) Account.publicKeyFromHex(pub))
                .operation(Operations.TRANSFER)
                .buildAndSignOnly((EdDSAPublicKey) Account.publicKeyFromHex(pub), (EdDSAPrivateKey) Account.privateKeyFromHex(priv));
          }
          RequestBody body = RequestBody.create(JSON, transaction.toString());
          NetworkUtils.sendPostRequest(BigChainDBGlobals.getBaseUrl() + BigchainDbApi.TRANSACTIONS + "?mode=commit", body, new CallBackBDB(
              (success) -> {
                wasSuccess.set(success);
                return null;
              }
          ));
          while (wasSuccess.get() == null) {
            sleep(0, 1);
          }
          status = wasSuccess.get();
        } catch (Exception e) {
          e.printStackTrace();
          status = Status.ERROR;
        }
      }
    }
  }
}
