#!/bin/bash

LOGGING_PROPS=" -Dlogging.config=file:/app/ssc/logback.xml"

DATABASE_PROPS=" -Dtip.wlan.cassandraConfigFileName=/app/ssc/cassandra-application.conf"

# SSC_URL: something like https://${SSC_SERVER_HOST}:9031
SSC_URL=${SSC_RELEASE_URL}
# PROV_URL: something like https://${PROV_SERVER_HOST}:9091
PROV_URL=${PROV_RELEASE_URL}

HOST_PROPS=" "
HOST_PROPS+=" -Dtip.wlan.cloudEventDispatcherBaseUrl=$SSC_URL"
HOST_PROPS+=" -Dtip.wlan.statusServiceBaseUrl=$SSC_URL"
HOST_PROPS+=" -Dtip.wlan.routingServiceBaseUrl=$SSC_URL"
HOST_PROPS+=" -Dtip.wlan.alarmServiceBaseUrl=$SSC_URL"
HOST_PROPS+=" -Dtip.wlan.systemEventServiceBaseUrl=$SSC_URL"
HOST_PROPS+=" -Dtip.wlan.clientServiceBaseUrl=$SSC_URL"
HOST_PROPS+=" -Dtip.wlan.serviceMetricServiceBaseUrl=$SSC_URL"

// PROV URLs
HOST_PROPS+=" -Dtip.wlan.customerServiceBaseUrl=$PROV_URL"
HOST_PROPS+=" -Dtip.wlan.portalUserServiceBaseUrl=$PROV_URL"
HOST_PROPS+=" -Dtip.wlan.firmwareServiceBaseUrl=$PROV_URL"
HOST_PROPS+=" -Dtip.wlan.locationServiceBaseUrl=$PROV_URL"
HOST_PROPS+=" -Dtip.wlan.manufacturerServiceBaseUrl=$PROV_URL"
HOST_PROPS+=" -Dtip.wlan.equipmentServiceBaseUrl=$PROV_URL"
HOST_PROPS+=" -Dtip.wlan.profileServiceBaseUrl=$PROV_URL"

export ALL_PROPS="$LOGGING_PROPS $DATABASE_PROPS $HOST_PROPS"

java $ALL_PROPS -jar app.jar