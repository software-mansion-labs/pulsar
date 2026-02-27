# Contributing to Pulsar

Thanks for your interest in contributing to Pulsar. All contributions are welcome!

## Repository structure

```
pulsar/
├── iOS/
│   ├── Pulsar/         # iOS Swift SDK (Swift Package, iOS 13+)
│   └── PulsarApp/      # iOS native demo app (Xcode project)
├── Android/
│   ├── Pulsar/         # Android Kotlin SDK (Gradle library)
│   └── PulsarApp/      # Android native demo app (Gradle project)
├── react-native/
│   ├── react-native-pulsar/  # React Native Turbo Module
│   │   ├── src/              # TypeScript API
│   │   ├── ios/              # ObjC/Swift bridge
│   │   ├── deps/Pulsar/      # iOS SDK sources (mirrored from iOS/Pulsar/Sources/)
│   │   └── android/          # Kotlin bridge + Android SDK sources
│   └── example/              # React Native example app
├── PulsarApp/          # React Native Expo showcase app
└── docs/               # Astro/Starlight documentation site
```

## SDK architecture

The iOS and Android SDKs are standalone native libraries. The React Native SDK is a Turbo Module that **copies** the native SDK sources rather than referencing them as package dependencies:

- **iOS:** `deps/Pulsar/Sources/` mirrors `iOS/Pulsar/Sources/Pulsar/` and is included via a glob in `Pulsar.podspec`
- **Android:** `android/src/main/java/com/swmansion/pulsar/` mirrors `Android/Pulsar/src/main/java/com/swmansion/pulsar/`, with `PulsarModule.kt` and `PulsarPackage.kt` added as the RN bridge layer

When you change native SDK code in `iOS/` or `Android/`, mirror those changes in the corresponding RN library paths.

## Development

### iOS Swift SDK

Open `iOS/Pulsar/` as a Swift Package in Xcode to work on the library, or open `iOS/PulsarApp/PulsarApp.xcodeproj` to run the native demo app.

### Android Kotlin SDK

Open `Android/` as a Gradle project in Android Studio. The `Pulsar` module is the library and `PulsarApp` is the demo app.

```bash
cd Android
./gradlew :Pulsar:build
./gradlew :PulsarApp:installDebug
```

### React Native SDK

```bash
cd react-native/react-native-pulsar
yarn install
yarn prepare    # Build TypeScript output to lib/
yarn typecheck  # TypeScript check
yarn lint       # ESLint
yarn test       # Jest tests
```

Run the example app:

```bash
cd react-native/example
npm install
npm run ios      # iOS simulator
npm run android  # Android emulator
```

For native changes (Swift/Kotlin), rebuild the app after running the above. For JS-only changes, Metro hot reload handles updates automatically.

### React Native Expo showcase app

```bash
cd PulsarApp
yarn install
yarn ios      # Build and run on iOS
yarn android  # Build and run on Android
yarn start    # Start Metro bundler
```

The app references the RN library locally via `"react-native-pulsar": "file:../react-native/react-native-pulsar"`. After making library changes, run `yarn prepare` in the library directory, then restart Metro.

### Documentation site

```bash
cd docs
npm install
npm run dev    # Start dev server
npm run build  # Production build
```
