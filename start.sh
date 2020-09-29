#!/bin/bash

mvn install || exit;

echo "Starting server"
mvn spring-boot:run -pl server & server_pid=$!
echo "Server pid: $server_pid"

echo "Starting javafx"
mvn javafx:run -pl gazelleFX & client_pid=$!

trap "kill $server_pid $client_pid" SIGINT

wait $client_pid

kill $server_pid;
