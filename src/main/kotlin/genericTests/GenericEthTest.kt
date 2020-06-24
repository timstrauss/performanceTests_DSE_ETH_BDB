@file:JvmName("GenericEthTest")

package genericTests

import genericTests.ethereum.EthereumBoolTests
import genericTests.ethereum.EthereumIntTests
import genericTests.ethereum.EthereumStringTests
import java.net.Socket
import java.util.concurrent.TimeUnit

fun main() {
    var ganache = setUpGanache()
    EthereumBoolTests.run(2)
    stopGanache(ganache)
    ganache = setUpGanache()
    EthereumIntTests.run(2)
    stopGanache(ganache)
    ganache = setUpGanache()
    EthereumStringTests.run(2)
    stopGanache(ganache)
}

fun stopGanache(ganacheProcess: Process) {
    ganacheProcess.destroy()

    try{
        ganacheProcess.waitFor(30000, TimeUnit.MILLISECONDS)
    } catch (e: Exception){
        throw Exception("Blockchain was not stopped")
    }
}

fun setUpGanache(): Process {
    val isWindows = System.getProperty("os.name").startsWith("Windows")
    val ganacheBuilder = ProcessBuilder()
    if (isWindows) {
        ganacheBuilder.command("cmd.exe",
            "/c",
            "ganache-cli " +
                    "                           -l 47000000000" +
                    "                           -g 0")
    } else {
        ganacheBuilder.command("bash",
            "-c",
            "ganache-cli " +
                    "                           -l 47000000000" +
                    "                           -g 0")
    }
    val ganacheProcess = ganacheBuilder.start()

    checkBlockchainRunning(8545, 300)

    return ganacheProcess
}

fun checkBlockchainRunning(port: Int, retries: Int) {
    if (retries > 0) {
        try {
            Socket("localhost", port)
        } catch (e: Exception) {
            Thread.sleep(100)
            checkBlockchainRunning(port, retries - 1)
        }
    }
}

