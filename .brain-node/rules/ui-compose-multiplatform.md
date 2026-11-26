# Rule: UI = Compose Multiplatform (2026-11-26, diminta user @fadlannoer)

Seluruh UI project ini **WAJIB** memakai **Compose Multiplatform** (`org.jetbrains.compose`,
plugin `org.jetbrains.compose`, saat ini **1.11.1**) — bukan Jetpack Compose Android murni.

## Konsekuensi konkret

1. **Composable UI hidup di `commonMain`** (`app/src/commonMain/kotlin/...`) supaya
   diwarisi semua target platform. Entry-point Android (MainActivity) hidup di
   `androidMain` dan hanya memanggil `App()` dari common.
2. **Dependency compose di `commonMain`** memakai accessor CMP:
   `compose.runtime`, `compose.foundation`, `compose.material3`, `compose.ui`.
   JANGAN menambahkan `androidx.compose.*` langsung di commonMain.
3. **`androidx.compose.*` / API Android-only hanya boleh di `androidMain`** —
   dan hanya untuk hal yang tidak disediakan CMP: `activity-compose`,
   `ui-tooling-preview` (@Preview), API `LocalContext`/`Build.VERSION` (dipecah
   via pola `expect`/`actual`, contoh: `dynamicColorScheme`).
4. **Jangan downgrade kembali ke Jetpack Compose Android-only** (plugin
   `org.jetbrains.kotlin.android`) — struktur KMP dengan `androidTarget` adalah
   baseline project ini.
5. **Versi**: CMP 1.12.x TERBUKTI tidak kompatibel dengan toolchain stable saat ini
   (butuh AGP ≥9.1.0 + KGP 2.5.0 yang masih RC — lihat workflow.md). Kalau mau naik
   ke CMP 1.12.x nanti, upgrade Ketlin+AGP+Gradle dulu dan probe `assembleDebug`
   sampai ✅ sebelum commit.

## Status versi (2026-11-26, diprobe empiris)

| Komponen | Versi |
|---|---|
| Compose Multiplatform | 1.11.1 (stable, verified build ✅) |
| Kotlin / KGP | 2.4.21 |
| AGP | 8.7.3 |
| Gradle | 8.10.2 |

Rule ini berada di tier `.brain-node` (committed bersama code) — berlaku untuk semua
agent, mesin, dan CI yang menyentuh repo ini.
