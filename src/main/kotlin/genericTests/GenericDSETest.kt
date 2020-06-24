@file:JvmName("GenericDSETest")

package genericTests

import com.datastax.oss.driver.api.querybuilder.SchemaBuilder
import connectionDetails.CassandraConnectionDetails
import connectionDetails.CassandraUtils
import issuer.CassandraIssuer
import java.io.File
import java.util.*

fun main() {
    val con = CassandraConnectionDetails("localhost", 9042, "tim", "abc")
    con.openSession()

    val uuid = UUID.randomUUID().toString()
    val createStatement = "CREATE TABLE IF NOT EXISTS ${CassandraUtils.getSpaceNameForUser("tim")}.testGeneric (id text PRIMARY KEY, boolVar boolean, stringVar text, intVar int);"
    val createStatement2 = "CREATE TABLE IF NOT EXISTS ${CassandraUtils.getSpaceNameForUser("tim")}.testintarray (id text PRIMARY KEY, intVar int);"
    val createStatement3 = "CREATE TABLE IF NOT EXISTS ${CassandraUtils.getSpaceNameForUser("tim")}.testboolarray (id text PRIMARY KEY, boolVar int);"
    con.session().execute(createStatement)
    con.session().execute(createStatement2)
    con.session().execute(createStatement3)
    val insertStatement = "INSERT INTO ${CassandraUtils.getSpaceNameForUser("tim")}.testGeneric (id, boolVar, stringVar, intVar) VALUES ('$uuid', true, 'text', 3);"
    con.session().execute(insertStatement)

    val min1 = 1 * 60 * 1000
    val t = File("./benchmarks/dse").mkdirs()

    var benchmarkFile = File("./benchmarks/dse/setBool.txt")
    if (benchmarkFile.exists()) {
        benchmarkFile.delete()
    }
    benchmarkFile.createNewFile()
    var start = System.currentTimeMillis()
    var first = true
    while((System.currentTimeMillis() - start) < min1) {
        if (first) {
            first = false
        } else {
            benchmarkFile.appendText("\n")
        }
        val boolVal: Boolean = (System.currentTimeMillis() % 2) == 0L
        val updateStatement = "UPDATE ${CassandraUtils.getSpaceNameForUser("tim")}.testGeneric SET boolVar = $boolVal WHERE id = '$uuid';"
        val transactionStart = System.nanoTime()
        con.session().execute(updateStatement)
        val transactionEnd = System.nanoTime()
        benchmarkFile.appendText("${(transactionEnd - transactionStart) / 1000}")
    }

    benchmarkFile = File("./benchmarks/dse/getBool.txt")
    if (benchmarkFile.exists()) {
        benchmarkFile.delete()
    }
    benchmarkFile.createNewFile()
    start = System.currentTimeMillis()
    first = true
    while((System.currentTimeMillis() - start) < min1) {
        if (first) {
            first = false
        } else {
            benchmarkFile.appendText("\n")
        }
        val updateStatement = "SELECT boolVar FROM ${CassandraUtils.getSpaceNameForUser("tim")}.testGeneric WHERE id = '$uuid';"
        val transactionStart = System.nanoTime()
        con.session().execute(updateStatement)
        val transactionEnd = System.nanoTime()
        benchmarkFile.appendText("${(transactionEnd - transactionStart) / 1000}")
    }

    con.closeSession()
}