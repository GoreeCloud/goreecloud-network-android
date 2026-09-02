# GoreeCloud Network for Android

GoreeCloud Network for Android is the Android client variant of **GoreeCloud Network**. It connects supported Android devices to the Network control plane and compatible WireGuard-based mesh services while preserving the upstream client interfaces required by the underlying implementation.

## Upstream foundation

This repository is a fork of [`netbirdio/android-client`](https://github.com/netbirdio/android-client). NetBird remains an upstream technology and codebase dependency. Its GPL-3.0 licensing obligations, copyright notices, package/API compatibility identifiers, and required attribution remain intact.

Upstream NetBird artwork is not the official identity of GoreeCloud Network. GoreeCloud-controlled Android surfaces use derivatives of the approved GoreeCloud Network identity documented in [`BRANDING.md`](./BRANDING.md).

## Canonical identity

Canonical GoreeCloud Network artwork is maintained in `GoreeCloud/goreecloud-branding-assets` at:

`products/network/app-icon.svg`

Approved canonical Git blob:

`7457cd187d65887189150016b44c28af279635e5`

The Android launcher, round icon, navigation mark, first-install mark, Quick Settings service mark, and Android TV banner are platform derivatives of that identity. They do not create a separate Android product identity.

## Building from source

The project currently targets Android API 35 with a minimum supported API level of 26 and Java 11 source compatibility.

Build the Go mobile library first when required by the selected development workflow:

```shell
./build-android-lib.sh
```

Then build the Android application with Gradle, for example:

```shell
./gradlew bundleDebug -PversionCode=123 -PversionName=1.2.3
```

See the repository build files and upstream NetBird Android documentation for implementation-specific toolchain requirements.

## Compatibility boundary

Identifiers such as the `io.netbird.client` namespace/application ID, `NETBIRD_*` signing/configuration properties, NetBird-compatible server terminology, and upstream class names are retained where required for source, runtime, or migration compatibility. Their presence does not establish NetBird artwork or product naming as GoreeCloud's official first-party identity.

## Project status and authority

Branding synchronization, build success, repository contents, or upstream compatibility do not by themselves establish production readiness, runtime acceptance, release acceptance, or Stable status. Those claims remain governed by the applicable GoreeCloud project documentation, standards, policies, and release process.

## License and attribution

This fork retains the upstream **GNU General Public License v3.0 (GPL-3.0)** license in `LICENSE` and preserves required third-party attribution. GoreeCloud-specific branding assets and derivatives remain governed separately by GoreeCloud branding and licensing policy and do not alter upstream code-license obligations.
