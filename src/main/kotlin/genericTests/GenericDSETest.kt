@file:JvmName("GenericDSETest")

package genericTests

import connectionDetails.CassandraConnectionDetails
import connectionDetails.CassandraUtils
import genericTests.dse.DSEBoolTests
import genericTests.dse.DSEIntTests
import genericTests.dse.DSEStringTests

fun main() {
    CassandraUtils.createNewUser("tim", "abc", 2)
    val con = CassandraConnectionDetails("localhost", 9042, "tim", "abc")
    con.openSession()

    con.session().execute("CREATE TABLE tim_space.generics (uuid text PRIMARY KEY, boolvar boolean, intvar int, stringvar text);")

    con.closeSession()

    DSEBoolTests.run(2)
    DSEIntTests.run(2)
    DSEStringTests.run(2)
}