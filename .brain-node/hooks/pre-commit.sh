#!/usr/bin/env bash
# pre-commit hook — before every commit. Fast gate.
# Thin wrapper: calls verify/verify-fast.sh (never duplicates its logic).
# Full gate (verify.sh) dijalankan sebelum push oleh pre-push.sh.
exec "$(dirname "$0")/../verify/verify-fast.sh"
