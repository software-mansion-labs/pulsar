#!/bin/bash
# Bidirectional sync between iOS and React Native Pulsar sources
# Watches both directories and syncs changes while preventing infinite loops

set -euo pipefail

IOS_DIR="~/projects/pulsar/iOS/Pulsar/Sources/Pulsar"
RN_DIR="~/projects/pulsar/react-native/react-native-pulsar/deps/Pulsar/Sources/Pulsar"

# Expand ~ paths
IOS_DIR="${IOS_DIR/#\~/$HOME}"
RN_DIR="${RN_DIR/#\~/$HOME}"

command -v fswatch >/dev/null 2>&1 || {
  echo "fswatch is required. Install with: brew install fswatch" >&2
  exit 1
}

if [ ! -d "$IOS_DIR" ]; then
  echo "iOS source directory not found: $IOS_DIR" >&2
  exit 1
fi

mkdir -p "$RN_DIR"

# Lock file to prevent simultaneous syncs
LOCK_FILE="/tmp/pulsar-sync.lock"
# Cooldown period in seconds after sync to ignore file changes
COOLDOWN=5

sync_ios_to_rn() {
  if [ -f "$LOCK_FILE" ]; then
    return
  fi
  
  touch "$LOCK_FILE"
  trap "rm -f $LOCK_FILE" RETURN
  
  echo "📱 Syncing iOS → React Native at $(date '+%H:%M:%S')"
  rsync -av --delete "$IOS_DIR"/ "$RN_DIR"/
  
  # Touch lock file to keep it recent for cooldown period
  sleep $COOLDOWN
}

sync_rn_to_ios() {
  if [ -f "$LOCK_FILE" ]; then
    return
  fi
  
  touch "$LOCK_FILE"
  trap "rm -f $LOCK_FILE" RETURN
  
  echo "⚛️  Syncing React Native → iOS at $(date '+%H:%M:%S')"
  rsync -av --delete "$RN_DIR"/ "$IOS_DIR"/
  
  # Touch lock file to keep it recent for cooldown period
  sleep $COOLDOWN
}

# Initial sync (iOS -> RN)
sync_ios_to_rn

echo "Watching bidirectional changes..."
echo "iOS: $IOS_DIR"
echo "RN:  $RN_DIR"

# Watch both directories with debounce to prevent rapid re-triggers
(
  fswatch -x -o "$IOS_DIR" | while IFS= read -r _; do
    sleep 1  # Debounce rapid changes
    if [ ! -f "$LOCK_FILE" ]; then
      sync_ios_to_rn
    fi
  done
) &
IOS_PID=$!

(
  fswatch -x -o "$RN_DIR" | while IFS= read -r _; do
    sleep 1  # Debounce rapid changes
    if [ ! -f "$LOCK_FILE" ]; then
      sync_rn_to_ios
    fi
  done
) &
RN_PID=$!

# Handle cleanup on exit
trap "kill $IOS_PID $RN_PID 2>/dev/null || true; rm -f $LOCK_FILE" EXIT

wait

