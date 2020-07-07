@file:JvmName("GenericEthTest")

package genericTests

import connectionDetails.EthereumConnectionDetails
import connectionDetails.EthereumContractGasProvider
import de.hpi.cc.datasource.decentralized.ethereum.smartcontracts.Generic
import genericTests.ethereum.*

fun main() {
    val con = EthereumConnectionDetails("http://${TestInfo.nodeHost}:8545")
    val generics = mutableListOf<String>()
    for (i in 0 until TestInfo.threads) {
        generics.add(Generic.deploy(con.web3j, con.credentials, EthereumContractGasProvider()).send().contractAddress)
    }
    EthereumBoolTests.run(TestInfo.threads, generics)
    println("bool done")
    EthereumIntTests.run(TestInfo.threads, generics)
    println("int done")
    EthereumStringTests.run(TestInfo.threads, generics)
    println("string done")
    EthereumBoolMappingTests.run(TestInfo.threads, generics)
    println("mapping done")
    EthereumArrayTests.run(TestInfo.threads, generics)
    println("array done")
}

