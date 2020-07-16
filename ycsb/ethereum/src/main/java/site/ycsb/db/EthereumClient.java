package site.ycsb.db;

import com.google.gson.Gson;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.ECKeyPair;
import org.web3j.crypto.Keys;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.RawTransactionManager;
import site.ycsb.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.ReentrantLock;

public class EthereumClient extends DB {
  private Web3j client;
  private Credentials credentials;
  private EthereumContractGasProvider gasProvider = new EthereumContractGasProvider();
  private static Map<String, String> keyMapping;
  private static Map<String, EthereumYCSB> contractCache = new HashMap<>();
  private Gson gson = new Gson();
  private File mappingFile;
  private static final boolean retry = false;

  AtomicBoolean doOnce = new AtomicBoolean(false);

  @Override
  public void init() throws DBException {
    try {
      mappingFile = new File("./keymapping");
      if (!mappingFile.exists()) {
        if (!doOnce.getAndSet(true)) {
          mappingFile.createNewFile();
          FileWriter fw = new FileWriter(mappingFile.getPath());
          fw.write(gson.toJson(new HashMap<String, String>()));
          fw.close();
          keyMapping = new ConcurrentHashMap<>();
        }
      } else {
        if (!doOnce.getAndSet(true)) {
          StringBuilder fileData = new StringBuilder();
          Scanner myReader = new Scanner(mappingFile);
          while (myReader.hasNextLine()) {
            String data = myReader.nextLine();
            fileData.append(data);
          }
          myReader.close();
          keyMapping = gson.fromJson(fileData.toString(), ConcurrentHashMap.class);
        }
      }

      client = Web3j.build(new HttpService("http://172.20.8.223:8545"));
      ECKeyPair keyPair = Keys.createEcKeyPair();
      credentials = Credentials.create(keyPair);
    } catch (Exception e) {
      e.printStackTrace();
      throw new DBException();
    }
  }

  private synchronized void addKey(String key, String id) {
    try {
      keyMapping.put(key, id);
      FileWriter fw = new FileWriter(mappingFile.getPath());
      fw.write(gson.toJson(keyMapping));
      fw.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  public Status read(String table, String key, Set<String> fields, Map<String, ByteIterator> result) {
    Status resultSuccess = null;
    while (resultSuccess == null || (resultSuccess != Status.OK && retry)) {
      try {
        String address = keyMapping.get(key);
        EthereumYCSB contract;
        if (!contractCache.containsKey(address)) {
          contract = EthereumYCSB.load(address, client, new RawTransactionManager(client, credentials, 1515L, 150, 100), gasProvider);
          contractCache.put(address, contract);
        } else {
          contract = contractCache.get(address);
        }
        if (fields == null) {
          List<String> names = contract.getFieldNames().send();
          names.remove(0);
          List<String> values = contract.getField().send();
          values.remove(0);
          for (int i = 0; i < names.size(); i++) {
            result.put(names.get(i), new StringByteIterator(values.get(i)));
          }
        } else {
          List<String> names = new ArrayList<>();
          for (String field : fields) {
            names.add(field);
          }
          List<String> values = contract.getField(names).send();
          for (int i = 0; i < names.size(); i++) {
            result.put(names.get(i), new StringByteIterator(values.get(i)));
          }
        }
        resultSuccess = Status.OK;
      } catch (Exception e) {
        resultSuccess = Status.ERROR;
      }
    }
    return resultSuccess;
  }

  @Override
  public Status scan(String table, String startkey, int recordcount, Set<String> fields, Vector<HashMap<String, ByteIterator>> result) {
    return null;
  }

  @Override
  public Status update(String table, String key, Map<String, ByteIterator> values) {
    Status resultSuccess = null;
    while (resultSuccess == null || (resultSuccess != Status.OK && retry)) {
      try {
        String address = keyMapping.get(key);
        EthereumYCSB contract;
        if (!contractCache.containsKey(address)) {
          contract = EthereumYCSB.load(address, client, new RawTransactionManager(client, credentials, 1515L, 150, 100), gasProvider);
          contractCache.put(address, contract);
        } else {
          contract = contractCache.get(address);
        }
        List<String> fields = new ArrayList<>();
        List<String> fieldValues = new ArrayList<>();
        for (String field : values.keySet()) {
          fields.add(field);
          fieldValues.add(values.get(field).toString());
        }
        contract.setValues(fields, fieldValues).send();
        resultSuccess = Status.OK;
      } catch (Exception e) {
        resultSuccess = Status.ERROR;
      }
    }
    return resultSuccess;
  }

  @Override
  public Status insert(String table, String key, Map<String, ByteIterator> values) {
    List<String> keys = new ArrayList<>();
    List<String> valuesOfKeys =  new ArrayList<>();
    for (String keyInValues : values.keySet()) {
      keys.add(keyInValues);
      valuesOfKeys.add(values.get(keyInValues).toString());
    }
    Status resultSuccess = null;
    while (resultSuccess == null || (resultSuccess != Status.OK && retry)) {
      try {
        EthereumYCSB entity = EthereumYCSB.deploy(client, new RawTransactionManager(client, credentials, 1515L, 150, 100), gasProvider, keys, valuesOfKeys).send();
        addKey(key, entity.getContractAddress());
      } catch (Exception e) {
        resultSuccess = Status.ERROR;
      }
      resultSuccess = Status.OK;
    }
    return resultSuccess;
  }

  @Override
  public Status delete(String table, String key) {
    return null;
  }
}
