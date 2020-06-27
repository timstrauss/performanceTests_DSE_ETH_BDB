pragma solidity >=0.5.1;
pragma experimental ABIEncoderV2;

contract Generic {

    string stringVar;
    int intVar;
    bool boolVar;

    int[] intArray;

    mapping(string => bool) boolMapping;

    constructor() public {
        boolMapping["test"] = true;
    }

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

    function removeInt(int removeVar) public {
        for(uint i = 0; i < intArray.length; i++) {
            if (removeVar == intArray[i]) {
                intArray[i] = intArray[intArray.length - 1];
                delete intArray[intArray.length - 1];
            }
        }
    }

    function setInts(int[] memory newInt) public {
        intArray = newInt;
    }

    function getInts() public view returns (int[] memory) {
        return intArray;
    }

    function getBoolFor(string memory id) public view returns (bool) {
        return boolMapping[id];
    }

    function setBoolFor(string memory id, bool value) public {
        boolMapping[id] = value;
    }
}
