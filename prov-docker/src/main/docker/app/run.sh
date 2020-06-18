#!/bin/bash

PROFILES=" -Dspring.profiles.include=use_ssl_with_client_cert_and_basic_auth,client_certificate_and_basic_auth,use_single_ds,RestTemplateConfiguration_X509_client_cert_auth"

LOGGING_PROPS=" -Dlogging.config=file:/app/prov/logback.xml"

DATABASE_PROPS=" -DsingleDataSource.props=file:/app/prov/datasource.properties"

export ALL_PROPS="$PROFILES $LOGGING_PROPS $DATABASE_PROPS"

java $ALL_PROPS -jar app.jar