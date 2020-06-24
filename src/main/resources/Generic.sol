pragma solidity >=0.5.1;
pragma experimental ABIEncoderV2;

contract Generic {

    string stringVar;
    int intVar;
    bool boolVar;

    int[] intArray;
    bool[] boolArray;

    constructor() public {}

    function getString() public view returns (string memory) {
        return stringVar;
    }

    function setString(string memory newVar) public {
        stringVar = newVar;
    }

    function getInt() public view returns (int) {
        return intVar;
    }

    function setInt(int newVar) public {
        intVar = newVar;
    }

    function getBool() public view returns (bool) {
        return boolVar;
    }

    function setBool(bool newVar) public {
        boolVar = newVar;
    }

    function addInt(int newVar) public {
        intArray.push(newVar);
    }

    function addBool(bool newVar) public {
        boolArray.push(newVar);
    }

    function removeInt(int removeVar) public {
        for(uint i = 0; i < intArray.length; i++) {
            if (removeVar == intArray[i]) {
                intArray[i] = intArray[intArray.length - 1];
                delete intArray[intArray.length - 1];
            }
        }
    }

    function removeBool(bool removeVar) public {
        for(uint i = 0; i < boolArray.length; i++) {
            if (removeVar == boolArray[i]) {
                boolArray[i] = boolArray[boolArray.length - 1];
                delete boolArray[boolArray.length - 1];
            }
        }
    }

    function getInts() public view returns (int[] memory) {
        return intArray;
    }

    function getBools() public view returns (bool[] memory) {
        return boolArray;
    }
}
