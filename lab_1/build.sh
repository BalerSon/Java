#!/bin/bash

mkdir -p build

javac -cp "lib/bcprov-jdk18on-1.78.jar" -d build src/*.java

jar cvfm HashCheck.jar manifest.txt -C build .