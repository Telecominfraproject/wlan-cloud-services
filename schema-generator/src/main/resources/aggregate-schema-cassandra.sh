#!/bin/bash

## Usage of this script: (see https://connectustechnologies.atlassian.net/browse/TW-698)
## ./src/main/resources/aggregate-schema-cassandra.sh <GeneratedAggregatedFileName> <Jfrog-UserId>
#             <EncryptedJfrog-Password> <Jfrog repo URL> <TARGET_FILE_PATH in Jfrog repo>


## Script to be executed from schema-generator
export SCHEMA_DIR=$PWD
cd $SCHEMA_DIR

# Aggregates the sql files into one file
# $1: Aggregated SQLFile
generateSchemaFile() {
  cat $SCHEMA_DIR/src/main/resources/user_create_cassandra.cql >> $1
  find ../. -type f -name "schema-cassandra.cql" | grep -v target |xargs cat >> $1
  echo "\q " >> $1
  mv $1 $SCHEMA_DIR/target
}

# Deploys the sql files in JFrog Artifactory
# $1: Aggregated SQLFile
# $2: Artifactory Username
# $3: Encrypted Artifactory Password
# $4: Artifactory Target file Path. E.g.; "/0.0.1-SNAPSHOT/sql/cloud-sdk-schema-cassandra.sql"
deployToArtifactory() {
  curl -u$2:$3 -T $SCHEMA_DIR/target/$1 "$4/$5"
}

#decryptPassword() {
#  echo $1 |  openssl enc -aes-128-cbc -a -d -salt -pass pass:abcdef
#}

generateSchemaFile $1

deployToArtifactory $1 $2 $3 $4 $5

