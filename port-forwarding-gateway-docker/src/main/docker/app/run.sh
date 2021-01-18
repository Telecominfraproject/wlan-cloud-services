#!/bin/bash

# local_port_range that Java process can use
# These are then assigned to the container ports (in the deployment.yaml) which can either:
# later be opened by the port-forwarding-gateway service as NodePorts (preferred)
# or use kubectl port-forwarding to forward the container ports. Example:
# kubectl port-forward pods/<port-forwarding-gw-pod> <local-machine-port>:<debugPort on the Pod>
LOCAL_PORT_RANGE_START=${EXT_PORT_RANGE_START:='30410'}
LOCAL_PORT_RANGE_END=${EXT_PORT_RANGE_END:='30435'}
sysctl -w net.ipv4.ip_local_port_range="$LOCAL_PORT_RANGE_START    $LOCAL_PORT_RANGE_END"

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


JVM_EXTRA_PROPS=" ${JVM_MEM_OPTIONS:- } "

# Port-Forwarder Gateway Specific
HOST_PROPS+=" -Dtip.wlan.portForwarderGatewayBaseUrl=$PF_GATEWAY_URL"
HOST_PROPS+=" -Dtip.wlan.websocketSessionTokenEncryptionKey=$PF_GATEWAY_ENCRYPTION_KEY"
HOST_PROPS+=" -Dtip.wlan.externallyVisibleHostName=$PF_GATEWAY_EXT_HOST"
HOST_PROPS+=" -Dtip.wlan.externallyVisiblePort=$PF_GATEWAY_EXT_PORT"



REMOTE_DEBUG_PORT=${REMOTE_DEBUG_PORT:-'5010'}
if [ "x$REMOTE_DEBUG_ENABLE" == "xtrue" ]
then
  REMOTE_DEBUG=" -agentlib:jdwp=transport=dt_socket,server=y,address=*:$REMOTE_DEBUG_PORT,suspend=n"
else
  REMOTE_DEBUG=" "
fi

export ALL_PROPS="$JVM_EXTRA_PROPS $PROFILES $LOGGING_PROPS $HOST_PROPS $REMOTE_DEBUG"

java $ALL_PROPS -jar app.jar
