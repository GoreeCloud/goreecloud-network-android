# GoreeCloud Network Android Architecture Boundary

## Purpose

This record maps the inherited Android client boundary before deeper GoreeCloud customization. It is intentionally descriptive: it does not change tunnel, authentication, enrollment, routing, or management-server behavior.

## Connection path

The current application follows this high-level path:

1. `MainActivity` owns the user-facing VPN permission and connection flow.
2. Android VPN permission is requested with `VpnService.prepare(...)` through the local service binder.
3. After permission is granted, `MainActivity` calls `VPNService.MyLocalBinder.runEngine(...)`.
4. `VPNService` owns the Android foreground VPN-service lifecycle, route-change handling, TUN recreation, network-change detection, and the `EngineRunner` instance.
5. `EngineRunner` creates the inherited Go mobile client through `Android.newClient(...)`.
6. `EngineRunner` passes profile-specific configuration/state paths, host DNS state, environment flags, the Android TUN adapter, and the authentication URL opener into the Go client.
7. The Go client remains the compatibility-critical networking/authentication core during the initial GoreeCloud fork phases.

## Authentication and enrollment boundary

`MainActivity` supplies an inherited `URLOpener` to the engine for interactive authentication. Browser/custom-tab flow is used on normal Android devices; device-code/QR flow is available for Android TV and related environments.

The server-selection interface is separate from the tunnel lifecycle. `ChangeServerFragment` supports:

- Changing the management-server URL.
- Logging in to a selected management server with a setup key.
- Falling back to the inherited default server when no custom management URL is entered.

The profile manager supplies the active configuration and state-file paths. This means GoreeCloud can later make self-hosted GoreeCloud enrollment the primary product workflow without rewriting the underlying Go networking core first.

## Android VPN-service boundary

`VPNService` must remain behaviorally stable during early branding and Glaze UI work. It currently owns:

- Foreground-service lifecycle.
- Android VPN permission/binding boundary.
- Engine start and stop.
- Always-on VPN startup.
- Network-change detection and controlled engine restart.
- Peer and network data exposed to the UI.
- Route selection and deselection.
- TUN renewal when routes change.
- Debug-bundle generation.

Changes to these responsibilities require dedicated networking regression tests and are not part of visual rebranding.

## Go-mobile boundary

`EngineRunner` delegates the compatibility-critical functions to `io.netbird.gomobile.android.Client`, including:

- Engine execution and login.
- Existing-session execution without login.
- Connection state callbacks.
- Peer/network state.
- DNS updates.
- TUN renewal.
- Route selection.
- Debug-bundle generation.

The first GoreeCloud releases should retain this boundary and treat it as an upstream-derived networking component. Fork-to-native work, if justified later, should replace pieces only after equivalent behavior is independently tested.

## Privacy boundary

The GoreeCloud branch has already removed the inherited optional Firebase Analytics/Crashlytics integration and Google Advertising ID permission. No GoreeCloud analytics dependency is required for the VPN lifecycle described above.

## Build-validation state

The inherited `build debug` workflow is already configured for pull requests and performs:

- Android application/library build.
- APK and AAB artifact generation.
- Unit tests.
- Instrumented Android emulator tests.

The shared build action also pins the Go/mobile toolchain to the version in the inherited NetBird Go module rather than allowing the generator to float independently.

At the time of this record, GitHub reports no pull-request-triggered workflow run for the current GoreeCloud branch head. Therefore build acceptance remains outstanding. This is an execution-state issue, not evidence that the source passes or fails.

## Safe next changes

The following are suitable after CI execution is available:

- GoreeCloud product strings and application presentation.
- Self-hosted GoreeCloud server as the primary enrollment path.
- Glaze UI conversion of activities/fragments/layouts.
- Wardveil Security connection/trust presentation.
- GoreeCloud package/application-ID migration with a new signing identity.
- Removal or renaming of remaining NetBird-specific product presentation where licensing and attribution requirements permit.

The following remain gated behind deeper regression testing:

- `VPNService` lifecycle behavior.
- `EngineRunner`/gomobile semantics.
- TUN creation and renewal.
- Route handling.
- DNS integration.
- Authentication protocol behavior.
- Setup-key protocol behavior.
- Peer networking and relay behavior.
