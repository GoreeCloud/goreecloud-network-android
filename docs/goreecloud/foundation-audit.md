# GoreeCloud Network Android Foundation Audit

## Scope

This audit records the first GoreeCloud review of the inherited NetBird Android client. It covers licensing, Android build targets, privacy-sensitive dependencies, permissions, CI, and the compatibility boundary for a dedicated GoreeCloud mobile client.

## Verified baseline characteristics

- The Android client remains a fork of `netbirdio/android-client` and is licensed under GPLv3.
- The inherited build targets compile/target SDK 35 with minimum SDK 26.
- The Android client depends on the inherited NetBird mobile/core integration and must preserve tunnel, routing, enrollment, and authentication compatibility during the first GoreeCloud phases.
- The inherited workflow set includes debug, release, snapshot, and NetBird-core bump workflows.
- The inherited app declared Google Advertising ID permission and could conditionally enable Google Services, Firebase Crashlytics, and Firebase Analytics when `google-services.json` was present.

## Privacy changes completed in this branch

- Removed Google Services and Firebase Crashlytics plugin declarations from the root build.
- Removed conditional Firebase Analytics and Crashlytics dependencies from the app module.
- Removed the Google Advertising ID permission from the Android manifest.
- No tunnel, routing, authentication, enrollment, or VPN behavior was intentionally changed.

## GoreeCloud mobile boundary

The dedicated GoreeCloud client will progressively replace upstream branding and UX with Glaze UI and Wardveil Security presentation while retaining the inherited networking core until a separately validated replacement is justified. Administrative control remains primarily in the web dashboard; the mobile client should focus on the enrolled device, connection state, approved resources, DNS/security state, and diagnostics.

## Next engineering gates

1. Validate debug build after telemetry removal.
2. Inventory VPN-service, gomobile, authentication, enrollment, and management-server configuration paths.
3. Define the GoreeCloud application ID/package migration plan without breaking upgrade or testing workflows.
4. Establish GoreeCloud signing and APK acceptance procedures.
5. Begin Glaze UI conversion only after the privacy-clean baseline builds successfully.
