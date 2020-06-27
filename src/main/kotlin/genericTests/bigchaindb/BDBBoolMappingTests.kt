package genericTests.bigchaindb

import com.bigchaindb.api.TransactionsApi
import com.bigchaindb.builders.BigchainDbTransactionBuilder
import com.bigchaindb.constants.Operations
import com.bigchaindb.model.FulFill
import com.bigchaindb.model.GenericCallback
import com.bigchaindb.model.MetaData
import com.google.gson.Gson
import com.mongodb.BasicDBObject
import com.mongodb.MongoClient
import com.mongodb.client.model.Filters
import connectionDetails.BigchainDBConnectionDetails
import genericTests.TestThread
import genericTests.TestInfo
import net.i2p.crypto.eddsa.EdDSAPrivateKey
import net.i2p.crypto.eddsa.EdDSAPublicKey
import okhttp3.Response
import org.bson.Document
import java.io.File
import java.util.*
import kotlin.collections.HashMap

object BDBBoolMappingTests {
    val gson = Gson()

    fun run(threads: Int) {
        val con = BigchainDBConnectionDetails("http://${TestInfo.nodeHost}:9984")
        val uuid = UUID.randomUUID().toString()
        val timePerTest = TestInfo.getTimeToRun()
        val t = File("./benchmarks/bdb").mkdirs()
        var assetData = TreeMap<String, String>()
        assetData["uuid"] = uuid
        assetData["property"] = "stringvar"
        var metadata = MetaData()
        metadata.setMetaData("value", "test")
        con.sendCreateTransaction(assetData, metadata)
        assetData = TreeMap<String, String>()
        assetData["uuid"] = uuid
        assetData["property"] = "intvar"
        metadata = MetaData()
        metadata.setMetaData("value", "3")
        con.sendCreateTransaction(assetData, metadata)
        assetData = TreeMap<String, String>()
        assetData["uuid"] = uuid
        assetData["property"] = "boolvar"
        metadata = MetaData()
        metadata.setMetaData("value", "true")
        con.sendCreateTransaction(assetData, metadata)
        assetData = TreeMap<String, String>()
        assetData["uuid"] = uuid
        assetData["property"] = "arrayvar"
        metadata = MetaData()
        metadata.setMetaData("value", gson.toJson(IntArray(0)))
        con.sendCreateTransaction(assetData, metadata)
        assetData = TreeMap<String, String>()
        assetData["uuid"] = uuid
        assetData["property"] = "booolmappingvar"
        metadata = MetaData()
        metadata.setMetaData("value", gson.toJson(HashMap<String, Boolean>(mutableMapOf(Pair("test", true)))))
        con.sendCreateTransaction(assetData, metadata)
        executeTests(threads, timePerTest, uuid, con)
    }

    private class SetThread(
        time: Long,
        threadNum: Int,
        workerThreads: Int,
        val uuid: String,
        val con: BigchainDBConnectionDetails,
        val mongoClient: MongoClient = MongoClient()
    ) : TestThread(workerThreads, threadNum, time, true, "setBoolMapping", "bdb") {
        var success: Boolean? = null

        override fun testFunc(): Boolean {
            success = null
            val db = mongoClient.getDatabase("bigchain")
            val assetCollection = db.getCollection("assets")
            val transactionsCollection = db.getCollection("transactions")
            val metadataCollection = db.getCollection("metadata")
            val assetId = assetCollection.find(
                Filters.and(
                    BasicDBObject("data", BasicDBObject("property", "booolmappingvar")),
                    BasicDBObject("data", BasicDBObject("uuid", uuid))
                )
            ).first()?.getString("id") ?: return false
            val metadataId = transactionsCollection.find(
                BasicDBObject(
                    "asset",
                    BasicDBObject("id", assetId)
                )
            ).sort(BasicDBObject("\$natural", -1)).first()?.getString("id") ?: assetId
            val metadataValue = gson.fromJson((metadataCollection.find(
                BasicDBObject(
                    "id",
                    metadataId
                )
            ).first()?.get("metadata") as Document?)?.getString("value") ?: return false, HashMap::class.java) as HashMap<String, Boolean>
            if (metadataValue["test2"] == setValue as Boolean) {
                return true
            }
            metadataValue["test2"] = true
            val fulFill = FulFill()
            fulFill.outputIndex = 0
            fulFill.transactionId = metadataId
            val previousSize = TransactionsApi.getTransactionsByAssetId(assetId, Operations.TRANSFER).transactions.size
            val tt: String? = null
            val metadata = MetaData()
            metadata.setMetaData("value", gson.toJson(metadataValue))
            BigchainDbTransactionBuilder
                .init()
                .addMetaData(metadata)
                .addAssets(assetId, String::class.java)
                .addInput(tt, fulFill, con.keyPair.public as EdDSAPublicKey)
                .addOutput("1", con.keyPair.public as EdDSAPublicKey)
                .operation(Operations.TRANSFER)
                .buildAndSign(con.keyPair.public as EdDSAPublicKey, con.keyPair.private as EdDSAPrivateKey)
                .sendTransaction(BDBBoolTests.CallBackBDB() {
                    success = it
                })
            while (success == null) {
                sleep(0, 1)
            }
            return success!!
        }

        override fun preaction() {
            setValue = (System.currentTimeMillis() % 2) == 0L
        }
    }

    private class GetThread(
        time: Long,
        threadNum: Int,
        workerThreads: Int,
        val uuid: String,
        val con: BigchainDBConnectionDetails,
        val mongoClient: MongoClient = MongoClient()
    ) : TestThread(workerThreads, threadNum, time, false, "getBoolMapping", "bdb") {
        override fun testFunc(): Boolean {
            val db = mongoClient.getDatabase("bigchain")
            val assetCollection = db.getCollection("assets")
            val transactionsCollection = db.getCollection("transactions")
            val metadataCollection = db.getCollection("metadata")
            val assetId = assetCollection.find(
                Filters.and(
                    BasicDBObject("data", BasicDBObject("property", "booolmappingvar")),
                    BasicDBObject("data", BasicDBObject("uuid", uuid))
                )
            ).first()?.getString("id") ?: return false
            val metadataId = transactionsCollection.find(
                BasicDBObject(
                    "asset",
                    BasicDBObject("id", assetId)
                )
            ).sort(BasicDBObject("\$natural", -1)).first()?.getString("id") ?: return false
            val metadataValue = (metadataCollection.find(
                BasicDBObject(
                    "id",
                    metadataId
                )
            ).first()?.get("metadata") as Document?)?.getString("value") ?: return false
            return gson.fromJson(metadataValue, HashMap::class.java)["test"] == true
        }
    }

    private fun executeTests(workerThreads: Int, time: Long, uuid: String, con: BigchainDBConnectionDetails) {
        val setThreads = Array(workerThreads) {
            val thread = SetThread(time, it, workerThreads, uuid, con)
            thread.start()
            thread
        }
        for (thread in setThreads) {
            thread.join()
        }
        var benchmarkFile = File("./benchmarks/bdb/setBoolMapping${workerThreads}.txt")
        if (benchmarkFile.exists()) {
            benchmarkFile.delete()
        }
        benchmarkFile.createNewFile()
        for (index in 0 until workerThreads) {
            if (index > 0) {
                benchmarkFile.appendText("\n")
            }
            val file = File("./benchmarks/bdb/setBoolMapping${workerThreads}T$index.txt")
            benchmarkFile.appendText(file.readText())
            file.delete()
        }

        val getThreads = Array(workerThreads) {
            val thread = GetThread(time, it, workerThreads, uuid, con)
            thread.start()
            thread
        }
        for (thread in getThreads) {
            thread.join()
        }
        benchmarkFile = File("./benchmarks/bdb/getBoolMapping${workerThreads}.txt")
        if (benchmarkFile.exists()) {
            benchmarkFile.delete()
        }
        benchmarkFile.createNewFile()
        for (index in 0 until workerThreads) {
            if (index > 0) {
                benchmarkFile.appendText("\n")
            }
            val file = File("./benchmarks/bdb/getBoolMapping${workerThreads}T$index.txt")
            benchmarkFile.appendText(file.readText())
            file.delete()
        }
    }

    class CallBackBDB(val success: (value: Boolean) -> Unit) : GenericCallback {
        override fun pushedSuccessfully(response: Response?) {
            success(true)
        }

        override fun transactionMalformed(response: Response?) {
            success(false)
        }

        override fun otherError(response: Response?) {
            success(false)
        }
    }
}