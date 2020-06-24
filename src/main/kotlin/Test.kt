@file:JvmName("DSETest")

import com.google.common.hash.Hashing
import connectionDetails.CassandraConnectionDetails
import issuer.AbstractIssuer
import issuer.CassandraIssuer
import java.util.*
import kotlin.concurrent.thread

fun main() {
    val con = CassandraConnectionDetails("localhost", 9042, "tim", "abc")
    con.openSession()
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
    val issuer = CassandraIssuer(
        con,
        "Test.domain",
        sKey,
        pKey,
        listOf(key2, key3)
    )
    var start = System.currentTimeMillis()
    for (i in 0 until 100) {
        issuer.domain = "a$i"
    }
    var end = System.currentTimeMillis()
    println(end - start)
    con.closeSession()
}