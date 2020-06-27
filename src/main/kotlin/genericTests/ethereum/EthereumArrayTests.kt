package genericTests.ethereum

import connectionDetails.EthereumConnectionDetails
import connectionDetails.EthereumContractGasProvider
import de.hpi.cc.datasource.decentralized.ethereum.smartcontracts.Generic
import genericTests.TestThread
import genericTests.TimeToRun
import java.io.File
import java.math.BigInteger

object EthereumArrayTests {
    fun run(threads: Int) {
        val con = EthereumConnectionDetails("http://localhost:8545")
        val generic = Generic.deploy(con.web3j, con.credentials, EthereumContractGasProvider()).send()
        val timePerTest = TimeToRun.get()
        val t = File("./benchmarks/ethereum").mkdirs()

        executeTests(threads, timePerTest, generic)
    }

    private class AddThread(time: Long, val generic: Generic, threadNum: Int, workerThreads: Int): TestThread(workerThreads, threadNum, time, true, "addArray", "ethereum") {
        override fun testFunc(): Boolean {
            return try {
                generic.addInt(setValue as BigInteger).send()
                true
            } catch (e: Exception) {
                false
            }
        }

        override fun preaction() {
            if (lastSuccess) {
                generic.removeInt(setValue as BigInteger).send()
            }
            setValue = BigInteger.valueOf((System.currentTimeMillis() % 2000) + 221)
        }
    }

    private class RemoveThread(time: Long, val generic: Generic, threadNum: Int, workerThreads: Int): TestThread(workerThreads, threadNum, time, true, "removeArray", "ethereum") {
        override fun testFunc(): Boolean {
            return try {
                generic.removeInt(setValue as BigInteger).send()
                true
            } catch (e: Exception) {
                false
            }
        }

        override fun preaction() {
            if (lastSuccess) {
                generic.addInt(setValue as BigInteger).send()
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

    private fun executeTests(workerThreads: Int, time: Long, generic: Generic) {
        val generics = Array<Generic>(workerThreads) {
            val con = EthereumConnectionDetails("http://localhost:8545")
            Generic.load(generic.contractAddress, con.web3j, con.credentials, EthereumContractGasProvider())
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