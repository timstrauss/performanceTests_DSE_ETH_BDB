package genericTests

import java.io.File
import kotlin.concurrent.thread

open class TestThread(val workerThreads: Int, val threadNum: Int, var time: Long, val setTest: Boolean, val fileName: String, val folder: String) : Thread() {
    var setValue: Any = 100000
    val creation = System.nanoTime() / 1000
    var text = ""
    var lastSuccess = false
    var needReset = false

    override fun run() {
        var benchmarkFile = File("./benchmarks/$folder/$fileName${workerThreads}T$threadNum.txt")
        if (benchmarkFile.exists()) {
            benchmarkFile.delete()
        }
        benchmarkFile.createNewFile()
        var done = false
        val t = thread(true) {
            while (!done) {
                benchmarkFile.appendText(setText("", true))
                sleep(1000)
            }
        }
        if (setTest) {
            preaction()
        }
        val transactionStartFirst: Long = System.nanoTime()
        val successFirst = testFunc()
        val transactionEndFirst: Long = System.nanoTime()
        setReset(true)
        lastSuccess = successFirst
        setText("${(transactionEndFirst - transactionStartFirst) / 1000}|$threadNum|${(transactionStartFirst / 1000) - creation}|${successFirst.toInt()}", false)
        time -= (transactionEndFirst - transactionStartFirst)
        while(0 < time) {
            if (setTest) {
                preaction()
            }
            val transactionStart: Long = System.nanoTime()
            val success = testFunc()
            val transactionEnd: Long = System.nanoTime()
            lastSuccess = success
            setText("\n${(transactionEnd - transactionStart) / 1000}|$threadNum|${(transactionStart / 1000) - creation}|${success.toInt()}", false)
            time -= (transactionEnd - transactionStart)
        }
        done = true
        t.join()
    }

    open fun testFunc(): Boolean {
        return true
    }

    open fun preaction() {
        setValue = ""
    }

    @Synchronized
    private fun setText(addVal: String, reset: Boolean): String {
        val returnVal = text
        if (reset) {
            text = ""
        } else {
            text += addVal
        }
        return returnVal
    }

    @Synchronized
    protected fun setReset(reset: Boolean) {
        needReset = reset
    }

    fun Boolean.toInt() = if (this) 1 else 0
}