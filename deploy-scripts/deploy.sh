#!/bin/bash
set -euo pipefail
. "$(dirname "$0")/lib.sh"

tag="${1:-}"
[[ "$tag" =~ ^[A-Za-z0-9._-]{1,128}$ ]] || { echo "usage: deploy.sh <tag>" >&2; exit 1; }
mkdir -p "$STATE"

# Database - Postgres DB must be up and running before deploying app
echo "==> ensuring database"
docker compose -p classroom-db -f "$CONF/db.yml" up -d
for i in $(seq 1 30); do
  [[ "$(docker inspect -f '{{.State.Health.Status}}' webapi-db 2>/dev/null)" == "healthy" ]] && break
  sleep 2
done
[[ "$(docker inspect -f '{{.State.Health.Status}}' webapi-db 2>/dev/null)" == "healthy" ]] \
  || { echo "!! database not healthy, aborting" >&2; exit 1; }

# Pick the target colour - deploy color that is not currently active
active=$(active_color)
if [[ -z "$active" ]]; then
  target=blue
  echo "==> cold start, deploying to blue"
else
  target=$(other "$active")
  echo "==> active=$active, deploying to $target"
fi
port=$(port_for "$target")

# Start the new colour
echo "==> starting webapi-$target on :$port with $tag"
compose_app "$target" "$tag" pull
compose_app "$target" "$tag" up -d --force-recreate

# Health check
echo "==> health-checking :$port"
sleep 15
if ! wait_healthy "$port" 15; then
  echo "!! health check failed, tearing down webapi-$target" >&2
  docker logs "webapi-$target" --tail 120 >&2 || true
  compose_app "$target" "$tag" down || true
  [[ -n "$active" ]] && echo "!! traffic untouched, webapi-$active still serving" >&2
  exit 1
fi

# Switch active app in NGINX
echo "==> switching nginx to $target"
nginx_point_to "$target"

# Update state
[[ -n "$active" ]] && echo "$active" > "$STATE/previous.color"
[[ -f "$STATE/current.tag" ]] && cp "$STATE/current.tag" "$STATE/previous.tag"
echo "$target" > "$STATE/active.color"
echo "$tag"    > "$STATE/current.tag"

# Remove old container
if [[ -n "$active" ]]; then
  echo "==> draining webapi-$active for 15s"
  sleep 15
  echo "==> stopping webapi-$active"
  old_tag=$(cat "$STATE/previous.tag" 2>/dev/null || echo latest)
  compose_app "$active" "$old_tag" down || true
fi

docker image prune -f > /dev/null
echo "==> deployed $tag on $target (:$port); previous=${active:-none}"