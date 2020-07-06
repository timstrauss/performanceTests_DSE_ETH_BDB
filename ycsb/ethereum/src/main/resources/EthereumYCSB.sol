pragma solidity >=0.5.1;
pragma experimental ABIEncoderV2;

contract EthereumYCSB {

    mapping(string => string) fields;

    string[] possibleFields;

    constructor(string[] memory createFields, string[] memory createValues) public {
        require(createFields.length == createValues.length);
        for (uint i = 0; i < createFields.length; i++) {
            possibleFields.push(createFields[i]);
            fields[createFields[i]] = createValues[i];
        }
    }

    function getField(string[] memory selectFields) public view returns(string[] memory) {
        string[] memory values = new string[](selectFields.length + 1);
        values[0] = "dummy";

        for (uint i = 0; i < selectFields.length; i++) {
            values[i+1] = fields[selectFields[i]];
        }

        return values;
    }

    function getFieldNames() public view returns(string[] memory) {
        string[] memory values = new string[](possibleFields.length + 1);
        values[0] = "dummy";
        for (uint i = 0; i < possibleFields.length; i++) {
            values[i + 1] = possibleFields[i];
        }

        return values;
    }

    function getField() public view returns(string[] memory) {
        string[] memory values = new string[](possibleFields.length + 1);
        values[0] = "dummy";
        for (uint i = 0; i < possibleFields.length; i++) {
            values[i + 1] = fields[possibleFields[i]];
        }

        return values;
    }

    function setValues(string[] memory updateFields, string[] memory updateValues) public {
        require(updateFields.length == updateValues.length);
        for (uint i = 0; i < updateFields.length; i++) {
            fields[updateFields[i]] = updateValues[i];
        }
    }
}
