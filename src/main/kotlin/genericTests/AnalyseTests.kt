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
    println("\\begin{tabular}{ |p{6cm}||p{2cm}|p{2cm}|p{2cm}|  }")
    println("\\hline")
    println("\\multicolumn{4}{|c|}{$title} (Number of threads: ${TestInfo.threads}) \\\\")
    println("\\hline")
    println("&Ethereum&DSE& BigchainDB \\\\")
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

    println("Number of transactions & ${timesEth.size} & ${timesBdb.size} & ${timesDse.size} \\\\")
    println("\\hline")
    println("\$\\varnothing\$ transaction time (rounded \$\\mu\$s) & ${timesEth.average().roundToInt()} & ${timesBdb.average().roundToInt()} & ${timesDse.average().roundToInt()} \\\\")
    println("\\hline")
    println("Max time of top 10\\% (\$\\mu\$s) & ${timesEth[(timesEth.size / 10) * 1]} & ${timesBdb[(timesBdb.size / 10) * 1]} & ${timesDse[(timesDse.size / 10) * 1]} \\\\")
    println("Max time of top 20\\% (\$\\mu\$s) & ${timesEth[(timesEth.size / 10) * 2]} & ${timesBdb[(timesBdb.size / 10) * 2]} & ${timesDse[(timesDse.size / 10) * 2]} \\\\")
    println("Max time of top 30\\% (\$\\mu\$s) & ${timesEth[(timesEth.size / 10) * 3]} & ${timesBdb[(timesBdb.size / 10) * 3]} & ${timesDse[(timesDse.size / 10) * 3]} \\\\")
    println("Max time of top 40\\% (\$\\mu\$s) & ${timesEth[(timesEth.size / 10) * 4]} & ${timesBdb[(timesBdb.size / 10) * 4]} & ${timesDse[(timesDse.size / 10) * 4]} \\\\")
    println("Max time of top 50\\% (\$\\mu\$s) & ${timesEth[(timesEth.size / 10) * 5]} & ${timesBdb[(timesBdb.size / 10) * 5]} & ${timesDse[(timesDse.size / 10) * 5]} \\\\")
    println("Max time of top 60\\% (\$\\mu\$s) & ${timesEth[(timesEth.size / 10) * 6]} & ${timesBdb[(timesBdb.size / 10) * 6]} & ${timesDse[(timesDse.size / 10) * 6]} \\\\")
    println("Max time of top 70\\% (\$\\mu\$s) & ${timesEth[(timesEth.size / 10) * 7]} & ${timesBdb[(timesBdb.size / 10) * 7]} & ${timesDse[(timesDse.size / 10) * 7]} \\\\")
    println("Max time of top 80\\% (\$\\mu\$s) & ${timesEth[(timesEth.size / 10) * 8]} & ${timesBdb[(timesBdb.size / 10) * 8]} & ${timesDse[(timesDse.size / 10) * 8]} \\\\")
    println("Max time of top 90\\% (\$\\mu\$s) & ${timesEth[(timesEth.size / 10) * 9]} & ${timesBdb[(timesBdb.size / 10) * 9]} & ${timesDse[(timesDse.size / 10) * 9]} \\\\")
    println("Max time of top 95\\% (\$\\mu\$s) & ${timesEth[(timesEth.size / 20) * 19]} & ${timesBdb[(timesBdb.size / 20) * 19]} & ${timesDse[(timesDse.size / 20) * 19]} \\\\")
    println("Max time of top 100\\% (\$\\mu\$s) & ${timesEth[timesEth.size - 1]} & ${timesBdb[timesBdb.size - 1]} & ${timesDse[timesDse.size -1]} \\\\")

    println("\\hline")

    println("\\end{tabular}")
}
