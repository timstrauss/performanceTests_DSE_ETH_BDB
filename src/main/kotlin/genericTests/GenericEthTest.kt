@file:JvmName("GenericEthTest")

package genericTests

import genericTests.ethereum.*

fun main() {
    EthereumBoolTests.run(2)
    println("bool done")
    EthereumIntTests.run(2)
    println("int done")
    EthereumStringTests.run(2)
    println("string done")
    EthereumBoolMappingTests.run(2)
    println("mapping done")
    EthereumArrayTests.run(2)
    println("array done")
}

