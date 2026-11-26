# AGENTS.md — .brain-node (aktivitas-hari-ini)

> **UI RULE (wajib)**: UI project ini memakai **Compose Multiplatform** — baca
> `rules/ui-compose-multiplatform.md` SEBELUM menambah UI/dependency compose.

Satu-satunya tier brain di dalam repo ini adalah `.brain-node/` (committed bersama
code — rules, knowledge, hooks, verify ikut berpindah mesin). Extends workspace
`../.brain-project/AGENTS.md` dan global `.brain/AGENTS.md`; node tier menang saat konflik.

## Project Context

- **Project name**: aktivitas-hari-ini (AktivitasHARINI)
- **Path**: `C:\Users\User\Documents\Coding Projects\aktivitas-daily-workspace\aktivitas-hari-ini\`
- **Stack**: Kotlin Multiplatform + Compose Multiplatform 1.11.1 (UI WAJIB CMP — rule di
  `rules/ui-compose-multiplatform.md`), target aktif Android, Kotlin 2.4.21, AGP 8.7.3, Gradle 8.10.2, JDK 21
- **Git**: repo lokal (branch `master`; belum ada remote; commit menunggu review user)
- **Plan files**: hidup di workspace tier `../.brain-project/plan/` (konvensi
  `YYYY-MM-DD-hhmm-<slug>/`, rename ke `DONE-*` setelah executed + verified)

## Precedence

`.brain-node/` (repo ini) > `.brain-project/` (workspace root) > `.brain/` (global).
Closest to the code wins. Ketika repo ini punya `verify/verify.sh`, itu adalah
**gate terakhir** sebelum klaim "done" — global hooks jalan dulu, node verify paling akhir.

## Verify contract (HARD RULE)

**Never report done, sealed, or success without
`bash .brain-node/verify/verify.sh` exiting 0 in this session.**

- Satu entry point: `bash .brain-node/verify/verify.sh` — hooks dan agents hanya memanggil ini.
- Exit 0 = verified. Non-zero = STOP — laporkan outputnya, jangan klaim done.
- `verify-fast.sh` untuk gate cepat (~2s, compileDebugKotlinAndroid); `verify.sh` full.
- `lintDebug` TIDAK termasuk verify: known crash lint 31.7.3 ↔ Kotlin 2.4.21
  (lihat `rules/workflow.md`). Jangan tambahkan lint ke verify sampai AGP di-upgrade
  dan diprobe ✅.

## Rules & Knowledge (baca SEBELUM coding di repo ini)

| File | Isi |
|---|---|
| `rules/workflow.md` | **Workflow map terverifikasi** — baca SEBELUM build/test; semua command ✅ diprobe dengan exit code nyata + Known failure modes (matrix versi CMP, daemon wedge, lint crash) |
| `rules/ui-compose-multiplatform.md` | Rule UI wajib CMP + status versi |
| `knowledge/aktivitas-hari-ini-stack.md` | Stack reference, dependencies, struktur source KMP |

## Hook triggers

| Hook | Trigger | Body |
|---|---|---|
| `hooks/post-edit.sh` | setelah setiap sesi edit code | `verify-fast.sh` (Kotlin compile) |
| `hooks/pre-commit.sh` | sebelum setiap commit | `verify-fast.sh` |
| `hooks/pre-push.sh` | sebelum setiap push | `verify.sh` (full) |

Hooks hanya wrapper — logic verification hidup di `verify/`. Global hooks (`.brain/hooks/`)
jalan SEBELUM node hooks untuk trigger yang sama; hasil node bersifat authoritative.

## Rule migration rule

Any rule in `rules/` that becomes machine-checkable MUST be converted into a
`verify/` or `hooks/` script in the next touch. `rules/` adalah residu terakhir
untuk constraint yang belum bisa diotomasi.

## Notes

- Never commit secrets or credentials — `local.properties` (path SDK) sudah di `.gitignore`, jangan pernah di-commit.
- Komentar code mengikuti gaya project: bahasa Indonesia, technical terms English.
- Version catalog tidak dipakai — tambah dependency langsung di `app/build.gradle.kts`.
- Respect the project's existing conventions and style.
