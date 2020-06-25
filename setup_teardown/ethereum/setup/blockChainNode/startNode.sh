#!/bin/bash

cp ../wallets1/* . -rf
ACC_ADDRESS=$(cat address)


authorityString=$(cat authorities)

GENESIS=$(cat genesisTemplate)
GENESIS="${GENESIS}\n\t\"extraData\": \"0x0000000000000000000000000000000000000000000000000000000000000000${authorityString}0000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000\"\n}"
echo -e "$GENESIS" >genesis.json
geth --datadir="node" init genesis.json
rm -f nohup.out
nohup geth --datadir node/ --syncmode 'full' --port 30311 --rpc --rpcaddr '0.0.0.0' --rpcport 8545 --rpcapi 'personal,db,eth,net,web3,txpool,miner' --bootnodes "$BOOT_ENODE" --networkid 1515 --gasprice '0' -unlock "$ACC_ADDRESS" --password node/password.txt --allow-insecure-unlock --mine &
