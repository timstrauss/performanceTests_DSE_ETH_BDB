package issuer

import com.bigchaindb.model.MetaData
import com.google.api.client.json.Json
import com.google.common.hash.Hashing
import com.google.gson.Gson
import connectionDetails.BigchainDBConnectionDetails
import java.lang.Exception
import java.nio.charset.Charset
import java.security.KeyFactory
import java.security.Signature
import java.security.spec.PKCS8EncodedKeySpec
import java.util.*

class BigchainDBIssuer : AbstractIssuer {
    constructor(
        connectionDetails: BigchainDBConnectionDetails,
        domain: String,
        sslSKey: ByteArray,
        sslPKey: ByteArray,
        keys: List<Key>
    ) : super() {
        val sslPKeyHash = Base64.getEncoder().encodeToString(Hashing.sha256().hashBytes(sslPKey).asBytes())
        this.connectionDetails = connectionDetails
        val assetData = TreeMap<String, String>()
        assetData["uuid"] = UUID.randomUUID().toString()
        val metadata = MetaData()
        metadata.setMetaData("domain", domain)
        metadata.setMetaData("sslPKeyHash", sslPKeyHash)
        metadata.setMetaData("keys", Gson().toJson(keys))
        assetId = connectionDetails.sendCreateTransaction(assetData, metadata)
        val sig = Signature.getInstance("SHA256WithRSA")
        sig.initSign(KeyFactory.getInstance("RSA").generatePrivate(PKCS8EncodedKeySpec(sslSKey)))
        sig.update(assetId.toByteArray(Charset.defaultCharset()))
        val signedBytes = sig.sign()
        val signedAddress = Base64.getEncoder().encodeToString(signedBytes)
        metadata.setMetaData("signedAddress", signedAddress)
        connectionDetails.update(metadata, connectionDetails.findLastTransactionForAsset(assetId), assetId)
    }

    constructor(
        connectionDetails: BigchainDBConnectionDetails,
        id: String
    ) : super() {
        assetId = id
        this.connectionDetails = connectionDetails
    }

    val assetId: String
    val connectionDetails: BigchainDBConnectionDetails
    override var domain: String
        get() = connectionDetails.getLatestMetadata(assetId)["domain"] ?: throw Exception("no domain")
        set(value) {
            val metaData = connectionDetails.getLatestMetadata(assetId)
            val latestId = connectionDetails.findLastTransactionForAsset(assetId)
            val newMetaData = MetaData()
            for (data in metaData) {
                newMetaData.setMetaData(data.key, data.value)
            }
            newMetaData.setMetaData("domain", value)
            var start = System.currentTimeMillis()
            connectionDetails.update(newMetaData, latestId, assetId)
            var end = System.currentTimeMillis()
            println(end - start)
        }
    override var signedAddress: String
        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.
        set(value) {}
    override var sslKeyHash: ByteArray
        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.
        set(value) {}

    override fun getKey(hash: ByteArray): Key {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun updateKey(hash: ByteArray, name: String, validFrom: Date, validTo: Date) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun removeKey(hash: ByteArray) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getKeys(): List<Key> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getRevocationCode(code: Byte): RevocationCode {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun setRevocationCode(code: Byte, value: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getRevocationCodes(): List<RevocationCode> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getRevocationCodeOfCredential(hash: ByteArray): RevocationCode {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun setRevocationCodeOfCredential(hash: ByteArray, code: Byte) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}