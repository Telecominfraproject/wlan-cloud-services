FROM adoptopenjdk/openjdk13:alpine-jre
MAINTAINER TipWlan

# Update the package list and upgrade installed packages
RUN apk update && apk upgrade && apk add bash
RUN mkdir /app && mkdir /app/logs && mkdir /app/port-forwarding-gateway

COPY maven/app.jar /app
COPY app/run.sh /app
COPY app/port-forwarding-gateway/logback.xml /app/port-forwarding-gateway/logback.xml

RUN chmod +x /app/run.sh

WORKDIR /app
ENTRYPOINT ["/app/run.sh"]