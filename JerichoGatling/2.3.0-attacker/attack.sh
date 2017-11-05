#!/bin/sh

$GATLING_HOME/bin/gatling.sh -nr -on $(hostname) -m
mv $GATLING_HOME/results/*/simulation.log  /opt/gatling/shared-results/$(hostname).log

while [ 1 ]
do
    sleep 3600
done