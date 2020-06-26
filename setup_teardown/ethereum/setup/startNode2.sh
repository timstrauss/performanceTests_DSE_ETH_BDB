#!/bin/bash
cd bootNode || exit
BOOTNODE_ADD=172.20.8.222
enode=$(cat enodeAdd)
partone=$(echo "$enode" | cut -d @ -f1)
partthree=$(echo "$enode" | cut -d @ -f2 | cut -d : -f2)
export BOOT_ENODE="${partone}@${BOOTNODE_ADD}:${partthree}"
echo "$BOOT_ENODE" > enode
sleep 5
cd ../blockChainNode || exit
/bin/bash ./startNode2.sh
