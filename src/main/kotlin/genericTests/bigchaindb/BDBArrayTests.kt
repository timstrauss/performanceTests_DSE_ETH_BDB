package genericTests.bigchaindb

import com.bigchaindb.api.TransactionsApi
import com.bigchaindb.builders.BigchainDbTransactionBuilder
import com.bigchaindb.constants.BigchainDbApi
import com.bigchaindb.constants.Operations
import com.bigchaindb.model.BigChainDBGlobals
import com.bigchaindb.model.FulFill
import com.bigchaindb.model.GenericCallback
import com.bigchaindb.model.MetaData
import com.bigchaindb.util.NetworkUtils
import com.google.gson.Gson
import com.mongodb.BasicDBObject
import com.mongodb.MongoClient
import com.mongodb.client.model.Filters
import connectionDetails.BigchainDBConnectionDetails
import genericTests.TestThread
import genericTests.TestInfo
import net.i2p.crypto.eddsa.EdDSAPrivateKey
import net.i2p.crypto.eddsa.EdDSAPublicKey
import okhttp3.RequestBody
import okhttp3.Response
import org.bson.Document
import java.io.File
import java.util.*

object BDBArrayTests {
    val gson = Gson()

    fun run(threads: Int) {
        val con = BigchainDBConnectionDetails("http://${TestInfo.nodeHost}:9984")
        val timePerTest = TestInfo.getTimeToRun()
        val t = File("./benchmarks/bdb").mkdirs()
        val uuids = mutableListOf<String>()
        for (i in 0 until threads) {
            val uuid = UUID.randomUUID().toString()
            uuids.add(uuid)
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
        }
        executeTests(threads, timePerTest, uuids, con)
    }

    private class AddThread(
        time: Long,
        threadNum: Int,
        workerThreads: Int,
        val uuid: String,
        val con: BigchainDBConnectionDetails,
        val mongoClient: MongoClient = MongoClient(TestInfo.nodeHost)
    ) : TestThread(workerThreads, threadNum, time, true, "addArray", "bdb") {
        var success: Boolean? = null

        override fun testFunc(): Boolean {
            success = null
            while (success != true) {
                success = null
                try {
                    val db = mongoClient.getDatabase("bigchain")
                    val assetCollection = db.getCollection("assets")
                    val transactionsCollection = db.getCollection("transactions")
                    val metadataCollection = db.getCollection("metadata")
                    val assetId = assetCollection.find(
                        Filters.and(
                            BasicDBObject("data", BasicDBObject("property", "arrayvar")),
                            BasicDBObject("data", BasicDBObject("uuid", uuid))
                        )
                    ).limit(1).first()?.getString("id") ?: return false
                    val metadataId = transactionsCollection.find(
                        BasicDBObject(
                            "asset",
                            BasicDBObject("id", assetId)
                        )
                    ).sort(BasicDBObject("\$natural", -1)).limit(1).first()?.getString("id") ?: assetId
                    val metadataValue = gson.fromJson(
                        (metadataCollection.find(
                            BasicDBObject(
                                "id",
                                metadataId
                            )
                        ).limit(1).first()?.get("metadata") as Document?)?.getString("value") ?: return false,
                        MutableList::class.java
                    ) as MutableList<Int>
                    metadataValue.add(setValue as Int)
                    val fulFill = FulFill()
                    fulFill.outputIndex = 0
                    fulFill.transactionId = metadataId
                    val tt: String? = null
                    val metadata = MetaData()
                    metadata.setMetaData("value", gson.toJson(metadataValue))
                    val transaction = BigchainDbTransactionBuilder
                        .init()
                        .addMetaData(metadata)
                        .addAssets(assetId, String::class.java)
                        .addInput(tt, fulFill, con.keyPair.public as EdDSAPublicKey)
                        .addOutput("1", con.keyPair.public as EdDSAPublicKey)
                        .operation(Operations.TRANSFER)
                        .buildAndSignOnly(con.keyPair.public as EdDSAPublicKey, con.keyPair.private as EdDSAPrivateKey)
                    if (transaction.id == metadataId) {
                        success = true
                    } else {
                        val body = RequestBody.create(BDBCallBack.JSON, transaction.toString())
                        NetworkUtils.sendPostRequest(BigChainDBGlobals.getBaseUrl() + BigchainDbApi.TRANSACTIONS + "?mode=commit", body, BDBCallBack {
                            success = it
                        })
                    }
                    while (success == null) {
                        sleep(0, 1)
                    }
                } catch (e: Exception) {
                }
            }

            return success!!
        }

        override fun preaction() {
            sleep(100)
            success = null
            while (success != true && needReset) {
                success = null
                try {
                    val db = mongoClient.getDatabase("bigchain")
                    val assetCollection = db.getCollection("assets")
                    val transactionsCollection = db.getCollection("transactions")
                    val metadataCollection = db.getCollection("metadata")
                    val assetId = assetCollection.find(
                        Filters.and(
                            BasicDBObject("data", BasicDBObject("property", "arrayvar")),
                            BasicDBObject("data", BasicDBObject("uuid", uuid))
                        )
                    ).limit(1).first()?.getString("id") ?: break
                    val metadataId = transactionsCollection.find(
                        BasicDBObject(
                            "asset",
                            BasicDBObject("id", assetId)
                        )
                    ).sort(BasicDBObject("\$natural", -1)).limit(1).first()?.getString("id") ?: assetId
                    val metadataValue = MutableList(221) { it }
                    val fulFill = FulFill()
                    fulFill.outputIndex = 0
                    fulFill.transactionId = metadataId
                    val tt: String? = null
                    val metadata = MetaData()
                    metadata.setMetaData("value", gson.toJson(metadataValue))
                    val transaction = BigchainDbTransactionBuilder
                        .init()
                        .addMetaData(metadata)
                        .addAssets(assetId, String::class.java)
                        .addInput(tt, fulFill, con.keyPair.public as EdDSAPublicKey)
                        .addOutput("1", con.keyPair.public as EdDSAPublicKey)
                        .operation(Operations.TRANSFER)
                        .buildAndSignOnly(
                            con.keyPair.public as EdDSAPublicKey,
                            con.keyPair.private as EdDSAPrivateKey
                        )
                    if (transaction.id == metadataId) {
                        success = true
                    } else {
                        val body = RequestBody.create(BDBCallBack.JSON, transaction.toString())
                        NetworkUtils.sendPostRequest(BigChainDBGlobals.getBaseUrl() + BigchainDbApi.TRANSACTIONS + "?mode=commit", body, BDBCallBack {
                            success = it
                        })
                    }
                    while (success == null) {
                        sleep(0, 1)
                    }
                } catch (e: Exception) {
                }
            }
            if (success == true) {
                setReset(false)
            }
        }
    }

    private class RemoveThread(
        time: Long,
        threadNum: Int,
        workerThreads: Int,
        val uuid: String,
        val con: BigchainDBConnectionDetails,
        val mongoClient: MongoClient = MongoClient(TestInfo.nodeHost)
    ) : TestThread(workerThreads, threadNum, time, true, "removeArray", "bdb") {
        var success: Boolean? = null

        override fun testFunc(): Boolean {
            success = null
            while (success != true) {
                success = null
                try {
                    val db = mongoClient.getDatabase("bigchain")
                    val assetCollection = db.getCollection("assets")
                    val transactionsCollection = db.getCollection("transactions")
                    val metadataCollection = db.getCollection("metadata")
                    val assetId = assetCollection.find(
                        Filters.and(
                            BasicDBObject("data", BasicDBObject("property", "arrayvar")),
                            BasicDBObject("data", BasicDBObject("uuid", uuid))
                        )
                    ).limit(1).first()?.getString("id") ?: return false
                    val metadataId = transactionsCollection.find(
                        BasicDBObject(
                            "asset",
                            BasicDBObject("id", assetId)
                        )
                    ).sort(BasicDBObject("\$natural", -1)).limit(1).first()?.getString("id") ?: assetId
                    val metadataValue = gson.fromJson(
                        (metadataCollection.find(
                            BasicDBObject(
                                "id",
                                metadataId
                            )
                        ).limit(1).first()?.get("metadata") as Document?)?.getString("value") ?: return false,
                        MutableList::class.java
                    ) as MutableList<Int>
                    metadataValue.remove(setValue as Int)
                    val fulFill = FulFill()
                    fulFill.outputIndex = 0
                    fulFill.transactionId = metadataId
                    val tt: String? = null
                    val metadata = MetaData()
                    metadata.setMetaData("value", gson.toJson(metadataValue))
                    val transaction = BigchainDbTransactionBuilder
                        .init()
                        .addMetaData(metadata)
                        .addAssets(assetId, String::class.java)
                        .addInput(tt, fulFill, con.keyPair.public as EdDSAPublicKey)
                        .addOutput("1", con.keyPair.public as EdDSAPublicKey)
                        .operation(Operations.TRANSFER)
                        .buildAndSignOnly(con.keyPair.public as EdDSAPublicKey, con.keyPair.private as EdDSAPrivateKey)
                    if (transaction.id == metadataId) {
                        success = true
                    } else {
                        val body = RequestBody.create(BDBCallBack.JSON, transaction.toString())
                        NetworkUtils.sendPostRequest(BigChainDBGlobals.getBaseUrl() + BigchainDbApi.TRANSACTIONS + "?mode=commit", body, BDBCallBack {
                            success = it
                        })
                    }
                    while (success == null) {
                        sleep(0, 1)
                    }
                } catch (e: Exception) {
                }
            }

            return success!!
        }

        override fun preaction() {
            sleep(100)
            success = null
            while (success != true && needReset) {
                success = null
                try {
                    val db = mongoClient.getDatabase("bigchain")
                    val assetCollection = db.getCollection("assets")
                    val transactionsCollection = db.getCollection("transactions")
                    val metadataCollection = db.getCollection("metadata")
                    val assetId = assetCollection.find(
                        Filters.and(
                            BasicDBObject("data", BasicDBObject("property", "arrayvar")),
                            BasicDBObject("data", BasicDBObject("uuid", uuid))
                        )
                    ).limit(1).first()?.getString("id") ?: break
                    val metadataId = transactionsCollection.find(
                        BasicDBObject(
                            "asset",
                            BasicDBObject("id", assetId)
                        )
                    ).sort(BasicDBObject("\$natural", -1)).limit(1).first()?.getString("id") ?: assetId
                    val metadataValue = MutableList(221) { it }
                    val fulFill = FulFill()
                    fulFill.outputIndex = 0
                    fulFill.transactionId = metadataId
                    val tt: String? = null
                    val metadata = MetaData()
                    metadata.setMetaData("value", gson.toJson(metadataValue))
                    val transaction = BigchainDbTransactionBuilder
                        .init()
                        .addMetaData(metadata)
                        .addAssets(assetId, String::class.java)
                        .addInput(tt, fulFill, con.keyPair.public as EdDSAPublicKey)
                        .addOutput("1", con.keyPair.public as EdDSAPublicKey)
                        .operation(Operations.TRANSFER)
                        .buildAndSignOnly(con.keyPair.public as EdDSAPublicKey, con.keyPair.private as EdDSAPrivateKey)
                    if (transaction.id == metadataId) {
                        success = true
                    } else {
                        val body = RequestBody.create(BDBCallBack.JSON, transaction.toString())
                        NetworkUtils.sendPostRequest(BigChainDBGlobals.getBaseUrl() + BigchainDbApi.TRANSACTIONS + "?mode=commit", body, BDBCallBack {
                            success = it
                        })
                    }
                    while (success == null) {
                        sleep(0, 1)
                    }
                } catch (e: Exception) {
                }
            }
            if (success == true) {
                setReset(false)
            }
            setValue = (System.currentTimeMillis() % 221).toInt()
        }
    }

    private class GetThread(
        time: Long,
        threadNum: Int,
        workerThreads: Int,
        val uuid: String,
        val con: BigchainDBConnectionDetails,
        val mongoClient: MongoClient = MongoClient(TestInfo.nodeHost)
    ) : TestThread(workerThreads, threadNum, time, false, "getArray", "bdb") {
        override fun testFunc(): Boolean {
            val db = mongoClient.getDatabase("bigchain")
            val assetCollection = db.getCollection("assets")
            val transactionsCollection = db.getCollection("transactions")
            val metadataCollection = db.getCollection("metadata")
            val assetId = assetCollection.find(
                Filters.and(
                    BasicDBObject("data", BasicDBObject("property", "arrayvar")),
                    BasicDBObject("data", BasicDBObject("uuid", uuid))
                )
            ).limit(1).first()?.getString("id") ?: return false
            val metadataId = transactionsCollection.find(
                BasicDBObject(
                    "asset",
                    BasicDBObject("id", assetId)
                )
            ).sort(BasicDBObject("\$natural", -1)).limit(1).first()?.getString("id") ?: return false
            val metadataValue = (metadataCollection.find(
                BasicDBObject(
                    "id",
                    metadataId
                )
            ).limit(1).first()?.get("metadata") as Document?)?.getString("value") ?: return false
            return gson.fromJson(metadataValue, MutableList::class.java) != null
        }
    }

    private fun executeTests(workerThreads: Int, time: Long, uuids: List<String>, con: BigchainDBConnectionDetails) {
        val setThreads = Array(workerThreads) {
            val thread = if (TestInfo.sameResource) {
                AddThread(time, it, workerThreads, uuids[0], con)
            } else {
                AddThread(time, it, workerThreads, uuids[it], con)
            }
            thread.start()
            thread
        }
        for (thread in setThreads) {
            thread.join()
        }
        var benchmarkFile = File("./benchmarks/bdb/addArray${workerThreads}.txt")
        if (benchmarkFile.exists()) {
            benchmarkFile.delete()
        }
        benchmarkFile.createNewFile()
        for (index in 0 until workerThreads) {
            if (index > 0) {
                benchmarkFile.appendText("\n")
            }
            val file = File("./benchmarks/bdb/addArray${workerThreads}T$index.txt")
            benchmarkFile.appendText(file.readText())
            file.delete()
        }

        val removeThreads = Array(workerThreads) {
            val thread = if (TestInfo.sameResource) {
                RemoveThread(time, it, workerThreads, uuids[0], con)
            } else {
                RemoveThread(time, it, workerThreads, uuids[it], con)
            }
            thread.start()
            thread
        }
        for (thread in removeThreads) {
            thread.join()
        }
        benchmarkFile = File("./benchmarks/bdb/removeArray${workerThreads}.txt")
        if (benchmarkFile.exists()) {
            benchmarkFile.delete()
        }
        benchmarkFile.createNewFile()
        for (index in 0 until workerThreads) {
            if (index > 0) {
                benchmarkFile.appendText("\n")
            }
            val file = File("./benchmarks/bdb/removeArray${workerThreads}T$index.txt")
            benchmarkFile.appendText(file.readText())
            file.delete()
        }

        val getThreads = Array(workerThreads) {
            val thread = if (TestInfo.sameResource) {
                GetThread(time, it, workerThreads, uuids[0], con)
            } else {
                GetThread(time, it, workerThreads, uuids[it], con)
            }
            thread.start()
            thread
        }
        for (thread in getThreads) {
            thread.join()
        }
        benchmarkFile = File("./benchmarks/bdb/getArray${workerThreads}.txt")
        if (benchmarkFile.exists()) {
            benchmarkFile.delete()
        }
        benchmarkFile.createNewFile()
        for (index in 0 until workerThreads) {
            if (index > 0) {
                benchmarkFile.appendText("\n")
            }
            val file = File("./benchmarks/bdb/getArray${workerThreads}T$index.txt")
            benchmarkFile.appendText(file.readText())
            file.delete()
        }
    }
}