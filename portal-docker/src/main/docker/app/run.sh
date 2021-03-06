#!/bin/bash

LOGGING_PROPS=" -Dlogging.config=file:/app/portal/log/logback.xml"

SSL_PROPS=" "
SSL_PROPS+=" -Dssl.props=file:/app/portal/certs/ssl.properties"

FILE_STORE_DIRECTORY="${FILE_STORE_DIRECTORY_INTERNAL:=/tmp/filestore}"

FILE_STORE_PROPS=" "
FILE_STORE_PROPS+=" -Dtip.wlan.fileStoreDirectory=$FILE_STORE_DIRECTORY"

# SSC_URL: something like https://${SSC_SERVER_HOST}:9031
SSC_URL=${SSC_RELEASE_URL}
# PROV_URL: something like https://${PROV_SERVER_HOST}:9091
PROV_URL=${PROV_RELEASE_URL}

# SSC URLs
HOST_PROPS=" "
HOST_PROPS+=" -Dtip.wlan.cloudEventDispatcherBaseUrl=$SSC_URL"
HOST_PROPS+=" -Dtip.wlan.statusServiceBaseUrl=$SSC_URL"
HOST_PROPS+=" -Dtip.wlan.routingServiceBaseUrl=$SSC_URL"
HOST_PROPS+=" -Dtip.wlan.alarmServiceBaseUrl=$SSC_URL"
HOST_PROPS+=" -Dtip.wlan.systemEventServiceBaseUrl=$SSC_URL"
HOST_PROPS+=" -Dtip.wlan.clientServiceBaseUrl=$SSC_URL"
HOST_PROPS+=" -Dtip.wlan.serviceMetricServiceBaseUrl=$SSC_URL"

# PROV URLs
HOST_PROPS+=" -Dtip.wlan.customerServiceBaseUrl=$PROV_URL"
HOST_PROPS+=" -Dtip.wlan.portalUserServiceBaseUrl=$PROV_URL"
HOST_PROPS+=" -Dtip.wlan.firmwareServiceBaseUrl=$PROV_URL"
HOST_PROPS+=" -Dtip.wlan.locationServiceBaseUrl=$PROV_URL"
HOST_PROPS+=" -Dtip.wlan.manufacturerServiceBaseUrl=$PROV_URL"
HOST_PROPS+=" -Dtip.wlan.equipmentServiceBaseUrl=$PROV_URL"
HOST_PROPS+=" -Dtip.wlan.profileServiceBaseUrl=$PROV_URL"
HOST_PROPS+=" -Dtip.wlan.adoptionMetricsServiceBaseUrl=$PROV_URL"

REMOTE_DEBUG_PORT=${REMOTE_DEBUG_PORT:-'5006'}
if [ "x$REMOTE_DEBUG_ENABLE" == "xtrue" ]
then
  REMOTE_DEBUG=" -agentlib:jdwp=transport=dt_socket,server=y,address=*:$REMOTE_DEBUG_PORT,suspend=n"
else
  REMOTE_DEBUG=" "
fi

JVM_EXTRA_PROPS=" ${JVM_MEM_OPTIONS:- } "

export ALL_PROPS="$JVM_EXTRA_PROPS $SSL_PROPS $LOGGING_PROPS $HOST_PROPS $FILE_STORE_PROPS $REMOTE_DEBUG"

java $ALL_PROPS -jar app.jar