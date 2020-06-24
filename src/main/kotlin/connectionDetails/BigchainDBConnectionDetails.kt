package connectionDetails

import com.bigchaindb.api.AssetsApi
import com.bigchaindb.api.TransactionsApi
import com.bigchaindb.builders.BigchainDbConfigBuilder
import com.bigchaindb.builders.BigchainDbTransactionBuilder
import com.bigchaindb.constants.Operations
import com.bigchaindb.model.*
import com.google.gson.internal.LinkedTreeMap
import net.i2p.crypto.eddsa.EdDSAPrivateKey
import net.i2p.crypto.eddsa.EdDSAPublicKey
import net.i2p.crypto.eddsa.KeyPairGenerator
import okhttp3.Response
import java.lang.Exception
import java.security.KeyPair
import java.util.*
import kotlin.concurrent.thread

class BigchainDBConnectionDetails (
    val url: String
){
    val a = "302a300506032b657003210033c43dc2180936a2a9138a05f06c892d2fb1cfda4562cbc35373bf13cd8ed373"
    val b = "302e020100300506032b6570042204206f6b0cd095f1e83fc5f08bffb79c7c8a30e77a3ab65f4bc659026b76394fcea8"
    val keyPair: KeyPair

    init {
        val priv = Account.privateKeyFromHex(b)
        val pub =  Account.publicKeyFromHex(a)
        keyPair = KeyPair(pub, priv)
        BigchainDbConfigBuilder
            .baseUrl(url)
            .addToken("app_id", "2bbaf3ff")
            .addToken("app_key", "c929b708177dcc8b9d58180082029b8d")
            .setup()
    }

    fun <S,T> sendCreateTransaction(assetData: TreeMap<S, T>, metadata: MetaData): String {
        var end = false
        val trans = BigchainDbTransactionBuilder
            .init()
            .addAssets(assetData, TreeMap::class.java)
            .addMetaData(metadata)
            .operation(Operations.CREATE)
            .buildAndSign(keyPair.public as EdDSAPublicKey, keyPair.private as EdDSAPrivateKey)
            .sendTransaction().id
        while (TransactionsApi.getTransactionsByAssetId(trans, Operations.CREATE).transactions.size == 0) {}
        return trans
    }

    fun update(metadata: MetaData, lastTransactionId: String, assetId: String): String {
        val fulFill = FulFill()
        fulFill.outputIndex = 0
        fulFill.transactionId = lastTransactionId
        val previousSize = TransactionsApi.getTransactionsByAssetId(assetId, Operations.TRANSFER).transactions.size
        val tt: String? = null
        val trans = BigchainDbTransactionBuilder
            .init()
            .addMetaData(metadata)
            .addAssets(assetId, String::class.java)
            .addInput(tt, fulFill, keyPair.public as EdDSAPublicKey)
            .addOutput("1", keyPair.public as EdDSAPublicKey)
            .operation(Operations.TRANSFER)
            .buildAndSign(keyPair.public as EdDSAPublicKey, keyPair.private as EdDSAPrivateKey)
            .sendTransaction().id
        while (TransactionsApi.getTransactionsByAssetId(assetId, Operations.TRANSFER).transactions.size == previousSize) {}
        return trans
    }

    fun findLastTransactionForAsset(s: String): String {
        val transferTransactions = TransactionsApi.getTransactionsByAssetId(s, Operations.TRANSFER).transactions
        if (transferTransactions.size == 0) {
            return s
        }
        var id = TransactionsApi.getTransactionsByAssetId(s, Operations.TRANSFER).transactions.last().id
        if (id.startsWith("\"") && id.endsWith("\"")) {
            id = id.substring(1, id.length-1)
        }
        return id
    }

    fun getLatestMetadata(assetId: String): LinkedTreeMap<String, String> {
        val transferTransactions = TransactionsApi.getTransactionsByAssetId(assetId, Operations.TRANSFER).transactions
        if (transferTransactions.size == 0) {
            return TransactionsApi.getTransactionsByAssetId(assetId, Operations.CREATE).transactions[0].metaData as LinkedTreeMap<String, String>
        }
        return transferTransactions[0].metaData as LinkedTreeMap<String, String>
    }

    fun getAssetId(uuid: String): String {
        val assets = AssetsApi.getAssetsWithLimit(uuid, "1")
        return assets.assets[0].id
    }
}
