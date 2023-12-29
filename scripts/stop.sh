#!/bin/bash
echo -ne "Stopping..."
PID=$(cat /appvol/application/bfe-message-broker/application.pid)
kill $PID

while kill -0 $pid 2> /dev/null; do
    sleep 1
    echo -ne "."
done

echo " OK"