package genericTests

import com.bigchaindb.api.TransactionsApi
import com.bigchaindb.builders.BigchainDbTransactionBuilder
import com.bigchaindb.constants.BigchainDbApi
import com.bigchaindb.constants.Operations
import com.bigchaindb.model.BigChainDBGlobals
import com.bigchaindb.model.FulFill
import com.bigchaindb.model.GenericCallback
import com.bigchaindb.model.MetaData
import com.bigchaindb.util.NetworkUtils
import com.mongodb.BasicDBObject
import com.mongodb.MongoClient
import com.mongodb.client.model.Filters
import connectionDetails.BigchainDBConnectionDetails
import net.i2p.crypto.eddsa.EdDSAPrivateKey
import net.i2p.crypto.eddsa.EdDSAPublicKey
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.Response
import org.bson.Document
import java.util.*

fun main() {
    val con = BigchainDBConnectionDetails("http://${TestInfo.nodeHost}:9984")
    val uuid = UUID.randomUUID().toString()
    var assetData = TreeMap<String, String>()
    assetData["uuid"] = uuid
    assetData["property"] = "boolvar"
    var metadata = MetaData()
    metadata.setMetaData("value", "true")
    val id = con.sendCreateTransaction(assetData, metadata)
    val fulFill = FulFill()
    fulFill.outputIndex = 0
    fulFill.transactionId = id
    val tt: String? = null
    metadata = MetaData()
    metadata.setMetaData("value", "false")
    val t1 = BigchainDbTransactionBuilder
        .init()
        .addMetaData(metadata)
        .addAssets(id, String::class.java)
        .addInput(tt, fulFill, con.keyPair.public as EdDSAPublicKey)
        .addOutput("1", con.keyPair.public as EdDSAPublicKey)
        .operation(Operations.TRANSFER)
        .buildAndSignOnly(con.keyPair.public as EdDSAPublicKey, con.keyPair.private as EdDSAPrivateKey)
    metadata = MetaData()
    metadata.setMetaData("value", "false")
    val t2 = BigchainDbTransactionBuilder
        .init()
        .addMetaData(metadata)
        .addAssets(id, String::class.java)
        .addInput(tt, fulFill, con.keyPair.public as EdDSAPublicKey)
        .addOutput("1", con.keyPair.public as EdDSAPublicKey)
        .operation(Operations.TRANSFER)
        .buildAndSignOnly(con.keyPair.public as EdDSAPublicKey, con.keyPair.private as EdDSAPrivateKey)
    metadata = MetaData()
    metadata.setMetaData("value", "true")
    val t3 = BigchainDbTransactionBuilder
        .init()
        .addMetaData(metadata)
        .addAssets(id, String::class.java)
        .addInput(tt, fulFill, con.keyPair.public as EdDSAPublicKey)
        .addOutput("1", con.keyPair.public as EdDSAPublicKey)
        .operation(Operations.TRANSFER)
        .buildAndSignOnly(con.keyPair.public as EdDSAPublicKey, con.keyPair.private as EdDSAPrivateKey)

    var body: RequestBody
    body = RequestBody.create("application/json; charset=utf-8".toMediaTypeOrNull(), t3.toString())
    NetworkUtils.sendPostRequest(BigChainDBGlobals.getBaseUrl() + BigchainDbApi.TRANSACTIONS + "?mode=async", body, TestCallBack("t3") {})
    Thread.sleep(100)
    body = RequestBody.create("application/json; charset=utf-8".toMediaTypeOrNull(), t1.toString())
    NetworkUtils.sendPostRequest(BigChainDBGlobals.getBaseUrl() + BigchainDbApi.TRANSACTIONS + "?mode=async", body, TestCallBack("t1") {})
    Thread.sleep(10)
    body = RequestBody.create("application/json; charset=utf-8".toMediaTypeOrNull(), t2.toString())
    NetworkUtils.sendPostRequest(BigChainDBGlobals.getBaseUrl() + BigchainDbApi.TRANSACTIONS + "?mode=async", body, TestCallBack("t2") {})


    Thread.sleep(1000)

    val mongoClient: MongoClient = MongoClient(TestInfo.nodeHost)
    val db = mongoClient.getDatabase("bigchain")
    val assetCollection = db.getCollection("assets")
    val transactionsCollection = db.getCollection("transactions")
    val metadataCollection = db.getCollection("metadata")
    val assetId = assetCollection.find(
        Filters.and(
            BasicDBObject("data", BasicDBObject("property", "boolvar")),
            BasicDBObject("data", BasicDBObject("uuid", uuid))
        )
    ).limit(1).first()?.getString("id") ?: return
    val metadataId = transactionsCollection.find(
        BasicDBObject(
            "asset",
            BasicDBObject("id", assetId)
        )
    ).sort(BasicDBObject("\$natural", -1)).limit(1).first()?.getString("id") ?: return
    val metadataValue = (metadataCollection.find(
        BasicDBObject(
            "id",
            metadataId
        )
    ).limit(1).first()?.get("metadata") as Document?)?.getString("value") ?: return
    println(metadataValue)
}

class TestCallBack(val name: String, val success: (value: Boolean) -> Unit) : GenericCallback {
    override fun pushedSuccessfully(response: Response?) {
        println(name + " success")
        success(true)
    }

    override fun transactionMalformed(response: Response?) {
        println(name + "ERROR " + response?.body?.string())
        success(false)
    }

    override fun otherError(response: Response?) {
        println(name + "ERROR " + response?.body?.string())
        success(false)
    }
}