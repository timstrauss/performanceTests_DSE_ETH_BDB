@file:JvmName("GenericBDBTest")

package genericTests

import genericTests.bigchaindb.BDBBoolTests
import genericTests.bigchaindb.BDBIntTests
import genericTests.bigchaindb.BDBStringTests

fun main() {
    BDBStringTests.run(2)
    BDBBoolTests.run(2)
    BDBIntTests.run(2)
}