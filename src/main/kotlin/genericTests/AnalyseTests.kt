@file:JvmName("Analyze")

package genericTests

import java.io.File
import kotlin.math.roundToInt

fun main() {
    printForFileName("getString${TestInfo.threads}", "Get String")
    printForFileName("setString${TestInfo.threads}", "Set String")
    printForFileName("getBool${TestInfo.threads}", "Get Bool")
    printForFileName("setBool${TestInfo.threads}", "Set Bool")
    printForFileName("getInt${TestInfo.threads}", "Get Integer")
    printForFileName("setInt${TestInfo.threads}", "Set Integer")
    printForFileName("setBoolMapping${TestInfo.threads}", "Set Mapping")
    printForFileName("getBoolMapping${TestInfo.threads}", "Get Mapping")
    printForFileName("addArray${TestInfo.threads}", "Add Array")
    printForFileName("removeArray${TestInfo.threads}", "Remove Array")
    printForFileName("getArray${TestInfo.threads}", "Get Array")
}

fun printForFileName(name: String, title: String) {
    println("\\begin{table}")
    println("\\begin{tabular}{ |p{6cm}||p{2cm}|p{2cm}|p{2cm}|  }")
    println("\\hline")
    println("\\multicolumn{4}{|c|}{$title (Number of threads: ${TestInfo.threads}) } \\\\")
    println("\\hline")
    println("&Ethereum&BigchainDB&DSE \\\\")
    println("\\hline")


    val benchmarkEthFile = File("./benchmarks/ethereum/$name.txt")
    val benchmarkEth = benchmarkEthFile.readText().lines().filter { it != "" }
    val successEth = benchmarkEth.filter { it.last() == '1' }
    val timesEth = LongArray(successEth.size) { it -> successEth[it].split("|")[0].toLong()}
    timesEth.sort()

    val benchmarkDseFile = File("./benchmarks/dse/$name.txt")
    val benchmarkDse = benchmarkDseFile.readText().lines().filter { it != "" }
    val successDse = benchmarkDse.filter { it.last() == '1' }
    val timesDse = LongArray(successDse.size) {it -> successDse[it].split("|")[0].toLong()}
    timesDse.sort()

    val benchmarkBdbFile = File("./benchmarks/bdb/$name.txt")
    val benchmarkBdb = benchmarkBdbFile.readText().lines().filter { it != "" }
    val successBdb = benchmarkBdb.filter { it.last() == '1' }
    val timesBdb = LongArray(successBdb.size) {it -> successBdb[it].split("|")[0].toLong()}
    timesBdb.sort()

    println("Number of transactions & ${formatter(timesEth.size)} & ${formatter(timesBdb.size)} & ${formatter(timesDse.size)} \\\\")
    println("\\hline")
    println("\$\\varnothing\$ transaction time (rounded \$\\mu\$s) & ${formatter(timesEth.average().roundToInt())} & ${formatter(timesBdb.average().roundToInt())} & ${formatter(timesDse.average().roundToInt())} \\\\")
    println("\\hline")
    println("Max time of top 10\\% (\$\\mu\$s) & ${formatter(timesEth[(timesEth.size / 10) * 1])} & ${formatter(timesBdb[(timesBdb.size / 10) * 1])} & ${formatter(timesDse[(timesDse.size / 10) * 1])} \\\\")
    println("Max time of top 50\\% (\$\\mu\$s) & ${formatter(timesEth[(timesEth.size / 10) * 5])} & ${formatter(timesBdb[(timesBdb.size / 10) * 5])} & ${formatter(timesDse[(timesDse.size / 10) * 5])} \\\\")
    println("Max time of top 80\\% (\$\\mu\$s) & ${formatter(timesEth[(timesEth.size / 10) * 8])} & ${formatter(timesBdb[(timesBdb.size / 10) * 8])} & ${formatter(timesDse[(timesDse.size / 10) * 8])} \\\\")
    println("Max time of top 90\\% (\$\\mu\$s) & ${formatter(timesEth[(timesEth.size / 10) * 9])} & ${formatter(timesBdb[(timesBdb.size / 10) * 9])} & ${formatter(timesDse[(timesDse.size / 10) * 9])} \\\\")
    println("Max time of top 95\\% (\$\\mu\$s) & ${formatter(timesEth[(timesEth.size / 20) * 19])} & ${formatter(timesBdb[(timesBdb.size / 20) * 19])} & ${formatter(timesDse[(timesDse.size / 20) * 19])} \\\\")
    println("Max time of top 100\\% (\$\\mu\$s) & ${formatter(timesEth[timesEth.size - 1])} & ${formatter(timesBdb[timesBdb.size - 1])} & ${formatter(timesDse[timesDse.size -1])} \\\\")

    println("\\hline")

    println("\\end{tabular}")
    println("\\label{table:${title.replace(' ', '_')}_${TestInfo.threads}}")
    println("\\end{table}")
}

fun formatter(i: Int): String {
    var num = i
    var formatted = ""
    while (num > 0) {
        if (formatted == "") {
            formatted = toString(num % 1000)
        } else {
            formatted = "${toString(num % 1000)}," + formatted
        }
        num /= 1000
    }
    while (formatted[0] == '0') {
        formatted = formatted.substring(1)
    }
    return formatted
}

fun formatter(i: Long): String {
    var num = i
    var formatted = ""
    while (num > 0) {
        if (formatted == "") {
            formatted = toString(num % 1000)
        } else {
            formatted = "${toString(num % 1000)}," + formatted
        }
        num /= 1000
    }
    while (formatted[0] == '0') {
        formatted = formatted.substring(1)
    }
    return formatted
}

fun toString(i: Int): String {
    return if (i < 10) {
        "00$i"
    } else if (i < 100) {
        "0$i"
    } else {
        i.toString()
    }
}

fun toString(i: Long): String {
    return if (i < 10) {
        "00$i"
    } else if (i < 100) {
        "0$i"
    } else {
        i.toString()
    }
}