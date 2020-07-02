@file:JvmName("Analyze")

package genericTests

import java.io.File

fun main() {
    printForFileName("getString2", "Get String")
    printForFileName("setString2", "Set String")
    printForFileName("getBool2", "Get Bool")
    printForFileName("setBool2", "Set Bool")
    printForFileName("getInt2", "Get Integer")
    printForFileName("setInt2", "Set Integer")
    printForFileName("setBoolMapping2", "Set Mapping")
    printForFileName("getBoolMapping2", "Get Mapping")
    printForFileName("addArray2", "Add Array")
    printForFileName("removeArray2", "Remove Array")
    printForFileName("getArray2", "Get Array")
}

fun printForFileName(name: String, title: String) {
    println("\\begin{tabular}{ |p{3cm}||p{3cm}|p{3cm}|p{3cm}|  }")
    println("\\hline")
    println("\\multicolumn{4}{|c|}{$title} \\\\")
    println("\\hline")
    println("&Ethereum&DataStax Enterprise& BigchainDB \\\\")
    println("\\hline")
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

    println("Number of transactions & ${timesEth.size} & ${timesDse.size} & ${timesBdb.size} \\\\")
    println("\\hline")
    println("Average transaction time (\$\\mu\$s) & ${timesEth.average()} & ${timesDse.average()} & ${timesBdb.average()} \\\\")
    println("\\hline")
    println("Max time of top 10% (\$\\mu\$s) & ${timesEth[(timesEth.size / 10) * 1]} & ${timesDse[(timesDse.size / 10) * 1]} & ${timesBdb[(timesBdb.size / 10) * 1]} \\\\")
    println("Max time of top 20% (\$\\mu\$s) & ${timesEth[(timesEth.size / 10) * 2]} & ${timesDse[(timesDse.size / 10) * 2]} & ${timesBdb[(timesBdb.size / 10) * 2]} \\\\")
    println("Max time of top 30% (\$\\mu\$s) & ${timesEth[(timesEth.size / 10) * 3]} & ${timesDse[(timesDse.size / 10) * 3]} & ${timesBdb[(timesBdb.size / 10) * 3]} \\\\")
    println("Max time of top 40% (\$\\mu\$s) & ${timesEth[(timesEth.size / 10) * 4]} & ${timesDse[(timesDse.size / 10) * 4]} & ${timesBdb[(timesBdb.size / 10) * 4]} \\\\")
    println("Max time of top 50% (\$\\mu\$s) & ${timesEth[(timesEth.size / 10) * 5]} & ${timesDse[(timesDse.size / 10) * 5]} & ${timesBdb[(timesBdb.size / 10) * 5]} \\\\")
    println("Max time of top 60% (\$\\mu\$s) & ${timesEth[(timesEth.size / 10) * 6]} & ${timesDse[(timesDse.size / 10) * 6]} & ${timesBdb[(timesBdb.size / 10) * 6]} \\\\")
    println("Max time of top 70% (\$\\mu\$s) & ${timesEth[(timesEth.size / 10) * 7]} & ${timesDse[(timesDse.size / 10) * 7]} & ${timesBdb[(timesBdb.size / 10) * 7]} \\\\")
    println("Max time of top 80% (\$\\mu\$s) & ${timesEth[(timesEth.size / 10) * 8]} & ${timesDse[(timesDse.size / 10) * 8]} & ${timesBdb[(timesBdb.size / 10) * 8]} \\\\")
    println("Max time of top 90% (\$\\mu\$s) & ${timesEth[(timesEth.size / 10) * 9]} & ${timesDse[(timesDse.size / 10) * 9]} & ${timesBdb[(timesBdb.size / 10) * 9]} \\\\")
    println("Max time of top 95% (\$\\mu\$s) & ${timesEth[(timesEth.size / 20) * 19]} & ${timesDse[(timesDse.size / 20) * 19]} & ${timesBdb[(timesBdb.size / 20) * 19]} \\\\")
    println("Max time of top 100% (\$\\mu\$s) & ${timesEth[timesEth.size - 1]} & ${timesDse[timesDse.size - 1]} & ${timesBdb[timesBdb.size -1]} \\\\")

    println("\\hline")

    println("\\end{tabular}")
}
