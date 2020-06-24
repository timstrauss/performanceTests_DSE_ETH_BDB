package genericTests

import java.io.File

fun main() {
    var benchmarkEthFile = File("./benchmarks/ethereum/setBool.txt")
    var benchmarkEth = benchmarkEthFile.readText().lines()
    var timesEth = LongArray(benchmarkEth.size) { it -> benchmarkEth[it].split("|")[0].toLong()}
    timesEth.sort()

    var benchmarkDseFile = File("./benchmarks/dse/setBool.txt")
    var benchmarkDse = benchmarkDseFile.readText().lines()
    var timesDse = LongArray(benchmarkDse.size) {it -> benchmarkDse[it].toLong()}
    timesDse.sort()

    println("SET BOOLEAN")
    println("OVERALL NUMBER OF TRANSACTIONS")
    println("ETH\t\t\t\t\t\tDSE\t\t\t\t\t\tBDB")
    println("${timesEth.size}\t\t\t\t\t${timesDse.size}")
    println()
    println("AVERAGE")
    println("ETH\t\t\t\t\t\tDSE\t\t\t\t\t\tBDB")
    println("${timesEth.average()}\t\t${timesDse.average()}")
    println()
    println("TOP % UNDER MS")
    println("%\t\t\t\t\t\tETH\t\t\t\t\t\tDSE\t\t\t\t\t\tBDB")
    println("50\t\t\t\t\t\t${timesEth[timesEth.size / 2]}\t\t\t\t\t${timesDse[timesDse.size / 2]}")
    println("60\t\t\t\t\t\t${timesEth[(timesEth.size / 10) * 6]}\t\t\t\t\t${timesDse[(timesDse.size / 10) * 6]}")
    println("70\t\t\t\t\t\t${timesEth[(timesEth.size / 10) * 7]}\t\t\t\t\t${timesDse[(timesDse.size / 10) * 7]}")
    println("80\t\t\t\t\t\t${timesEth[(timesEth.size / 10) * 8]}\t\t\t\t\t${timesDse[(timesDse.size / 10) * 8]}")
    println("90\t\t\t\t\t\t${timesEth[(timesEth.size / 10) * 9]}\t\t\t\t\t${timesDse[(timesDse.size / 10) * 9]}")
    println("100\t\t\t\t\t\t${timesEth[timesEth.size - 1]}\t\t\t\t\t${timesDse[timesDse.size - 1]}")
    println("\n\n")

    benchmarkEthFile = File("./benchmarks/ethereum/getBool.txt")
    benchmarkEth = benchmarkEthFile.readText().lines()
    timesEth = LongArray(benchmarkEth.size) { it -> benchmarkEth[it].split("|")[0].toLong()}
    timesEth.sort()

    benchmarkDseFile = File("./benchmarks/dse/getBool.txt")
    benchmarkDse = benchmarkDseFile.readText().lines()
    timesDse = LongArray(benchmarkDse.size) {it -> benchmarkDse[it].toLong()}
    timesDse.sort()

    println("GET BOOLEAN")
    println("OVERALL NUMBER OF TRANSACTIONS")
    println("ETH\t\t\t\t\t\tDSE\t\t\t\t\t\tBDB")
    println("${timesEth.size}\t\t\t\t\t${timesDse.size}")
    println()
    println("AVERAGE")
    println("ETH\t\t\t\t\t\tDSE\t\t\t\t\t\tBDB")
    println("${timesEth.average()}\t\t${timesDse.average()}")
    println()
    println("TOP % UNDER MS")
    println("%\t\t\t\t\t\tETH\t\t\t\t\t\tDSE\t\t\t\t\t\tBDB")
    println("50\t\t\t\t\t\t${timesEth[timesEth.size / 2]}\t\t\t\t\t${timesDse[timesDse.size / 2]}")
    println("60\t\t\t\t\t\t${timesEth[(timesEth.size / 10) * 6]}\t\t\t\t\t${timesDse[(timesDse.size / 10) * 6]}")
    println("70\t\t\t\t\t\t${timesEth[(timesEth.size / 10) * 7]}\t\t\t\t\t${timesDse[(timesDse.size / 10) * 7]}")
    println("80\t\t\t\t\t\t${timesEth[(timesEth.size / 10) * 8]}\t\t\t\t\t${timesDse[(timesDse.size / 10) * 8]}")
    println("90\t\t\t\t\t\t${timesEth[(timesEth.size / 10) * 9]}\t\t\t\t\t${timesDse[(timesDse.size / 10) * 9]}")
    println("100\t\t\t\t\t\t${timesEth[timesEth.size - 1]}\t\t\t\t\t${timesDse[timesDse.size - 1]}")
    println("\n\n")
}