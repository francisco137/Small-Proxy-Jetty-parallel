#!/bin/bash


ROOT_DIR=$(cd "$(dirname "$0")" ; pwd -P)
#"
cd $ROOT_DIR

sbt "test"

if [ $? -gt 0 ]; then
  echo "Tests failed, quitiing..."
  exit 1
fi

echo "Tests passed, starting server, please wait..."

sbt ";~jetty:start;console"

#( sbt ";~jetty:start;console" > sbt.log & ) &
