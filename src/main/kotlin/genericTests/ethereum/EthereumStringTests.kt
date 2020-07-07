package genericTests.ethereum

import connectionDetails.EthereumConnectionDetails
import connectionDetails.EthereumContractGasProvider
import de.hpi.cc.datasource.decentralized.ethereum.smartcontracts.Generic
import genericTests.TestThread
import genericTests.TestInfo
import java.io.File

object EthereumStringTests {
    fun run(threads: Int) {
        val con = EthereumConnectionDetails("http://${TestInfo.nodeHost}:8545")
        val generics = mutableListOf<String>()
        for (i in 0 until threads) {
            generics.add(Generic.deploy(con.web3j, con.credentials, EthereumContractGasProvider()).send().contractAddress)
        }
        val timePerTest = TestInfo.getTimeToRun()
        val t = File("./benchmarks/ethereum").mkdirs()

        executeStringTests(threads, timePerTest, generics)
    }

    private class SetThread(time: Long, val generic: Generic, threadNum: Int, workerThreads: Int): TestThread(workerThreads, threadNum, time, true, "setString", "ethereum") {
        override fun testFunc(): Boolean {
            return try {
                generic.setString(setValue as String).send()
                true
            } catch (e: Exception) {
                false
            }
        }

        override fun preaction() {
            setValue = "abcsd" + (System.currentTimeMillis() % 2000)
        }
    }

    private class GetThread(time: Long, val generic: Generic, threadNum: Int, workerThreads: Int): TestThread(workerThreads, threadNum, time, false, "getString", "ethereum") {
        override fun testFunc(): Boolean {
            return try {
                generic.string.send()
                true
            } catch (e: Exception) {
                false
            }
        }
    }

    private fun executeStringTests(workerThreads: Int, time: Long, generic: List<String>) {
        val generics = Array<Generic>(workerThreads) {
            val con = EthereumConnectionDetails("http://${TestInfo.nodeHost}:8545")
            if (TestInfo.sameResource) {
                Generic.load(generic[0], con.web3j, TestInfo.getEthTransactionManager(con.web3j, con.credentials), EthereumContractGasProvider())
            } else {
                Generic.load(generic[it], con.web3j, TestInfo.getEthTransactionManager(con.web3j, con.credentials), EthereumContractGasProvider())
            }
        }
        val setThreads = Array(workerThreads) {
            val thread = SetThread(time, generics[it], it, workerThreads)
            thread.start()
            thread
        }
        for (thread in setThreads) {
            thread.join()
        }
        var benchmarkFile = File("./benchmarks/ethereum/setString${workerThreads}.txt")
        if (benchmarkFile.exists()) {
            benchmarkFile.delete()
        }
        benchmarkFile.createNewFile()
        for (index in 0 until workerThreads) {
            if (index > 0) {
                benchmarkFile.appendText("\n")
            }
            val file = File("./benchmarks/ethereum/setString${workerThreads}T$index.txt")
            benchmarkFile.appendText(file.readText())
            file.delete()
        }


        val getThreads = Array(workerThreads) {
            val thread = GetThread(time, generics[it], it, workerThreads)
            thread.start()
            thread
        }
        for (thread in getThreads) {
            thread.join()
        }
        benchmarkFile = File("./benchmarks/ethereum/getString${workerThreads}.txt")
        if (benchmarkFile.exists()) {
            benchmarkFile.delete()
        }
        benchmarkFile.createNewFile()
        for (index in 0 until workerThreads) {
            if (index > 0) {
                benchmarkFile.appendText("\n")
            }
            val file = File("./benchmarks/ethereum/getString${workerThreads}T$index.txt")
            benchmarkFile.appendText(file.readText())
            file.delete()
        }
    }
}