package connectionDetails

import org.web3j.crypto.Credentials
import org.web3j.crypto.Keys
import org.web3j.crypto.WalletUtils
import org.web3j.protocol.Web3j
import org.web3j.protocol.http.HttpService
import java.io.File

class EthereumConnectionDetails(
    val nodeUrl: String,
    val credentialFile: File? = null,
    val credentialPassword: String? = null,
    var issuerAdminDatabaseAddress: String? = null
) {
    var web3j: Web3j
    val credentials: Credentials

    init {
        web3j = Web3j.build(HttpService(nodeUrl))
        credentials = if (credentialFile != null) {
            WalletUtils.loadCredentials(credentialPassword, credentialFile)
        } else {
            val keyPair = Keys.createEcKeyPair()
            Credentials.create(keyPair)
        }
    }
}