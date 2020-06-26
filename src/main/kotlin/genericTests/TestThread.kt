package genericTests

import java.io.File

open class TestThread(val workerThreads: Int, val threadNum: Int, var time: Int, val setTest: Boolean, val fileName: String, val folder: String) : Thread() {
    var setValue: Any = 1

    override fun run() {
        var first = true
        var benchmarkFile = File("./benchmarks/$folder/$fileName${workerThreads}T$threadNum.txt")
        if (benchmarkFile.exists()) {
            benchmarkFile.delete()
        }
        benchmarkFile.createNewFile()
        while(0 < time) {
            if (first) {
                first = false
            } else {
                benchmarkFile.appendText("\n")
            }
            if (setTest) {
                preaction()
            }
            val transactionStart: Long = System.nanoTime()
            testFunc()
            val transactionEnd: Long = System.nanoTime()
            benchmarkFile.appendText("${(transactionEnd - transactionStart) / 1000}|$threadNum")
            time -= ((transactionEnd - transactionStart) / 1000000).toInt()
        }
    }

    open fun testFunc() {

    }

    open fun preaction() {
        setValue = ""
    }
}