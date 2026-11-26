#!/usr/bin/env bash
# .brain-node/verify/verify-fast.sh — fast gate (sub-5s): Kotlin compile only.
# Contract: exit 0 = verified, non-zero = STOP.
# ✅ verified: exit 0 (2026-11-26). Task name KMP: compileDebugKotlinAndroid
# (compile commonMain + androidMain, menggantikan compileDebugKotlin pre-KMP).

set -uo pipefail
cd "$(dirname "$0")/../.." || exit 1

./gradlew compileDebugKotlinAndroid || {
  echo "VERIFY-FAST FAILED — do not report done." >&2
  exit 1
}

echo "VERIFY-FAST OK"
exit 0
