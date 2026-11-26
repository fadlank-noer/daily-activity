# Stack Reference — aktivitas-hari-ini (AktivitasHARINI)

## Identitas

| Field | Value |
|---|---|
| Root project name | `AktivitasHARINI` |
| Package / namespace | `com.harini.aktivitas` |
| applicationId | `com.harini.aktivitas` |
| versionName / versionCode | `1.0` / `1` |
| Entry point Android | `androidMain/.../MainActivity.kt` (LAUNCHER, exported) |
| UI utama (shared) | `commonMain/.../App.kt` — `App()`, `Greeting()` |

## Toolchain (diverifikasi 2026-11-26, semua stable)

| Komponen | Versi |
|---|---|
| JDK | 21.0.13 (Microsoft OpenJDK), `JAVA_HOME` sudah set |
| Gradle | 8.10.2 (wrapper) |
| Android Gradle Plugin | 8.7.3 |
| Kotlin / KGP | 2.4.21 (multiplatform) + `kotlin.plugin.compose` 2.4.21 |
| **Compose Multiplatform** | **1.11.1** (plugin `org.jetbrains.compose`) |
| compileSdk / targetSdk | 35 / 35 |
| minSdk | 26 |
| JVM target | 11 |

⚠️ **CMP 1.12.x TERBUKTI tidak kompatibel dengan stack stable ini** (butuh AGP ≥9.1.0 +
KGP 2.5.0 yang masih RC) — detail dan urutan upgrade nanti di
`rules/workflow.md` → Known failure modes.

## Dependencies

- `commonMain` (via accessor CMP): `compose.runtime`, `compose.foundation`,
  `compose.material3`, `compose.ui`
- `androidMain`: `core-ktx` 1.16.0, `lifecycle-runtime-ktx` 2.9.4,
  `activity-compose` 1.10.1, `ui-tooling-preview` 1.8.3 (@Preview)
- Tidak ada: navigation, room, network, DI, test deps

## Struktur source (KMP)

```
app/src/
├── commonMain/kotlin/com/harini/aktivitas/
│   ├── App.kt                        ← App() = theme + Scaffold + Greeting (SHARED UI)
│   └── ui/theme/
│       ├── Color.kt, Type.kt         ← tanpa perubahan dari template
│       ├── Theme.kt                  ← AktivitasHARINITheme (multiplatform-safe)
│       └── DynamicColor.kt           ← expect fun dynamicColorScheme(darkTheme): ColorScheme?
├── androidMain/kotlin/com/harini/aktivitas/
│   ├── MainActivity.kt               ← ComponentActivity, setContent { App() }
│   └── ui/theme/
│       ├── DynamicColor.android.kt   ← actual (Material You, Android 12+)
│       └── GreetingPreview.kt        ← @Preview (Android-only, perlu ui-tooling-preview)
├── androidMain/AndroidManifest.xml
└── androidMain/res/                  ← values (strings/themes/colors), drawable, mipmap
```

## Catatan lint (per 2026-11-26)

`lintDebug` masih crash (lint 31.7.3 ↔ Kotlin 2.4.21) — tidak dipakai sebagai gate.
Fix real menyusul upgrade AGP (tertahan matrix versi CMP; lihat workflow.md).
