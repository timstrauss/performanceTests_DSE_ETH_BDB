@file:JvmName("GenericDSETest")

package genericTests

import connectionDetails.CassandraConnectionDetails
import connectionDetails.CassandraUtils

fun main() {
    CassandraUtils.createNewUser("tim", "abc", 2)
    val con = CassandraConnectionDetails("localhost", 9042, "tim", "abc")
    con.openSession()

    con.session().execute("CREATE TABLE tim_space.generics (uuid string PRIMARY KEY, boolvar boolean, intvar int, stringvar text);")

    con.closeSession()
}