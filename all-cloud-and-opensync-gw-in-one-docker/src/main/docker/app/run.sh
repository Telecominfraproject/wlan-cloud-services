#!/bin/bash

#no need for extra profiles here - defaults in application.properties are enough
#PROFILES=" -Dspring.profiles.include="

SSL_PROPS=" "
SSL_PROPS+=" -Dssl.props=file:/opt/tip-wlan/certs/ssl.properties"
SSL_PROPS+=" -Dtip.wlan.httpClientConfig=file:/opt/tip-wlan/certs/httpClientConfig.json"

CLIENT_MQTT_SSL_PROPS=" "
CLIENT_MQTT_SSL_PROPS+=" -Djavax.net.ssl.keyStore=/opt/tip-wlan/certs/client_keystore.jks"
CLIENT_MQTT_SSL_PROPS+=" -Djavax.net.ssl.keyStorePassword=mypassword"
CLIENT_MQTT_SSL_PROPS+=" -Djavax.net.ssl.trustStore=/opt/tip-wlan/certs/truststore.jks"
CLIENT_MQTT_SSL_PROPS+=" -Djavax.net.ssl.trustStorePassword=mypassword"
CLIENT_MQTT_SSL_PROPS+=" -Dtip.wlan.mqttBroker.password=admin"

OVSDB_PROPS=" "
OVSDB_PROPS+=" -Dtip.wlan.ovsdb.managerAddr=opensync-controller"
OVSDB_PROPS+=" -Dtip.wlan.ovsdb.listenPort=6640 "
OVSDB_PROPS+=" -Dtip.wlan.ovsdb.redirector.listenPort=6643"
OVSDB_PROPS+=" -Dtip.wlan.ovsdb.timeoutSec=30"
OVSDB_PROPS+=" -Dtip.wlan.ovsdb.trustStore=/opt/tip-wlan/certs/truststore.jks"
OVSDB_PROPS+=" -Dtip.wlan.ovsdb.keyStore=/opt/tip-wlan/certs/server.pkcs12"
OVSDB_PROPS+=" -Dtip.wlan.ovsdb.customerEquipmentFileName=$OVSDB_EQUIPMENT_CONFIG_FILE"
OVSDB_PROPS+=" -Dtip.wlan.ovsdb.apProfileFileName=$OVSDB_APPROFILE_CONFIG_FILE"
OVSDB_PROPS+=" -Dtip.wlan.ovsdb.metricsProfileFileName=$OVSDB_METRICSPROFILE_CONFIG_FILE"
OVSDB_PROPS+=" -Dtip.wlan.ovsdb.hotspot20ProfileFileName=$OVSDB_HOTSPOT20PROFILE_CONFIG_FILE"
OVSDB_PROPS+=" -Dtip.wlan.ovsdb.operatorProfileFileName=$OVSDB_OPERATORPROFILE_CONFIG_FILE"
OVSDB_PROPS+=" -Dtip.wlan.ovsdb.venueProfileFileName=$OVSDB_VENUEPROFILE_CONFIG_FILE"
OVSDB_PROPS+=" -Dtip.wlan.ovsdb.idProviderProfileFileName=$OVSDB_IDPROVIDERPROFILE_CONFIG_FILE"
OVSDB_PROPS+=" -Dtip.wlan.ovsdb.rfProfileFileName=$OVSDB_RFPROFILE_CONFIG_FILE"
OVSDB_PROPS+=" -Dtip.wlan.ovsdb.ssidProfileFileName=$OVSDB_SSIDPROFILE_CONFIG_FILE"
OVSDB_PROPS+=" -Dtip.wlan.ovsdb.radiusProfileFileName=$OVSDB_RADIUSPROFILE_CONFIG_FILE"
OVSDB_PROPS+=" -Dtip.wlan.ovsdb.locationFileName=$OVSDB_LOCATION_CONFIG_FILE"
OVSDB_PROPS+=" -Dtip.wlan.ovsdb.wifi-iface.default_bridge=$OVSDB_IF_DEFAULT_BRIDGE"
OVSDB_PROPS+=" -Dtip.wlan.ovsdb.wifi-iface.default_radio0=$OVSDB_IF_DEFAULT_RADIO_0"
OVSDB_PROPS+=" -Dtip.wlan.ovsdb.wifi-iface.default_radio1=$OVSDB_IF_DEFAULT_RADIO_1"
OVSDB_PROPS+=" -Dtip.wlan.ovsdb.wifi-iface.default_radio2=$OVSDB_IF_DEFAULT_RADIO_2"
OVSDB_PROPS+=" -Dtip.wlan.ovsdb.wifi-device.radio0=$OVSDB_DEVICE_RADIO_0"
OVSDB_PROPS+=" -Dtip.wlan.ovsdb.wifi-device.radio1=$OVSDB_DEVICE_RADIO_1"
OVSDB_PROPS+=" -Dtip.wlan.ovsdb.wifi-device.radio2=$OVSDB_DEVICE_RADIO_2"

echo OVSDB_PROPS $OVSDB_PROPS

MQTT_PROPS=" "
MQTT_PROPS+=" -Dtip.wlan.mqttBroker.address.internal=tip-wlan-opensync-mqtt-broker-internal"
MQTT_PROPS+=" -Dtip.wlan.mqttBroker.address.external=tip-wlan-opensync-mqtt-broker-external"
MQTT_PROPS+=" -Dtip.wlan.mqttBroker.listenPort=1883"

LOGGING_PROPS=" -Dlogging.config=file:/app/opensync/logback.xml"

RESTAPI_PROPS=" "
RESTAPI_PROPS+=" -Dserver.port=443  -Dtip.wlan.secondaryPort=444 "
RESTAPI_PROPS+=" -Dtip.wlan.introspectTokenApi.host=localhost:444 "

echo "REST APIs are available over port 443 with client certificate auth, and over port 444 with webtoken auth"
echo "Documentation for the supported REST APIs is at "
echo "https://github.com/Telecominfraproject/wlan-cloud-services/blob/master/portal-services/src/main/resources/portal-services-openapi.yaml "

SPRING_EXTRA_PROPS=" --add-opens java.base/java.lang=ALL-UNNAMED"

export ALL_PROPS="$PROFILES $SSL_PROPS $CLIENT_MQTT_SSL_PROPS $OVSDB_PROPS $MQTT_PROPS $LOGGING_PROPS $RESTAPI_PROPS $SPRING_EXTRA_PROPS"

java $ALL_PROPS -jar app.jar