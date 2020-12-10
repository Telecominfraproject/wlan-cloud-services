#!/bin/bash

PROFILES=" -Dspring.profiles.include=use_ssl_with_client_cert_and_basic_auth,client_certificate_and_basic_auth,use_single_ds,RestTemplateConfiguration_X509_client_cert_auth"

LOGGING_PROPS=" -Dlogging.config=file:/app/prov/logback.xml"

DATABASE_PROPS=" -DsingleDataSource.props=file:/app/prov/datasource.properties"

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

REMOTE_DEBUG_PORT=${REMOTE_DEBUG_PORT:-'5007'}
if [ "x$REMOTE_DEBUG_ENABLE" == "xtrue" ]
then
  REMOTE_DEBUG=" -agentlib:jdwp=transport=dt_socket,server=y,address=*:$REMOTE_DEBUG_PORT,suspend=n"
else
  REMOTE_DEBUG=" "
fi

JVM_EXTRA_PROPS=" ${JVM_MEM_OPTIONS:- } "

export ALL_PROPS="$JVM_EXTRA_PROPS $PROFILES $LOGGING_PROPS $DATABASE_PROPS $HOST_PROPS $REMOTE_DEBUG"

java $ALL_PROPS -jar app.jar