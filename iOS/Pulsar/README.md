<p align="center">
  <img src="docs/src/assets/og.png" alt="Pulsar - Rich and ready-to use haptics library" />
</p>

A haptic feedback SDK for iOS, written in Swift. Pulsar provides ready-to-use haptic presets, a pattern composer for custom haptic sequences, and a real-time composer for gesture-driven feedback.

## Requirements

- iOS 13+
- macOS 10.15+ (for Mac Catalyst)
- Swift 6.1+

## Installation

### Swift Package Manager (Xcode)

In Xcode, go to **File → Add Package Dependencies** and enter the repository URL:

```
https://github.com/software-mansion/pulsar
```

### Package.swift

```swift
dependencies: [
  .package(url: "https://github.com/software-mansion/pulsar", from: "0.1.0")
]
```

Then add `"Pulsar"` to your target's dependencies.

## Usage

### Presets

Play a built-in haptic preset:

```swift
import Pulsar

let pulsar = Pulsar()
pulsar.getPresets().success()
pulsar.getPresets().earthquake()
```

Play a system haptic style:

```swift
pulsar.getPresets().system.impactMedium()
pulsar.getPresets().system.notificationSuccess()
pulsar.getPresets().system.selection()
```

### Pattern Composer

Define and play a custom haptic pattern using discrete events and continuous amplitude/frequency envelopes:

```swift
let composer = pulsar.getPatternComposer()

let patternData = PatternData(
  discretePattern: [
    DiscretePoint(time: 0.0, amplitude: 1.0, frequency: 0.5),
    DiscretePoint(time: 0.2, amplitude: 0.8, frequency: 0.3)
  ],
  continuousPattern: ContinuousPattern(
    amplitude: [CurvePoint(time: 0.0, value: 0.5), CurvePoint(time: 1.0, value: 0.0)],
    frequency: [CurvePoint(time: 0.0, value: 0.5), CurvePoint(time: 1.0, value: 0.5)]
  )
)

composer.parsePattern(hapticsData: patternData)
composer.play()
```

### Realtime Composer

Control haptic amplitude and frequency in real time, useful for gesture-driven feedback:

```swift
let realtime = pulsar.getRealtimeComposer()

// Drive haptics from a gesture (call on every update)
realtime.set(amplitude: 0.8, frequency: 0.5)

// Stop when the gesture ends
realtime.stop()

// Play a single transient event
realtime.playDiscrete(amplitude: 1.0, frequency: 0.5)
```

## Configuration

```swift
// Enable or disable haptics
pulsar.enableHaptics(state: true)

// Enable or disable audio simulation
pulsar.enableSound(state: true)

// Enable or disable preset caching
pulsar.enableCache(state: true)

// Preload specific presets for low-latency playback
pulsar.preloadPresets(presetNames: ["Success", "Earthquake"])

// Clear the preset cache
pulsar.clearCache()

// Stop all active haptics
pulsar.stopHaptics()

// Shut down the haptic engine
pulsar.shutDownEngine()

// Check if haptics are supported on the current device
let supported = pulsar.isHapticsSupported()
```

## Documentation

Full API reference and guides: [pulsar.swmansion.com/sdk/ios](https://pulsar.swmansion.com/sdk/ios)

## Contributing

See [CONTRIBUTING.md](../../CONTRIBUTING.md) for development setup and guidelines.

## License

Pulsar is licensed under [The MIT License](../../LICENSE).

## Pulsar is created by Software Mansion

Since 2012 [Software Mansion](https://swmansion.com) is a software agency with experience in building web and mobile apps. We are Core React Native Contributors and experts in dealing with all kinds of React Native issues. We can help you build your next dream product – [Hire us](https://swmansion.com/contact/projects?utm_source=pulsar&utm_medium=readme).
