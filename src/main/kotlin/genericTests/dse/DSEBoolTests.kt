package genericTests.dse

import com.datastax.oss.driver.api.core.CqlSession
import connectionDetails.CassandraConnectionDetails
import genericTests.TestThread
import java.io.File
import java.util.*

object DSEBoolTests {
    fun run(threads: Int) {
        val con = CassandraConnectionDetails("localhost", 9042, "tim", "abc")
        con.openSession()
        val uuid = UUID.randomUUID().toString()
        con.session().execute("INSERT INTO tim_space.generics (uuid, boolvar, intvar, stringvar) VALUES ('$uuid', true, 3, 'test');")
        val timePerTest = 20 * 1000
        val t = File("./benchmarks/dse").mkdirs()

        executeBoolTests(threads, timePerTest, con.session(), uuid)
        con.closeSession()
    }

    private class SetBoolThread(time: Int, val session: CqlSession, threadNum: Int, workerThreads: Int, val uuid: String): TestThread(workerThreads, threadNum, time, true, "setBool") {
        override fun testFunc() {
            session.execute("UPDATE tim_space.generics SET boolvar = ${this.setValue as Boolean} WHERE uuid = '$uuid'")
        }

        override fun preaction() {
            setValue = (System.currentTimeMillis() % 2) == 0L
        }
    }

    private class GetBoolThread(time: Int, val session: CqlSession, threadNum: Int, workerThreads: Int, val uuid: String): TestThread(workerThreads, threadNum, time, true, "getBool") {
        override fun testFunc() {
            session.execute("SELECT boolvar FROM tim_space.generics WHERE uuid = '$uuid").one()?.getBoolean(0)
        }
    }

    private fun executeBoolTests(workerThreads: Int, time: Int, session: CqlSession, uuid: String) {
        val setThreads = Array(workerThreads) {
            val thread = SetBoolThread(time, session, it, workerThreads, uuid)
            thread.start()
            thread
        }
        for (thread in setThreads) {
            thread.join()
        }
        var benchmarkFile = File("./benchmarks/dse/setBool${workerThreads}.txt")
        if (benchmarkFile.exists()) {
            benchmarkFile.delete()
        }
        benchmarkFile.createNewFile()
        for (index in 0 until workerThreads) {
            if (index > 0) {
                benchmarkFile.appendText("\n")
            }
            val file = File("./benchmarks/dse/setBool${workerThreads}T$index.txt")
            benchmarkFile.appendText(file.readText())
            file.delete()
        }


        val getThreads = Array(workerThreads) {
            val thread = GetBoolThread(time, session, it, workerThreads, uuid)
            thread.start()
            thread
        }
        for (thread in getThreads) {
            thread.join()
        }
        benchmarkFile = File("./benchmarks/dse/getBool${workerThreads}.txt")
        if (benchmarkFile.exists()) {
            benchmarkFile.delete()
        }
        benchmarkFile.createNewFile()
        for (index in 0 until workerThreads) {
            if (index > 0) {
                benchmarkFile.appendText("\n")
            }
            val file = File("./benchmarks/dse/getBool${workerThreads}T$index.txt")
            benchmarkFile.appendText(file.readText())
            file.delete()
        }
    }
}