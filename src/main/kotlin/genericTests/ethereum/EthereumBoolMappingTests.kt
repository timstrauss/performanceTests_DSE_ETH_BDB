package genericTests.ethereum

import connectionDetails.EthereumConnectionDetails
import connectionDetails.EthereumContractGasProvider
import de.hpi.cc.datasource.decentralized.ethereum.smartcontracts.Generic
import genericTests.TestThread
import genericTests.TestInfo
import java.io.File

object EthereumBoolMappingTests {
    fun run(threads: Int) {
        val con = EthereumConnectionDetails("http://${TestInfo.nodeHost}:8545")
        val generics = mutableListOf<String>()
        for (i in 0 until threads) {
            generics.add(Generic.deploy(con.web3j, con.credentials, EthereumContractGasProvider()).send().contractAddress)
        }
        val timePerTest = TestInfo.getTimeToRun()
        val t = File("./benchmarks/ethereum").mkdirs()

        executeTests(threads, timePerTest, generics)
    }

    private class SetThread(time: Long, val generic: Generic, threadNum: Int, workerThreads: Int): TestThread(workerThreads, threadNum, time, true, "setBoolMapping", "ethereum") {
        override fun testFunc(): Boolean {
            var success = false
            while (!success) {
                try {
                    generic.setBoolFor("test2}", setValue as Boolean).send()
                    success = true
                } catch (e: Exception) {
                    success = false
                }
            }
            return true
        }

        override fun preaction() {
            setValue = (System.currentTimeMillis() % 2) == 0L
        }
    }

    private class GetThread(time: Long, val generic: Generic, threadNum: Int, workerThreads: Int): TestThread(workerThreads, threadNum, time, false, "getBoolMapping", "ethereum") {
        override fun testFunc():Boolean {
            return try {
                generic.getBoolFor("test").send()
            } catch (e: Exception) {
                false
            }
        }
    }

    private fun executeTests(workerThreads: Int, time: Long, generic: List<String>) {
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
        var benchmarkFile = File("./benchmarks/ethereum/setBoolMapping${workerThreads}.txt")
        if (benchmarkFile.exists()) {
            benchmarkFile.delete()
        }
        benchmarkFile.createNewFile()
        for (index in 0 until workerThreads) {
            if (index > 0) {
                benchmarkFile.appendText("\n")
            }
            val file = File("./benchmarks/ethereum/setBoolMapping${workerThreads}T$index.txt")
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
        benchmarkFile = File("./benchmarks/ethereum/getBoolMapping${workerThreads}.txt")
        if (benchmarkFile.exists()) {
            benchmarkFile.delete()
        }
        benchmarkFile.createNewFile()
        for (index in 0 until workerThreads) {
            if (index > 0) {
                benchmarkFile.appendText("\n")
            }
            val file = File("./benchmarks/ethereum/getBoolMapping${workerThreads}T$index.txt")
            benchmarkFile.appendText(file.readText())
            file.delete()
        }
    }
}