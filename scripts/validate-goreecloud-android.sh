#!/usr/bin/env bash
set -euo pipefail

fail() {
  printf 'GoreeCloud Android validation failed: %s\n' "$1" >&2
  exit 1
}

check_absent() {
  local pattern="$1"
  shift
  if grep -RInE --exclude-dir=build --exclude-dir=.gradle -- "$pattern" "$@" >/tmp/goreecloud-android-validation.txt 2>/dev/null; then
    cat /tmp/goreecloud-android-validation.txt >&2
    fail "retired integration or identifier detected: $pattern"
  fi
}

# These integrations were intentionally removed from the GoreeCloud client.
check_absent 'com\.google\.gms\.google-services|com\.google\.firebase|firebase-(analytics|crashlytics)|google-services\.json' app gradle build.gradle.kts settings.gradle.kts
check_absent 'com\.airbnb\.android:lottie|libs\.lottie' app gradle
check_absent 'com\.google\.android\.gms\.permission\.AD_ID' app/src

# Retired upstream release-secret names must not return to active build/workflow files.
check_absent 'NETBIRD_UPLOAD_|GOOGLE_JSON|GPLAY_KEYSTORE' app .github/workflows .github/actions

# The GoreeCloud self-hosted management default is an intentional product requirement.
grep -RIn --exclude-dir=build --exclude-dir=.gradle -- 'https://netbird.goreecloud.com' app/src >/dev/null \
  || fail 'self-hosted GoreeCloud management endpoint is not present in app source'

printf 'GoreeCloud Android source-integrity validation passed.\n'
