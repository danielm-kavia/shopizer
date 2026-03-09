#!/usr/bin/env sh
# -----------------------------------------------------------------------------
# Maven Wrapper startup script (POSIX)
#
# This repository is executed in environments where Maven may not be installed.
# The preview manager startCommand relies on Maven goals (tomcat7:run), so we
# ship Maven Wrapper to bootstrap a local Maven distribution automatically.
# -----------------------------------------------------------------------------

# Resolve the directory of this script (handles symlinks)
PRG="$0"
while [ -h "$PRG" ]; do
  ls="$(ls -ld "$PRG")"
  link="$(expr "$ls" : '.*-> \(.*\)$')"
  if expr "$link" : '/.*' >/dev/null; then
    PRG="$link"
  else
    PRG="$(dirname "$PRG")/$link"
  fi
done

BASEDIR="$(cd "$(dirname "$PRG")" >/dev/null 2>&1 && pwd)"

WRAPPER_JAR="$BASEDIR/.mvn/wrapper/maven-wrapper.jar"
WRAPPER_PROPERTIES="$BASEDIR/.mvn/wrapper/maven-wrapper.properties"

if [ ! -f "$WRAPPER_PROPERTIES" ]; then
  echo "Missing $WRAPPER_PROPERTIES" >&2
  exit 1
fi

# Prefer JAVA_HOME if set, else rely on java on PATH
if [ -n "$JAVA_HOME" ] && [ -x "$JAVA_HOME/bin/java" ]; then
  JAVA_EXEC="$JAVA_HOME/bin/java"
else
  JAVA_EXEC="java"
fi

exec "$JAVA_EXEC" -jar "$WRAPPER_JAR" -Dmaven.multiModuleProjectDirectory="$BASEDIR" "$@"
