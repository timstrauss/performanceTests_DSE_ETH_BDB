@file:JvmName("SetupDSE")

import connectionDetails.CassandraUtils

fun main() {
    CassandraUtils.createNewUser("tim", "abc")
}