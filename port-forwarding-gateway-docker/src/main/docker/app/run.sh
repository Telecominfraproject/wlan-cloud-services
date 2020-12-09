#!/bin/bash

PROFILES=" -Dspring.profiles.include=use_ssl_with_client_cert_and_digest_auth,client_certificate_and_digest_auth,RestTemplateConfiguration_X509_client_cert_auth"

LOGGING_PROPS=" -Dlogging.config=file:/app/port-forwarding-gateway/logback.xml"

# SSC_URL: something like https://${SSC_SERVER_HOST}:9031
SSC_URL=${SSC_RELEASE_URL}
# PROV_URL: something like https://${PROV_SERVER_HOST}:9091
PROV_URL=${PROV_RELEASE_URL}
# PF_GATEWAY_URL: something like https://${PF_GATEWAY_SERVER_HOST}:7070
PF_GATEWAY_URL=${PF_GATEWAY_RELEASE_URL}
PF_GATEWAY_ENCRYPTION_KEY=${PF_GATEWAY_RELEASE_ENCRYPTION_KEY:='MyToKeN0MyToKeN1'}

PF_GATEWAY_EXT_HOST=${PF_GATEWAY_RELEASE_EXT_HOST:=''}
PF_GATEWAY_EXT_PORT=${PF_GATEWAY_RELEASE_EXT_PORT:='0'}

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

# Port-Forwarder Gateway Specific
HOST_PROPS+=" -Dtip.wlan.portForwarderGatewayBaseUrl=$PF_GATEWAY_URL"
HOST_PROPS+=" -Dtip.wlan.websocketSessionTokenEncryptionKey=$PF_GATEWAY_ENCRYPTION_KEY"
HOST_PROPS+=" -Dtip.wlan.externallyVisibleHostName=$PF_GATEWAY_EXT_HOST"
HOST_PROPS+=" -Dtip.wlan.externallyVisiblePort=$PF_GATEWAY_EXT_PORT"

JVM_EXTRA_PROPS=" ${JVM_MEM_OPTIONS:-' '} "

export ALL_PROPS="$JVM_EXTRA_PROPS $PROFILES $LOGGING_PROPS $HOST_PROPS"

java $ALL_PROPS -jar app.jar