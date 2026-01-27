#!/bin/bash
# Bidirectional sync between Android and React Native Pulsar sources
# Watches both directories and syncs changes while preventing infinite loops
# Ignores HapticsModule.kt and HapticsPackage.kt in React Native

set -euo pipefail

ANDROID_DIR="~/projects/pulsar/Android/Pulsar/src/main/java/com/swmansion/pulsar"
RN_DIR="~/projects/pulsar/react-native/react-native-pulsar/android/src/main/java/com/swmansion/pulsar"

# Expand ~ paths
ANDROID_DIR="${ANDROID_DIR/#\~/$HOME}"
RN_DIR="${RN_DIR/#\~/$HOME}"

command -v fswatch >/dev/null 2>&1 || {
  echo "fswatch is required. Install with: brew install fswatch" >&2
  exit 1
}

if [ ! -d "$ANDROID_DIR" ]; then
  echo "Android source directory not found: $ANDROID_DIR" >&2
  exit 1
fi

mkdir -p "$RN_DIR"

# Lock file to prevent simultaneous syncs
LOCK_FILE="/tmp/android-pulsar-sync.lock"
# Cooldown period in seconds after sync to ignore file changes
COOLDOWN=5

sync_android_to_rn() {
  if [ -f "$LOCK_FILE" ]; then
    return
  fi
  
  touch "$LOCK_FILE"
  trap "rm -f $LOCK_FILE" RETURN
  
  echo "🤖 Syncing Android → React Native at $(date '+%H:%M:%S')"
  rsync -av --delete \
    --exclude='PulsarModule.kt' \
    --exclude='PulsarPackage.kt' \
    "$ANDROID_DIR"/ "$RN_DIR"/
  
  # Touch lock file to keep it recent for cooldown period
  sleep $COOLDOWN
}

sync_rn_to_android() {
  if [ -f "$LOCK_FILE" ]; then
    return
  fi
  
  touch "$LOCK_FILE"
  trap "rm -f $LOCK_FILE" RETURN
  
  echo "⚛️  Syncing React Native → Android at $(date '+%H:%M:%S')"
  rsync -av --delete \
    --exclude='PulsarModule.kt' \
    --exclude='PulsarPackage.kt' \
    "$RN_DIR"/ "$ANDROID_DIR"/
  
  # Touch lock file to keep it recent for cooldown period
  sleep $COOLDOWN
}

# Initial sync (Android -> RN)
sync_android_to_rn

echo "Watching bidirectional changes..."
echo "Android: $ANDROID_DIR"
echo "RN:      $RN_DIR"
echo "Ignored: PulsarModule.kt, PulsarPackage.kt"

# Watch both directories with debounce to prevent rapid re-triggers
(
  fswatch -x -o "$ANDROID_DIR" | while IFS= read -r _; do
    sleep 1  # Debounce rapid changes
    if [ ! -f "$LOCK_FILE" ]; then
      sync_android_to_rn
    fi
  done
) &
ANDROID_PID=$!

(
  fswatch -x -o "$RN_DIR" | while IFS= read -r _; do
    sleep 1  # Debounce rapid changes
    if [ ! -f "$LOCK_FILE" ]; then
      sync_rn_to_android
    fi
  done
) &
RN_PID=$!

# Handle cleanup on exit
trap "kill $ANDROID_PID $RN_PID 2>/dev/null || true; rm -f $LOCK_FILE" EXIT

wait
