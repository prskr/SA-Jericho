#!/usr/bin/env bash

CODEGEN_VERSION='2.2.3'
VERTX_SWAGGER_VERSION='1.0.0'

[[ -d out/swagger ]] || mkdir -p out/swagger

[[ -e swagger-codegen.jar ]] || curl "http://central.maven.org/maven2/io/swagger/swagger-codegen-cli/${CODEGEN_VERSION}/swagger-codegen-cli-${CODEGEN_VERSION}.jar" -o swagger-codegen.jar
[[ -e vertx-swagger-codegen.jar ]] || curl "http://central.maven.org/maven2/com/github/phiz71/vertx-swagger-codegen/${VERTX_SWAGGER_VERSION}/vertx-swagger-codegen-${VERTX_SWAGGER_VERSION}.jar" -o vertx-swagger-codegen.jar

java -cp swagger-codegen.jar:vertx-swagger-codegen.jar io.swagger.codegen.SwaggerCodegen generate \
  -l java-vertx \
  -o out/swagger \
  -i src/main/resources/swagger.yml \
  --group-id de.fhro.inf.sa \
  --artifact-id jerichoDemo
