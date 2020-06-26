#!/bin/bash
cd bootNode || exit
NODE_HOST=172.20.8.222
/bin/bash ./startBootNode.sh
enode=$(cat enodeAdd)
partone=$(echo "$enode" | cut -d @ -f1)
partthree=$(echo "$enode" | cut -d @ -f2 | cut -d : -f2)
export BOOT_ENODE="${partone}@${NODE_HOST}:${partthree}"
echo "$BOOT_ENODE" > enode
sleep 5
cd ../blockChainNode || exit
/bin/bash ./startNode.sh
