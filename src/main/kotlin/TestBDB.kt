@file:JvmName("BDBTest")

import com.bigchaindb.model.FulFill
import com.bigchaindb.model.MetaData
import com.google.common.hash.Hashing
import connectionDetails.BigchainDBConnectionDetails
import connectionDetails.CassandraConnectionDetails
import issuer.AbstractIssuer
import issuer.BigchainDBIssuer
import java.util.*

fun main() {
    val con = BigchainDBConnectionDetails("http://127.0.0.1:9984")
    val sKey = CassandraConnectionDetails::class.java.classLoader.getResource("key.der").readBytes()
    val pKey = CassandraConnectionDetails::class.java.classLoader.getResource("pubkey.der").readBytes()
    val key2 = AbstractIssuer.Key(
        Hashing.sha256().hashBytes(CassandraConnectionDetails::class.java.classLoader.getResource("anotherPubKey.der").readBytes()).asBytes(),
        "key2",
        Date(1292108400000L),
        Date(1607727600000L)
    )
    val key3 = AbstractIssuer.Key(
        Hashing.sha256().hashBytes(CassandraConnectionDetails::class.java.classLoader.getResource("evilpubkey.der").readBytes()).asBytes(),
        "key3",
        Date(1292108400000L),
        Date(1607727600000L)
    )
        val issuer = BigchainDBIssuer(
            con,
            "Test.domain",
            sKey,
            pKey,
            listOf(key2, key3)
        )
    for (i in 0 until 100) {
        issuer.domain = "a$i"
    }
}