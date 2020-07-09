FROM adoptopenjdk/openjdk13:alpine-jre
MAINTAINER ConnectUs

# Update the package list and upgrade installed packages
RUN apk update && apk upgrade && apk add bash
RUN mkdir /app && mkdir /app/logs && mkdir /app/ssc

COPY maven/app.jar /app
COPY app/run.sh /app
COPY app/logback.xml /app/ssc/logback.xml
COPY app/cassandra-application.conf /app/ssc/cassandra-application.conf

RUN chmod +x /app/run.sh

WORKDIR /app
ENTRYPOINT ["/app/run.sh"]