pragma solidity >=0.5.1;
pragma experimental ABIEncoderV2;

contract Issuer {
    address owner;

    string domain;
    bytes32 sslKeyHash;
    string signedAddress;

    bytes32[] publicKeyHashes;
    string[] keyNames;
    int64[] validityTimespanStarts;
    int64[] validityTimespanEnds;
    mapping(bytes32 => bool) publicKeyMapping;

    bytes32[] revocationHashes;
    int8[] revocationStatuses;
    mapping(bytes32 => uint) revocationLookup;

    string[] customRevocationCodes;

    constructor() public {
        owner = msg.sender;
        // Needed because web3j currently is buggy with handling dynamic arrays, making first element invalid
        publicKeyHashes.push(0x0);
        keyNames.push("NotaValidKey");
        validityTimespanStarts.push(0);
        validityTimespanEnds.push(0);
        customRevocationCodes.push("invalid");

        for (uint8 i = 0; i < 128; i++) {
            customRevocationCodes.push(" "); // web3j requires the strings to be non-empty
        }

        // reserve first element
        revocationHashes.push(0x0);
        revocationStatuses.push(0);
    }

    modifier isOwner() {
        require (msg.sender == owner, "Not authorized");
        _;
    }

    function getSignedAddress() public view returns(string memory) {
        return signedAddress;
    }

    function setSignedAddress(string memory newSignedAddress) public isOwner() {
        signedAddress = newSignedAddress;
    }

    function getDomain() public view returns(string memory) {
        return domain;
    }

    function setDomain(string memory newDomain) public isOwner() {
        domain = newDomain;
    }

    function getSSLKeyHash() public view returns(bytes32) {
        return sslKeyHash;
    }

    function setSSLKeyHash(bytes32 newSslKeyHash) public isOwner() {
        sslKeyHash = newSslKeyHash;
    }

    function addPublicKeysToIssuer(string[] memory keyName, bytes32[] memory pKeyHash, int64[] memory validFrom, int64[] memory validTo) public isOwner() {
        require(keyName.length == pKeyHash.length);
        require(keyName.length == validFrom.length);
        require(keyName.length == validTo.length);
        for (uint i = 0; i < keyName.length; i++) {
            if (!publicKeyMapping[pKeyHash[i]]) {
                keyNames.push(keyName[i]);
                publicKeyHashes.push(pKeyHash[i]);
                validityTimespanStarts.push(validFrom[i]);
                validityTimespanEnds.push(validTo[i]);
                publicKeyMapping[pKeyHash[i]] = true;
            }
        }
    }

    function updatePublicKeyOfIssuer(bytes32 pKeyHash, string memory keyName, int64 validFrom, int64 validTo) public isOwner() {
        for (uint i = 0; i < publicKeyHashes.length; i++) {
            if (publicKeyHashes[i] == pKeyHash) {
                keyNames[i] = keyName;
                validityTimespanStarts[i] = validFrom;
                validityTimespanEnds[i] = validTo;
                break;
            }
        }
    }

    function containsPublicKeyForIssuer(bytes32 pKeyHash) public view returns (bool) {
        return publicKeyMapping[pKeyHash];
    }

    function getPublicKeys() public view returns (bytes32 [] memory) {
        return publicKeyHashes;
    }

    function getKeyNames() public view returns (string [] memory) {
        return keyNames;
    }

    function getKeyNameOf(bytes32 pKeyHash) public view returns(string memory) {
        for (uint index = 0; index < publicKeyHashes.length; index++) {
            if (publicKeyHashes[index] == pKeyHash) {
                return keyNames[index];
            }
        }
        revert("Could not find key with this hash");
    }

    function getValidityTimespanStarts() public view returns(int64[] memory) {
        return validityTimespanStarts;
    }

    function getValidityTimespanStartsOf(bytes32 pKeyHash) public view returns(int64) {
        for (uint index = 0; index < publicKeyHashes.length; index++) {
            if (publicKeyHashes[index] == pKeyHash) {
                return validityTimespanStarts[index];
            }
        }
        revert("Could not find key with this hash");
    }

    function getValidityTimespanEnds() public view returns(int64[] memory) {
        return validityTimespanEnds;
    }

    function getValidityTimespanEndsOf(bytes32 pKeyHash) public view returns(int64) {
        for (uint index = 0; index < publicKeyHashes.length; index++) {
            if (publicKeyHashes[index] == pKeyHash) {
                return validityTimespanEnds[index];
            }
        }
        revert("Could not find key with this hash");
    }

    function getRevocationHashes() public view returns (bytes32[] memory){
        return revocationHashes;
    }

    function getRevocationStatuses() public view returns (int8[] memory){
        return revocationStatuses;
    }

    function getRevocationStatus(bytes32 credentialHash) public view returns (int8) {
        uint index = revocationLookup[credentialHash];
        // 0 means it does not exist in the list
        if (index == 0) {
            return 0;
        } else {
            return revocationStatuses[index];
        }
    }

    function setRevocationStatus(bytes32 credentialHash, int8 status) public {
        uint index = revocationLookup[credentialHash];
        // 0 means it does not exist in the list

        if (status == 0) {
            if (index != 0) {// entry exists and is removed from list
                revocationHashes[index] = revocationHashes[revocationHashes.length - 1];
                revocationStatuses[index] = revocationStatuses[revocationStatuses.length - 1];
                revocationLookup[revocationHashes[revocationHashes.length - 1]] = index;
                delete revocationHashes[revocationHashes.length - 1];
                delete revocationStatuses[revocationStatuses.length - 1];
                revocationLookup[credentialHash] = 0;
            }
            // else: entry does not exist and status is 0: do nothing
        } else {
            if (index == 0) {// entry does not exist and will be added to list
                revocationHashes.push(credentialHash);
                revocationStatuses.push(status);
                revocationLookup[credentialHash] = revocationHashes.length - 1;
            } else {// entry exists and will be updated
                revocationStatuses[index] = status;
            }
        }
    }

    function getCustomRevocationMapping() public view returns (string[] memory) {
        return customRevocationCodes;
    }

    function getCustomRevocationMapping(int8 code) public view returns (string memory) {
        require(code < 0 && code >= - 128, "code not in range [-128,-1]");
        uint8 index = uint8(code + int8(128));
        return customRevocationCodes[index + 1];
    }

    function setCustomRevocationMapping(string[] memory descriptions) public isOwner() {
        require(descriptions.length == 128, "input array does not match size of 128");
        for (uint i = 0; i < 128; i++) {
            if (bytes(descriptions[i]).length < 1) {
                revert("string must not be empty");
            }
            customRevocationCodes[i + 1] = descriptions[i];
        }
    }

    function setCustomRevocationMapping(int8 code, string memory description) public isOwner() {
        require(code < 0 && code >= - 128, "code not in range [-128,-1]");
        require(bytes(description).length > 0, "string must not be empty");
        uint8 index = uint8(code + int8(128));
        customRevocationCodes[index + 1] = description;
    }
}
