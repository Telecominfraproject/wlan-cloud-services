FROM adoptopenjdk/openjdk13:alpine-jre
MAINTAINER ConnectUs

# Update the package list and upgrade installed packages
RUN apk update && apk upgrade && apk add bash
RUN mkdir /app && mkdir /app/logs && mkdir /app/portal

COPY maven/app.jar /app
COPY app/run.sh /app
COPY app/logback.xml /app/portal/logback.xml

RUN chmod +x /app/run.sh

WORKDIR /app
ENTRYPOINT ["/app/run.sh"]