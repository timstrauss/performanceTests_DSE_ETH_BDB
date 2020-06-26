#!/bin/bash
nodepid=$(lsof -i :8545 | sed -n 2p | cut -d ' ' -f5)
kill $nodepid
rm -rf ../setup/blockChainNode/node