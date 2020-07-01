@file:JvmName("GenericBDBTest")

package genericTests

import genericTests.bigchaindb.*

fun main() {
    BDBBoolTests.run(TestInfo.threads)
    println("bool done")
    BDBIntTests.run(TestInfo.threads)
    println("int done")
    BDBStringTests.run(TestInfo.threads)
    println("string done")
    BDBBoolMappingTests.run(TestInfo.threads)
    println("mapping done")
    BDBArrayTests.run(TestInfo.threads)
    println("array done")
}