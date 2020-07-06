package site.ycsb.db;

import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.DynamicArray;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.RemoteFunctionCall;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tx.Contract;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.gas.ContractGasProvider;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * <p>Auto generated code.
 * <p><strong>Do not modify!</strong>
 * <p>Please use the <a href="https://docs.web3j.io/command_line.html">web3j command line tools</a>,
 * or the org.web3j.codegen.SolidityFunctionWrapperGenerator in the 
 * <a href="https://github.com/web3j/web3j/tree/master/codegen">codegen module</a> to update.
 *
 * <p>Generated with web3j version 4.5.11.
 */
@SuppressWarnings("rawtypes")
public class EthereumYCSB extends Contract {
    public static final String BINARY = "60806040523480156200001157600080fd5b5060405162000dc438038062000dc4833981016040819052620000349162000274565b80518251146200004357600080fd5b60005b8251811015620000fb5760018382815181106200005f57fe5b602090810291909101810151825460018101808555600094855293839020825162000091949190920192019062000104565b5050818181518110620000a057fe5b60200260200101516000848381518110620000b757fe5b6020026020010151604051620000ce919062000317565b90815260200160405180910390209080519060200190620000f192919062000104565b5060010162000046565b505050620003d8565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f106200014757805160ff191683800117855562000177565b8280016001018555821562000177579182015b82811115620001775782518255916020019190600101906200015a565b506200018592915062000189565b5090565b620001a691905b8082111562000185576000815560010162000190565b90565b600082601f830112620001bb57600080fd5b8151620001d2620001cc8262000353565b6200032c565b81815260209384019390925082018360005b83811015620002145781518601620001fd88826200021e565b8452506020928301929190910190600101620001e4565b5050505092915050565b600082601f8301126200023057600080fd5b815162000241620001cc8262000374565b915080825260208301602083018583830111156200025e57600080fd5b6200026b838284620003a5565b50505092915050565b600080604083850312156200028857600080fd5b82516001600160401b038111156200029f57600080fd5b620002ad85828601620001a9565b92505060208301516001600160401b03811115620002ca57600080fd5b620002d885828601620001a9565b9150509250929050565b6000620002ef826200039c565b620002fb8185620003a0565b93506200030d818560208601620003a5565b9290920192915050565b6000620003258284620002e2565b9392505050565b6040518181016001600160401b03811182821017156200034b57600080fd5b604052919050565b60006001600160401b038211156200036a57600080fd5b5060209081020190565b60006001600160401b038211156200038b57600080fd5b506020601f91909101601f19160190565b5190565b919050565b60005b83811015620003c2578181015183820152602001620003a8565b83811115620003d2576000848401525b50505050565b6109dc80620003e86000396000f3fe608060405234801561001057600080fd5b506004361061004c5760003560e01c80634ff457881461005157806399949b6614610066578063bec84db214610084578063e1c95d1f14610097575b600080fd5b61006461005f3660046106be565b61009f565b005b61006e61011f565b60405161007b91906108ae565b60405180910390f35b61006e610092366004610681565b610287565b61006e6103e7565b80518251146100ad57600080fd5b60005b825181101561011a578181815181106100c557fe5b602002602001015160008483815181106100db57fe5b60200260200101516040516100f09190610896565b9081526020016040518091039020908051906020019061011192919061052e565b506001016100b0565b505050565b60608060018054905060010160405190808252806020026020018201604052801561015e57816020015b60608152602001906001900390816101495790505b5090506040518060400160405280600581526020016464756d6d7960d81b8152508160008151811061018c57fe5b602090810291909101015260005b600154811015610280576000600182815481106101b357fe5b906000526020600020016040516101ca91906108a2565b9081526040805160209281900383018120805460026001821615610100026000190190911604601f810185900485028301850190935282825290929091908301828280156102595780601f1061022e57610100808354040283529160200191610259565b820191906000526020600020905b81548152906001019060200180831161023c57829003601f168201915b505050505082826001018151811061026d57fe5b602090810291909101015260010161019a565b5090505b90565b60608082516001016040519080825280602002602001820160405280156102c257816020015b60608152602001906001900390816102ad5790505b5090506040518060400160405280600581526020016464756d6d7960d81b815250816000815181106102f057fe5b602090810291909101015260005b83518110156103e057600084828151811061031557fe5b602002602001015160405161032a9190610896565b9081526040805160209281900383018120805460026001821615610100026000190190911604601f810185900485028301850190935282825290929091908301828280156103b95780601f1061038e576101008083540402835291602001916103b9565b820191906000526020600020905b81548152906001019060200180831161039c57829003601f168201915b50505050508282600101815181106103cd57fe5b60209081029190910101526001016102fe565b5092915050565b60608060018054905060010160405190808252806020026020018201604052801561042657816020015b60608152602001906001900390816104115790505b5090506040518060400160405280600581526020016464756d6d7960d81b8152508160008151811061045457fe5b602090810291909101015260005b600154811015610280576001818154811061047957fe5b600091825260209182902001805460408051601f60026000196101006001871615020190941693909304928301859004850281018501909152818152928301828280156105075780601f106104dc57610100808354040283529160200191610507565b820191906000526020600020905b8154815290600101906020018083116104ea57829003601f168201915b505050505082826001018151811061051b57fe5b6020908102919091010152600101610462565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f1061056f57805160ff191683800117855561059c565b8280016001018555821561059c579182015b8281111561059c578251825591602001919060010190610581565b506105a89291506105ac565b5090565b61028491905b808211156105a857600081556001016105b2565b600082601f8301126105d757600080fd5b81356105ea6105e5826108e6565b6108bf565b81815260209384019390925082018360005b8381101561062857813586016106128882610632565b84525060209283019291909101906001016105fc565b5050505092915050565b600082601f83011261064357600080fd5b81356106516105e582610907565b9150808252602083016020830185838301111561066d57600080fd5b610678838284610953565b50505092915050565b60006020828403121561069357600080fd5b813567ffffffffffffffff8111156106aa57600080fd5b6106b6848285016105c6565b949350505050565b600080604083850312156106d157600080fd5b823567ffffffffffffffff8111156106e857600080fd5b6106f4858286016105c6565b925050602083013567ffffffffffffffff81111561071157600080fd5b61071d858286016105c6565b9150509250929050565b600061073383836107a8565b9392505050565b600061074582610941565b61074f8185610945565b9350836020820285016107618561092f565b8060005b8581101561079b578484038952815161077e8582610727565b94506107898361092f565b60209a909a0199925050600101610765565b5091979650505050505050565b60006107b382610941565b6107bd8185610945565b93506107cd81856020860161095f565b6107d68161098f565b9093019392505050565b60006107eb82610941565b6107f5818561094e565b935061080581856020860161095f565b9290920192915050565b60008154600181166000811461082c576001811461084f5761088e565b607f600283041661083d818761094e565b60ff198416815295508501925061088e565b6002820461085d818761094e565b955061086885610935565b60005b828110156108875781548882015260019091019060200161086b565b5050850192505b505092915050565b600061073382846107e0565b6000610733828461080f565b60208082528101610733818461073a565b60405181810167ffffffffffffffff811182821017156108de57600080fd5b604052919050565b600067ffffffffffffffff8211156108fd57600080fd5b5060209081020190565b600067ffffffffffffffff82111561091e57600080fd5b506020601f91909101601f19160190565b60200190565b60009081526020902090565b5190565b90815260200190565b919050565b82818337506000910152565b60005b8381101561097a578181015183820152602001610962565b83811115610989576000848401525b50505050565b601f01601f19169056fea365627a7a72315820f43b1265e5b3dcdc4a343a38fba4135ca99663432c7082c3bd8ada0da8bc7ff46c6578706572696d656e74616cf564736f6c634300050e0040";

    public static final String FUNC_getField = "getField";

    public static final String FUNC_GETFIELDNAMES = "getFieldNames";

    public static final String FUNC_SETVALUES = "setValues";

    @Deprecated
    protected EthereumYCSB(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected EthereumYCSB(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, credentials, contractGasProvider);
    }

    @Deprecated
    protected EthereumYCSB(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    protected EthereumYCSB(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public RemoteFunctionCall<List> getField() {
        final Function function = new Function(FUNC_getField,
                Arrays.<Type>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<DynamicArray<Utf8String>>() {}));
        return new RemoteFunctionCall<List>(function,
                new Callable<List>() {
                    @Override
                    @SuppressWarnings("unchecked")
                    public List call() throws Exception {
                        List<Type> result = (List<Type>) executeCallSingleValueReturn(function, List.class);
                        return convertToNative(result);
                    }
                });
    }

    public RemoteFunctionCall<List> getField(List<String> selectFields) {
        final Function function = new Function(FUNC_getField,
                Arrays.<Type>asList(new org.web3j.abi.datatypes.DynamicArray<org.web3j.abi.datatypes.Utf8String>(
                        org.web3j.abi.datatypes.Utf8String.class,
                        org.web3j.abi.Utils.typeMap(selectFields, org.web3j.abi.datatypes.Utf8String.class))),
                Arrays.<TypeReference<?>>asList(new TypeReference<DynamicArray<Utf8String>>() {}));
        return new RemoteFunctionCall<List>(function,
                new Callable<List>() {
                    @Override
                    @SuppressWarnings("unchecked")
                    public List call() throws Exception {
                        List<Type> result = (List<Type>) executeCallSingleValueReturn(function, List.class);
                        return convertToNative(result);
                    }
                });
    }

    public RemoteFunctionCall<List> getFieldNames() {
        final Function function = new Function(FUNC_GETFIELDNAMES,
                Arrays.<Type>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<DynamicArray<Utf8String>>() {}));
        return new RemoteFunctionCall<List>(function,
                new Callable<List>() {
                    @Override
                    @SuppressWarnings("unchecked")
                    public List call() throws Exception {
                        List<Type> result = (List<Type>) executeCallSingleValueReturn(function, List.class);
                        return convertToNative(result);
                    }
                });
    }

    public RemoteFunctionCall<TransactionReceipt> setValues(List<String> updateFields, List<String> updateValues) {
        final Function function = new Function(
                FUNC_SETVALUES,
                Arrays.<Type>asList(new org.web3j.abi.datatypes.DynamicArray<org.web3j.abi.datatypes.Utf8String>(
                        org.web3j.abi.datatypes.Utf8String.class,
                        org.web3j.abi.Utils.typeMap(updateFields, org.web3j.abi.datatypes.Utf8String.class)),
                new org.web3j.abi.datatypes.DynamicArray<org.web3j.abi.datatypes.Utf8String>(
                        org.web3j.abi.datatypes.Utf8String.class,
                        org.web3j.abi.Utils.typeMap(updateValues, org.web3j.abi.datatypes.Utf8String.class))),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    @Deprecated
    public static EthereumYCSB load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new EthereumYCSB(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    @Deprecated
    public static EthereumYCSB load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new EthereumYCSB(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static EthereumYCSB load(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return new EthereumYCSB(contractAddress, web3j, credentials, contractGasProvider);
    }

    public static EthereumYCSB load(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return new EthereumYCSB(contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static RemoteCall<EthereumYCSB> deploy(Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider, List<String> createFields, List<String> createValues) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.DynamicArray<org.web3j.abi.datatypes.Utf8String>(
                        org.web3j.abi.datatypes.Utf8String.class,
                        org.web3j.abi.Utils.typeMap(createFields, org.web3j.abi.datatypes.Utf8String.class)),
                new org.web3j.abi.datatypes.DynamicArray<org.web3j.abi.datatypes.Utf8String>(
                        org.web3j.abi.datatypes.Utf8String.class,
                        org.web3j.abi.Utils.typeMap(createValues, org.web3j.abi.datatypes.Utf8String.class))));
        return deployRemoteCall(EthereumYCSB.class, web3j, credentials, contractGasProvider, BINARY, encodedConstructor);
    }

    public static RemoteCall<EthereumYCSB> deploy(Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider, List<String> createFields, List<String> createValues) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.DynamicArray<org.web3j.abi.datatypes.Utf8String>(
                        org.web3j.abi.datatypes.Utf8String.class,
                        org.web3j.abi.Utils.typeMap(createFields, org.web3j.abi.datatypes.Utf8String.class)),
                new org.web3j.abi.datatypes.DynamicArray<org.web3j.abi.datatypes.Utf8String>(
                        org.web3j.abi.datatypes.Utf8String.class,
                        org.web3j.abi.Utils.typeMap(createValues, org.web3j.abi.datatypes.Utf8String.class))));
        return deployRemoteCall(EthereumYCSB.class, web3j, transactionManager, contractGasProvider, BINARY, encodedConstructor);
    }

    @Deprecated
    public static RemoteCall<EthereumYCSB> deploy(Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit, List<String> createFields, List<String> createValues) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.DynamicArray<org.web3j.abi.datatypes.Utf8String>(
                        org.web3j.abi.datatypes.Utf8String.class,
                        org.web3j.abi.Utils.typeMap(createFields, org.web3j.abi.datatypes.Utf8String.class)),
                new org.web3j.abi.datatypes.DynamicArray<org.web3j.abi.datatypes.Utf8String>(
                        org.web3j.abi.datatypes.Utf8String.class,
                        org.web3j.abi.Utils.typeMap(createValues, org.web3j.abi.datatypes.Utf8String.class))));
        return deployRemoteCall(EthereumYCSB.class, web3j, credentials, gasPrice, gasLimit, BINARY, encodedConstructor);
    }

    @Deprecated
    public static RemoteCall<EthereumYCSB> deploy(Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit, List<String> createFields, List<String> createValues) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.DynamicArray<org.web3j.abi.datatypes.Utf8String>(
                        org.web3j.abi.datatypes.Utf8String.class,
                        org.web3j.abi.Utils.typeMap(createFields, org.web3j.abi.datatypes.Utf8String.class)),
                new org.web3j.abi.datatypes.DynamicArray<org.web3j.abi.datatypes.Utf8String>(
                        org.web3j.abi.datatypes.Utf8String.class,
                        org.web3j.abi.Utils.typeMap(createValues, org.web3j.abi.datatypes.Utf8String.class))));
        return deployRemoteCall(EthereumYCSB.class, web3j, transactionManager, gasPrice, gasLimit, BINARY, encodedConstructor);
    }
}
