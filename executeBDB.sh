#!/bin/bash
mkdir -p ./benchmarks/bdb
rm -f ./benchmarks/bdb/resource.log
while true; do top -n 1 -b | sed -n '3p;4p;8p;9p;10p;11p;12p;' >> ./benchmarks/bdb/resource.log; sleep 60; done &
ID=$!
./gradlew runBDBGenericTests
kill $ID