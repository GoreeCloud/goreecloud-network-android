# GoreeCloud Network Android — GitHub Actions Activation

## Purpose

This record separates repository-level GitHub Actions execution from Android build correctness.

The GoreeCloud Network Android repository originated as a fork. GitHub documents that workflows do not run in forked repositories by default until GitHub Actions is enabled for the fork. Repository administrators must also ensure the repository's Actions permissions allow the actions used by the workflows.

## Current evidence

The development branch contains the full `build debug` workflow and a minimal `GoreeCloud Android Actions smoke` workflow.

The smoke workflow intentionally avoids Gradle, gomobile, Android SDK setup, signing material, submodule checkout, and emulator execution. It performs only:

1. Exact-source checkout.
2. Exact-source SHA verification.
3. `scripts/validate-goreecloud-android.sh` source-boundary validation.
4. A workflow-summary statement that distinguishes Actions execution from Android build acceptance.

A push that adds this smoke workflow produces no workflow run and no commit status while repository Actions execution remains suppressed. That outcome must not be interpreted as a source/build failure.

## Administrative activation boundary

An administrator with repository settings access must verify the following in GitHub:

1. Open the repository **Actions** tab and enable workflows for the fork if GitHub presents the fork-workflow activation prompt.
2. Open **Settings → Actions → General**.
3. Confirm GitHub Actions is allowed for the repository.
4. Confirm the selected Actions policy permits the actions required by this repository, including GitHub-authored actions and the Android emulator action used by the debug workflow.
5. Keep the default workflow token permissions restricted unless a specific workflow has a documented need for additional permissions.

The GoreeCloud connector used for repository development does not expose the repository-level Actions enable/disable administration endpoint, so this activation is an explicit repository-administrator action rather than a source-code mutation.

## Acceptance sequence after activation

After Actions is enabled, acceptance must proceed in this order:

1. Require `GoreeCloud Android Actions smoke` to execute successfully on the exact development head.
2. Require `build debug` to execute on the same exact Android source revision.
3. Correct any evidence-backed source, Gradle, gomobile, unit-test, or emulator failures without bypassing the failing gate.
4. Retain APK/AAB and test evidence bound to the exact source SHA.
5. Configure approved signing material only in the `android-release` environment.
6. Execute a signed snapshot and verify signer fingerprint, package identity, APK signature, APK/AAB hashes, and provenance output.
7. Execute the real-device acceptance matrix against the isolated GoreeCloud Network staging control plane.

## Safety

Actions activation proves only that GitHub may execute repository workflows. A passing smoke workflow is not Android build acceptance. A passing debug workflow is not signed-release or real-device acceptance. None of these results authorize production migration by themselves.
