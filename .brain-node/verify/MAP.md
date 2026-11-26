# Verify Map — aktivitas-hari-ini (AktivitasHARINI)

Kotlin Multiplatform + Compose Multiplatform project. Feature area → verify command.
Agents: verify only the blast radius you touched; full gate before any "done" claim
is always `verify/verify.sh`.

| Feature area | Command | Catatan |
|---|---|---|
| Full gate (build + tests) | `bash .brain-node/verify/verify.sh` | assembleDebug + testDebugUnitTest |
| Kotlin source (`.kt` edit, commonMain/androidMain) | `bash .brain-node/verify/verify-fast.sh` | `compileDebugKotlinAndroid` (nama task KMP, compile commonMain+androidMain) |
| Unit tests saja | `./gradlew testDebugUnitTest` | saat ini NO-SOURCE (exit 0); nanti: `./gradlew testDebugUnitTest --tests "com.harini.aktivitas.*"` |
| APK artifact | `./gradlew assembleDebug` | output: `app/build/outputs/apk/debug/app-debug.apk` |
| Kotlin metadata (cek API common utk target future) | `./gradlew compileKotlinMetadata` | opsional, saat tambah target baru |
| Lint | ❌ **JANGAN dipakai sebagai gate** | `lintDebug` crash: lint 31.7.3 ↔ Kotlin 2.4.21, fase `lintAnalyzeDebug` — lihat `.brain-node/rules/workflow.md` → Known failure modes |
| Run di device | `./gradlew installDebug` | ⚠️ UNVERIFIED — butuh emulator (`Medium_Phone_API_35` tersedia) |

Semua command ✅ sudah diprobe dengan exit code nyata pada 2026-11-26 (kecuali yang
ditandai UNVERIFIED). Detail lengkap: `.brain-node/rules/workflow.md`.

## Catatan verifikasi multiplatform

- Nama task compile di layout KMP berbeda dari Android murni:
  `compileDebugKotlinAndroid` (bukan `compileDebugKotlin`).
- Saat menambah target baru (desktop/iOS/web), tambahkan task compile-nya di sini
  dan ke `verify.sh` HANYA setelah diprobe ✅.
