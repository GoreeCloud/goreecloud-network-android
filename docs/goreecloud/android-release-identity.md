# GoreeCloud Network Android Release Identity

## Status

Active design constraint. No application-ID or Java namespace migration has been approved for a release candidate yet.

## Current inherited identity

The Android fork currently retains:

- namespace: `io.netbird.client`
- application ID: `io.netbird.client`

This is intentional during compatibility-focused foundation work. It must not be interpreted as the final GoreeCloud release identity.

## GoreeCloud signing boundary

Release and signed-snapshot builds use GoreeCloud-specific signing inputs:

- `GOREECLOUD_NETWORK_KEYSTORE`
- `GOREECLOUD_NETWORK_UPLOAD_STORE_FILE`
- `GOREECLOUD_NETWORK_UPLOAD_STORE_PASSWORD`
- `GOREECLOUD_NETWORK_UPLOAD_KEY_ALIAS`
- `GOREECLOUD_NETWORK_UPLOAD_KEY_PASSWORD`
- `GOREECLOUD_NETWORK_SIGNING_CERT_SHA256`

Signing material must be stored only in approved GitHub environment/repository secrets or the designated GoreeCloud secrets system. Keystores, passwords, aliases, private keys, and reusable credentials must never be committed to the repository.

The release workflow must fail if required signing material is absent. It must not silently produce an unsigned artifact while presenting it as a GoreeCloud release.

`GOREECLOUD_NETWORK_SIGNING_CERT_SHA256` is the expected public certificate fingerprint for the approved GoreeCloud Android signing identity. Signed release and snapshot workflows verify the produced APK cryptographically and compare the signer against that fingerprint when configured.

## Artifact verification contract

Every signed release or signed snapshot must pass `scripts/verify-goreecloud-artifact.sh` before distribution. The verifier currently requires evidence that:

- `apksigner` reports a valid APK signature;
- the signer certificate SHA-256 digest can be extracted;
- the signer digest matches `GOREECLOUD_NETWORK_SIGNING_CERT_SHA256` when the fingerprint is configured;
- the built package identity matches the currently approved expected package identity;
- the APK does not request Google Advertising ID permission;
- retired Firebase, Crashlytics, Google Services, and Lottie markers are not present in the APK archive;
- the APK SHA-256 digest is recorded;
- the release AAB exists, is non-empty, passes ZIP integrity validation, and has a SHA-256 digest recorded.

Until the final application ID is approved, the artifact validator expects the inherited compatibility identity `io.netbird.client`. The validator must be updated in the same reviewed migration that changes the application ID.

## Migration decision required

Before a production GoreeCloud Android release, choose and document the final application ID and namespace. The migration must account for:

1. **Coexistence** — whether GoreeCloud Network must install alongside the stock NetBird Android client during migration and recovery testing.
2. **Upgrade behavior** — Android will not treat a differently signed or differently identified package as an in-place update. A migration plan must not assume upgrade continuity that Android cannot provide.
3. **VPN permission state** — package identity changes can require the user to grant VPN permission again and can affect Always On VPN configuration.
4. **Profiles and local state** — local connection profiles, preferences, enrollment state, and diagnostic settings must not be assumed to migrate automatically between application IDs.
5. **Deep links / browser authentication** — redirect handling and any package-specific intent filters must be tested after identity changes.
6. **Quick Settings tile / shortcuts** — Android system integrations should be verified after package migration.
7. **Signing custody** — the GoreeCloud signing key becomes a long-lived release identity and must have controlled backup, access, rotation/recovery documentation, and restricted use.
8. **Distribution** — GitHub Releases, direct APK distribution, managed distribution, and any future app-store path must all consume artifacts signed by the same approved GoreeCloud release identity unless a documented migration is intentionally performed.

## Proposed production direction

A dedicated GoreeCloud application ID should be preferred for the final product so GoreeCloud Network is independently installable, independently signed, and does not impersonate or overwrite the upstream NetBird package. The exact identifier remains intentionally undecided until build validation and coexistence testing can be performed.

## Release gates

Do not mark a GoreeCloud Network Android release candidate production-ready until all of the following have evidence:

- clean debug build;
- clean signed release APK and AAB build;
- artifact signature inspection and approved signer-fingerprint match;
- APK/AAB integrity and SHA-256 records;
- package/application-ID decision documented;
- coexistence behavior tested against the stock NetBird client where migration requires it;
- VPN permission and Always On VPN behavior tested;
- SSO/browser callback behavior tested;
- setup-key enrollment tested;
- connection profiles tested;
- routing, managed DNS, firewall, resource selection, and reconnection tested on a real Android device;
- rollback/recovery path documented;
- no Google Services, Firebase Analytics, Firebase Crashlytics, or Advertising ID integration reintroduced without an explicit GoreeCloud architectural decision.

## Compatibility boundary

This release-identity and artifact-verification work does not change WireGuard/tunnel establishment, TUN behavior, routing, DNS mechanics, firewall mechanics, gomobile behavior, SSO protocol behavior, setup-key protocol behavior, or management-server APIs.
