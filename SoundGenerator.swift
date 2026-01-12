import SwiftUI
import AVFoundation
import CoreAudio // Added for some potential audio utility, though not strictly needed here.
import Accelerate // Added for math functions like sin, though often optional.

// MARK: - Waveform Type
enum WaveformType: Int, CaseIterable, Identifiable {
    case sine = 0
    case square = 1
    case sawtooth = 2
    case triangle = 3

    var id: Int { rawValue }
    var name: String {
        switch self {
        case .sine: return "Sine"
        case .square: return "Square"
        case .sawtooth: return "Sawtooth"
        case .triangle: return "Triangle"
        }
    }
}

// MARK: - Oscillator Configuration
struct OscillatorConfig {
    var type: WaveformType
    var baseFreq: Float
    var freqMod: Float
    var gain: Float
    var attack: Float
    var release: Float
}

// MARK: - Audio Engine Manager
class AudioEngineManager: ObservableObject {
    private var engine = AVAudioEngine()
    private var mixer = AVAudioMixerNode()
    private var filter = AVAudioUnitEQ(numberOfBands: 1)
    private var masterGain = AVAudioMixerNode()

    @Published var isPlaying = false
    @Published var masterVolume: Float = 0.3
    @Published var filterFrequency: Float = 2000

    @Published var oscillatorConfigs: [OscillatorConfig] = [
        OscillatorConfig(type: .sine, baseFreq: 220, freqMod: 2, gain: 0.3, attack: 0.5, release: 0.5),
        OscillatorConfig(type: .square, baseFreq: 440, freqMod: 3, gain: 0.2, attack: 0.3, release: 0.4),
        OscillatorConfig(type: .sawtooth, baseFreq: 110, freqMod: 1.5, gain: 0.25, attack: 0.4, release: 0.6),
        OscillatorConfig(type: .triangle, baseFreq: 330, freqMod: 2.5, gain: 0.28, attack: 0.2, release: 0.3)
    ]

    private var oscillatorNodes: [(player: AVAudioPlayerNode, gainNode: AVAudioMixerNode)] = []
    private var audioBuffers: [AVAudioPCMBuffer] = []
    private var frequencyTimers: [Timer] = []
    private var modulationPhase: [Double] = Array(repeating: 0.0, count: 4)
    
    // An array to hold the DispatchQueueWorkItems for Release to ensure we can cancel them
    private var releaseWorkItems: [DispatchWorkItem] = [] 
    // An array to hold work items for ongoing gain changes (slider updates)
    private var gainRampWorkItems: [DispatchWorkItem] = []


    init() {
        setupAudioEngine()
    }

    private func setupAudioEngine() {
        engine.attach(mixer)
        engine.attach(filter)
        engine.attach(masterGain)

        // Setup filter
        let filterParams = filter.bands[0]
        filterParams.filterType = .lowPass
        filterParams.frequency = filterFrequency
        filterParams.bandwidth = 1.0
        filterParams.bypass = false

        // Connect: mixer -> filter -> masterGain -> output
        engine.connect(mixer, to: filter, format: nil)
        engine.connect(filter, to: masterGain, format: nil)
        engine.connect(masterGain, to: engine.mainMixerNode, format: nil)

        masterGain.outputVolume = masterVolume

        do {
            try engine.start()
        } catch {
            print("Error starting engine: \(error.localizedDescription)")
        }
    }

    func updateMasterVolume(_ value: Float) {
        masterVolume = value
        masterGain.outputVolume = value
    }

    func updateFilterFrequency(_ value: Float) {
        filterFrequency = value
        filter.bands[0].frequency = value
    }

    func startSynthesis() {
        guard !isPlaying else { return }
        isPlaying = true
        
        // Reset modulation phase when starting
        modulationPhase = Array(repeating: 0.0, count: oscillatorConfigs.count)

        for (index, config) in oscillatorConfigs.enumerated() {
            createAndStartOscillator(config: config, index: index)
        }
    }

    private func createAndStartOscillator(config: OscillatorConfig, index: Int) {
        let playerNode = AVAudioPlayerNode()
        let gainNode = AVAudioMixerNode()

        engine.attach(playerNode)
        engine.attach(gainNode)

        let format = AVAudioFormat(standardFormatWithSampleRate: 44100, channels: 1)!

        engine.connect(playerNode, to: gainNode, format: format)
        engine.connect(gainNode, to: mixer, format: format)

        // Create audio buffer with waveform
        let buffer = createWaveformBuffer(
            frequency: config.baseFreq,
            type: config.type.rawValue,
            format: format
        )

        audioBuffers.append(buffer)
        oscillatorNodes.append((playerNode, gainNode))

        // Apply envelope
        gainNode.outputVolume = 0

        playerNode.scheduleBuffer(buffer, at: nil, options: .loops, completionHandler: nil)
        playerNode.play()

        // ⚠️ FIXED ATTACK RAMP
        let attackRamp = config.attack
        let targetGain = config.gain
        let rampSteps: Int = 50 
        let timeStep = TimeInterval(attackRamp) / TimeInterval(rampSteps)
        let volumeStep = targetGain / Float(rampSteps)

        for i in 1...rampSteps {
            DispatchQueue.main.asyncAfter(deadline: .now() + timeStep * Double(i)) {
                // Ensure volume doesn't overshoot the target gain
                gainNode.outputVolume = min(volumeStep * Float(i), targetGain)
            }
        }


        // Frequency modulation timer
        let timer = Timer.scheduledTimer(withTimeInterval: 0.1, repeats: true) { [weak self] _ in
            guard let self = self, self.isPlaying else { return }
            self.modulateFrequency(index: index)
        }
        // Using RunLoop.current.add is generally safe inside a method called from the main thread
        RunLoop.current.add(timer, forMode: .common)
        frequencyTimers.append(timer)
    }

    private func modulateFrequency(index: Int) {
        guard index < oscillatorNodes.count else { return }

        let config = oscillatorConfigs[index]
        if index < modulationPhase.count {
            // Use fmod() to keep the phase within a manageable range (e.g., 0 to 2*pi)
            modulationPhase[index] = fmod(modulationPhase[index] + 0.05, 2.0 * .pi)
        } else {
             return
        }

        let lfo = sin(modulationPhase[index])
        let freqRange = config.baseFreq * (config.freqMod - 1.0)
        let targetFreq = config.baseFreq + freqRange * Float((lfo + 1.0) / 2.0)

        // Update buffer with new frequency
        let format = AVAudioFormat(standardFormatWithSampleRate: 44100, channels: 1)!
        let newBuffer = createWaveformBuffer(frequency: targetFreq, type: config.type.rawValue, format: format)

        let (player, _) = oscillatorNodes[index]
        player.stop()
        player.scheduleBuffer(newBuffer, at: nil, options: .loops, completionHandler: nil)
        player.play()
    }

    private func createWaveformBuffer(frequency: Float, type: Int, format: AVAudioFormat) -> AVAudioPCMBuffer {
        let sampleRate = format.sampleRate
        let periodFrames = sampleRate / Double(frequency)
        let bufferSize = AVAudioFrameCount(ceil(periodFrames))
        
        let buffer = AVAudioPCMBuffer(pcmFormat: format, frameCapacity: bufferSize)!
        buffer.frameLength = bufferSize

        let channels = buffer.floatChannelData!
        let channelData = channels[0]
        
        let twoPi: Float = 2.0 * .pi
        let phaseStep: Float = twoPi / Float(bufferSize)
        var currentPhase: Float = 0.0

        for frame in 0..<Int(bufferSize) {
            var value: Float

            switch type {
            case WaveformType.sine.rawValue: // Sine
                value = sin(currentPhase)
                currentPhase += phaseStep
            case WaveformType.square.rawValue: // Square
                value = Float(frame) < Float(bufferSize) / 2.0 ? 1.0 : -1.0
            case WaveformType.sawtooth.rawValue: // Sawtooth
                value = (2.0 * Float(frame) / Float(bufferSize - 1)) - 1.0
            case WaveformType.triangle.rawValue: // Triangle
                let normalizedFrame = Float(frame) / Float(bufferSize - 1)
                if normalizedFrame < 0.5 {
                    value = (4.0 * normalizedFrame) - 1.0
                } else {
                    value = 3.0 - (4.0 * normalizedFrame)
                }
            default:
                value = 0
            }

            channelData[frame] = value * 0.5 
        }

        return buffer
    }

    func stopSynthesis() {
        guard isPlaying else { return }
        isPlaying = false

        // Stop timers
        frequencyTimers.forEach { $0.invalidate() }
        frequencyTimers.removeAll()
        
        // Cancel any pending work items
        releaseWorkItems.forEach { $0.cancel() }
        releaseWorkItems.removeAll()
        gainRampWorkItems.forEach { $0.cancel() }
        gainRampWorkItems.removeAll()


        // ⚠️ FIXED RELEASE RAMP
        for (index, (player, gainNode)) in oscillatorNodes.enumerated() {
            let releaseRamp = oscillatorConfigs[index].release
            let currentVolume = gainNode.outputVolume
            let rampSteps: Int = 50
            let timeStep = TimeInterval(releaseRamp) / TimeInterval(rampSteps)
            let volumeStep = currentVolume / Float(rampSteps)
            
            // Dispatch the work item that handles the release ramp
            let releaseWorkItem = DispatchWorkItem {
                for i in 0...rampSteps {
                    DispatchQueue.main.asyncAfter(deadline: .now() + timeStep * Double(i)) {
                        let newVolume = currentVolume - volumeStep * Float(i)
                        gainNode.outputVolume = max(newVolume, 0)
                        
                        // Once the ramp is complete (last step), stop the nodes
                        if i == rampSteps {
                            player.stop()
                            // Detach asynchronously to avoid blocking the main thread immediately
                            DispatchQueue.main.async {
                                self.engine.detach(player)
                                self.engine.detach(gainNode)
                            }
                        }
                    }
                }
            }
            
            DispatchQueue.main.async(execute: releaseWorkItem)
            releaseWorkItems.append(releaseWorkItem)
        }

        // Clear arrays immediately after scheduling the stop/detach operations
        oscillatorNodes.removeAll()
        audioBuffers.removeAll()
        modulationPhase = Array(repeating: 0.0, count: oscillatorConfigs.count)
    }

    func updateOscillatorGain(index: Int, gain: Float) {
        guard index < oscillatorNodes.count else { return }
        
        // Cancel any previous gain ramp for this oscillator index before starting a new one
        if index < gainRampWorkItems.count {
            gainRampWorkItems[index].cancel()
        }

        oscillatorConfigs[index].gain = gain
        
        let gainNode = oscillatorNodes[index].gainNode
        let currentVolume = gainNode.outputVolume
        let targetVolume = gain
        let rampDuration: TimeInterval = 0.1 
        
        // ⚠️ FIX: Use the incremental loop for gain updates
        let rampSteps: Int = 10 
        let timeStep = rampDuration / TimeInterval(rampSteps)
        let volumeStep = (targetVolume - currentVolume) / Float(rampSteps)
        
        let gainRampWorkItem = DispatchWorkItem {
            for i in 1...rampSteps {
                DispatchQueue.main.asyncAfter(deadline: .now() + timeStep * Double(i)) {
                    let newVolume = currentVolume + volumeStep * Float(i)
                    // Ensure the volume doesn't go below 0 or above 1.0
                    gainNode.outputVolume = min(max(newVolume, 0), 1.0)
                }
            }
        }
        
        // Execute the new work item and store/update the reference
        if index >= gainRampWorkItems.count {
            gainRampWorkItems.append(gainRampWorkItem)
        } else {
            gainRampWorkItems[index] = gainRampWorkItem
        }
        DispatchQueue.main.async(execute: gainRampWorkItem)
    }
}

// MARK: - SwiftUI Views (No Changes Needed Here)
struct ContentView: View {
    @StateObject private var audioManager = AudioEngineManager()

    var body: some View {
        ScrollView {
            VStack(spacing: 20) {
                Text("🎵 Multi-Oscillator Synthesizer")
                    .font(.largeTitle)
                    .fontWeight(.bold)
                    .padding(.top)

                // Global Controls
                VStack(spacing: 15) {
                    VStack(alignment: .leading, spacing: 8) {
                        HStack {
                            Text("Master Gain")
                                .font(.headline)
                            Spacer()
                            Text(String(format: "%.2f", audioManager.masterVolume))
                                .foregroundColor(.secondary)
                        }
                        Slider(value: Binding(
                            get: { audioManager.masterVolume },
                            set: { audioManager.updateMasterVolume($0) }
                        ), in: 0...1)
                    }

                    VStack(alignment: .leading, spacing: 8) {
                        HStack {
                            Text("Low Pass Filter")
                                .font(.headline)
                            Spacer()
                            Text("\(Int(audioManager.filterFrequency)) Hz")
                                .foregroundColor(.secondary)
                        }
                        Slider(value: Binding(
                            get: { audioManager.filterFrequency },
                            set: { audioManager.updateFilterFrequency($0) }
                        ), in: 100...5000)
                    }
                }
                .padding()
                .background(Color(.systemGray6))
                .cornerRadius(15)

                // Oscillators
                ForEach(0..<audioManager.oscillatorConfigs.count, id: \.self) { index in
                    OscillatorView(
                        config: $audioManager.oscillatorConfigs[index],
                        index: index,
                        audioManager: audioManager
                    )
                }

                // Control Buttons
                VStack(spacing: 12) {
                    if !audioManager.isPlaying {
                        Button(action: {
                            audioManager.startSynthesis()
                        }) {
                            Text("Start Synthesis")
                                .font(.headline)
                                .foregroundColor(.white)
                                .frame(maxWidth: .infinity)
                                .padding()
                                .background(
                                    LinearGradient(
                                        gradient: Gradient(colors: [Color.blue, Color.purple]),
                                        startPoint: .leading,
                                        endPoint: .trailing
                                    )
                                )
                                .cornerRadius(12)
                        }
                    } else {
                        Button(action: {
                            audioManager.stopSynthesis()
                        }) {
                            Text("Stop Synthesis")
                                .font(.headline)
                                .foregroundColor(.white)
                                .frame(maxWidth: .infinity)
                                .padding()
                                .background(
                                    LinearGradient(
                                        gradient: Gradient(colors: [Color.pink, Color.red]),
                                        startPoint: .leading,
                                        endPoint: .trailing
                                    )
                                )
                                .cornerRadius(12)
                        }
                    }
                }
                .padding(.bottom, 30)
            }
            .padding()
        }
    }
}

struct OscillatorView: View {
    @Binding var config: OscillatorConfig
    let index: Int
    let audioManager: AudioEngineManager

    private var waveformName: String {
        config.type.name
    }

    var body: some View {
        VStack(alignment: .leading, spacing: 12) {
            Text("Oscillator \(index + 1) - \(waveformName)")
                .font(.headline)
                .fontWeight(.bold)

            Picker("Waveform Type", selection: $config.type) {
                ForEach(WaveformType.allCases) { type in
                    Text(type.name).tag(type)
                }
            }
            .pickerStyle(.segmented)
            .onChange(of: config.type) { _ in
                 // Note: The frequency modulation loop will automatically pick up the new waveform type on its next cycle.
            }

            VStack(alignment: .leading, spacing: 8) {
                HStack {
                    Text("Base Frequency")
                        .font(.subheadline)
                    Spacer()
                    Text("\(Int(config.baseFreq)) Hz")
                        .foregroundColor(.secondary)
                }
                Slider(value: $config.baseFreq, in: 50...880, step: 1)
            }

            VStack(alignment: .leading, spacing: 8) {
                HStack {
                    Text("Frequency Modulation")
                        .font(.subheadline)
                    Spacer()
                    Text(String(format: "%.1fx", config.freqMod))
                        .foregroundColor(.secondary)
                }
                Slider(value: $config.freqMod, in: 0.5...5, step: 0.1)
            }

            VStack(alignment: .leading, spacing: 8) {
                HStack {
                    Text("Gain")
                        .font(.subheadline)
                    Spacer()
                    Text(String(format: "%.2f", config.gain))
                        .foregroundColor(.secondary)
                }
                Slider(value: Binding(
                    get: { config.gain },
                    set: { newValue in
                        config.gain = newValue
                        audioManager.updateOscillatorGain(index: index, gain: newValue)
                    }
                ), in: 0...0.5, step: 0.01)
            }

            VStack(alignment: .leading, spacing: 8) {
                HStack {
                    Text("Attack")
                        .font(.subheadline)
                    Spacer()
                    Text(String(format: "%.1fs", config.attack))
                        .foregroundColor(.secondary)
                }
                Slider(value: $config.attack, in: 0.1...2, step: 0.1)
            }

            VStack(alignment: .leading, spacing: 8) {
                HStack {
                    Text("Release")
                        .font(.subheadline)
                    Spacer()
                    Text(String(format: "%.1fs", config.release))
                        .foregroundColor(.secondary)
                }
                Slider(value: $config.release, in: 0.1...2, step: 0.1)
            }
        }
        .padding()
        .background(Color(.systemGray6))
        .cornerRadius(15)
    }
}
