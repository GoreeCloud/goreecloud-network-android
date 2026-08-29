#!/usr/bin/env bash
set -euo pipefail

apk_path="${1:-}"
expected_cert_sha256="${GOREECLOUD_NETWORK_SIGNING_CERT_SHA256:-}"
expected_package="${GOREECLOUD_NETWORK_EXPECTED_PACKAGE:-io.netbird.client}"

if [[ -z "$apk_path" || ! -f "$apk_path" ]]; then
  echo "::error::Usage: $0 <apk-path>"
  exit 1
fi

command -v apksigner >/dev/null 2>&1 || {
  echo "::error::apksigner is required to verify the APK"
  exit 1
}
command -v aapt >/dev/null 2>&1 || {
  echo "::error::aapt is required to inspect the APK manifest"
  exit 1
}

verify_output="$(apksigner verify --verbose --print-certs "$apk_path")"
echo "$verify_output"

if ! grep -q "Verifies" <<<"$verify_output"; then
  echo "::error::APK signature verification did not report a verified artifact"
  exit 1
fi

signer_sha256="$(sed -n 's/^Signer #1 certificate SHA-256 digest: //p' <<<"$verify_output" | head -n1 | tr '[:lower:]' '[:upper:]')"
if [[ -z "$signer_sha256" ]]; then
  echo "::error::Could not extract signer SHA-256 digest"
  exit 1
fi

echo "Signer SHA-256: $signer_sha256"

if [[ -n "$expected_cert_sha256" ]]; then
  normalized_expected="$(tr '[:lower:]' '[:upper:]' <<<"$expected_cert_sha256" | tr -d ':[:space:]')"
  normalized_actual="$(tr -d ':[:space:]' <<<"$signer_sha256")"
  if [[ "$normalized_actual" != "$normalized_expected" ]]; then
    echo "::error::APK signer does not match GOREECLOUD_NETWORK_SIGNING_CERT_SHA256"
    exit 1
  fi
else
  echo "::warning::GOREECLOUD_NETWORK_SIGNING_CERT_SHA256 is not configured; cryptographic signature validity was checked, but signer identity was not pinned"
fi

badging="$(aapt dump badging "$apk_path")"
package_name="$(sed -n "s/^package: name='\([^']*\)'.*/\1/p" <<<"$badging" | head -n1)"
version_name="$(sed -n "s/^package: .*versionName='\([^']*\)'.*/\1/p" <<<"$badging" | head -n1)"
version_code="$(sed -n "s/^package: .*versionCode='\([^']*\)'.*/\1/p" <<<"$badging" | head -n1)"

if [[ "$package_name" != "$expected_package" ]]; then
  echo "::error::Unexpected package name '$package_name'; expected '$expected_package'"
  exit 1
fi

if aapt dump permissions "$apk_path" | grep -q 'com.google.android.gms.permission.AD_ID'; then
  echo "::error::Advertising ID permission is present in the built APK"
  exit 1
fi

if unzip -l "$apk_path" | grep -Eqi 'firebase|crashlytics|google-services|lottie'; then
  echo "::error::Retired telemetry/presentation integration markers were found in the APK"
  exit 1
fi

sha256sum "$apk_path"
echo "Package: $package_name"
echo "Version code: $version_code"
echo "Version name: $version_name"
echo "GoreeCloud Android artifact verification passed."
