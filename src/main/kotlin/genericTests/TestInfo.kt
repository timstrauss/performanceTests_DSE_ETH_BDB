package genericTests

import org.web3j.crypto.Credentials
import org.web3j.protocol.Web3j
import org.web3j.tx.FastRawTransactionManager
import org.web3j.tx.response.PollingTransactionReceiptProcessor

object TestInfo {
    val nodeHost = "172.20.8.223"
    val threads = 8
    val sameResource = false

    fun getTimeToRun(): Long {
        return 10L * 60 * 1000 * 1000 * 1000
    }

    val pollingIntervalEthereum = 10L

    fun getEthTransactionManager(web3j: Web3j, credentials: Credentials): FastRawTransactionManager {
        return FastRawTransactionManager(web3j, credentials, PollingTransactionReceiptProcessor(web3j, pollingIntervalEthereum, 1500))
    }
}