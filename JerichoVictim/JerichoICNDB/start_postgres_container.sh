#!/usr/bin/env bash

docker run -d --rm -p 5432:5432 -e POSTGRES_PASSWORD=W@c[3~DV>~:]4%+5 -e POSTGRES_USER=jericho -e POSTGRES_db=jericho -e PGDATA=/var/lib/postgresql/data/pgdata -v "~/tmp/jericho-pgsql:/var/lib/postgresql/data" postgres:alpine