#!/bin/bash

./compile.sh

cat > MANIFEST.MF << EOF
Manifest-Version: 1.0
Main-Class: people.app.ConsoleApp
Class-Path: libs/jackson-core-2.15.2.jar libs/jackson-databind-2.15.2.jar libs/jackson-annotations-2.15.2.jar
EOF

jar cfm people-system.jar MANIFEST.MF -C build .

rm -f MANIFEST.MF

echo "JAR файл создан: people-system.jar"