@file:JvmName("GenericDSETest")

package genericTests

import connectionDetails.CassandraConnectionDetails
import connectionDetails.CassandraUtils
import genericTests.dse.*

fun main() {
    CassandraUtils.createNewUser("tim", "abc", 2)
    val con = CassandraConnectionDetails(TestInfo.nodeHost, 9042, "tim", "abc")
    con.openSession()

    con.session().execute("CREATE TABLE IF NOT EXISTS tim_space.generics (uuid text PRIMARY KEY, boolvar boolean, intvar int, stringvar text);")
    con.session().execute("CREATE TABLE IF NOT EXISTS tim_space.genericsMapping (uuid text, stringvar text, boolvar boolean, PRIMARY KEY(uuid, stringvar));")
    con.session().execute("CREATE TABLE IF NOT EXISTS tim_space.genericsArray (id UUID, uuid text, intvar int, PRIMARY KEY((uuid), intvar, id));")

    Thread.sleep(1000)

    con.closeSession()

    DSEBoolTests.run(2)
    println("bool done")
    DSEIntTests.run(2)
    println("int done")
    DSEStringTests.run(2)
    println("string done")
    DSEBoolMappingTests.run(2)
    println("mapping done")
    DSEArrayTests.run(2)
    println("array done")
}