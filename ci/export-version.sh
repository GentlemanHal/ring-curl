#!/bin/bash -eu

version=$(head -n 1 project.clj | cut -d'"' -f 2)

echo "export VERSION=\"$version\"" >> $BASH_ENV
