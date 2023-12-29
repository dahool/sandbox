#!/bin/bash
echo "Starting..."
#ENV JAVA_TOOL_OPTIONS "-Xms128m -Xmx228m -Xss512k -XX:MaxRAM=256m"
ROOTPATH=/appvol/application/bfe-message-broker
PIDFILE=$ROOTPATH/application.pid

mkdir -p /appvol/LOGS
nohup java -jar $ROOTPATH/bfe-message-broker.jar > /appvol/LOGS/console-bfe-message-broker.log 2>&1 &
echo $! > $PIDFILE

sleep 2

if ! [ -f $PIDFILE ]; then
  tail -20 /appvol/LOGS/console-bfe-message-broker.log
  echo "FAILED TO START"
  exit -1
fi

sleep 3

PID=$(cat $PIDFILE)

if ps -p $PID > /dev/null
then
  tail -20 /appvol/LOGS/console-bfe-message-broker.log
else
  tail -20 /appvol/LOGS/console-bfe-message-broker.log
  echo "FAILED TO START"
  exit -1
fi