package genericTests.dse

import com.datastax.oss.driver.api.core.CqlSession
import connectionDetails.CassandraConnectionDetails
import genericTests.TestThread
import genericTests.TestInfo
import java.io.File
import java.util.*

object DSEBoolMappingTests {
    fun run(threads: Int) {
        val con = CassandraConnectionDetails(TestInfo.nodeHost, 9042, "tim", "abc")
        con.openSession()
        val uuid = UUID.randomUUID().toString()
        con.session().execute("INSERT INTO tim_space.generics (uuid, boolvar, intvar, stringvar) VALUES ('$uuid', true, 3, 'test');")
        con.session().execute("INSERT INTO tim_space.genericsMapping (uuid, boolvar, stringvar) VALUES ('$uuid', true, 'test');")
        resetArrayTable(con.session(), uuid)
        val timePerTest = TestInfo.getTimeToRun()
        val t = File("./benchmarks/dse").mkdirs()
        Thread.sleep(1000)
        con.closeSession()
        executeTests(threads, timePerTest, uuid)
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

    private class SetThread(time: Long, val session: CqlSession, threadNum: Int, workerThreads: Int, val uuid: String): TestThread(workerThreads, threadNum, time, true, "setBoolMapping", "dse") {
        override fun testFunc(): Boolean {
            val exists = session.execute("SELECT COUNT(*) as num FROM tim_space.genericsMapping WHERE uuid = '$uuid' AND stringvar = 'test2'").one()?.getLong(0) ?: return false
            return if (exists == 0L) {
                session.execute("INSERT INTO tim_space.genericsMapping (uuid, boolvar, stringvar) VALUES ('$uuid', ${setValue as Boolean}, 'test2') IF NOT EXISTS;").wasApplied()
            } else {
                session.execute("UPDATE tim_space.genericsMapping SET boolvar = ${this.setValue as Boolean} WHERE uuid = '$uuid' AND stringvar = 'test2' IF EXISTS").wasApplied()
            }
        }

        override fun preaction() {
            setValue = (System.currentTimeMillis() % 2) == 0L
        }
    }

    private class GetThread(time: Long, val session: CqlSession, threadNum: Int, workerThreads: Int, val uuid: String): TestThread(workerThreads, threadNum, time, false, "getBoolMapping", "dse") {
        override fun testFunc(): Boolean {
            return try {
                session.execute("SELECT boolvar FROM tim_space.genericsMapping WHERE uuid = '$uuid' AND stringvar = 'test'").one()?.getBoolean(0) ?: false
            } catch (e: Exception) {
                false
            }
        }
    }

    private fun executeTests(workerThreads: Int, time: Long, uuid: String) {
        val sessions = Array(workerThreads) {
            val con = CassandraConnectionDetails(TestInfo.nodeHost, 9042, "tim", "abc")
            con.openSession()
            con.session()
        }
        val setThreads = Array(workerThreads) {
            val thread = SetThread(time, sessions[it], it, workerThreads, uuid)
            thread.start()
            thread
        }
        for (thread in setThreads) {
            thread.join()
        }
        var benchmarkFile = File("./benchmarks/dse/setBoolMapping${workerThreads}.txt")
        if (benchmarkFile.exists()) {
            benchmarkFile.delete()
        }
        benchmarkFile.createNewFile()
        for (index in 0 until workerThreads) {
            if (index > 0) {
                benchmarkFile.appendText("\n")
            }
            val file = File("./benchmarks/dse/setBoolMapping${workerThreads}T$index.txt")
            benchmarkFile.appendText(file.readText())
            file.delete()
        }

        val getThreads = Array(workerThreads) {
            val thread = GetThread(time, sessions[it], it, workerThreads, uuid)
            thread.start()
            thread
        }
        for (thread in getThreads) {
            thread.join()
        }
        benchmarkFile = File("./benchmarks/dse/getBoolMapping${workerThreads}.txt")
        if (benchmarkFile.exists()) {
            benchmarkFile.delete()
        }
        benchmarkFile.createNewFile()
        for (index in 0 until workerThreads) {
            if (index > 0) {
                benchmarkFile.appendText("\n")
            }
            val file = File("./benchmarks/dse/getBoolMapping${workerThreads}T$index.txt")
            benchmarkFile.appendText(file.readText())
            file.delete()
        }

        for (session in sessions) {
            session.close()
        }
    }
}