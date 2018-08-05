#!/bin/bash -eu

echo "cleaning build folders"
./lein.sh clean

echo "building a jar"
./lein.sh jar
