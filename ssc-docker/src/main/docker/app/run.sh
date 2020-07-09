#!/bin/bash

LOGGING_PROPS=" -Dlogging.config=file:/app/ssc/logback.xml"

DATABASE_PROPS=" -Dtip.wlan.cassandraConfigFileName=/app/ssc/cassandra-application.conf"

export ALL_PROPS="$LOGGING_PROPS $DATABASE_PROPS"

java $ALL_PROPS -jar app.jar