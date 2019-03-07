#!/bin/sh

mkdir -p build
docker run --rm --entrypoint cat graal-app  /home/application/function.zip > build/function.zip

sam local start-api -t sam.yaml -p 3000 --docker-network host


