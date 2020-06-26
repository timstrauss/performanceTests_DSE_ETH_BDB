package genericTests.bigchaindb

import com.bigchaindb.api.TransactionsApi
import com.bigchaindb.builders.BigchainDbTransactionBuilder
import com.bigchaindb.constants.Operations
import com.bigchaindb.model.FulFill
import com.bigchaindb.model.GenericCallback
import com.bigchaindb.model.MetaData
import com.mongodb.BasicDBObject
import com.mongodb.MongoClient
import com.mongodb.client.model.Filters
import connectionDetails.BigchainDBConnectionDetails
import genericTests.TestThread
import genericTests.TimeToRun
import net.i2p.crypto.eddsa.EdDSAPrivateKey
import net.i2p.crypto.eddsa.EdDSAPublicKey
import okhttp3.Response
import org.bson.Document
import java.io.File
import java.util.*

object BDBBoolTests {
    fun run(threads: Int) {
        val con = BigchainDBConnectionDetails("http://127.0.0.1:9984")
        val uuid = UUID.randomUUID().toString()
        val timePerTest = TimeToRun.get()
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
        executeTests(threads, timePerTest, uuid, con)
    }

    private class SetBoolThread(
        time: Long,
        threadNum: Int,
        workerThreads: Int,
        val uuid: String,
        val con: BigchainDBConnectionDetails,
        val mongoClient: MongoClient = MongoClient()
    ) : TestThread(workerThreads, threadNum, time, true, "setBool", "bdb") {
        var success: Boolean? = null

        override fun testFunc(): Boolean {
            success = null
            val db = mongoClient.getDatabase("bigchain")
            val assetCollection = db.getCollection("assets")
            val transactionsCollection = db.getCollection("transactions")
            val metadataCollection = db.getCollection("metadata")
            val assetId = assetCollection.find(
                Filters.and(
                    BasicDBObject("data", BasicDBObject("property", "boolvar")),
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
            if (metadataValue.toBoolean() == setValue as Boolean) {
                return true
            }
            val fulFill = FulFill()
            fulFill.outputIndex = 0
            fulFill.transactionId = metadataId
            val previousSize = TransactionsApi.getTransactionsByAssetId(assetId, Operations.TRANSFER).transactions.size
            val tt: String? = null
            val metadata = MetaData()
            metadata.setMetaData("value", (setValue as Boolean).toString())
            BigchainDbTransactionBuilder
                .init()
                .addMetaData(metadata)
                .addAssets(assetId, String::class.java)
                .addInput(tt, fulFill, con.keyPair.public as EdDSAPublicKey)
                .addOutput("1", con.keyPair.public as EdDSAPublicKey)
                .operation(Operations.TRANSFER)
                .buildAndSign(con.keyPair.public as EdDSAPublicKey, con.keyPair.private as EdDSAPrivateKey)
                .sendTransaction(CallBackBDB() {
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

    private class GetBoolThread(
        time: Long,
        threadNum: Int,
        workerThreads: Int,
        val uuid: String,
        val con: BigchainDBConnectionDetails,
        val mongoClient: MongoClient = MongoClient()
    ) : TestThread(workerThreads, threadNum, time, false, "getBool", "bdb") {
        override fun testFunc(): Boolean {
            val db = mongoClient.getDatabase("bigchain")
            val assetCollection = db.getCollection("assets")
            val transactionsCollection = db.getCollection("transactions")
            val metadataCollection = db.getCollection("metadata")
            val assetId = assetCollection.find(
                Filters.and(
                    BasicDBObject("data", BasicDBObject("property", "boolvar")),
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
            return true
        }
    }

    private fun executeTests(workerThreads: Int, time: Long, uuid: String, con: BigchainDBConnectionDetails) {
        val start = System.currentTimeMillis()
        val setThreads = Array(workerThreads) {
            val thread = SetBoolThread(time, it, workerThreads, uuid, con)
            thread.start()
            thread
        }
        for (thread in setThreads) {
            thread.join()
        }
        val end = System.currentTimeMillis()
        println(end - start)
        var benchmarkFile = File("./benchmarks/bdb/setBool${workerThreads}.txt")
        if (benchmarkFile.exists()) {
            benchmarkFile.delete()
        }
        benchmarkFile.createNewFile()
        for (index in 0 until workerThreads) {
            if (index > 0) {
                benchmarkFile.appendText("\n")
            }
            val file = File("./benchmarks/bdb/setBool${workerThreads}T$index.txt")
            benchmarkFile.appendText(file.readText())
            file.delete()
        }

        val getThreads = Array(workerThreads) {
            val thread = GetBoolThread(time, it, workerThreads, uuid, con)
            thread.start()
            thread
        }
        for (thread in getThreads) {
            thread.join()
        }
        benchmarkFile = File("./benchmarks/bdb/getBool${workerThreads}.txt")
        if (benchmarkFile.exists()) {
            benchmarkFile.delete()
        }
        benchmarkFile.createNewFile()
        for (index in 0 until workerThreads) {
            if (index > 0) {
                benchmarkFile.appendText("\n")
            }
            val file = File("./benchmarks/bdb/getBool${workerThreads}T$index.txt")
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