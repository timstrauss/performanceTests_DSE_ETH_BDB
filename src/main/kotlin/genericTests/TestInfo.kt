package genericTests

import org.web3j.crypto.Credentials
import org.web3j.protocol.Web3j
import org.web3j.tx.FastRawTransactionManager
import org.web3j.tx.RawTransactionManager
import org.web3j.tx.response.PollingTransactionReceiptProcessor

object TestInfo {
    val nodeHost = "172.20.8.223"
    val threads = 8
    val sameResource = false

    fun getTimeToRun(): Long {
        return 10L * 60 * 1000 * 1000 * 1000
    }

    val pollingIntervalEthereum = 200L

    fun getEthTransactionManager(web3j: Web3j, credentials: Credentials): RawTransactionManager {
        return RawTransactionManager(web3j, credentials, 1515L, 100, pollingIntervalEthereum)
    }
}