#!/bin/bash

# Print every command, exit if any command fails
set -ex

# Build and install everything to the maven repository
mvn clean install

# Start the server and fork it once ready
mvn spring-boot:start -pl server

# Start the client, fork and remember the pid
mvn javafx:run -pl gazelleFX & pid=$!

# In case the user wants to Ctrl-C out of the script
# - we kill the javafx:run parent process and all its children
# - we kindly ask the server to shutdown
trap "
kill $pid \$(ps -o pid= --ppid $pid)
curl -X POST localhost:8080/actuator/shutdown
" SIGINT

# Wait for the client to stop before shutting down the server
wait $pid

# Kindly tell the server to shut down
curl -X POST localhost:8080/actuator/shutdown
sleep 2 # To allow the server to output some last stuff
