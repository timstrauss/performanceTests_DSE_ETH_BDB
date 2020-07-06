package site.ycsb.db;

import com.google.gson.Gson;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.ECKeyPair;
import org.web3j.crypto.Keys;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;
import site.ycsb.*;

import java.io.File;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class EthereumClient extends DB {
  private Web3j client;
  private Credentials credentials;
  private EthereumContractGasProvider gasProvider = new EthereumContractGasProvider();
  private static Map<String, String> keyMapping;
  private static Map<String, EthereumYCSB> contractCache = new HashMap<>();
  private Gson gson = new Gson();
  private File mappingFile;

  @Override
  public void init() throws DBException {
    try {
      mappingFile = new File("./keymapping");
      if (!mappingFile.exists()) {
        mappingFile.createNewFile();
        FileWriter fw = new FileWriter(mappingFile.getPath());
        fw.write(gson.toJson(new HashMap<String, String>()));
        fw.close();
      } else {
        StringBuilder fileData = new StringBuilder();
        Scanner myReader = new Scanner(mappingFile);
        while (myReader.hasNextLine()) {
          String data = myReader.nextLine();
          fileData.append(data);
        }
        myReader.close();
        keyMapping = gson.fromJson(fileData.toString(), HashMap.class);
      }

      client = Web3j.build(new HttpService("http://127.0.0.1:8545"));
      ECKeyPair keyPair = Keys.createEcKeyPair();
      credentials = Credentials.create(keyPair);
    } catch (Exception e) {
      e.printStackTrace();
      throw new DBException();
    }
  }

  private synchronized void addKey(String key, String id) {
    try {
      StringBuilder fileData = new StringBuilder();
      Scanner myReader = new Scanner(mappingFile);
      while (myReader.hasNextLine()) {
        String data = myReader.nextLine();
        fileData.append(data);
      }
      myReader.close();
      keyMapping = gson.fromJson(fileData.toString(), HashMap.class);
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
    try {
      String address = keyMapping.get(key);
      EthereumYCSB contract;
      if (!contractCache.containsKey(address)) {
        contract = EthereumYCSB.load(address, client, credentials, gasProvider);
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
      return Status.OK;
    } catch (Exception e) {
      return Status.ERROR;
    }
  }

  @Override
  public Status scan(String table, String startkey, int recordcount, Set<String> fields, Vector<HashMap<String, ByteIterator>> result) {
    return null;
  }

  @Override
  public Status update(String table, String key, Map<String, ByteIterator> values) {
    try {
      String address = keyMapping.get(key);
      EthereumYCSB contract;
      if (!contractCache.containsKey(address)) {
        contract = EthereumYCSB.load(address, client, credentials, gasProvider);
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
      return Status.OK;
    } catch (Exception e) {
      return Status.ERROR;
    }
  }

  @Override
  public Status insert(String table, String key, Map<String, ByteIterator> values) {
    List<String> keys = new ArrayList<>();
    List<String> valuesOfKeys =  new ArrayList<>();
    for (String keyInValues : values.keySet()) {
      keys.add(keyInValues);
      valuesOfKeys.add(values.get(keyInValues).toString());
    }
    try {
      EthereumYCSB entity = EthereumYCSB.deploy(client, credentials, gasProvider, keys, valuesOfKeys).send();
      addKey(key, entity.getContractAddress());
    } catch (Exception e) {
      return Status.ERROR;
    }
    return Status.OK;
  }

  @Override
  public Status delete(String table, String key) {
    return null;
  }
}
