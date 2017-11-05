#!/bin/sh

REPORTS_DIR="/opt/gatling/results/"
HTML_OUT="/opt/gatling/html-out"

function calcChecksum()
{
    echo $( tar c --absolute-names $REPORTS_DIR | md5sum )
}

LAST_CHECKSUM=$(calcChecksum)

while [ 1 ]
do
    NEW_CHECKSUM=$(calcChecksum)
    if [ "$LAST_CHECKSUM" != "$NEW_CHECKSUM" ]; then
        LAST_CHECKSUM="$NEW_CHECKSUM"
        echo "Updating HTML report..."
        cp "$REPORTS_DIR/*.log" "$HTML_OUT/"
        gatling.sh -ro "$HTML_OUT"
        rm -f "$HTML_OUT/*.log"
    else
        echo "Nothing to do..."
    fi
    sleep 10
done