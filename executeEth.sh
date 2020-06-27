#!/bin/bash
mkdir -p ./benchmarks/ethereum
rm -f ./benchmarks/ethereum/resource.log
while true; do top -n 1 -b | sed -n '3p;4p;8p;9p;10p;11p;12p;' >> ./benchmarks/ethereum/resource.log; sleep 60; done &
ID=$!
./gradlew runEthGenericTests
kill $ID