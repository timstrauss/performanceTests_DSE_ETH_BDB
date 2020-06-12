package connectionDetails

import com.datastax.oss.driver.api.core.CqlSession
import com.datastax.oss.driver.api.core.cql.ResultSet
import java.net.InetSocketAddress

class CassandraConnectionDetails(
    val hostName: String,
    val port: Int,
    val username: String,
    val password: String
) {
    var connectionSession: CqlSession? = null

    fun session(): CqlSession {
        if (connectionSession == null) {
            openSession()
        }
        return connectionSession!!
    }

    fun openSession() {
        val builder = CqlSession.builder()

        val contactPoint = InetSocketAddress(hostName, port)
        builder.addContactPoint(contactPoint)
        builder.withAuthCredentials(username, password)
        builder.withLocalDatacenter("Cassandra")

        connectionSession = builder.build()
    }

    fun closeSession() {
        connectionSession?.close()
        connectionSession = null
    }



    internal fun executeSingleStatement(statement: String): ResultSet {
        val session = session()
        return session.execute(statement)
    }
}