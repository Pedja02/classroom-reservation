#!/bin/bash
set -euo pipefail
. "$(dirname "$0")/lib.sh"

# Load env vars
set -a
. "$CONF/db.env"
. "$CONF/backup.env"
set +a

DIR="$ROOT/db-backups"
TS=$(date +%Y%m%d_%H%M%S)
DUMP="$DIR/${POSTGRES_DB}_${TS}.dump"

# ── Dump ──────────────────────────────────────────────────────────
running db || { echo "database is not running" >&2; exit 1; }
mkdir -p "$DIR"

echo "==> dumping $POSTGRES_DB"
docker exec webapi-db pg_dump -U "$POSTGRES_USER" -d "$POSTGRES_DB" \
  --no-owner --no-privileges --format=custom > "$DUMP"

# check if dump is empty
[[ -s "$DUMP" ]] || { echo "dump is empty" >&2; rm -f "$DUMP"; exit 1; }

gzip -f "$DUMP"
echo "==> local: ${DUMP}.gz ($(du -h "${DUMP}.gz" | cut -f1))"


if [[ -n "${S3_BUCKET:-}" ]]; then
  AWS_ACCESS_KEY_ID="$S3_ACCESS_KEY" \
  AWS_SECRET_ACCESS_KEY="$S3_SECRET_KEY" \
  aws s3 cp "${DUMP}.gz" "s3://${S3_BUCKET}/backups/db/$(basename "${DUMP}.gz")" \
    --endpoint-url "$S3_ENDPOINT" --region "${S3_REGION:-eu-central}" \
    || echo "!! upload failed, local copy saved" >&2
fi

# delete any backup older than 14 days
find "$DIR" -name '*.dump.gz' -mtime +14 -delete