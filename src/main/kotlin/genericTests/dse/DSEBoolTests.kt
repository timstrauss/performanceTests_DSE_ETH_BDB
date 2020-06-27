package genericTests.dse

import com.datastax.oss.driver.api.core.CqlSession
import connectionDetails.CassandraConnectionDetails
import genericTests.TestThread
import genericTests.TimeToRun
import java.io.File
import java.util.*

object DSEBoolTests {
    fun run(threads: Int) {
        val con = CassandraConnectionDetails("localhost", 9042, "tim", "abc")
        con.openSession()
        val uuid = UUID.randomUUID().toString()
        con.session().execute("INSERT INTO tim_space.generics (uuid, boolvar, intvar, stringvar) VALUES ('$uuid', true, 3, 'test');")
        con.session().execute("INSERT INTO tim_space.genericsMapping (uuid, boolvar, stringvar) VALUES ('$uuid', true, 'test');")
        resetArrayTable(con.session(), uuid)
        val timePerTest = TimeToRun.get()
        val t = File("./benchmarks/dse").mkdirs()
        Thread.sleep(1000)
        con.closeSession()
        executeBoolTests(threads, timePerTest, uuid)
    }

    private fun resetArrayTable(session: CqlSession, uuid: String) {
        session.execute("TRUNCATE tim_space.genericsArray;")
        var batch = "BEGIN BATCH\n"
        for (i in 0 until 221) {
            batch += "INSERT INTO tim_space.genericsArray (id, uuid, intvar) VALUES (now(), '$uuid', $i);\n"
        }
        batch += "APPLY BATCH;"
        session.execute(batch)
    }

    private class SetBoolThread(time: Long, val session: CqlSession, threadNum: Int, workerThreads: Int, val uuid: String): TestThread(workerThreads, threadNum, time, true, "setBool", "dse") {
        override fun testFunc(): Boolean {
            return session.execute("UPDATE tim_space.generics SET boolvar = ${this.setValue as Boolean} WHERE uuid = '$uuid'").wasApplied()
        }

        override fun preaction() {
            setValue = (System.currentTimeMillis() % 2) == 0L
        }
    }

    private class GetBoolThread(time: Long, val session: CqlSession, threadNum: Int, workerThreads: Int, val uuid: String): TestThread(workerThreads, threadNum, time, false, "getBool", "dse") {
        override fun testFunc(): Boolean {
            return try {
                session.execute("SELECT boolvar FROM tim_space.generics WHERE uuid = '$uuid'").one()?.getBoolean(0)
                true
            } catch (e: Exception) {
                false
            }
        }
    }

    private fun executeBoolTests(workerThreads: Int, time: Long, uuid: String) {
        val sessions = Array(workerThreads) {
            val con = CassandraConnectionDetails("localhost", 9042, "tim", "abc")
            con.openSession()
            con.session()
        }
        val setThreads = Array(workerThreads) {
            val thread = SetBoolThread(time, sessions[it], it, workerThreads, uuid)
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
            val thread = GetBoolThread(time, sessions[it], it, workerThreads, uuid)
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

        for (session in sessions) {
            session.close()
        }
    }
}