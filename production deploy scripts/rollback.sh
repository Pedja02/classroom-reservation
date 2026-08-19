#!/bin/bash
set -euo pipefail
. "$(dirname "$0")/lib.sh"
[[ -f "$STATE/previous.color" ]] || { echo "no previous colour recorded" >&2; exit 1; }
prev=$(tr -d '[:space:]' < "$STATE/previous.color")
[[ "$prev" =~ ^(blue|green)$ ]] || { echo "corrupt state" >&2; exit 1; }
running "$prev" || { echo "webapi-$prev is not running; cannot roll back" >&2; exit 1; }
wait_healthy "$(port_for "$prev")" 3 \
  || { echo "webapi-$prev is not healthy; refusing" >&2; exit 1; }

cur=$(tr -d '[:space:]' < "$STATE/active.color" 2>/dev/null || echo "")
prev_tag=$(cat "$STATE/previous.tag" 2>/dev/null || echo "")
cur_tag=$(cat "$STATE/current.tag" 2>/dev/null || echo "")

echo "==> rolling back to $prev"
nginx_point_to "$prev"

echo "$prev" > "$STATE/active.color"
[[ -n "$cur" ]] && echo "$cur" > "$STATE/previous.color"
[[ -n "$prev_tag" ]] && echo "$prev_tag" > "$STATE/current.tag"
[[ -n "$cur_tag" ]]  && echo "$cur_tag"  > "$STATE/previous.tag"
echo "==> rolled back to $prev"