FROM adoptopenjdk/openjdk13:alpine-jre

LABEL maintainer="TipWlan"
LABEL version="1.0"
LABEL description="This image runs the mqtt broker (mosquitto) and \
a java process that hosts all cloud services (accessible via REST APIs) with the OpenSync gateway."

# Update the package list and upgrade installed packages
RUN apk update && apk upgrade 
RUN mkdir -p /opt/tip-wlan/certs
VOLUME ["/opt/tip-wlan/certs"]

# Configure mqtt broker
RUN apk add mosquitto 
RUN mkdir -p /mosquitto/data /mosquitto/log
COPY app/mqtt/mosquitto.conf /etc/mosquitto/mosquitto.conf
VOLUME ["/mosquitto/data", "/mosquitto/log"]


# Configure Cloud and OpenSync Gateway
RUN mkdir -p /app/logs /app/opensync /app/config
COPY maven/app.jar /app
COPY app/opensync/logback.xml /app/opensync/logback.xml
COPY app/run.sh /app
COPY app/opensync/EquipmentExample.json /app/opensync/EquipmentExample.json
COPY app/opensync/ProfileAPExample.json /app/opensync/ProfileAPExample.json
COPY app/opensync/ProfileSsid.json /app/opensync/ProfileSsid.json
COPY app/opensync/LocationBuildingExample.json /app/opensync/LocationBuildingExample.json
COPY app/opensync/ProfileMetrics.json /app/opensync/ProfileMetrics.json
COPY app/opensync/ProfileHotspot20.json /app/opensync/ProfileHotspot20.json
COPY app/opensync/ProfileOperator.json /app/opensync/ProfileOperator.json
COPY app/opensync/ProfileVenue.json /app/opensync/ProfileVenue.json
COPY app/opensync/ProfileIdProvider.json /app/opensync/ProfileIdProvider.json
COPY app/opensync/ProfileRf.json /app/opensync/ProfileRf.json
COPY app/commit.properties /app


VOLUME ["/app/logs", "/app/config"]

RUN chmod +x /app/run.sh

# The ports are used as follows:
# 1883 - MQTT broker
# 6643 - OVSDB redirector 
# 6640 - OVSDB manager 
# 4043 - REST APIs with client certificate auth
# 4044 - REST APIs with webtoken auth
EXPOSE 1883 6640 6643 4043 4044

WORKDIR /app
CMD ["/app/run.sh"]
