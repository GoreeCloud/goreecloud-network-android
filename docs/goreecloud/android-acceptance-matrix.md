# GoreeCloud Network Android Acceptance Matrix

## Purpose

This matrix defines the minimum Android validation evidence required before the dedicated GoreeCloud Network client may be considered for production use.

## Artifact identity

For every candidate artifact record:

- source commit SHA;
- version name and version code;
- APK SHA-256;
- AAB SHA-256 where applicable;
- application ID;
- signing-certificate SHA-256;
- build workflow/run identifier;
- whether the candidate is debug, signed snapshot, or release.

The artifact must pass `scripts/verify-goreecloud-artifact.sh` before functional testing begins.

## Enrollment and identity

Validate:

1. First-run flow defaults to the approved GoreeCloud management endpoint.
2. Browser/SSO login succeeds and returns to the application correctly.
3. Setup-key enrollment succeeds with an approved one-off test key.
4. Expired, revoked, already-used, and invalid setup keys fail safely.
5. Sign-out disconnects the profile as expected.
6. Removing a local profile does not falsely imply server-side device revocation.
7. Server-side revocation removes effective network access from the device.
8. Reauthentication succeeds after an intentionally expired session where supported.

## VPN lifecycle

Validate on a real approved Android device:

- initial Android VPN permission request;
- connect and disconnect from the main GoreeCloud control;
- Quick Settings tile behavior;
- reconnect after Wi-Fi to mobile-data transition;
- reconnect after mobile-data to Wi-Fi transition;
- reconnect after temporary loss of Internet connectivity;
- reconnect after app process restart;
- reconnect after device reboot when applicable;
- behavior when another VPN is active;
- Always On VPN behavior if GoreeCloud intends to support it;
- user-visible state remains consistent with actual tunnel state.

## Networking behavior

Validate:

- direct peer reachability;
- relayed peer reachability where the controlled environment can force or observe it;
- approved resource reachability;
- denied resource behavior;
- resource-selection toggles;
- client routes enabled/disabled;
- server routes enabled/disabled;
- managed DNS enabled/disabled;
- DNS resolution of approved private names;
- excluded DNS behavior where applicable;
- client firewall enforcement enabled/disabled only in controlled tests;
- inbound-blocking behavior where supported;
- IPv6 overlay behavior where supported;
- force-relay behavior only in the isolated environment;
- network SSH only in a controlled test with a documented authorization path.

## Glaze UI and accessibility

Validate:

- GoreeCloud Network naming is used in ordinary user-facing surfaces;
- Devices and Resources terminology is consistent;
- connection state is understandable without animation-only cues;
- screen-reader labels exist for resource toggles and primary connection actions;
- destructive profile/server actions include clear consequences;
- diagnostics explain local creation and redaction accurately;
- Wardveil presentation does not claim a protected/secure state unless runtime evidence is available.

## Privacy and provenance

Validate:

- no Advertising ID permission in the installed APK;
- no Firebase Analytics, Firebase Crashlytics, Google Services, or Lottie runtime markers in the candidate APK;
- no NetBird-hosted Terms/Privacy links are presented as GoreeCloud policy;
- About/provenance information accurately identifies the maintained-fork origin;
- diagnostic bundles do not automatically upload to an external service;
- sensitive diagnostic output can be redacted before sharing.

## Package migration/coexistence

Before changing from the inherited `io.netbird.client` application ID, test the intended final GoreeCloud package separately and record:

- coexistence with stock NetBird if migration requires both installed simultaneously;
- VPN permission state after installing the new package;
- Always On VPN configuration impact;
- SSO/deep-link callback behavior;
- Quick Settings tile migration;
- local profile/state migration strategy;
- rollback to the previous accepted package;
- whether uninstall/reinstall is required and what user data is lost.

## Failure handling

The candidate fails acceptance if it:

- reports connected when the VPN tunnel is not effective;
- silently broadens access after a settings change;
- cannot recover from ordinary network transitions;
- loses or corrupts unrelated connection profiles;
- requires production setup keys or credentials for testing;
- sends analytics/telemetry through integrations removed by GoreeCloud;
- is signed by an unapproved certificate;
- uses an unexpected application ID;
- cannot be rolled back to the previously accepted client state.

## Exit criteria

A candidate may advance only after debug/signed build validation, artifact identity checks, unit/emulator tests, and this real-device matrix have evidence. Final production approval additionally requires successful isolated control-plane testing and a documented rollback path.
