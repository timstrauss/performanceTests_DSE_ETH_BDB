@file:JvmName("GenericEthTest")

package genericTests

import genericTests.ethereum.EthereumBoolTests
import genericTests.ethereum.EthereumIntTests
import genericTests.ethereum.EthereumStringTests

fun main() {
    EthereumBoolTests.run(2)
    EthereumIntTests.run(2)
    EthereumStringTests.run(2)
}

