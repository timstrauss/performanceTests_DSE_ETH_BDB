package connectionDetails

import com.datastax.oss.driver.api.core.CqlSession
import com.datastax.oss.driver.api.core.cql.SimpleStatement
import genericTests.TestInfo
import issuer.CassandraIssuer

object CassandraUtils {
    val statemenBuilder = SimpleStatement.builder("")

    fun createNewUser(username: String, password: String, replicationFactor: Int = 1) {
        var con = CassandraConnectionDetails(TestInfo.nodeHost, 9042,"cassandra", "cassandra")
        con.openSession()
        createUserAndKeySpace(con, username, password, replicationFactor.toLong())
        con.closeSession()
        con = CassandraConnectionDetails(TestInfo.nodeHost, 9042,username, password)
        con.openSession()
        createInitialTables(con, username)
        con.closeSession()
    }

    private fun createInitialTables(con: CassandraConnectionDetails, username: String) {
        val spaceName = "\"" + getSpaceNameForUser(username) + "\""
        val session = con.session()

        buildAndExecuteStatement(session, "CREATE TABLE IF NOT EXISTS $spaceName.${CassandraIssuer.TableNames.ISSUERS_TABLE.tableName} (id text, domain text, sslpkey text, signed_address text, PRIMARY KEY(id));")
        buildAndExecuteStatement(session, "CREATE TABLE IF NOT EXISTS $spaceName.${CassandraIssuer.TableNames.ISSUER_KEYS_TABLE.tableName} (issuer_id text, key text, valid_to timestamp, valid_from timestamp, name text, PRIMARY KEY(issuer_id, key));")
        buildAndExecuteStatement(session, "CREATE TABLE IF NOT EXISTS $spaceName.${CassandraIssuer.TableNames.ISSUER_CUSTOM_REVOCATIONS.tableName} (issuer_id text, code_id int, code text, PRIMARY KEY(issuer_id, code_id));")
        buildAndExecuteStatement(session, "CREATE TABLE IF NOT EXISTS $spaceName.${CassandraIssuer.TableNames.ISSUER_REVOKED_CREDENTIAL.tableName} (issuer_id text, credential_hash text, code_id int, PRIMARY KEY(issuer_id, credential_hash, code_id));")
    }

    private fun createUserAndKeySpace(con: CassandraConnectionDetails,
                                      username: String,
                                      password: String,
                                      replicationFactor: Long) {
        val session = con.session()

        val spaceName = "\"" + getSpaceNameForUser(username) + "\""

        buildAndExecuteStatement(session, "CREATE KEYSPACE IF NOT EXISTS $spaceName WITH replication = {'class':'SimpleStrategy', 'replication_factor' : $replicationFactor};")
        buildAndExecuteStatement(session, "CREATE ROLE IF NOT EXISTS $username WITH PASSWORD = '$password' AND LOGIN = true;")
        buildAndExecuteStatement(session, "GRANT ALL PERMISSIONS ON KEYSPACE $spaceName TO $username;")
        buildAndExecuteStatement(session, "GRANT MODIFY ON KEYSPACE $spaceName TO $username;")
        buildAndExecuteStatement(session, "GRANT AUTHORIZE ON KEYSPACE $spaceName TO $username;")
        buildAndExecuteStatement(session, "GRANT CREATE ON ALL ROLES TO $username;")
    }

    private fun buildAndExecuteStatement(session: CqlSession, statement: String) {
        statemenBuilder.setQuery(statement)
        try {
            session.execute(statemenBuilder.build())
        } catch (e: Exception) {
            System.err.println("Error on statement: $statement")
            throw e
        }
    }

    fun getSpaceNameForUser(username: String): String {
        return username + "_space"
    }
}