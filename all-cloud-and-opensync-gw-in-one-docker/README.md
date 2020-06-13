# Docker images for combined cloud services and opensync gateway

Documentation for the supported REST APIs is at <https://github.com/Telecominfraproject/wlan-cloud-services/blob/master/portal-services/src/main/resources/portal-services-openapi.yaml> 

REST APIs are available over port 4043 with client certificate auth, and over port 4044 with webtoken auth


## This docker image has the cloud services (REST APIs), OpenSync Gateway, and mosquitto MQTT broker  

    docker run --rm -it -p 1883:1883 -p 6640:6640 -p 6643:6643 -p 4043:4043 -p 4044:4044 \
      -v /Users/dtop/static_opensync_gw/db:/mosquitto/data \
      -v /Users/dtop/static_opensync_gw/log:/mosquitto/log \
      -v /opt/tip-wlan/certs:/opt/tip-wlan/certs \
      -v /Users/dtop/static_opensync_gw/log:/app/logs \
      tip-tip-wlan-cloud-docker-repo.jfrog.io/all-cloud-and-opensync-gw-and-mqtt:0.0.1-SNAPSHOT


## This docker image has the cloud services (REST APIs) and OpenSync Gateway 

    docker run --rm -it -p 6640:6640 -p 6643:6643 -p 4043:443 -p 4044:444 \
      -v /Users/dtop/static_opensync_gw/db:/mosquitto/data \
      -v /Users/dtop/static_opensync_gw/log:/mosquitto/log \
      -v /opt/tip-wlan/certs:/opt/tip-wlan/certs \
      -v /Users/dtop/static_opensync_gw/log:/app/logs \
      tip-tip-wlan-cloud-docker-repo.jfrog.io/all-cloud-and-opensync-gw:0.0.1-SNAPSHOT
