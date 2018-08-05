#!/bin/bash -eu

# Move the
mv target/jar/*.jar target/

echo "deploying to clojars"
./lein.sh deploy releases
