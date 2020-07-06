package genericTests

object TestInfo {
    val nodeHost = "172.20.8.223"
    val threads = 2
    val sameResource = false

    fun getTimeToRun(): Long {
        return 10L * 60 * 1000 * 1000 * 1000
    }
}