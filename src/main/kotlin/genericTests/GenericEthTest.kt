@file:JvmName("GenericEthTest")

package genericTests

import genericTests.ethereum.*

fun main() {
    EthereumBoolTests.run(TestInfo.threads)
    println("bool done")
    EthereumIntTests.run(TestInfo.threads)
    println("int done")
    EthereumStringTests.run(TestInfo.threads)
    println("string done")
    EthereumBoolMappingTests.run(TestInfo.threads)
    println("mapping done")
    EthereumArrayTests.run(TestInfo.threads)
    println("array done")
}

