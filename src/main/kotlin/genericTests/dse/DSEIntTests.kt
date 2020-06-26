package genericTests.dse

import com.datastax.oss.driver.api.core.CqlSession
import connectionDetails.CassandraConnectionDetails
import genericTests.TestThread
import java.io.File
import java.util.*

object DSEIntTests {
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

    private class SetIntThread(time: Int, val session: CqlSession, threadNum: Int, workerThreads: Int, val uuid: String): TestThread(workerThreads, threadNum, time, true, "setInt") {
        override fun testFunc() {
            session.execute("UPDATE tim_space.generics SET intvar = ${this.setValue as Int} WHERE uuid = '$uuid'")
        }

        override fun preaction() {
            setValue = (System.currentTimeMillis() % 2000).toInt()
        }
    }

    private class GetIntThread(time: Int, val session: CqlSession, threadNum: Int, workerThreads: Int, val uuid: String): TestThread(workerThreads, threadNum, time, true, "getInt") {
        override fun testFunc() {
            session.execute("SELECT intvar FROM tim_space.generics WHERE uuid = '$uuid").one()?.getInt(0)
        }
    }

    private fun executeBoolTests(workerThreads: Int, time: Int, session: CqlSession, uuid: String) {
        val setThreads = Array(workerThreads) {
            val thread = SetIntThread(time, session, it, workerThreads, uuid)
            thread.start()
            thread
        }
        for (thread in setThreads) {
            thread.join()
        }
        var benchmarkFile = File("./benchmarks/dse/setInt${workerThreads}.txt")
        if (benchmarkFile.exists()) {
            benchmarkFile.delete()
        }
        benchmarkFile.createNewFile()
        for (index in 0 until workerThreads) {
            if (index > 0) {
                benchmarkFile.appendText("\n")
            }
            val file = File("./benchmarks/dse/setInt${workerThreads}T$index.txt")
            benchmarkFile.appendText(file.readText())
            file.delete()
        }


        val getThreads = Array(workerThreads) {
            val thread = GetIntThread(time, session, it, workerThreads, uuid)
            thread.start()
            thread
        }
        for (thread in getThreads) {
            thread.join()
        }
        benchmarkFile = File("./benchmarks/dse/getInt${workerThreads}.txt")
        if (benchmarkFile.exists()) {
            benchmarkFile.delete()
        }
        benchmarkFile.createNewFile()
        for (index in 0 until workerThreads) {
            if (index > 0) {
                benchmarkFile.appendText("\n")
            }
            val file = File("./benchmarks/dse/getInt${workerThreads}T$index.txt")
            benchmarkFile.appendText(file.readText())
            file.delete()
        }
    }
}