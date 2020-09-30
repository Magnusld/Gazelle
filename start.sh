#!/bin/bash

# Print every command, exit if any command fails
set -ex

mvn install -pl common
mvn spring-boot:start -pl server
mvn javafx:run -pl gazelleFX & pid=$!

# In case the user wants to Ctrl-C out of the script
# - we kill the javafx:run parent process
# - we find the java process for GazelleFX
# - kill it
# - we kindly ask the server to shutdown
trap "\
kill $pid
client=\$(jps -l | grep gazelle.ui.GazelleFX | awk '{print \$1}' | head -n 1)
kill \$client
curl -X POST localhost:8080/actuator/shutdown
sleep 2" SIGINT

wait $pid

curl -X POST localhost:8080/actuator/shutdown
sleep 2 # To allow the server to output some last stuff
