#!/bin/bash

docker-compose -f ./docker-compose-attacker-local.yml up --scale gatling-attacker=3
