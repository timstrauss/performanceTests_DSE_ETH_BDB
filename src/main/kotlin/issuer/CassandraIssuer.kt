package issuer

import com.datastax.oss.driver.api.core.cql.BatchStatement
import com.datastax.oss.driver.api.core.cql.BatchType
import com.datastax.oss.driver.api.querybuilder.QueryBuilder.*
import com.google.common.hash.Hashing
import connectionDetails.CassandraConnectionDetails
import connectionDetails.CassandraUtils
import java.nio.charset.Charset
import java.security.KeyFactory
import java.security.Signature
import java.security.spec.PKCS8EncodedKeySpec
import java.text.SimpleDateFormat
import java.util.*

class CassandraIssuer: AbstractIssuer {
    constructor(
        connectionDetails: CassandraConnectionDetails,
        domain: String,
        sslSKey: ByteArray,
        sslPKey: ByteArray,
        keys: List<Key>
    ) : super() {
        this.connectionDetails = connectionDetails
        owner = connectionDetails.username
        uuid = UUID.randomUUID().toString()

        val sslPKeyHash = Hashing.sha256().hashBytes(sslPKey).asBytes()
        val sig = Signature.getInstance("SHA256WithRSA")
        sig.initSign(KeyFactory.getInstance("RSA").generatePrivate(PKCS8EncodedKeySpec(sslSKey)))
        sig.update(uuid.toByteArray(Charset.defaultCharset()))
        val signedBytes = sig.sign()
        val signedAddress = Base64.getEncoder().encodeToString(signedBytes)

        connectionDetails.executeSingleStatement(
            insertInto(CassandraUtils.getSpaceNameForUser(owner), TableNames.ISSUERS_TABLE.tableName)
                .value("id", literal(uuid))
                .value("domain", literal(domain))
                .value("sslpkey", literal(Base64.getEncoder().encodeToString(sslPKeyHash)))
                .value("signed_address", literal(signedAddress)).build().query
        )


        val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
        sdf.setTimeZone(TimeZone.getTimeZone("CET"))
        var batchStatement = "BEGIN BATCH\n"
        for (key in keys) {
            batchStatement += insertInto(CassandraUtils.getSpaceNameForUser(owner), TableNames.ISSUER_KEYS_TABLE.tableName)
                .value("issuer_id", literal(uuid))
                .value("key", literal(Base64.getEncoder().encodeToString(key.hash)))
                .value("valid_to", literal(sdf.format(key.validTo)))
                .value("valid_from", literal(sdf.format(key.validFrom)))
                .value("name", literal(key.name)).build().query + ";\n"
        }
        batchStatement += " APPLY BATCH;"
        connectionDetails.executeSingleStatement(batchStatement)


        val batchBuilder = BatchStatement.builder(BatchType.LOGGED)
        for (index in 0 until 128) {
            batchBuilder.addStatement(
                insertInto(CassandraUtils.getSpaceNameForUser(owner), TableNames.ISSUER_CUSTOM_REVOCATIONS.tableName)
                    .value("issuer_id", literal(uuid))
                    .value("code_id", raw(index.toString()))
                    .value("code", literal("")).build()
            )
        }
        val batch = batchBuilder.build()
        connectionDetails.session().execute(batch)
    }

    constructor(
        connectionDetails: CassandraConnectionDetails,
        owner: String,
        id: String
    ) : super() {
        this.connectionDetails = connectionDetails
        this.owner = owner
        uuid = id
    }

    private val connectionDetails: CassandraConnectionDetails
    private val owner: String
    private val uuid: String

    override var domain: String
        get() = connectionDetails.executeSingleStatement(
            selectFrom(CassandraUtils.getSpaceNameForUser(owner), TableNames.ISSUERS_TABLE.tableName)
                .column("domain")
                .whereColumn("id").isEqualTo(literal(uuid)).build().query
        ).one()?.getString(0) ?: ""
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

    enum class TableNames(val tableName: String) {
        ISSUERS_TABLE("issuers"),
        ISSUER_KEYS_TABLE("issuer_keys"),
        ISSUER_CUSTOM_REVOCATIONS("issuer_custom_revocations"),
        ISSUER_REVOKED_CREDENTIAL("issuer_revoked_credentials")

    }
}