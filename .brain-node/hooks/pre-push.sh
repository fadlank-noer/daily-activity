#!/usr/bin/env bash
# pre-push hook — before every push. Full gate.
# Thin wrapper: calls verify/verify.sh (never duplicates its logic).
exec "$(dirname "$0")/../verify/verify.sh"
