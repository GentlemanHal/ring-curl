#!/bin/bash -eu

echo "running linting"
./lein.sh lint

echo "running the tests"
./lein.sh test
