#!/bin/bash -eu

echo "cleaning build folders"
./lein.sh clean

echo "building a jar"
./lein.sh jar

echo "moving the built jar"
# This is because CircleCI doesn't support wildcards when storing artifacts and the jar contains the version number
mkdir target/jar
mv target/*.jar target/jar/