#!/usr/bin/env bash
# post-edit hook — after every code-edit session. Fast checks only.
# Thin wrapper: calls verify/verify-fast.sh (never duplicates its logic).
exec "$(dirname "$0")/../verify/verify-fast.sh"
