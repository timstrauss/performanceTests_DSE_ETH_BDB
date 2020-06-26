@file:JvmName("GenericBDBTest")

package genericTests

import com.mongodb.BasicDBObject
import com.mongodb.MongoClient
import com.mongodb.MongoClientURI
import com.mongodb.client.model.Filters.and
import genericTests.bigchaindb.BDBBoolTests
import genericTests.bigchaindb.BDBIntTests
import genericTests.bigchaindb.BDBStringTests

fun main() {
    val mongoClient = MongoClient()
    val db = mongoClient.getDatabase("bigchain")
    val assetCollection = db.getCollection("assets")
    val transactionsCollection = db.getCollection("transactions")
    val metadataCollection = db.getCollection("metadata")
    val assetId = assetCollection.find(and(BasicDBObject("data", BasicDBObject("property", "boolvar")), BasicDBObject("data", BasicDBObject("uuid", "bff17042-77a4-4e9f-8620-9e0535d6a8ea")))).first()?.get("id")
    println(assetId as String)
    val metadataId = transactionsCollection.find(BasicDBObject("asset", BasicDBObject("id", assetId))).sort(BasicDBObject("\$natural", "-1")).first()?.get("id")
    /*BDBStringTests.run(2)
    BDBBoolTests.run(2)
    BDBIntTests.run(2)*/
}