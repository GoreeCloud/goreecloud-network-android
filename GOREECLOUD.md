# GoreeCloud Network for Android

This repository is the dedicated GoreeCloud Android private-networking client derived from the NetBird Android client.

## Product decision

The standard NetBird Android app may remain available temporarily for compatibility, migration, troubleshooting, and recovery. The intended long-term GoreeCloud mobile experience is this dedicated GoreeCloud-controlled client.

The initial implementation preserves the mature inherited networking core and Android VPN behavior while GoreeCloud replaces the product experience, enrollment workflow, settings, diagnostics, branding, Glaze UI presentation, and GoreeCloud-specific integrations in controlled phases.

## Upstream relationship

Upstream project: `netbirdio/android-client`

The inherited project currently uses Gradle Kotlin DSL. Its top-level build configuration targets Android compile/target SDK 35 with minimum SDK 26. The inherited build also declares Google Services and Firebase Crashlytics Gradle plugins, which must be reviewed before GoreeCloud production use.

## Licensing

The inherited Android client is licensed under GNU General Public License version 3. GoreeCloud will preserve the license and applicable source-code, attribution, copyright, and modification requirements.

## GoreeCloud mobile requirements

- Use **Glaze UI** adapted for Android ergonomics and accessibility.
- Present clear connection state, enrolled device identity, approved resources, DNS/security state, and useful diagnostics.
- Integrate **Wardveil Security by GoreeCloud** for security-state presentation and actionable warnings.
- Keep privileged administration separate from the ordinary mobile client unless a dedicated admin mode is deliberately approved later.
- Minimize telemetry and review every external analytics, crash-reporting, and hosted-service dependency before enabling it.
- Store credentials and sensitive device material only through appropriate Android secure-storage mechanisms.
- Support explicit enrollment, reauthentication, revocation, and device-lifecycle workflows.
- Preserve a safe fallback path to the stock NetBird client during development and migration.

## Privacy review targets

The inherited top-level Gradle configuration includes Google Services and Firebase Crashlytics plugins. Their declaration does not prove they are active in every build, but their usage and configuration must be audited. GoreeCloud production releases must not include unnecessary telemetry or crash reporting that conflicts with GoreeCloud privacy requirements.

## Current branch purpose

`agent/stable-foundation` is the initial controlled GoreeCloud Android development branch. The first goal is to understand the inherited modules, VPN service, authentication/enrollment path, build/signing process, networking-core integration, permissions, and CI before changing tunnel behavior or producing a GoreeCloud acceptance APK.
