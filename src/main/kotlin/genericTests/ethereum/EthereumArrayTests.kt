package genericTests.ethereum

import connectionDetails.EthereumConnectionDetails
import connectionDetails.EthereumContractGasProvider
import de.hpi.cc.datasource.decentralized.ethereum.smartcontracts.Generic
import genericTests.TestThread
import genericTests.TestInfo
import org.web3j.tx.ReadonlyTransactionManager
import java.io.File
import java.math.BigInteger

object EthereumArrayTests {
    fun run(threads: Int, generics: List<String>) {
        val timePerTest = TestInfo.getTimeToRun()
        val t = File("./benchmarks/ethereum").mkdirs()

        executeTests(threads, timePerTest, generics)
    }

    private class AddThread(time: Long, val generic: Generic, threadNum: Int, workerThreads: Int): TestThread(workerThreads, threadNum, time, true, "addArray", "ethereum") {
        override fun testFunc(): Boolean {
            var success = false
            while (!success) {
                try {
                    generic.addInt(setValue as BigInteger).send()
                    success = true
                } catch (e: Exception) {
                    success = false
                }
            }
            return true
        }

        override fun preaction() {
            if (lastSuccess) {
                var success = false
                while (!success && generic.ints.send().size > 442) {
                    try {
                        generic.setInts(MutableList(221) { BigInteger.valueOf(it.toLong()) }).send()
                        success = true
                    } catch (e: Exception) {
                        success = false
                    }
                }
            }
            setValue = BigInteger.valueOf((System.currentTimeMillis() % 2000) + 221)
        }
    }

    private class RemoveThread(time: Long, val generic: Generic, threadNum: Int, workerThreads: Int): TestThread(workerThreads, threadNum, time, true, "removeArray", "ethereum") {
        override fun testFunc(): Boolean {
            var success = false
            while (!success) {
                try {
                    generic.removeInt(setValue as BigInteger).send()
                    success = true
                } catch (e: Exception) {
                    success = false
                }
            }
            return true
        }

        override fun preaction() {
            if (lastSuccess) {
                var success = false
                while (!success && generic.ints.send().size < 100) {
                    try {
                        generic.setInts(MutableList(221) { BigInteger.valueOf(it.toLong()) }).send()
                        success = true
                    } catch (e: Exception) {
                        success = false
                    }
                }
            }
            setValue = BigInteger.valueOf(System.currentTimeMillis() % 221)
        }
    }

    private class GetThread(time: Long, val generic: Generic, threadNum: Int, workerThreads: Int): TestThread(workerThreads, threadNum, time, false, "getArray", "ethereum") {
        override fun testFunc():Boolean {
            return try {
                generic.ints.send()
                true
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
            val thread = AddThread(time, generics[it], it, workerThreads)
            thread.start()
            thread
        }
        for (thread in setThreads) {
            thread.join()
        }
        var benchmarkFile = File("./benchmarks/ethereum/addArray${workerThreads}.txt")
        if (benchmarkFile.exists()) {
            benchmarkFile.delete()
        }
        benchmarkFile.createNewFile()
        for (index in 0 until workerThreads) {
            if (index > 0) {
                benchmarkFile.appendText("\n")
            }
            val file = File("./benchmarks/ethereum/addArray${workerThreads}T$index.txt")
            benchmarkFile.appendText(file.readText())
            file.delete()
        }

        val removeThreads = Array(workerThreads) {
            val thread = RemoveThread(time, generics[it], it, workerThreads)
            thread.start()
            thread
        }
        for (thread in removeThreads) {
            thread.join()
        }
        benchmarkFile = File("./benchmarks/ethereum/removeArray${workerThreads}.txt")
        if (benchmarkFile.exists()) {
            benchmarkFile.delete()
        }
        benchmarkFile.createNewFile()
        for (index in 0 until workerThreads) {
            if (index > 0) {
                benchmarkFile.appendText("\n")
            }
            val file = File("./benchmarks/ethereum/removeArray${workerThreads}T$index.txt")
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
        benchmarkFile = File("./benchmarks/ethereum/getArray${workerThreads}.txt")
        if (benchmarkFile.exists()) {
            benchmarkFile.delete()
        }
        benchmarkFile.createNewFile()
        for (index in 0 until workerThreads) {
            if (index > 0) {
                benchmarkFile.appendText("\n")
            }
            val file = File("./benchmarks/ethereum/getArray${workerThreads}T$index.txt")
            benchmarkFile.appendText(file.readText())
            file.delete()
        }
    }
}