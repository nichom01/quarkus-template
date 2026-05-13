#!/usr/bin/env bash
# Build all Quarkus extensions (dependency order) then the reference application.
# Run from anywhere; paths are resolved relative to this script.
# Extra arguments are passed to every Maven invocation (e.g. -DskipTests, --batch-mode).

set -euo pipefail

ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
cd "$ROOT"

MVN="${MVN:-mvn}"

# Hibernate bytecode enhancement uses Byte Buddy; on very new JDKs (e.g. 25) this flag is required until Byte Buddy catches up.
export MAVEN_OPTS="-Dnet.bytebuddy.experimental=true${MAVEN_OPTS:+ ${MAVEN_OPTS}}"

build_module() {
  local pom="$1"
  shift
  echo "================================================================================"
  echo "==> mvn clean install -f ${pom}" "$@"
  echo "================================================================================"
  "${MVN}" clean install -f "${pom}" "$@"
}

# Extensions 1–4: no cross-dependencies; this order matches docs/FINAL-ARCHITECTURE.md
build_module "quarkus-drools-extension/pom.xml" "$@"
build_module "quarkus-kafka-reactive-extension/pom.xml" "$@"
build_module "quarkus-json-transform-extension/pom.xml" "$@"
build_module "quarkus-postgres-extension/pom.xml" "$@"

# Extension 5 depends on Extension 4 (PostgreSQL)
build_module "quarkus-transaction-processor-extension/pom.xml" "$@"

# Reference application depends on all five extensions
build_module "fraud-detection-service/pom.xml" "$@"

echo "================================================================================"
echo "Build finished successfully."
echo "================================================================================"
