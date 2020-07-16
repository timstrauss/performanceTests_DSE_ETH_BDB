package site.ycsb.db;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.ColumnDefinition;
import com.datastax.oss.driver.api.core.cql.Row;
import site.ycsb.*;

import java.net.InetSocketAddress;
import java.sql.ResultSet;
import java.util.*;

import static java.lang.Thread.sleep;

public class OwnCassandraClient extends DB {

  private CqlSession session;
  private static final boolean retry = false;

  @Override
  public void init() throws DBException {
    try {
      session = CqlSession.builder()
          .addContactPoint(new InetSocketAddress("172.20.8.223", 9042))
          .withAuthCredentials("cassandra", "cassandra")
          .withLocalDatacenter("Cassandra")
          .build();
      session.execute("CREATE KEYSPACE IF NOT EXISTS testspace WITH replication = {'class':'SimpleStrategy', 'replication_factor' : 2};");
      session.execute("CREATE TABLE IF NOT EXISTS testspace.test(field0 text, field1 text, field2 text, field3 text, field4 text, field5 text, field6 text, field7 text, field8 text, field9 text, key text PRIMARY KEY);");
      sleep(1000);
    } catch (Exception e) {}
  }

  @Override
  public Status read(String table, String key, Set<String> fields, Map<String, ByteIterator> result) {
    StringBuilder statement = new StringBuilder("SELECT ");
    if (fields == null) {
      statement.append("*");
    } else {
      for (String property : fields) {
        statement.append(property).append(",");
      }
      statement.deleteCharAt(statement.length() - 1);
    }
    statement.append(" FROM testspace.test WHERE key = '").append(key).append("';");
    Status resultSuccess = null;
    while (resultSuccess == null || (resultSuccess != Status.OK && retry)) {
      try {
        List<Row> rs = session.execute(statement.toString()).all();
        for (Row r : rs) {
          for (ColumnDefinition column : r.getColumnDefinitions()) {
            result.put(column.getName().toString(), new StringByteIterator(r.getString(column.getName().toString())));
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
    StringBuilder statement = new StringBuilder("UPDATE testspace.test SET ");
    for (String property : values.keySet()) {
      statement.append(property).append(" = ").append("'").append(values.get(property).toString().replace("'", "''")).append("',");
    }
    statement.deleteCharAt(statement.length() - 1);
    statement.append(" WHERE key = '").append(key).append("';");
    Status resultSuccess = null;
    while (resultSuccess == null || (resultSuccess != Status.OK && retry)) {
      try {
        session.execute(statement.toString());
        resultSuccess = Status.OK;
      } catch (Exception e) {
        resultSuccess = Status.ERROR;
      }
    }
    return resultSuccess;
  }

  @Override
  public Status insert(String table, String key, Map<String, ByteIterator> values) {
    StringBuilder statement = new StringBuilder("INSERT INTO testspace.test ( ");
    StringBuilder valuesString = new StringBuilder();
    for (String property : values.keySet()) {
      statement.append(property).append(",");
      valuesString.append("'" + values.get(property).toString().replace("'", "''") + "'").append(",");
    }
    statement.append("key) VALUES (").append(valuesString.toString()).append("'" + key + "');");
    Status resultSuccess = null;
    while (resultSuccess == null || (resultSuccess != Status.OK && retry)) {
      try {
        session.execute(statement.toString());
        resultSuccess = Status.OK;
      } catch (Exception e) {
        resultSuccess = Status.ERROR;
      }
    }
    return resultSuccess;
  }

  @Override
  public Status delete(String table, String key) {
    return null;
  }
}
