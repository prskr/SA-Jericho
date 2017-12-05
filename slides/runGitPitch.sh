#!/bin/bash

docker run -d --rm --name gitpitch -e GP_GITHUB_AS_DEFAULT=true -p 9000:9000 knsit/gitpitch-pdf:latest