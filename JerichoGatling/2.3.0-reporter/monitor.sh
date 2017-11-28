#!/bin/sh

REPORTS_DIR="results"
HTML_OUT="/opt/gatling/html-out"

function calcChecksum()
{
    echo $( tar c $REPORTS_DIR | md5sum )
}

rm -rf "$HTML_OUT/"*
mkdir -p "$HTML_OUT"
LAST_CHECKSUM=$(calcChecksum)

while [ 1 ]
do
    NEW_CHECKSUM=$(calcChecksum)
    if [ "$LAST_CHECKSUM" != "$NEW_CHECKSUM" ]; then
        LAST_CHECKSUM="$NEW_CHECKSUM"
        echo "Updating HTML report..."
        cp "$REPORTS_DIR/"*.log  "$HTML_OUT/"
        gatling.sh -ro "$HTML_OUT/"
        if [ $? -ne 0 ]; then
            LAST_CHECKSUM=""
        fi
    else
        echo "Nothing to do..."
    fi
    sleep 10
done