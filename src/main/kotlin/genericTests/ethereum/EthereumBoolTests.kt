package genericTests.ethereum

import connectionDetails.EthereumConnectionDetails
import connectionDetails.EthereumContractGasProvider
import de.hpi.cc.datasource.decentralized.ethereum.smartcontracts.Generic
import genericTests.TestThread
import genericTests.TestInfo
import java.io.File

object EthereumBoolTests {
    fun run(threads: Int) {
        val con = EthereumConnectionDetails("http://${TestInfo.nodeHost}:8545")
        val generic = Generic.deploy(con.web3j, con.credentials, EthereumContractGasProvider()).send()
        val timePerTest = TestInfo.getTimeToRun()
        val t = File("./benchmarks/ethereum").mkdirs()

        executeBoolTests(threads, timePerTest, generic)
    }

    private class SetBoolThread(time: Long, val generic: Generic, threadNum: Int, workerThreads: Int): TestThread(workerThreads, threadNum, time, true, "setBool", "ethereum") {
        override fun testFunc(): Boolean {
            return try {
                generic.setBool(setValue as Boolean).send()
                true
            } catch (e: Exception) {
                false
            }
        }

        override fun preaction() {
            setValue = (System.currentTimeMillis() % 2) == 0L
        }
    }

    private class GetBoolThread(time: Long, val generic: Generic, threadNum: Int, workerThreads: Int): TestThread(workerThreads, threadNum, time, false, "getBool", "ethereum") {
        override fun testFunc():Boolean {
            return try {
                generic.bool.send()
                true
            } catch (e: Exception) {
                false
            }
        }
    }

    private fun executeBoolTests(workerThreads: Int, time: Long, generic: Generic) {
        val generics = Array<Generic>(workerThreads) {
            val con = EthereumConnectionDetails("http://${TestInfo.nodeHost}:8545")
            Generic.load(generic.contractAddress, con.web3j, con.credentials, EthereumContractGasProvider())
        }
        val setThreads = Array(workerThreads) {
            val thread = SetBoolThread(time, generics[it], it, workerThreads)
            thread.start()
            thread
        }
        for (thread in setThreads) {
            thread.join()
        }
        var benchmarkFile = File("./benchmarks/ethereum/setBool${workerThreads}.txt")
        if (benchmarkFile.exists()) {
            benchmarkFile.delete()
        }
        benchmarkFile.createNewFile()
        for (index in 0 until workerThreads) {
            if (index > 0) {
                benchmarkFile.appendText("\n")
            }
            val file = File("./benchmarks/ethereum/setBool${workerThreads}T$index.txt")
            benchmarkFile.appendText(file.readText())
            file.delete()
        }


        val getThreads = Array(workerThreads) {
            val thread = GetBoolThread(time, generics[it], it, workerThreads)
            thread.start()
            thread
        }
        for (thread in getThreads) {
            thread.join()
        }
        benchmarkFile = File("./benchmarks/ethereum/getBool${workerThreads}.txt")
        if (benchmarkFile.exists()) {
            benchmarkFile.delete()
        }
        benchmarkFile.createNewFile()
        for (index in 0 until workerThreads) {
            if (index > 0) {
                benchmarkFile.appendText("\n")
            }
            val file = File("./benchmarks/ethereum/getBool${workerThreads}T$index.txt")
            benchmarkFile.appendText(file.readText())
            file.delete()
        }
    }
}