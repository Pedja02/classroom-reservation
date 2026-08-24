#!/bin/bash
set -euo pipefail
. "$(dirname "$0")/lib.sh"


link=$(readlink -f "$NGINX_DIR/active-upstream.conf" 2>/dev/null || echo "?")
echo "nginx upstream : $(basename "$link")"

echo "state active   : $(cat "$STATE/active.color"   2>/dev/null || echo none)"
echo "state previous : $(cat "$STATE/previous.color" 2>/dev/null || echo none)"
echo "current tag    : $(cat "$STATE/current.tag"    2>/dev/null || echo none)"
echo "previous tag   : $(cat "$STATE/previous.tag"   2>/dev/null || echo none)"
echo

for color in blue green; do
  port=$(port_for "$color")

  if running "$color"; then
    image=$(docker inspect -f '{{.Config.Image}}' "webapi-$color")

    health=$(curl -fsS --max-time 3 "http://127.0.0.1:${port}${HEALTH_PATH}" 2>/dev/null \
             | grep -o '"status":"[A-Z]*"' || echo unreachable)

    printf "%-6s :%-5s up    %-50s %s\n" "$color" "$port" "$image" "$health"
  else
    printf "%-6s :%-5s down\n" "$color" "$port"
  fi
done
echo

docker compose -p classroom-db -f "$CONF/db.yml" ps