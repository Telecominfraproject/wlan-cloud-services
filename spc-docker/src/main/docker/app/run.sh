#!/bin/bash

LOGGING_PROPS=" -Dlogging.config=file:/app/spc/logback.xml"

export ALL_PROPS="$LOGGING_PROPS"

java $ALL_PROPS -jar app.jar