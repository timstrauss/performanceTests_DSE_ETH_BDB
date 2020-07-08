package genericTests.ethereum

import connectionDetails.EthereumConnectionDetails
import connectionDetails.EthereumContractGasProvider
import de.hpi.cc.datasource.decentralized.ethereum.smartcontracts.Generic
import genericTests.TestThread
import genericTests.TestInfo
import java.io.File

object EthereumBoolTests {
    fun run(threads: Int, generics: List<String>) {
        val timePerTest = TestInfo.getTimeToRun()
        val t = File("./benchmarks/ethereum").mkdirs()

        executeBoolTests(threads, timePerTest, generics)
    }

    private class SetBoolThread(
        time: Long,
        var generic: Generic,
        threadNum: Int,
        workerThreads: Int,
        val con: EthereumConnectionDetails
    ): TestThread(workerThreads, threadNum, time, true, "setBool", "ethereum") {
        override fun testFunc(): Boolean {
            var success = false
            while (!success) {
                try {
                    generic.setBool(setValue as Boolean).send()
                    success = true
                } catch (e: Exception) {
                    e.printStackTrace()
                    Generic.load(
                        generic.contractAddress,
                        con.web3j,
                        TestInfo.getEthTransactionManager(con.web3j, con.credentials),
                        EthereumContractGasProvider()
                    )
                    success = false
                }
            }
            return true
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

    private fun executeBoolTests(workerThreads: Int, time: Long, generic: List<String>) {
        val con = EthereumConnectionDetails("http://${TestInfo.nodeHost}:8545")
        val generics = Array<Generic>(workerThreads) {
            if (TestInfo.sameResource) {
                Generic.load(
                    generic[0],
                    con.web3j,
                    TestInfo.getEthTransactionManager(con.web3j, con.credentials),
                    EthereumContractGasProvider()
                )
            } else {
                Generic.load(
                    generic[it],
                    con.web3j,
                    TestInfo.getEthTransactionManager(con.web3j, con.credentials),
                    EthereumContractGasProvider()
                )
            }
        }
        val setThreads = Array(workerThreads) {
            val thread = SetBoolThread(time, generics[it], it, workerThreads, con)
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