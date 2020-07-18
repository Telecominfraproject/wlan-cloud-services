#!/bin/bash

## Usage of this script: 
## ./src/main/resources/aggregate-schema-prov.sh <GeneratedAggregatedFileName> <Jfrog-UserId>

#             <EncryptedJfrog-Password> <Jfrog repo URL> <TARGET_FILE_PATH in Jfrog repo>


## Script to be executed from schema-generator
export SCHEMA_DIR=$PWD
cd $SCHEMA_DIR

# Aggregates the sql files into one file
# $1: Aggregated SQLFile
generateSchemaFile() {
  cat $SCHEMA_DIR/src/main/resources/user_create_prov.sql >> $1
  find ../. -type f -name "schema-postgresql.sql" | grep -v target |xargs cat >> $2
  cat $SCHEMA_DIR/src/main/resources/default_values_prov.sql >> $2
  mv $1 $2 $SCHEMA_DIR/target
}

# Deploys the sql files in JFrog Artifactory
# $1: SQLFile to create DB and Tip User
# $2: Aggregated SQLFile with DB Tables
# $3: Artifactory Username
# $4: Encrypted Artifactory Password
# $5: Artifactory Target file Path for DB-User schema. E.g.; "/0.0.1-SNAPSHOT/sql/cloud-sdk-schema-postgresql.sql" -- this needs to be executed by Postgres user
# $6: Artifactory Target file Path for creating Tables E.g.; "/0.0.1-SNAPSHOT/sql/cloud-sdk-schema-postgresql.sql" -- this needs to be executed by tip_user user
# This is important since the Postgres user is being authenticated using client certs whereas Tip_user is authenticated using Password.
deployToArtifactory() {
  curl -u$3:$4 -T $SCHEMA_DIR/target/$1 "$5/$6"
  curl -u$3:$4 -T $SCHEMA_DIR/target/$2 "$5/$7"
}

#decryptPassword() {
#  echo $1 |  openssl enc -aes-128-cbc -a -d -salt -pass pass:abcdef
#}

generateSchemaFile $1 $2

deployToArtifactory $1 $2 $3 $4 $5 $6 $7

