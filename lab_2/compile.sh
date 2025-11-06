#!/bin/bash

mkdir -p build

find src -name "*.java" > sources.txt
javac -cp "libs/*" -d build @sources.txt

if [ $? -eq 0 ]; then
    echo "✅ Компиляция успешна!"
    rm sources.txt
else
    echo "❌ Ошибка компиляции"
    rm sources.txt
    exit 1
fi