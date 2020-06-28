@file:JvmName("Analyze")

package genericTests

import java.io.File

fun main() {
    printForFileName("getString2", "GET STRING")
    printForFileName("setString2", "SET STRING")
    printForFileName("getBool2", "GET BOOL")
    printForFileName("setBool2", "SET BOOL")
    printForFileName("getInt2", "GET INT")
    printForFileName("setInt2", "SET INT")
    printForFileName("setBoolMapping2", "SET MAPPING")
    printForFileName("getBoolMapping2", "GET MAPPING")
    printForFileName("addArray2", "ADD ARRAY")
    printForFileName("removeArray2", "REMOVE ARRAY")
    printForFileName("getArray2", "GET ARRAY")
}

fun printForFileName(name: String, title: String) {
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

    println(title)
    println()
    println("SUCCESS RATE")
    println("ETH=${successEth.size/benchmarkEth.size.toFloat()}")
    println("DSE=${successDse.size/benchmarkDse.size.toFloat()}")
    println("BDB=${successBdb.size/benchmarkBdb.size.toFloat()}")
    println("OVERALL NUMBER OF TRANSACTIONS")
    println("ETH=${timesEth.size}")
    println("DSE=${timesDse.size}")
    println("BDB=${timesBdb.size}")
    println()
    println("AVERAGE")
    println("ETH=${timesEth.average()}")
    println("DSE=${timesDse.average()}")
    println("BDB=${timesBdb.average()}")
    println()
    println("TOP % UNDER MÜS")
    println("ETH")
    println("10%:\t${timesEth[(timesEth.size / 10) * 1]}")
    println("20%:\t${timesEth[(timesEth.size / 10) * 2]}")
    println("30%:\t${timesEth[(timesEth.size / 10) * 3]}")
    println("40%:\t${timesEth[(timesEth.size / 10) * 4]}")
    println("50%:\t${timesEth[(timesEth.size / 10) * 5]}")
    println("60%:\t${timesEth[(timesEth.size / 10) * 6]}")
    println("70%:\t${timesEth[(timesEth.size / 10) * 7]}")
    println("80%:\t${timesEth[(timesEth.size / 10) * 8]}")
    println("90%:\t${timesEth[(timesEth.size / 10) * 9]}")
    println("100%:\t${timesEth[(timesEth.size - 1)]}")
    println("DSE")
    println("10%:\t${timesDse[(timesDse.size / 10) * 1]}")
    println("20%:\t${timesDse[(timesDse.size / 10) * 2]}")
    println("30%:\t${timesDse[(timesDse.size / 10) * 3]}")
    println("40%:\t${timesDse[(timesDse.size / 10) * 4]}")
    println("50%:\t${timesDse[(timesDse.size / 10) * 5]}")
    println("60%:\t${timesDse[(timesDse.size / 10) * 6]}")
    println("70%:\t${timesDse[(timesDse.size / 10) * 7]}")
    println("80%:\t${timesDse[(timesDse.size / 10) * 8]}")
    println("90%:\t${timesDse[(timesDse.size / 10) * 9]}")
    println("100%:\t${timesDse[(timesDse.size - 1)]}")
    println("ETH")
    println("10%:\t${timesBdb[(timesBdb.size / 10) * 1]}")
    println("20%:\t${timesBdb[(timesBdb.size / 10) * 2]}")
    println("30%:\t${timesBdb[(timesBdb.size / 10) * 3]}")
    println("40%:\t${timesBdb[(timesBdb.size / 10) * 4]}")
    println("50%:\t${timesBdb[(timesBdb.size / 10) * 5]}")
    println("60%:\t${timesBdb[(timesBdb.size / 10) * 6]}")
    println("70%:\t${timesBdb[(timesBdb.size / 10) * 7]}")
    println("80%:\t${timesBdb[(timesBdb.size / 10) * 8]}")
    println("90%:\t${timesBdb[(timesBdb.size / 10) * 9]}")
    println("100%:\t${timesBdb[(timesBdb.size - 1)]}")
}
