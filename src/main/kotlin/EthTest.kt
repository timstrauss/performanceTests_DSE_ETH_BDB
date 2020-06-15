@file:JvmName("EthTest")

import com.google.common.hash.Hashing
import connectionDetails.CassandraConnectionDetails
import connectionDetails.EthereumConnectionDetails
import issuer.AbstractIssuer
import issuer.EthIssuer
import java.util.*
import kotlin.concurrent.thread

fun main() {
    val con = EthereumConnectionDetails("http://localhost:8545")
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
    val issuer = EthIssuer(
        con,
        "Test.domain",
        sKey,
        pKey,
        listOf(key2, key3)
    )
    val start = System.currentTimeMillis()
    var amount = IntArray(10) { 0 }
    for (i in 0 until 10) {
        thread(true) {
            val index = i
            while (System.currentTimeMillis() - start < 40000) {
                issuer.domain
                amount[index]++
            }
        }
    }
    while (System.currentTimeMillis() - start < 42000) {
        issuer.domain
    }
    println(amount.joinToString("|"))
}
