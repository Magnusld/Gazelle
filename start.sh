#!/bin/bash

# Print every command, exit if any command fails
set -ex

mvn install -pl common
mvn spring-boot:start -pl server
mvn javafx:run -pl gazelleFX & pid=$!

# In case the user wants to Ctrl-C out of the script
# - we kill the javafx:run parent process and all its children
# - we kindly ask the server to shutdown
trap "\
kill $pid \$(ps -o pid= --ppid $pid)
curl -X POST localhost:8080/actuator/shutdown\
" SIGINT

wait $pid

curl -X POST localhost:8080/actuator/shutdown
sleep 2 # To allow the server to output some last stuff
