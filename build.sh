#!/bin/bash
./gradlew clean build
if [ $? -ne 0 ]; then
    echo "Build failed!"
    exit 1
fi
./gradlew bootRun
echo "Build succeeded!"
