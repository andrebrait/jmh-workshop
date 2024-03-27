#!/usr/bin/env bash

# http://redsymbol.net/articles/unofficial-bash-strict-mode/
set -euo pipefail
IFS=$'\n\t'

for i in "allan" "bob" "joe" "steve"; do
  ./gradlew -q -PmainClass="com.github.andrebrait.workshops.jmh.presentation.SuperDuperBenchmark_Fix${1}" ${2:-} run --args="${i}"
done