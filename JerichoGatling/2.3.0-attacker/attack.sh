#!/bin/sh

$GATLING_HOME/bin/gatling.sh -nr -on $(hostname) -m
mv $GATLING_HOME/results/*/simulation.log  /opt/gatling/shared-results/$(hostname).log