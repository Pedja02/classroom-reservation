# sourced, not executed
ROOT=/apps/classroom-reservation
APPDIR="$ROOT/web-api"
CONF="$APPDIR/config"
STATE="$APPDIR/state"
LOGS="$APPDIR/logs"
NGINX_DIR=/etc/nginx/classroom-reservation
. "$CONF/deploy.conf"

declare -A PORT_OF=( [blue]="$BLUE_PORT" [green]="$GREEN_PORT" )
port_for() { echo "${PORT_OF[$1]}"; }
other()    { [[ "$1" == blue ]] && echo green || echo blue; }

running() {
  [[ "$(docker inspect -f '{{.State.Running}}' "webapi-$1" 2>/dev/null)" == "true" ]]
}

# Colour nginx should point at
active_color() {
  local c
  c=$(tr -d '[:space:]' < "$STATE/active.color" 2>/dev/null)
  [[ "$c" =~ ^(blue|green)$ ]] && running "$c" && { echo "$c"; return; }
  running blue  && { echo blue;  return; }
  running green && { echo green; return; }
  echo ""
}

compose_app() {
  local colour=$1 tag=$2; shift 2
  COLOR="$colour" PORT="${PORT_OF[$colour]}" APP_TAG="$tag" IMAGE="$IMAGE" \
    docker compose -p "classroom-$colour" -f "$CONF/app.yml" "$@"
}

nginx_point_to() {
  ln -sfn "$NGINX_DIR/upstream-$1.conf" "$NGINX_DIR/active-upstream.conf"
  sudo /usr/sbin/nginx -t
  sudo /usr/bin/systemctl reload nginx
}

wait_healthy() {
  local port=$1 attempts=${2:-15}
  for i in $(seq 1 "$attempts"); do
    curl -fsS --max-time 5 "http://127.0.0.1:${port}${HEALTH_PATH}" 2>/dev/null \
      | grep -q '"status":"UP"' && return 0
    echo "    health attempt $i/$attempts"
    sleep 5
  done
  return 1
}