#!/usr/bin/env bash

if [[ -z "${SPARK_HOME}" ]]; then
  echo "SPARK_HOME is not defined"
  exit 1
fi
#Launch Assignment engine job
export APP_HOME="$( cd "$(dirname "$0")" ; cd ..;pwd -P )"

#export SPARK_HOME=/opt/mapr/spark/spark-2.1.0

CLASSPATH="$(find ${APP_HOME}/lib/ -name '*.jar' -type f -print0| tr '\0' ',' )"

APP_JAR="$(ls $APP_HOME/*.jar)"

printf "APP_HOME:%s \nAPP_JAR=%s \nclasspath: %s" "$APP_HOME" "$APP_JAR" "$CLASSPATH"

${SPARK_HOME}/bin/spark-submit \
--class com.ts.blog.batch.CategoryAssignmentApp \
--master yarn \
--deploy-mode client \
--jars "$CLASSPATH" \
"${APP_JAR}" "$@"