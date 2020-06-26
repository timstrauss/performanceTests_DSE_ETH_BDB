package genericTests.bigchaindb

import com.bigchaindb.api.AssetsApi
import com.bigchaindb.api.TransactionsApi
import com.bigchaindb.builders.BigchainDbTransactionBuilder
import com.bigchaindb.constants.Operations
import com.bigchaindb.model.FulFill
import com.bigchaindb.model.GenericCallback
import com.bigchaindb.model.MetaData
import com.google.gson.internal.LinkedTreeMap
import connectionDetails.BigchainDBConnectionDetails
import genericTests.TestThread
import genericTests.TimeToRun
import net.i2p.crypto.eddsa.EdDSAPrivateKey
import net.i2p.crypto.eddsa.EdDSAPublicKey
import okhttp3.Response
import java.io.File
import java.util.*

object BDBIntTests {
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

    private class SetIntThread(time: Long, threadNum: Int, workerThreads: Int, val uuid: String, val con: BigchainDBConnectionDetails): TestThread(workerThreads, threadNum, time, true, "setInt", "bdb") {
        var success: Boolean? = null

        override fun testFunc(): Boolean {
            success = null
            val assetId = AssetsApi.getAssetsWithLimit(uuid + " intvar", "1").assets[0].id
            val latestMetaData: LinkedTreeMap<String, String>
            var latestMetaDataId: String
            val transfers = TransactionsApi.getTransactionsByAssetId(assetId, Operations.TRANSFER).transactions
            if (transfers.size > 0) {
                latestMetaData = transfers.last().metaData as LinkedTreeMap<String, String>
                latestMetaDataId = transfers.last().id
            } else {
                latestMetaData = TransactionsApi.getTransactionById(assetId).metaData as LinkedTreeMap<String, String>
                latestMetaDataId = assetId
            }
            if (latestMetaData["value"]!!.toInt() == setValue as Int) {
                return true
            }
            if (latestMetaDataId.startsWith("\"") && latestMetaDataId.endsWith("\"")) {
                latestMetaDataId = latestMetaDataId.substring(1, latestMetaDataId.length-1)
            }
            val fulFill = FulFill()
            fulFill.outputIndex = 0
            fulFill.transactionId = latestMetaDataId
            val previousSize = TransactionsApi.getTransactionsByAssetId(assetId, Operations.TRANSFER).transactions.size
            val tt: String? = null
            val metadata = MetaData()
            metadata.setMetaData("value", (setValue as Int).toString())
            val trans = BigchainDbTransactionBuilder
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
            while (success == null) { sleep(0,1) }
            return success!!
        }

        override fun preaction() {
            setValue = (System.currentTimeMillis() % 2000).toInt()
        }
    }

    private class GetIntThread(time: Long, threadNum: Int, workerThreads: Int, val uuid: String, val con: BigchainDBConnectionDetails): TestThread(workerThreads, threadNum, time, false, "getInt", "bdb") {
        override fun testFunc(): Boolean {
            val assetId = AssetsApi.getAssetsWithLimit(uuid, "1").assets[0].id
            val latestVal = TransactionsApi.getTransactionsByAssetId(assetId, Operations.TRANSFER).transactions.last().metaData
            return true
        }
    }

    private fun executeTests(workerThreads: Int, time: Long, uuid: String, con: BigchainDBConnectionDetails) {
        val start = System.currentTimeMillis()
        val setThreads = Array(workerThreads) {
            val thread = SetIntThread(time, it, workerThreads, uuid, con)
            thread.start()
            thread
        }
        for (thread in setThreads) {
            thread.join()
        }
        val end = System.currentTimeMillis()
        println(end - start)
        var benchmarkFile = File("./benchmarks/bdb/setInt${workerThreads}.txt")
        if (benchmarkFile.exists()) {
            benchmarkFile.delete()
        }
        benchmarkFile.createNewFile()
        for (index in 0 until workerThreads) {
            if (index > 0) {
                benchmarkFile.appendText("\n")
            }
            val file = File("./benchmarks/bdb/setInt${workerThreads}T$index.txt")
            benchmarkFile.appendText(file.readText())
            file.delete()
        }

        val getThreads = Array(workerThreads) {
            val thread = GetIntThread(time, it, workerThreads, uuid, con)
            thread.start()
            thread
        }
        for (thread in getThreads) {
            thread.join()
        }
        benchmarkFile = File("./benchmarks/bdb/getInt${workerThreads}.txt")
        if (benchmarkFile.exists()) {
            benchmarkFile.delete()
        }
        benchmarkFile.createNewFile()
        for (index in 0 until workerThreads) {
            if (index > 0) {
                benchmarkFile.appendText("\n")
            }
            val file = File("./benchmarks/bdb/getInt${workerThreads}T$index.txt")
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