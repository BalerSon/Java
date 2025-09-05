#!/bin/bash

mkdir -p build

javac -cp "lib/bcprov-jdk18on-1.78.jar" -d build src/*.java

if [ $? -ne 0 ]; then
    echo "❌ Ошибка компиляции!"
    exit 1
fi

cat > manifest.txt << EOF
Manifest-Version: 1.0
Main-Class: Main
Class-Path: lib/bcprov-jdk18on-1.78.jar
EOF

jar cvfm HashCheck.jar manifest.txt -C build .

if [ $? -ne 0 ]; then
    echo "❌ Ошибка создания JAR!"
    exit 1
fi