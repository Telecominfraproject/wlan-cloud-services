#!/bin/bash

LOGGING_PROPS=" -Dlogging.config=file:/app/prov/logback.xml"

export ALL_PROPS="$LOGGING_PROPS"

java $ALL_PROPS -jar app.jar