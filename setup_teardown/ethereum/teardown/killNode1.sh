#!/bin/bash
bootnodepid=$(lsof -i :30310 | sed -n 2p | cut -d ' ' -f2)
nodepid=$(lsof -i :8545 | sed -n 2p | cut -d ' ' -f5)
kill $nodepid
kill $bootnodepid
rm -rf ../setup/blockChainNode/node