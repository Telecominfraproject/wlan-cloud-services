#!/bin/bash

LOGGING_PROPS=" -Dlogging.config=file:/app/intcloudcomp/logback.xml"

BACKEND_SERVER="${BACKEND_SERVER}"

HOST_PROPS=" "
if [[ -n $BACKEND_SERVER ]]
then
  #echo Use specifed local host
  HOST_PROPS+=" -Dtip.wlan.externalHostName=$BACKEND_SERVER"
  HOST_PROPS+=" -Dtip.wlan.internalHostName=$BACKEND_SERVER"
  HOST_PROPS+=" -Dtip.wlan.introspectTokenApi.host=${BACKEND_SERVER}:9091"
fi

JVM_EXTRA_PROPS=" ${JVM_MEM_OPTIONS:- } "

export ALL_PROPS="$JVM_EXTRA_PROPS $LOGGING_PROPS $HOST_PROPS"

java $ALL_PROPS -jar app.jar
