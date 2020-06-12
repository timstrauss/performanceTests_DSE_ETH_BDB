package issuer

import java.util.*

abstract class AbstractIssuer {
    abstract var domain: String
    abstract var signedAddress: String
    abstract var sslKeyHash: ByteArray

    abstract fun getKey(hash: ByteArray): Key
    abstract fun updateKey(hash: ByteArray, name: String, validFrom: Date, validTo: Date)
    abstract fun removeKey(hash: ByteArray)
    abstract fun getKeys(): List<Key>

    abstract fun getRevocationCode(code: Byte): RevocationCode
    abstract fun setRevocationCode(code: Byte, value: String)
    abstract fun getRevocationCodes(): List<RevocationCode>

    abstract fun getRevocationCodeOfCredential(hash: ByteArray): RevocationCode
    abstract fun setRevocationCodeOfCredential(hash: ByteArray, code: Byte)

    class Key(val hash: ByteArray, val name: String, val validFrom: Date, val validTo: Date)
    class RevocationCode(val code: Byte, val value: String)
}