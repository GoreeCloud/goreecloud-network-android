# GoreeCloud Network Android Branding

All GoreeCloud branding authority belongs to `GoreeCloud/goreecloud-branding-assets`.

This repository is the Android client variant of **GoreeCloud Network**. It does not own an independent product identity.

## Canonical identity

- Product: GoreeCloud Network
- Canonical repository: `GoreeCloud/goreecloud-branding-assets`
- Canonical asset: `products/network/app-icon.svg`
- Canonical Git blob: `7457cd187d65887189150016b44c28af279635e5`
- Catalog status: approved

## Android derivatives

The following Android resources are governed platform derivatives of the approved Network identity:

- `app/src/main/res/drawable/goreecloud_network_app_icon.xml` — full launcher/navigation/first-install mark using the canonical cyan-to-blue material family and node/link geometry.
- `app/src/main/res/drawable/goreecloud_network_tile.xml` — reduced monochrome service derivative for Quick Settings.
- `app/src/main/res/drawable/goreecloud_network_banner.xml` — Android TV banner derivative.
- `app/src/main/res/values/goreecloud_branding_strings.xml` — first-party product labels and accessibility descriptions for governed surfaces.

These derivatives may adapt geometry to Android resource constraints but must remain recognizably derived from the canonical Network identity. They must not introduce a separate Android-only logo.

## Governed surfaces

The Android manifest and layouts must use GoreeCloud Network identity for:

- application/launcher icon and round icon;
- Android TV banner;
- application label;
- Quick Settings service icon and label;
- navigation header artwork;
- first-install product artwork and accessibility description.

## Upstream NetBird boundary

This repository is a fork of `netbirdio/android-client` and retains upstream code, GPL-3.0 obligations, package/API compatibility identifiers, class names, configuration names, server terminology, and legal attribution where technically or legally required.

Upstream NetBird artwork and generic Android Studio launcher artwork are classified as **upstream/obsolete for GoreeCloud-controlled identity surfaces**. They must not be used as the launcher, round icon, TV banner, navigation mark, Quick Settings product mark, first-install product logo, store artwork, or other GoreeCloud first-party presentation.

Compatibility identifiers such as `io.netbird.client`, `NetbirdTileService`, `NETBIRD_*`, and `Theme.NetBird` are technical implementation names and are not by themselves branding violations.

## Validation and acceptance

Branding validation must fail closed if governed surfaces regress to upstream NetBird/generic launcher artwork or if the manifest stops resolving to the approved Network derivatives.

Source conformance does not establish runtime acceptance, production readiness, release acceptance, or Stable status. Human/evidence-backed visual acceptance remains required for any new derivative or changed optical treatment before it may be treated as fully accepted portfolio artwork.
