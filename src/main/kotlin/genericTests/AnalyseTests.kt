@file:JvmName("Analyze")

package genericTests

import java.io.File

fun main() {
    var benchmarkEthFile = File("./benchmarks/ethereum/setBool2.txt")
    var benchmarkEth = benchmarkEthFile.readText().lines().filter { it != "" }
    var successEth = benchmarkEth.filter { it.last() == '1' }
    var timesEth = LongArray(successEth.size) { it -> successEth[it].split("|")[0].toLong()}
    timesEth.sort()

    var benchmarkDseFile = File("./benchmarks/dse/setBool2.txt")
    var benchmarkDse = benchmarkDseFile.readText().lines().filter { it != "" }
    var successDse = benchmarkDse.filter { it.last() == '1' }
    var timesDse = LongArray(successDse.size) {it -> successDse[it].split("|")[0].toLong()}
    timesDse.sort()

    var benchmarkBdbFile = File("./benchmarks/bdb/setBool2.txt")
    var benchmarkBdb = benchmarkBdbFile.readText().lines().filter { it != "" }
    var successBdb = benchmarkBdb.filter { it.last() == '1' }
    var timesBdb = LongArray(successBdb.size) {it -> successBdb[it].split("|")[0].toLong()}
    timesBdb.sort()

    println("SET BOOLEAN")
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

    benchmarkEthFile = File("./benchmarks/ethereum/getBool2.txt")
    benchmarkEth = benchmarkEthFile.readText().lines().filter { it != "" }
    successEth = benchmarkEth.filter { it.last() == '1' }
    timesEth = LongArray(successEth.size) { it -> successEth[it].split("|")[0].toLong()}
    timesEth.sort()

    benchmarkDseFile = File("./benchmarks/dse/getBool2.txt")
    benchmarkDse = benchmarkDseFile.readText().lines().filter { it != "" }
    successDse = benchmarkDse.filter { it.last() == '1' }
    timesDse = LongArray(successDse.size) {it -> successDse[it].split("|")[0].toLong()}
    timesDse.sort()

    benchmarkBdbFile = File("./benchmarks/bdb/getBool2.txt")
    benchmarkBdb = benchmarkBdbFile.readText().lines().filter { it != "" }
    successBdb = benchmarkBdb.filter { it.last() == '1' }
    timesBdb = LongArray(successBdb.size) {it -> successBdb[it].split("|")[0].toLong()}
    timesBdb.sort()

    println("GET BOOLEAN")
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

    benchmarkEthFile = File("./benchmarks/ethereum/setInt2.txt")
    benchmarkEth = benchmarkEthFile.readText().lines().filter { it != "" }
    successEth = benchmarkEth.filter { it.last() == '1' }
    timesEth = LongArray(successEth.size) { it -> successEth[it].split("|")[0].toLong()}
    timesEth.sort()

    benchmarkDseFile = File("./benchmarks/dse/setInt2.txt")
    benchmarkDse = benchmarkDseFile.readText().lines().filter { it != "" }
    successDse = benchmarkDse.filter { it.last() == '1' }
    timesDse = LongArray(successDse.size) {it -> successDse[it].split("|")[0].toLong()}
    timesDse.sort()

    benchmarkBdbFile = File("./benchmarks/bdb/setInt2.txt")
    benchmarkBdb = benchmarkBdbFile.readText().lines().filter { it != "" }
    successBdb = benchmarkBdb.filter { it.last() == '1' }
    timesBdb = LongArray(successBdb.size) {it -> successBdb[it].split("|")[0].toLong()}
    timesBdb.sort()

    println("SET INT")
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

    benchmarkEthFile = File("./benchmarks/ethereum/getInt2.txt")
    benchmarkEth = benchmarkEthFile.readText().lines().filter { it != "" }
    successEth = benchmarkEth.filter { it.last() == '1' }
    timesEth = LongArray(successEth.size) { it -> successEth[it].split("|")[0].toLong()}
    timesEth.sort()

    benchmarkDseFile = File("./benchmarks/dse/getInt2.txt")
    benchmarkDse = benchmarkDseFile.readText().lines().filter { it != "" }
    successDse = benchmarkDse.filter { it.last() == '1' }
    timesDse = LongArray(successDse.size) {it -> successDse[it].split("|")[0].toLong()}
    timesDse.sort()

    benchmarkBdbFile = File("./benchmarks/bdb/getInt2.txt")
    benchmarkBdb = benchmarkBdbFile.readText().lines().filter { it != "" }
    successBdb = benchmarkBdb.filter { it.last() == '1' }
    timesBdb = LongArray(successBdb.size) {it -> successBdb[it].split("|")[0].toLong()}
    timesBdb.sort()

    println("GET INT")
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

    benchmarkEthFile = File("./benchmarks/ethereum/setString2.txt")
    benchmarkEth = benchmarkEthFile.readText().lines().filter { it != "" }
    successEth = benchmarkEth.filter { it.last() == '1' }
    timesEth = LongArray(successEth.size) { it -> successEth[it].split("|")[0].toLong()}
    timesEth.sort()

    benchmarkDseFile = File("./benchmarks/dse/setString2.txt")
    benchmarkDse = benchmarkDseFile.readText().lines().filter { it != "" }
    successDse = benchmarkDse.filter { it.last() == '1' }
    timesDse = LongArray(successDse.size) {it -> successDse[it].split("|")[0].toLong()}
    timesDse.sort()

    benchmarkBdbFile = File("./benchmarks/bdb/setString2.txt")
    benchmarkBdb = benchmarkBdbFile.readText().lines().filter { it != "" }
    successBdb = benchmarkBdb.filter { it.last() == '1' }
    timesBdb = LongArray(successBdb.size) {it -> successBdb[it].split("|")[0].toLong()}
    timesBdb.sort()

    println("SET STRING")
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

    benchmarkEthFile = File("./benchmarks/ethereum/getString2.txt")
    benchmarkEth = benchmarkEthFile.readText().lines().filter { it != "" }
    successEth = benchmarkEth.filter { it.last() == '1' }
    timesEth = LongArray(successEth.size) { it -> successEth[it].split("|")[0].toLong()}
    timesEth.sort()

    benchmarkDseFile = File("./benchmarks/dse/getString2.txt")
    benchmarkDse = benchmarkDseFile.readText().lines().filter { it != "" }
    successDse = benchmarkDse.filter { it.last() == '1' }
    timesDse = LongArray(successDse.size) {it -> successDse[it].split("|")[0].toLong()}
    timesDse.sort()

    benchmarkBdbFile = File("./benchmarks/bdb/getString2.txt")
    benchmarkBdb = benchmarkBdbFile.readText().lines().filter { it != "" }
    successBdb = benchmarkBdb.filter { it.last() == '1' }
    timesBdb = LongArray(successBdb.size) {it -> successBdb[it].split("|")[0].toLong()}
    timesBdb.sort()

    println("GET STRING")
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
