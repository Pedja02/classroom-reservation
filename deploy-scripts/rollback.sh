#!/bin/bash
set -euo pipefail
. "$(dirname "$0")/lib.sh"

prev_tag=$(tr -d '[:space:]' < "$STATE/previous.tag" 2>/dev/null || echo "")
[[ "$prev_tag" =~ ^[A-Za-z0-9._-]{1,128}$ ]] || { echo "no previous tag recorded" >&2; exit 1; }

echo "==> redeploying previous tag $prev_tag"
exec "$(dirname "$0")/deploy.sh" "$prev_tag"