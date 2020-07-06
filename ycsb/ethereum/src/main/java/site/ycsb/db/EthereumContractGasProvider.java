package site.ycsb.db;


import org.web3j.tx.gas.ContractGasProvider;

import java.math.BigInteger;

class EthereumContractGasProvider implements ContractGasProvider {
  @Override
  public BigInteger getGasPrice(String s) {
    return new BigInteger("0", 10);
  }

  @Override
  public BigInteger getGasPrice() {
    return new BigInteger("0", 10);
  }

  @Override
  public BigInteger getGasLimit(String s) {
    return new BigInteger("4700000", 10);
  }

  @Override
  public BigInteger getGasLimit() {
    return new BigInteger("4700000", 10);
  }
}