#!/bin/bash
# Program name: pingip.sh
date
TEMPFILE=/tmp/tempfile.tmp
echo 0 > $TEMPFILE
cat list.txt |  while read output
do
    ping -c 1 -w 5 "$output">/dev/null
    if [ $? -eq 0 ]; then
    echo "node $output is up"
    counter=$[$(cat $TEMPFILE) + 1]
    echo $counter > $TEMPFILE
    else
    echo "node $output is down"
    fi
done
cat $TEMPFILE
