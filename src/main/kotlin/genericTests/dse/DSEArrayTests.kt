package genericTests.dse

import com.datastax.oss.driver.api.core.CqlSession
import connectionDetails.CassandraConnectionDetails
import genericTests.TestThread
import genericTests.TestInfo
import java.io.File
import java.util.*

object DSEArrayTests {
    fun run(threads: Int) {
        val con = CassandraConnectionDetails(TestInfo.nodeHost, 9042, "tim", "abc")
        con.openSession()
        val uuids = mutableListOf<String>()
        for (i in 0 until threads) {
            val uuid = UUID.randomUUID().toString()
            con.session().execute("INSERT INTO tim_space.generics (uuid, boolvar, intvar, stringvar) VALUES ('$uuid', true, 3, 'test');")
            con.session().execute("INSERT INTO tim_space.genericsMapping (uuid, boolvar, stringvar) VALUES ('$uuid', true, 'test');")
            uuids.add(uuid)
        }
        resetArrayTable(con.session(), uuids)
        val timePerTest = TestInfo.getTimeToRun()
        val t = File("./benchmarks/dse").mkdirs()
        Thread.sleep(1000)
        con.closeSession()
        executeTests(threads, timePerTest, uuids)
    }

    private fun resetArrayTable(session: CqlSession, uuids: List<String>) {
        for (uuid in uuids) {
            session.execute("TRUNCATE tim_space.genericsArray;")
            var batch = "BEGIN BATCH\n"
            for (i in 0 until 221) {
                batch += "INSERT INTO tim_space.genericsArray (id, uuid, intvar) VALUES (now(), '$uuid', $i);\n"
            }
            batch += "APPLY BATCH;"
            session.execute(batch)
        }
    }

    private class AddThread(time: Long, val session: CqlSession, threadNum: Int, workerThreads: Int, val uuid: String) :
        TestThread(workerThreads, threadNum, time, true, "addArray", "dse") {
        var id: String? = null

        override fun testFunc(): Boolean {
            id = UUID.randomUUID().toString()
            var success = false
            while (!success) {
                try {
                    session.execute("INSERT INTO tim_space.genericsArray (id, uuid, intvar) VALUES ($id, '$uuid', ${setValue as Int}) IF NOT EXISTS;")
                        .wasApplied()
                    success = true
                } catch (e: Exception) {
                }
            }
            return true
        }

        override fun preaction() {
            if (lastSuccess && id != null) {
                var success = false
                while (!success) {
                    try {
                        session.execute("DELETE FROM tim_space.genericsArray WHERE id = $id AND uuid = '$uuid' AND intvar = ${setValue as Int} IF EXISTS;")
                        success = true
                    } catch (e: Exception) {
                    }
                }
            }
            setValue = (System.currentTimeMillis() % 2000).toInt() + 221
        }
    }

    private class RemoveThread(
        time: Long,
        val session: CqlSession,
        threadNum: Int,
        workerThreads: Int,
        val uuid: String
    ) : TestThread(workerThreads, threadNum, time, true, "removeArray", "dse") {
        var id: UUID? = null

        override fun testFunc(): Boolean {
            var success = false
            while (!success) {
                try {
                    id =
                        session.execute("SELECT id FROM tim_space.genericsArray WHERE uuid = '$uuid' AND intvar = ${setValue as Int} LIMIT 1;").one()?.getUuid(
                            0
                        )
                            ?: return false
                    session.execute("DELETE FROM tim_space.genericsArray WHERE id = $id AND uuid = '$uuid' AND intvar = ${setValue as Int}  IF EXISTS;")
                        .wasApplied()
                    success = true
                } catch (e: Exception) {
                }
            }
            return true
        }

        override fun preaction() {
            if (lastSuccess) {
                var success = false
                while (!success) {
                    try {
                        session.execute("INSERT INTO tim_space.genericsArray (id, uuid, intvar) VALUES ($id, '$uuid', ${setValue as Int}) IF NOT EXISTS;")
                        success = true
                    } catch (e: Exception) {
                    }
                }
            }
            setValue = (System.currentTimeMillis() % 221).toInt()
        }
    }

    private class GetThread(time: Long, val session: CqlSession, threadNum: Int, workerThreads: Int, val uuid: String) :
        TestThread(workerThreads, threadNum, time, false, "getArray", "dse") {
        override fun testFunc(): Boolean {
            return try {
                session.execute("SELECT intvar FROM tim_space.genericsArray WHERE uuid = '$uuid'").all()
                true
            } catch (e: Exception) {
                e.printStackTrace()
                false
            }
        }
    }

    private fun executeTests(workerThreads: Int, time: Long, uuid: List<String>) {
        val sessions = Array(workerThreads) {
            val con = CassandraConnectionDetails(TestInfo.nodeHost, 9042, "tim", "abc")
            con.openSession()
            con.session()
        }
        val setThreads = Array(workerThreads) {
            val thread = if (TestInfo.sameResource) {
                AddThread(time, sessions[it], it, workerThreads, uuid[0])
            } else {
                AddThread(time, sessions[it], it, workerThreads, uuid[it])
            }
            thread.start()
            thread
        }
        for (thread in setThreads) {
            thread.join()
        }
        var benchmarkFile = File("./benchmarks/dse/addArray${workerThreads}.txt")
        if (benchmarkFile.exists()) {
            benchmarkFile.delete()
        }
        benchmarkFile.createNewFile()
        for (index in 0 until workerThreads) {
            if (index > 0) {
                benchmarkFile.appendText("\n")
            }
            val file = File("./benchmarks/dse/addArray${workerThreads}T$index.txt")
            benchmarkFile.appendText(file.readText())
            file.delete()
        }

        val removeThreads = Array(workerThreads) {
            val thread = if (TestInfo.sameResource) {
                RemoveThread(time, sessions[it], it, workerThreads, uuid[0])
            } else {
                RemoveThread(time, sessions[it], it, workerThreads, uuid[it])
            }
            thread.start()
            thread
        }
        for (thread in removeThreads) {
            thread.join()
        }
        benchmarkFile = File("./benchmarks/dse/removeArray${workerThreads}.txt")
        if (benchmarkFile.exists()) {
            benchmarkFile.delete()
        }
        benchmarkFile.createNewFile()
        for (index in 0 until workerThreads) {
            if (index > 0) {
                benchmarkFile.appendText("\n")
            }
            val file = File("./benchmarks/dse/removeArray${workerThreads}T$index.txt")
            benchmarkFile.appendText(file.readText())
            file.delete()
        }

        resetArrayTable(sessions[0], uuid)

        val getThreads = Array(workerThreads) {
            val thread = if (TestInfo.sameResource) {
                GetThread(time, sessions[it], it, workerThreads, uuid[0])
            } else {
                GetThread(time, sessions[it], it, workerThreads, uuid[it])
            }
            thread.start()
            thread
        }
        for (thread in getThreads) {
            thread.join()
        }
        benchmarkFile = File("./benchmarks/dse/getArray${workerThreads}.txt")
        if (benchmarkFile.exists()) {
            benchmarkFile.delete()
        }
        benchmarkFile.createNewFile()
        for (index in 0 until workerThreads) {
            if (index > 0) {
                benchmarkFile.appendText("\n")
            }
            val file = File("./benchmarks/dse/getArray${workerThreads}T$index.txt")
            benchmarkFile.appendText(file.readText())
            file.delete()
        }

        for (session in sessions) {
            session.close()
        }
    }
}