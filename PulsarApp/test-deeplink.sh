#!/bin/bash

# Colors
GREEN='\033[0;32m'
BLUE='\033[0;34m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

CODE=${1:-123456}

echo -e "${BLUE}╔════════════════════════════════════════╗${NC}"
echo -e "${BLUE}║  PulsarApp Deep Link Tester          ║${NC}"
echo -e "${BLUE}╚════════════════════════════════════════╝${NC}\n"

echo -e "${YELLOW}Testing deep link with code: $CODE${NC}\n"

echo -e "${GREEN}Choose platform:${NC}"
echo "1) iOS Simulator"
echo "2) Android Emulator"
echo "3) Exit"
read -p "Enter choice (1, 2, or 3): " choice

if [ "$choice" = "1" ]; then
    echo -e "\n${BLUE}Opening deep link in iOS Simulator...${NC}"
    xcrun simctl openurl booted "pulsarapp://connect?code=$CODE"
    echo -e "${GREEN}✓ Deep link sent!${NC}"
    echo -e "${YELLOW}Check your simulator - the code should appear in the input field${NC}\n"
elif [ "$choice" = "2" ]; then
    echo -e "\n${BLUE}Opening deep link in Android Emulator...${NC}"
    adb shell am start -W -a android.intent.action.VIEW -d "pulsarapp://connect?code=$CODE"
    echo -e "${GREEN}✓ Deep link sent!${NC}"
    echo -e "${YELLOW}Check your emulator - the code should appear in the input field${NC}\n"
elif [ "$choice" = "3" ]; then
    echo "Goodbye!"
else
    echo -e "${YELLOW}Invalid choice${NC}"
fi
