#!/usr/bin/env sh
set -eu

: "${BROKEN_LINKS_USERNAME:=guest}"
: "${BROKEN_LINKS_PASSWORD:=guest2026}"
: "${BROKEN_LINKS_MAX_PAGES:=25}"
: "${BROKEN_LINKS_MAX_LINKS:=500}"
: "${BROKEN_LINKS_FAIL_ON_BROKEN:=true}"
: "${BROKEN_LINKS_CHECK_EXTERNAL:=false}"
: "${BROKEN_LINKS_INCLUDE_ASSETS:=false}"

mvn test -Ptesting-xataka-broken-links \
  -DbrokenLinks.username="$BROKEN_LINKS_USERNAME" \
  -DbrokenLinks.password="$BROKEN_LINKS_PASSWORD" \
  -DbrokenLinks.maxPages="$BROKEN_LINKS_MAX_PAGES" \
  -DbrokenLinks.maxLinks="$BROKEN_LINKS_MAX_LINKS" \
  -DbrokenLinks.failOnBroken="$BROKEN_LINKS_FAIL_ON_BROKEN" \
  -DbrokenLinks.checkExternalLinks="$BROKEN_LINKS_CHECK_EXTERNAL" \
  -DbrokenLinks.includeAssets="$BROKEN_LINKS_INCLUDE_ASSETS"
