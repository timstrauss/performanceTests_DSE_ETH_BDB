package issuer

import com.google.common.hash.Hashing
import connectionDetails.EthereumConnectionDetails
import connectionDetails.EthereumContractGasProvider
import de.hpi.cc.datasource.decentralized.ethereum.smartcontracts.Issuer
import java.math.BigInteger
import java.nio.charset.Charset
import java.security.KeyFactory
import java.security.Signature
import java.security.spec.PKCS8EncodedKeySpec
import java.util.*
import javax.crypto.Cipher

class EthIssuer : AbstractIssuer {
    constructor(
        connectionDetails: EthereumConnectionDetails,
        domain: String,
        sslSKey: ByteArray,
        sslPKey: ByteArray,
        keys: List<Key>
    ) : super() {
        issuerContract = Issuer.deploy(connectionDetails.web3j, connectionDetails.credentials, EthereumContractGasProvider()).send()
        issuerContract.setDomain(domain).send()
        issuerContract.setSSLKeyHash(Hashing.sha256().hashBytes(sslPKey).asBytes()).send()
        val sig = Signature.getInstance("SHA256WithRSA")
        sig.initSign(KeyFactory.getInstance("RSA").generatePrivate(PKCS8EncodedKeySpec(sslSKey)))
        sig.update(issuerContract.contractAddress.toByteArray(Charset.defaultCharset()))
        val signedBytes = sig.sign()
        val signedAddress = Base64.getEncoder().encodeToString(signedBytes)
        issuerContract.setSignedAddress(signedAddress).send()
        val hashes = mutableListOf<ByteArray>()
        val names = mutableListOf<String>()
        val validTos = mutableListOf<BigInteger>()
        val validFroms = mutableListOf<BigInteger>()
        for (key in keys) {
            hashes.add(key.hash)
            names.add(key.name)
            validTos.add(BigInteger.valueOf(key.validTo.time / 1000L))
            validFroms.add(BigInteger.valueOf(key.validFrom.time / 1000L))
        }
        issuerContract.addPublicKeysToIssuer(names, hashes, validFroms, validTos).send()
    }

    constructor(
        connectionDetails: EthereumConnectionDetails,
        id: String
    ) : super() {
        issuerContract = Issuer.load(id, connectionDetails.web3j, connectionDetails.credentials, EthereumContractGasProvider())
    }

    private val issuerContract: Issuer

    override var domain: String
        get() = issuerContract.domain.send()
        set(value) { issuerContract.setDomain(value).send() }
    override var signedAddress: String
        get() = issuerContract.signedAddress.send()
        set(value) { issuerContract.setSignedAddress(value).send() }
    override var sslKeyHash: ByteArray
        get() = issuerContract.sslKeyHash.send()
        set(value) { issuerContract.setSSLKeyHash(value).send() }

    override fun getKey(hash: ByteArray): Key {
        return Key(
            hash,
            issuerContract.getKeyNameOf(hash).send(),
            Date(issuerContract.getValidityTimespanStartsOf(hash).send().toLong() * 1000L),
            Date(issuerContract.getValidityTimespanEndsOf(hash).send().toLong() * 1000L)
        )
    }

    override fun updateKey(hash: ByteArray, name: String, validFrom: Date, validTo: Date) {
        issuerContract.updatePublicKeyOfIssuer(
            hash,
            name,
            BigInteger.valueOf(validFrom.time / 1000L),
            BigInteger.valueOf(validTo.time / 1000L)
        ).send()
    }

    override fun removeKey(hash: ByteArray) {
        issuerContract.updatePublicKeyOfIssuer(
            hash,
            issuerContract.getKeyNameOf(hash).send(),
            BigInteger.valueOf(Date(issuerContract.getValidityTimespanStartsOf(hash).send().toLong() * 1000L).time / 1000L),
            BigInteger.valueOf(Date().time / 1000L)
        ).send()
    }

    override fun getKeys(): List<Key> {
        val keys = mutableListOf<Key>()
        val hashes: List<ByteArray> = issuerContract.publicKeys.send() as List<ByteArray>
        val names: List<String> = issuerContract.keyNames.send() as List<String>
        val froms: List<BigInteger> = issuerContract.validityTimespanStarts.send() as List<BigInteger>
        val tos: List<BigInteger> = issuerContract.validityTimespanEnds.send() as List<BigInteger>

        for (index in hashes.indices) {
            keys.add(Key(
                hashes[index],
                names[index],
                Date(froms[index].toLong() * 1000L),
                Date(tos[index].toLong() * 1000L)
            ))
        }

        return keys
    }

    override fun getRevocationCode(code: Byte): RevocationCode {
        return RevocationCode(
            code,
            issuerContract.getCustomRevocationMapping(BigInteger.valueOf(code.toLong())).send()
        )
    }

    override fun setRevocationCode(code: Byte, value: String) {
        issuerContract.setCustomRevocationMapping(
            BigInteger.valueOf(code.toLong()),
            value
        ).send()
    }

    override fun getRevocationCodes(): List<RevocationCode> {
        val codes = mutableListOf<RevocationCode>()
        val codeValues = issuerContract.customRevocationMapping.send().drop(1).map { it as String }
        for (i in 0 until 128) {
            val uint = i.toUInt()
            codes.add(RevocationCode(
                uint.toByte(),
                codeValues[i]
            ))
        }
        return codes
    }

    override fun getRevocationCodeOfCredential(hash: ByteArray): RevocationCode {
        val code = issuerContract.getRevocationStatus(hash).send()
        return RevocationCode(
            code.toInt().toUInt().toByte(),
            issuerContract.getCustomRevocationMapping(code).send()
        )
    }

    override fun setRevocationCodeOfCredential(hash: ByteArray, code: Byte) {
        issuerContract.setRevocationStatus(hash, BigInteger.valueOf(code.toUInt().toLong()))
    }
}