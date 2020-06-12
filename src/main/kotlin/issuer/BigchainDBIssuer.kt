package issuer

import connectionDetails.BigchainDBConnectionDetails
import java.util.*

class BigchainDBIssuer : AbstractIssuer {
    constructor(
        connectionDetails: BigchainDBConnectionDetails,
        domain: String,
        sslSKey: ByteArray,
        sslPKey: ByteArray,
        keys: List<ByteArray>
    ) : super() {

    }

    constructor(
        connectionDetails: BigchainDBConnectionDetails,
        id: String
    ) : super() {

    }
    override var domain: String
        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.
        set(value) {}
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