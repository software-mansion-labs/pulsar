# Deep Link Testing Guide

## Deep Link Format

Your app now supports the following deep link format:
```
pulsarapp://connect?code=YOUR_CODE_HERE
```

Example:
```
pulsarapp://connect?code=123456
```

---

## Testing Methods

### 1️⃣ iOS Simulator

**Option A: Using Terminal**
```bash
xcrun simctl openurl booted "pulsarapp://connect?code=123456"
```

**Option B: Using Safari**
1. Open Safari in the iOS Simulator
2. Type in the address bar: `pulsarapp://connect?code=123456`
3. Press Enter

### 2️⃣ Android Emulator

**Using ADB (Android Debug Bridge)**
```bash
adb shell am start -W -a android.intent.action.VIEW -d "pulsarapp://connect?code=123456"
```

If you don't have adb in PATH, you can find it at:
```bash
~/Library/Android/sdk/platform-tools/adb
```

### 3️⃣ Physical Device (iOS)

**Option A: Send via Messages/Notes**
1. Send yourself a message with the link: `pulsarapp://connect?code=123456`
2. Tap the link on your device

**Option B: Using Safari**
1. Open Safari on your iPhone
2. Type the deep link in the address bar
3. Safari will ask if you want to open PulsarApp

### 4️⃣ Physical Device (Android)

**Option A: Using ADB over WiFi/USB**
```bash
adb shell am start -W -a android.intent.action.VIEW -d "pulsarapp://connect?code=123456"
```

**Option B: Send via Messages/Email**
1. Send yourself the link: `pulsarapp://connect?code=123456`
2. Tap the link on your device

### 5️⃣ QR Code Generation

You can generate a QR code that contains the deep link:

**Online Tools:**
- https://www.qr-code-generator.com/
- https://www.qrcode-monkey.com/

**Input for QR Code:**
```
pulsarapp://connect?code=123456
```

**Test QR Code:**
1. Generate QR code with your connecting code
2. Scan it with your device camera
3. Tap the notification to open the app
4. The code should auto-populate in the input field

---

## Testing Scenarios

### Scenario 1: App is Closed
1. Make sure PulsarApp is completely closed (not in background)
2. Open the deep link using any method above
3. App should launch and the code should be pre-filled

### Scenario 2: App is in Background
1. Open PulsarApp
2. Go to home screen (don't close the app)
3. Open the deep link
4. App should come to foreground with the code pre-filled

### Scenario 3: App is Already Open
1. Have PulsarApp open and visible
2. Open the deep link from another app
3. The code should update in the input field

---

## Testing with Expo Go

If you're using Expo Go for development:

```bash
# iOS
npx uri-scheme open "pulsarapp://connect?code=123456" --ios

# Android
npx uri-scheme open "pulsarapp://connect?code=123456" --android
```

**Note:** Deep linking works differently in Expo Go vs development builds. For full deep link testing, you should use a development build:

```bash
# Create development build
npx expo run:ios
# or
npx expo run:android
```

---

## Quick Test Script

Save this as `test-deeplink.sh` in your project root:

```bash
#!/bin/bash

# Colors
GREEN='\033[0;32m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

CODE=${1:-123456}

echo -e "${BLUE}Testing deep link with code: $CODE${NC}\n"

echo -e "${GREEN}Choose platform:${NC}"
echo "1) iOS Simulator"
echo "2) Android Emulator"
read -p "Enter choice (1 or 2): " choice

if [ "$choice" = "1" ]; then
    echo -e "${BLUE}Opening deep link in iOS Simulator...${NC}"
    xcrun simctl openurl booted "pulsarapp://connect?code=$CODE"
elif [ "$choice" = "2" ]; then
    echo -e "${BLUE}Opening deep link in Android Emulator...${NC}"
    adb shell am start -W -a android.intent.action.VIEW -d "pulsarapp://connect?code=$CODE"
else
    echo "Invalid choice"
fi
```

Make it executable:
```bash
chmod +x test-deeplink.sh
```

Run it:
```bash
# Default code (123456)
./test-deeplink.sh

# Custom code
./test-deeplink.sh 789012
```

---

## Debugging Tips

1. **Check if deep link is received:**
   - Add console.log in the `handleDeepLink` function
   - Check Metro bundler console for logs

2. **Verify app scheme:**
   - Make sure `"scheme": "pulsarapp"` is in app.json
   - Rebuild the app after changing app.json

3. **Android specific:**
   - After changing intentFilters, rebuild the app
   - Clear app data if deep links aren't working

4. **iOS specific:**
   - Make sure bundle identifier is set
   - Rebuild after app.json changes

---

## Production Considerations

For production, you might want to:

1. **Add Universal Links (iOS):**
   - Configure associated domains
   - Example: `https://pulsar.app/connect?code=123456`

2. **Add App Links (Android):**
   - Configure digital asset links
   - Verify domain ownership

3. **Validate codes:**
   - Add code format validation
   - Handle invalid codes gracefully

4. **Analytics:**
   - Track deep link opens
   - Monitor conversion rates
