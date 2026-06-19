# AADisplay-101

[![AADisplay-101](https://img.shields.io/badge/AADisplay--101-Project-blue?logo=github)](https://github.com/Masterain98/AADisplay-101)
![Xposed Module](https://img.shields.io/badge/Xposed-Module-blue)
![Android SDK](https://img.shields.io/badge/Android%20SDK-min%2031%20%C2%B7%20target%2037-brightgreen?logo=android)

> **Note:** This project is largely a **vibe programming** product — generated and iterated through extensive LLM-assisted development rather than traditional human-written code. Use with appropriate caution.

An Xposed / LSPosed module that mirrors almost any app onto the Android Auto screen via a VirtualDisplay. Migrated to **libxposed API 101** with support for Android 17 (API 37).

> [!IMPORTANT]
> AADisplay-101 has GitHub Immutable Releases feature enabled. All APK releases are exclusively built and published through GitHub Actions CI — they cannot be manually modified, ensuring software supply chain security.

## Upstream

This project is forked from [`koalaauto/AADisplay-Beta`](https://github.com/koalaauto/AADisplay-Beta), which itself derives from [`Nitsuya/AADisplay`](https://github.com/Nitsuya/AADisplay).

Renamed to **AADisplay-101** (package `com.aadisplay101.app`) to avoid conflicts with the original module on the same device.

## Requirements

- Android 12+ (SDK 31+; Android 10–11 unsupported)
- Rooted device with **LSPosed** (or a compatible Xposed environment supporting libxposed API 101)
- Working Android Auto (`com.google.android.projection.gearhead`)

> Some ROMs may be unstable or crash — use at your own risk.

## Usage

1. Enable the module in **LSPosed**, scoped to **System Framework** + **Android Auto**.
2. Set your launcher's package name in the module settings.
3. Optional: tune **DPI** and **resolution** for the car screen, or inject Android Auto **properties**.

Root is only used for user-configured shell commands — deny it if you don't need that.

## License

Same as upstream — see `LICENSE`.
