#!/bin/sh
set -e

# Default port to 8080 if PORT is not set by Render / cloud provider
PORT="${PORT:-8080}"
echo "[HarmeshMart] Configuring Apache Tomcat HTTP connector to port: ${PORT}..."

# Replace the connector port dynamically in Tomcat conf/server.xml
sed -i -E "s/<Connector port=\"[0-9]+\"/<Connector port=\"${PORT}\"/g" /usr/local/tomcat/conf/server.xml

# Configure optimal JVM memory flags for Render 512MB container if not overridden
if [ -z "$JAVA_OPTS" ]; then
    export JAVA_OPTS="-Xms128m -Xmx384m -XX:+UseG1GC -Djava.awt.headless=true"
fi
echo "[HarmeshMart] Starting Apache Tomcat with JAVA_OPTS: ${JAVA_OPTS}..."

exec catalina.sh run
