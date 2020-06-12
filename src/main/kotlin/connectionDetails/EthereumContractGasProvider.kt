package connectionDetails

import org.web3j.tx.gas.ContractGasProvider
import java.math.BigInteger

internal class EthereumContractGasProvider : ContractGasProvider {
    override fun getGasLimit(contractFunc: String?): BigInteger {
        return BigInteger("9000000", 10)
    }

    override fun getGasLimit(): BigInteger {
        return BigInteger("9000000", 10)
    }

    override fun getGasPrice(contractFunc: String?): BigInteger {
        return BigInteger("0", 10)
    }

    override fun getGasPrice(): BigInteger {
        return BigInteger("0", 10)
    }
}