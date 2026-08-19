# helper script
ROOT=/apps/classroom-reservation
APPDIR="$ROOT/web-api"
CONF="$APPDIR/config"
STATE="$APPDIR/state"
LOGS="$APPDIR/logs"
NGINX_DIR=/etc/nginx/classroom-reservation
. "$CONF/deploy.conf"

declare -A PORT_OF=( [blue]="$BLUE_PORT" [green]="$GREEN_PORT" )
port_for() { echo "${PORT_OF[$1]}"; }

other() {
  if [[ "$1" == blue ]]; then
    echo green
  else
    echo blue
  fi
}

running() {
  local status
  status=$(docker inspect -f '{{.State.Running}}' "webapi-$1" 2>/dev/null)
  [[ "$status" == "true" ]]
}

# Returns currently active color
active_color() {
  local recorded

  recorded=$(tr -d '[:space:]' < "$STATE/active.color" 2>/dev/null)
  if [[ "$recorded" =~ ^(blue|green)$ ]] && running "$recorded"; then
    echo "$recorded"
    return
  fi

  if running blue; then
    echo blue
    return
  fi
  if running green; then
    echo green
    return
  fi

  echo ""
}

# Compose UP new app version
compose_app() {
  local colour=$1
  local tag=$2
  shift 2

  COLOR="$colour" \
  PORT="${PORT_OF[$colour]}" \
  APP_TAG="$tag" \
  IMAGE="$IMAGE" \
    docker compose -p "classroom-$colour" -f "$CONF/app.yml" "$@"
}

# Point NGINX traffic to specific color
nginx_point_to() {
  local color=$1
  ln -sfn "$NGINX_DIR/upstream-$color.conf" "$NGINX_DIR/active-upstream.conf"
  sudo /usr/sbin/nginx -t
  sudo /usr/bin/systemctl reload nginx
}

# Health check port
wait_healthy() {
  local port=$1
  local attempts=${2:-15}

  for i in $(seq 1 "$attempts"); do
    if curl -fsS --max-time 5 "http://127.0.0.1:${port}${HEALTH_PATH}" 2>/dev/null \
         | grep -q '"status":"UP"'; then
      return 0
    fi
    echo "    health attempt $i/$attempts"
    sleep 5
  done

  return 1
}