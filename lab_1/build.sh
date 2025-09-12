#!/bin/bash

mkdir -p build

javac -cp "lib/bcprov-jdk18on-1.78.jar" -d build src/*.java

cd lib
jar xf bcprov-jdk18on-1.78.jar
cp -r . ../build/
cd ..

rm -rf build/META-INF/*.SF build/META-INF/*.DSA build/META-INF/*.RSA build/META-INF/*.EC

echo "Manifest-Version: 1.0" > manifest.txt
echo "Main-Class: Main" >> manifest.txt

jar cfm HashCheck.jar manifest.txt -C build .
