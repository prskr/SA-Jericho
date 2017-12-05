#!/bin/bash

docker run -d --rm --name gitpitch -e GP_GITLAB_BASE=https://inf-git.fh-rosenheim.de/ -e GP_GITLAB_API=https://inf-git.fh-rosenheim.de/api/v4/ -e GP_GITLAB_AS_DEFAULT=true -p 9000:9000 knsit/gitpitch
