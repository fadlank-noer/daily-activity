#!/usr/bin/env bash
# .brain-node/verify/verify.sh — single verification entry point.
# Contract: exit 0 = verified, non-zero = STOP (never claim done on failure).
# Every command below is an empirically probed ✅ command (exit code seen,
# see .brain-node/rules/workflow.md, Verified: 2026-11-26 10:39).
# lintDebug is EXCLUDED on purpose: known crash (lint 31.7.3 <-> Kotlin 2.4.21).

set -uo pipefail
cd "$(dirname "$0")/../.." || exit 1

fail=0

echo "==> [1/2] assembleDebug (build APK debug + Kotlin typecheck)"
./gradlew assembleDebug || fail=1

echo "==> [2/2] testDebugUnitTest (unit tests)"
./gradlew testDebugUnitTest || fail=1

if [ "$fail" -ne 0 ]; then
  echo "VERIFY FAILED — do not report done." >&2
  exit 1
fi

echo "VERIFY OK"
exit 0
