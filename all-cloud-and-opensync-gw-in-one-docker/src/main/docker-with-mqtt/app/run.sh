#!/bin/sh

# Prepare the hosts file - do it only if does not have required entries
n1=`grep opensync-mqtt-broker.zone1.lab.wlan.tip.build /etc/hosts | wc -l`

if [[ $n1 -eq 0  ]]
then
  echo Adding opensync-mqtt-broker.zone1.lab.wlan.tip.build to /etc/hosts
  echo "127.0.0.1 opensync-mqtt-broker.zone1.lab.wlan.tip.build" >> /etc/hosts
fi

n2=`grep opensync-controller.zone1.lab.wlan.tip.build /etc/hosts | wc -l`

if [[ $n2 -eq 0  ]]
then
  echo Adding opensync-controller.zone1.lab.wlan.tip.build to /etc/hosts
  echo "127.0.0.1 opensync-controller.zone1.lab.wlan.tip.build" >> /etc/hosts
fi


echo Starting mosquitto MQTT broker
/usr/sbin/mosquitto -d -c /etc/mosquitto/mosquitto.conf

# Provide default values for the environment variables
MQTT_CLIENT_KEYSTORE_PASSWORD="${MQTT_CLIENT_KEYSTORE_PASSWORD:=mypassword}"
MQTT_CLIENT_KEYSTORE_FILE="${MQTT_CLIENT_KEYSTORE_FILE:=/opt/tip-wlan/certs/client_keystore.jks}"
MQTT_TRUSTSTORE_FILE="${MQTT_TRUSTSTORE_FILE:=/opt/tip-wlan/certs/truststore.jks}"
MQTT_TRUSTSTORE_PASSWORD="${MQTT_TRUSTSTORE_PASSWORD:=mypassword}"

OVSDB_SERVER_KEYSTORE_FILE="${OVSDB_SERVER_KEYSTORE_FILE:=/opt/tip-wlan/certs/server.pkcs12}"
OVSDB_SERVER_KEYSTORE_PASSWORD="${OVSDB_SERVER_KEYSTORE_PASSWORD:=mypassword}"
OVSDB_SERVER_TRUSTSTORE_FILE="${OVSDB_SERVER_TRUSTSTORE_FILE:=/opt/tip-wlan/certs/truststore.jks}"
OVSDB_SERVER_TRUSTSTORE_PASSWORD="${OVSDB_SERVER_TRUSTSTORE_PASSWORD:=mypassword}"
OVSDB_EQUIPMENT_CONFIG_FILE="${OVSDB_EQUIPMENT_CONFIG_FILE:=/app/opensync/EquipmentExample.json}"
OVSDB_APPROFILE_CONFIG_FILE="${OVSDB_AP_PROFILE_CONFIG_FILE:=/app/opensync/ProfileAPExample.json}"
OVSDB_METRICSPROFILE_CONFIG_FILE="${OVSDB_METRICSPROFILE_CONFIG_FILE:=/app/opensync/ProfileMetrics.json}"
OVSDB_HOTSPOT20SPROFILE_CONFIG_FILE="${OVSDB_HOTSPOT20PROFILE_CONFIG_FILE:=/app/opensync/ProfileHotspot20.json}"
OVSDB_OPERATORPROFILE_CONFIG_FILE="${OVSDB_OPERATORPROFILE_CONFIG_FILE:=/app/opensync/ProfileOperator.json}"
OVSDB_VENUEPROFILE_CONFIG_FILE="${OVSDB_VENUEPROFILE_CONFIG_FILE:=/app/opensync/ProfileVenue.json}"
OVSDB_IDPROVIDERPROFILE_CONFIG_FILE="${OVSDB_IDPROVIDERPROFILE_CONFIG_FILE:=/app/opensync/ProfileIdProvider.json}"
OVSDB_RFPROFILE_CONFIG_FILE="${OVSDB_RFPROFILE_CONFIG_FILE:=/app/opensync/ProfileRf.json}"
OVSDB_SSIDPROFILE_CONFIG_FILE="${OVSDB_SSIDPROFILE_CONFIG_FILE:=/app/opensync/ProfileSsid.json}"
OVSDB_LOCATION_CONFIG_FILE="${OVSDB_LOCATION_CONFIG_FILE:=/app/opensync/LocationBuildingExample.json}"
OVSDB_RADIUSPROFILE_CONFIG_FILE="${OVSDB_RADIUSPROFILE_CONFIG_FILE:=/app/opensync/ProfileRadius.json}"

OVSDB_IF_DEFAULT_BRIDGE="${OVSDB_IF_DEFAULT_BRIDGE:=lan}"
echo $OVSDB_IF_DEFAULT_BRIDGE
OVSDB_IF_DEFAULT_RADIO_0="${OVSDB_IF_DEFAULT_RADIO_0:=wlan0}"
echo $OVSDB_IF_DEFAULT_RADIO_0
OVSDB_IF_DEFAULT_RADIO_1="${OVSDB_IF_DEFAULT_RADIO_1:=wlan1}"
echo $OVSDB_IF_DEFAULT_RADIO_1
OVSDB_IF_DEFAULT_RADIO_2="${OVSDB_IF_DEFAULT_RADIO_2:=wlan2}"
echo $OVSDB_IF_DEFAULT_RADIO_2
OVSDB_DEVICE_RADIO_0="${OVSDB_DEVICE_RADIO_0:=radio0}"
echo $OVSDB_DEVICE_RADIO_0
OVSDB_DEVICE_RADIO_1="${OVSDB_DEVICE_RADIO_1:=radio1}"
echo $OVSDB_DEVICE_RADIO_1
OVSDB_DEVICE_RADIO_2="${OVSDB_DEVICE_RADIO_2:=radio2}"
echo $OVSDB_DEVICE_RADIO_2
OVSDB_OFF_CHANNEL_REPORTING_INTERVAL_SECONDS="${OVSDB_OFF_CHANNEL_REPORTING_INTERVAL_SECONDS:=120}"
OVSDB_REPORTING_INTERVAL_SECONDS="${OVSDB_REPORTING_INTERVAL_SECONDS:=60}"


echo Reading AP configuration from $OVSDB_CONFIG_FILE

EXT_CLIENT_KEYSTORE_PASSWORD="${EXT_CLIENT_KEYSTORE_PASSWORD:=mypassword}"
EXT_CLIENT_KEYSTORE_FILE="${EXT_CLIENT_KEYSTORE_FILE:=/opt/tip-wlan/certs/client_keystore.jks}"
EXT_TRUSTSTORE_FILE="${EXT_TRUSTSTORE_FILE:=/opt/tip-wlan/certs/truststore.jks}"
EXT_TRUSTSTORE_PASSWORD="${EXT_TRUSTSTORE_PASSWORD:=mypassword}"

MQTT_BROKER_HOST_INTERNAL="${MQTT_BROKER_HOST_INTERNAL:=opensync-mqtt-broker.zone1.lab.wlan.tip.build}"
MQTT_BROKER_HOST_EXTERNAL="${MQTT_BROKER_HOST_EXTERNAL:=opensync-mqtt-broker.zone1.lab.wlan.tip.build}"
OVSDB_MANAGER_HOST="${OVSDB_MANAGER_HOST:=opensync-controller.zone1.lab.wlan.tip.build}"

LOGBACK_CONFIG_FILE="${LOGBACK_CONFIG_FILE:=/app/opensync/logback.xml}"

# Create ssl.properties file
cat > /app/ssl.properties <<END_OF_FILE
truststorePass=$OVSDB_SERVER_TRUSTSTORE_PASSWORD
truststoreFile=file:$OVSDB_SERVER_TRUSTSTORE_FILE
truststoreType=JKS
truststoreProvider=SUN

keyAlias=1
keystorePass=$OVSDB_SERVER_KEYSTORE_PASSWORD
keystoreFile=file:$OVSDB_SERVER_KEYSTORE_FILE
keystoreType=pkcs12
keystoreProvider=SunJSSE

sslProtocol=TLS
END_OF_FILE

# Create httpClientConfig.json file
cat > /app/httpClientConfig.json <<END_OF_FILE
{
"maxConnectionsTotal":100,
"maxConnectionsPerRoute":10,
"truststoreType":"JKS",
"truststoreProvider":"SUN",
"truststoreFile":"file:$EXT_TRUSTSTORE_FILE",
"truststorePass":"$EXT_TRUSTSTORE_PASSWORD",
"keystoreType":"JKS",
"keystoreProvider":"SUN",
"keystoreFile":"file:$EXT_CLIENT_KEYSTORE_FILE",
"keystorePass":"$EXT_CLIENT_KEYSTORE_PASSWORD",
"keyAlias":"clientkeyalias",
"credentialsList":[
    {"host":"localhost","port":-1,"user":"user","password":"password"}
    ]

}
END_OF_FILE

# Set environment for the cloud and opensync gateway process

#no need for extra profiles here - defaults in application.properties are enough
#PROFILES=" -Dspring.profiles.include="

SSL_PROPS=" "
SSL_PROPS="$SSL_PROPS -Dssl.props=file:/app/ssl.properties"
SSL_PROPS="$SSL_PROPS -Dtip.wlan.httpClientConfig=file:/app/httpClientConfig.json"

CLIENT_MQTT_SSL_PROPS=" "
CLIENT_MQTT_SSL_PROPS="$CLIENT_MQTT_SSL_PROPS -Djavax.net.ssl.keyStore=$MQTT_CLIENT_KEYSTORE_FILE"
CLIENT_MQTT_SSL_PROPS="$CLIENT_MQTT_SSL_PROPS -Djavax.net.ssl.keyStorePassword=$MQTT_CLIENT_KEYSTORE_PASSWORD"
CLIENT_MQTT_SSL_PROPS="$CLIENT_MQTT_SSL_PROPS -Djavax.net.ssl.trustStore=$MQTT_TRUSTSTORE_FILE"
CLIENT_MQTT_SSL_PROPS="$CLIENT_MQTT_SSL_PROPS -Djavax.net.ssl.trustStorePassword=$MQTT_TRUSTSTORE_PASSWORD"

OVSDB_PROPS=" "
OVSDB_PROPS="$OVSDB_PROPS -Dtip.wlan.ovsdb.managerAddr=$OVSDB_MANAGER_HOST"
OVSDB_PROPS="$OVSDB_PROPS -Dtip.wlan.ovsdb.listenPort=6640 "
OVSDB_PROPS="$OVSDB_PROPS -Dtip.wlan.ovsdb.redirector.listenPort=6643"
OVSDB_PROPS="$OVSDB_PROPS -Dtip.wlan.ovsdb.timeoutSec=30"
OVSDB_PROPS="$OVSDB_PROPS -Dtip.wlan.ovsdb.trustStore=$OVSDB_SERVER_TRUSTSTORE_FILE"
OVSDB_PROPS="$OVSDB_PROPS -Dtip.wlan.ovsdb.trustStorePassword=$OVSDB_SERVER_TRUSTSTORE_PASSWORD"
OVSDB_PROPS="$OVSDB_PROPS -Dtip.wlan.ovsdb.keyStore=$OVSDB_SERVER_KEYSTORE_FILE"
OVSDB_PROPS="$OVSDB_PROPS -Dtip.wlan.ovsdb.keyStorePassword=$OVSDB_SERVER_KEYSTORE_PASSWORD"
OVSDB_PROPS="$OVSDB_PROPS -Dtip.wlan.ovsdb.customerEquipmentFileName=$OVSDB_EQUIPMENT_CONFIG_FILE"
OVSDB_PROPS="$OVSDB_PROPS -Dtip.wlan.ovsdb.apProfileFileName=$OVSDB_APPROFILE_CONFIG_FILE"
OVSDB_PROPS="$OVSDB_PROPS -Dtip.wlan.ovsdb.metricsProfileFileName=$OVSDB_METRICSPROFILE_CONFIG_FILE"
OVSDB_PROPS="$OVSDB_PROPS -Dtip.wlan.ovsdb.hotspot20ProfileFileName=$OVSDB_HOTSPOT20PROFILE_CONFIG_FILE"
OVSDB_PROPS="$OVSDB_PROPS -Dtip.wlan.ovsdb.operatorProfileFileName=$OVSDB_OPERATORPROFILE_CONFIG_FILE"
OVSDB_PROPS="$OVSDB_PROPS -Dtip.wlan.ovsdb.venueProfileFileName=$OVSDB_VENUEPROFILE_CONFIG_FILE"
OVSDB_PROPS="$OVSDB_PROPS -Dtip.wlan.ovsdb.idProviderProfileFileName=$OVSDB_IDPROVIDERPROFILE_CONFIG_FILE"
OVSDB_PROPS="$OVSDB_PROPS -Dtip.wlan.ovsdb.rfProfileFileName=$OVSDB_RFPROFILE_CONFIG_FILE"
OVSDB_PROPS="$OVSDB_PROPS -Dtip.wlan.ovsdb.ssidProfileFileName=$OVSDB_SSIDPROFILE_CONFIG_FILE"
OVSDB_PROPS="$OVSDB_PROPS -Dtip.wlan.ovsdb.radiusProfileFileName=$OVSDB_RADIUSPROFILE_CONFIG_FILE"
OVSDB_PROPS="$OVSDB_PROPS -Dtip.wlan.ovsdb.locationFileName=$OVSDB_LOCATION_CONFIG_FILE"
OVSDB_PROPS="$OVSDB_PROPS -Dtip.wlan.ovsdb.wifi-iface.default_bridge=$OVSDB_IF_DEFAULT_BRIDGE"
OVSDB_PROPS="$OVSDB_PROPS -Dtip.wlan.ovsdb.wifi-iface.default_radio0=$OVSDB_IF_DEFAULT_RADIO_0"
OVSDB_PROPS="$OVSDB_PROPS -Dtip.wlan.ovsdb.wifi-iface.default_radio1=$OVSDB_IF_DEFAULT_RADIO_1"
OVSDB_PROPS="$OVSDB_PROPS -Dtip.wlan.ovsdb.wifi-iface.default_radio2=$OVSDB_IF_DEFAULT_RADIO_2"
OVSDB_PROPS="$OVSDB_PROPS -Dtip.wlan.ovsdb.wifi-device.radio0=$OVSDB_DEVICE_RADIO_0"
OVSDB_PROPS="$OVSDB_PROPS -Dtip.wlan.ovsdb.wifi-device.radio1=$OVSDB_DEVICE_RADIO_1"
OVSDB_PROPS="$OVSDB_PROPS -Dtip.wlan.ovsdb.wifi-device.radio2=$OVSDB_DEVICE_RADIO_2"
OVSDB_PROPS="$OVSDB_PROPS -Dtip.wlan.defaultOffChannelReportingIntervalSeconds=$OVSDB_OFF_CHANNEL_REPORTING_INTERVAL_SECONDS"
OVSDB_PROPS="$OVSDB_PROPS -Dtip.wlan.defaultReportingIntervalSeconds=$OVSDB_REPORTING_INTERVAL_SECONDS"

echo OVSDB_PROPS $OVSDB_PROPS


MQTT_PROPS=" "
MQTT_PROPS="$MQTT_PROPS -Dtip.wlan.mqttBroker.address.internal=$MQTT_BROKER_HOST_INTERNAL"
MQTT_PROPS="$MQTT_PROPS -Dtip.wlan.mqttBroker.address.external=$MQTT_BROKER_HOST_EXTERNAL"
MQTT_PROPS="$MQTT_PROPS -Dtip.wlan.mqttBroker.listenPort=1883"

LOGGING_PROPS=" -Dlogging.config=file:$LOGBACK_CONFIG_FILE"

RESTAPI_PROPS=" "
RESTAPI_PROPS="$RESTAPI_PROPS -Dserver.port=4043 -Dtip.wlan.secondaryPort=4044 "
RESTAPI_PROPS="$RESTAPI_PROPS -Dtip.wlan.introspectTokenApi.host=localhost:4044 "

echo "REST APIs are available over port 4043 with client certificate auth, and over port 4044 with webtoken auth"
echo "Documentation for the supported REST APIs is at "
echo "https://github.com/Telecominfraproject/wlan-cloud-services/blob/master/portal-services/src/main/resources/portal-services-openapi.yaml "

SPRING_EXTRA_PROPS=" --add-opens java.base/java.lang=ALL-UNNAMED"

JVM_EXTRA_PROPS=" ${JVM_MEM_OPTIONS:- } "

export ALL_PROPS="$JVM_EXTRA_PROPS $PROFILES $SSL_PROPS $CLIENT_MQTT_SSL_PROPS $OVSDB_PROPS $MQTT_PROPS $LOGGING_PROPS $RESTAPI_PROPS $SPRING_EXTRA_PROPS "

echo Starting dynamic opensync wifi controller

#echo Result: $ALL_PROPS
java $ALL_PROPS -jar app.jar > /app/opensync-wifi-controller-stdout.out 2>&1

