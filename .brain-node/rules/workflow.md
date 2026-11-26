# Workflow Map — aktivitas-hari-ini (AktivitasHARINI)

Verified: 2026-11-26 11:45 | probes: 12+ | fails: 3 (lint root-caused, AGP 9.1 block, CMP 1.12.2 matrix)

**Kotlin Multiplatform + Compose Multiplatform app** (target aktif: Android; target lain
tinggal ditambah). UI WAJIB Compose Multiplatform — rule lengkap di
`.brain-node/rules/ui-compose-multiplatform.md`. Namespace/applicationId
`com.harini.aktivitas`, minSdk 26, target/compileSdk 35, JVM target 11.
Toolchain (semua stable, verified): JDK 21 (`JAVA_HOME` set) + Gradle 8.10.2 wrapper +
AGP 8.7.3 + Kotlin 2.4.21 + **CMP 1.11.1**. Windows: semua command ✅ via Git Bash.

## Setup from scratch

1. JDK 21 terinstall, `JAVA_HOME=C:\Users\User\opt\java\jdk-21.0.13`
2. Android SDK di `C:\Users\User\AppData\Local\Android\Sdk` (dibaca dari `local.properties`,
   sudah benar — jangan commit file ini)
3. Tidak ada env var tambahan. Tidak ada service eksternal (DB/API) yang dibutuhkan.

## Build / Test / Verify

| Command | Status | Catatan |
|---|---|---|
| `./gradlew --version` | ✅ exit 0 | Gradle 8.10.2, jalan langsung di Git Bash |
| `./gradlew tasks` | ✅ exit 0 | Konfigurasi KMP OK |
| `./gradlew assembleDebug` | ✅ exit 0 | APK: `app/build/outputs/apk/debug/app-debug.apk` (~75s pertama kali post-migrasi, ~2s warm) |
| `./gradlew compileDebugKotlinAndroid` | ✅ exit 0 | **Nama task KMP** untuk compile Kotlin (menggantikan `compileDebugKotlin` pre-KMP); compile commonMain+androidMain |
| `./gradlew testDebugUnitTest` | ✅ exit 0 | Saat ini `NO-SOURCE` (belum ada unit test) — tetap exit 0 |
| `bash .brain-node/verify/verify.sh` | ✅ exit 0 | Gate final: assembleDebug + testDebugUnitTest |
| `bash .brain-node/verify/verify-fast.sh` | ✅ exit 0 | Gate cepat: compileDebugKotlinAndroid |
| `./gradlew compileKotlinMetadata` | ⚠️ UNVERIFIED | berguna saat mulai multi-target |
| `./gradlew lintDebug` | ❌ CRASH | KNOWN-BROKEN, lihat Known failure modes |

Kompilasi Kotlin = typecheck de-facto. Tidak ada test source set saat ini.
Gate "done" untuk semua perubahan code: `verify.sh` exit 0.

## Run / Deploy

- Device: `adb devices` ✅ exit 0 tapi kosong (tidak ada device saat probing).
- AVD tersedia: `Infinix_Note30Pro_API_34`, `Medium_Phone_API_35`.
- Jalankan di emulator (⚠️ UNVERIFIED):
  ```bash
  "$ANDROID_SDK/emulator/emulator" -avd Medium_Phone_API_35 &
  ./gradlew installDebug
  adb shell am start -n com.harini.aktivitas/.MainActivity
  ```
- Deploy/release: `assembleRelease` ⚠️ UNVERIFIED.

## Struktur source (KMP)

```
app/src/
├── commonMain/kotlin/com/harini/aktivitas/
│   ├── App.kt                      ← UI utama (App(), Greeting()) — SEMUA composable baru di sini
│   └── ui/theme/{Color,Theme,Type,DynamicColor(expect)}.kt
├── androidMain/kotlin/com/harini/aktivitas/
│   ├── MainActivity.kt             ← entry point, cuma setContent { App() }
│   └── ui/theme/{DynamicColor.android.kt (actual), GreetingPreview.kt (@Preview)}
├── androidMain/AndroidManifest.xml
└── androidMain/res/                ← resources Android
```

## Conventions detected

- Bahasa komentar code & Gradle config: Indonesia.
- Version catalog TIDAK dipakai — versi plugin/library langsung di build.gradle.kts.
  Dep compose di commonMain pakai accessor CMP (`compose.runtime`, `compose.material3`,
  dst.) — ada deprecation warning "Specify dependency directly", aman, belum diganti.
- API Android-only (`LocalContext`, `Build.VERSION`) dipecah via pola `expect`/`actual`
  (lihat `DynamicColor.kt` vs `DynamicColor.android.kt`).
- `gradle.properties`: JVM args `-Xmx2048m`, `android.useAndroidX=true`, `kotlin.code.style=official`, `nonTransitiveRClass=true`.
- ⚠️ Deprecation warning tiap configure: Gradle 8.10.2 deprecated untuk KGP — Kotlin 2.5.0
  akan minimum Gradle 8.14.4. Kalau upgrade Kotlin ke 2.5.x, upgrade wrapper dulu.
- Belum ada CI, Makefile, atau scripts folder.
- Git repo lokal (branch `master`; belum ada remote; commit menunggu review user).

## Known failure modes → fix yang dicoba

- **MATRIX VERSI CMP (dialami 2026-11-26, diprobe empiris — PENTING sebelum naik versi)**:
  - CMP 1.12.2 menarik compose androidx 1.12.1 yang **wajib AGP ≥ 9.1.0 + compileSdk ≥ 37**
    (error eksplisit dari dependency checker AGP).
  - AGP 9.1.0 wajib **Gradle ≥ 9.3.1** (error eksplisit version-check AGP).
  - AGP 9.1.0 + KGP 2.4.21 (Kotlin stable TERAKHIR) = **CRASH** `BaseExtension` cast saat
    apply `kotlin-multiplatform` — KGP stable belum support AGP 9.1; baru ada di Kotlin
    **2.5.0-RC2** (belum stable, dicek via plugin portal 2026-11-26).
  - Kesimpulan: CMP 1.12.x butuh stack semua-bleeding-edge (Kotlin RC). Dipilih CMP
    **1.11.1** di stack stable → verified build ✅. Naik ke 1.12.x saat Kotlin 2.5.0
    stable rilis: upgrade Kotlin → Gradle (≥9.3.1) → AGP (≥9.1.0) → compileSdk 37,
    probe bertahap, HANYA setelah verify.sh ✅.
- **Gradle daemon wedge setelah rename/move file massal** (dialami 2026-11-26 ~11:00):
  launcher diam >10 menit, daemon cuma heartbeat idle. Fix: kill launcher java stuck
  (`taskkill //PID <pid> //F`) + `./gradlew --stop`, re-run → sukses.
- **`platform(...)` tidak ada di DSL `androidMain.dependencies {}`** (KGP source-set DSL):
  Unresolved reference. Fix: pin versi eksplisit langsung (ui-tooling-preview → 1.8.3,
  hasil resolusi BOM 2025.06.01 sebelumnya; diverifikasi dari cache Gradle).
- **`./gradlew lintDebug` crash** (masih, pre-migrasi): lint 31.7.3 ↔ Kotlin 2.4.21,
  `KaCallableMemberCall` interface mismatch di `NonNullableMutableLiveDataDetector`,
  fase `lintAnalyzeDebug`. Disable `NullSafeMutableLiveData` TIDAK menolong (crash di
  fase analisis). Fix real = upgrade AGP (lint ikut baru) — tertahan matrix CMP di atas.
  Opsi dijalankan lagi saat naik ke stack CMP 1.12.x.

## UNVERIFIED

- `installDebug` + emulator run — tidak ada device saat discovery.
- `assembleRelease` — belum diprobe.
- Instrumented test (`connectedDebugAndroidTest`) — butuh device.
- `compileKotlinMetadata` — belum diprobe.
