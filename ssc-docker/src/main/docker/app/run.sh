#!/bin/bash

LOGGING_PROPS=" -Dlogging.config=file:/app/ssc/logback.xml"

CASSANDRA_HOST="tip-wlan-cassandra-headless:9042"
CASSANDRA_TIP_USER="tip_user"
CASSANDRA_TIP_PASSWORD="tip_password"

DATABASE_PROPS=" "
DATABASE_PROPS+=" -Ddatastax-java-driver.basic.contact-points.0=$CASSANDRA_HOST"
DATABASE_PROPS+=" -Ddatastax-java-driver.advanced.auth-provider.username=$CASSANDRA_TIP_USER"
DATABASE_PROPS+=" -Ddatastax-java-driver.advanced.auth-provider.password=$CASSANDRA_TIP_PASSWORD"

export ALL_PROPS="$LOGGING_PROPS $DATABASE_PROPS"

java $ALL_PROPS -jar app.jar