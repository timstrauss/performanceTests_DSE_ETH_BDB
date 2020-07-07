package genericTests.ethereum

import connectionDetails.EthereumConnectionDetails
import connectionDetails.EthereumContractGasProvider
import de.hpi.cc.datasource.decentralized.ethereum.smartcontracts.Generic
import genericTests.TestThread
import genericTests.TestInfo
import java.io.File
import java.math.BigInteger

object EthereumIntTests {
    fun run(threads: Int, generics: List<String>) {
        val timePerTest = TestInfo.getTimeToRun()
        val t = File("./benchmarks/ethereum").mkdirs()

        executeIntTests(threads, timePerTest, generics)
    }

    private class SetThread(time: Long, val generic: Generic, threadNum: Int, workerThreads: Int): TestThread(workerThreads, threadNum, time, true, "setInt", "ethereum") {
        override fun testFunc(): Boolean {
            var success = false
            while (!success) {
                try {
                    generic.setInt(setValue as BigInteger).send()
                    success = true
                } catch (e: Exception) {
                    success = false
                }
            }
            return true
        }

        override fun preaction() {
            setValue = BigInteger.valueOf(System.currentTimeMillis() % 2000)
        }
    }

    private class GetThread(time: Long, val generic: Generic, threadNum: Int, workerThreads: Int): TestThread(workerThreads, threadNum, time, false, "getInt", "ethereum") {
        override fun testFunc(): Boolean {
            return try {
                generic.int.send()
                true
            } catch (e: Exception) {
                false
            }
        }
    }

    private fun executeIntTests(workerThreads: Int, time: Long, generic: List<String>) {
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
        var benchmarkFile = File("./benchmarks/ethereum/setInt${workerThreads}.txt")
        if (benchmarkFile.exists()) {
            benchmarkFile.delete()
        }
        benchmarkFile.createNewFile()
        for (index in 0 until workerThreads) {
            if (index > 0) {
                benchmarkFile.appendText("\n")
            }
            val file = File("./benchmarks/ethereum/setInt${workerThreads}T$index.txt")
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
        benchmarkFile = File("./benchmarks/ethereum/getInt${workerThreads}.txt")
        if (benchmarkFile.exists()) {
            benchmarkFile.delete()
        }
        benchmarkFile.createNewFile()
        for (index in 0 until workerThreads) {
            if (index > 0) {
                benchmarkFile.appendText("\n")
            }
            val file = File("./benchmarks/ethereum/getInt${workerThreads}T$index.txt")
            benchmarkFile.appendText(file.readText())
            file.delete()
        }
    }
}