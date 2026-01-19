import Foundation
import AVFoundation

public enum WaveformType: String {
	case sine = "sine"
	case square = "square"
	case triangle = "triangle"
	case sawtooth = "sawtooth"
}

struct BarEvent {
  let timestamp: Double
  let oscillators: [OscillatorConfig]
}

struct OscillatorConfig {
  let initialFreq: Double
  let finalFreq: Double
  let decayTime: Double
  let attack: Double
  let release: Double
  let volume: Float
  let waveform: WaveformType
}

public class AudioSimulator: NSObject {
	private let sampleRate: Double = 44100
	private var engine: AVAudioEngine = AVAudioEngine()
	private var playerNode: AVAudioPlayerNode = AVAudioPlayerNode()
	private var eq: AVAudioUnitEQ = AVAudioUnitEQ(numberOfBands: 1)
	private var renderedBuffer: AVAudioPCMBuffer?
    
	private var isEngineConfigured = false
    
	public override init() {
		super.init()
		configureEngine()
	}
    
	private func configureAudioSession() {
		let session = AVAudioSession.sharedInstance()
		do {
			try session.setCategory(.playback, mode: .default, options: [.mixWithOthers])
			try session.setActive(true)
		} catch {
			print("AudioSession error: \(error)")
		}
	}
    
	private func configureEngine() {
		guard !isEngineConfigured else { return }
		configureAudioSession()
        
		// Player -> EQ(lowpass) -> MainMixer
		engine.attach(playerNode)
        
		let band = eq.bands.first!
		band.filterType = .lowPass
		band.frequency = 700
		band.bandwidth = 0.7
		band.gain = 0
		band.bypass = false
		engine.attach(eq)
        
		let format = AVAudioFormat(standardFormatWithSampleRate: sampleRate, channels: 1)!
		engine.connect(playerNode, to: eq, format: format)
		engine.connect(eq, to: engine.mainMixerNode, format: format)
        
		do {
			try engine.start()
			isEngineConfigured = true
		} catch {
			print("Failed to start AVAudioEngine: \(error)")
		}
	}
	
	// MARK: - Waveform Generators
	
	private func generateWaveform(_ waveform: WaveformType, phase: Double) -> Double {
		let normalizedPhase = phase.truncatingRemainder(dividingBy: Double.pi * 2) / (Double.pi * 2)
		
		switch waveform {
		case .sine:
			return sin(phase)
		case .square:
			return normalizedPhase < 0.5 ? 1.0 : -1.0
		case .triangle:
			if normalizedPhase < 0.25 {
				return normalizedPhase * 4.0
			} else if normalizedPhase < 0.75 {
				return 2.0 - (normalizedPhase * 4.0)
			} else {
				return (normalizedPhase - 1.0) * 4.0
			}
		case .sawtooth:
			return (normalizedPhase * 2.0) - 1.0
		}
	}

	@discardableResult
	public func render(from data: PlaygroundData) -> Double {
		let amplitudePoints: [ChartPoint] = data.line.count > 0 ? data.line[0] : []
		let frequencyPoints: [ChartPoint] = data.line.count > 1 ? data.line[1] : []
		let barPoints: [BarChartPoint] = data.bar
        
		// Check if we have any data to render
		let hasLines = !amplitudePoints.isEmpty || !frequencyPoints.isEmpty
		let hasBars = !barPoints.isEmpty
        
		guard hasLines || hasBars else {
			renderedBuffer = nil
			return 0
		}
        
		let lastAmpTime = amplitudePoints.last?.x ?? 0
		let lastFreqTime = frequencyPoints.last?.x ?? 0
		let lastBarTime = barPoints.last?.x ?? 0
		var duration = max(lastAmpTime, lastFreqTime, lastBarTime)
        
		// Add buffer for bar event release times
		if hasBars {
			duration += 0.05 // 50ms buffer for release
		}
        
		if duration <= 0 {
			renderedBuffer = nil
			return 0
		}
        
		let frameCount = AVAudioFrameCount(Int(duration * sampleRate) + 1)
		let format = AVAudioFormat(standardFormatWithSampleRate: sampleRate, channels: 1)!
		guard let buffer = AVAudioPCMBuffer(pcmFormat: format, frameCapacity: frameCount) else {
			renderedBuffer = nil
			return 0
		}
		buffer.frameLength = frameCount
		let out = buffer.floatChannelData![0]
        
		// Initialize buffer to silence
		memset(out, 0, Int(frameCount) * MemoryLayout<Float>.size)
        
		var phasesContinuous: [Double] = [0, 0, 0, 0] // 4 continuous oscillators
		var phasesBar: [Double] = Array(repeating: 0, count: barPoints.count * 3) // Pre-allocate for all bar harmonics
		let twoPi = Double.pi * 2
        
		// Helpers to sample curves
		func valueForTime(_ points: [ChartPoint], _ t: Double) -> Float {
			if points.isEmpty { return 0 }
			if t <= points[0].x { return points[0].y }
			if t >= points.last!.x { return points.last!.y }
			for i in 1..<points.count {
				let p1 = points[i - 1]
				let p2 = points[i]
				if t <= p2.x {
					let t0 = p1.x, t1 = p2.x
					let v0 = p1.y, v1 = p2.y
					let ratio = Float((t - t0) / (t1 - t0))
					return v0 + (v1 - v0) * ratio
				}
			}
			return points.last!.y
		}
        
		// Map normalized frequency (0..1) -> Hz (80..230)
		func mapFrequency(_ y: Float) -> Double {
			let hz = 80.0 + (230.0 - 80.0) * Double(y)
			return hz
		}
        
		var barEvents: [BarEvent] = []
		for bar in barPoints {
			let sources = 3
			let maxFreq = 440.0
			let minFreq = 60.0
			let baseFreq = minFreq + (maxFreq - minFreq) * Double(bar.y2)
			let targetBaseFreq = baseFreq * 0.2
            
			func alignVolume(_ x: Float, _ sources: Int) -> Float {
				return Float(0.1 / Double(sources) + 0.9 / Double(sources) * Double(x))
			}
            
			let vol = alignVolume(bar.y1, sources)
			var oscConfigs: [OscillatorConfig] = []
            
			// Base oscillator
			oscConfigs.append(OscillatorConfig(
				initialFreq: baseFreq,
				finalFreq: targetBaseFreq,
				decayTime: 0.028,
				attack: 0.002,
				release: 0.014,
				volume: vol,
				waveform: .sine
			))
            
			// Harmonic 1 (1.5x)
			let harm1 = baseFreq * 1.5
			let targetHarm1 = harm1 * 0.4
			oscConfigs.append(OscillatorConfig(
				initialFreq: harm1,
				finalFreq: targetHarm1,
				decayTime: 0.031,
				attack: 0,
				release: 0.015,
				volume: vol,
				waveform: .sine
			))
            
			// Harmonic 2 (0.3x)
			let harm2 = baseFreq * 0.3
			let targetHarm2 = harm2 * 0.5
			oscConfigs.append(OscillatorConfig(
				initialFreq: harm2,
				finalFreq: targetHarm2,
				decayTime: 0.039,
				attack: 0.005,
				release: 0.018,
				volume: vol,
				waveform: .sine
			))
            
			barEvents.append(BarEvent(timestamp: Double(bar.x), oscillators: oscConfigs))
		}
        
		// Render all samples
		for i in 0..<Int(frameCount) {
			let t = Double(i) / sampleRate
			var sampleValue: Double = 0
            
			// Continuous waves (line data)
			if hasLines && !amplitudePoints.isEmpty && !frequencyPoints.isEmpty {
				let amp = Double(valueForTime(amplitudePoints, t)) * 0.6
				let freq = mapFrequency(valueForTime(frequencyPoints, t))
                
				phasesContinuous[0] += twoPi * freq / sampleRate
				if phasesContinuous[0] > twoPi { phasesContinuous[0] -= twoPi }
				sampleValue += amp * generateWaveform(.sine, phase: phasesContinuous[0])
			}
            
			// Bar events (transients with harmonics)
			if hasBars {
				for (eventIdx, barEvent) in barEvents.enumerated() {
					let eventStartTime = barEvent.timestamp
                    
					for (oscIdx, osc) in barEvent.oscillators.enumerated() {
						let relativeTime = t - eventStartTime
						if relativeTime < 0 { continue }
                        
						let totalDuration = osc.attack + osc.decayTime + osc.release
						if relativeTime >= totalDuration { continue }
                        
						let phaseIdx = eventIdx * 3 + oscIdx
						guard phaseIdx < phasesBar.count else { continue }
                        
						// ADSR envelope
						var envValue: Float = 0
						if relativeTime < osc.attack {
							// Attack
							if osc.attack > 0 {
								envValue = Float(relativeTime / osc.attack)
							} else {
								envValue = 1.0
							}
						} else if relativeTime < osc.attack + osc.decayTime {
							// Decay
							envValue = 1.0
						} else {
							// Release
							let releaseTime = relativeTime - (osc.attack + osc.decayTime)
							if osc.release > 0 {
								envValue = Float(1.0 - (releaseTime / osc.release))
							} else {
								envValue = 0
							}
						}
                        
						// Frequency sweep
						var freq = osc.initialFreq
						if osc.decayTime > 0 {
							let sweepDuration = min(osc.decayTime, totalDuration)
							if relativeTime < sweepDuration {
								let ratio = relativeTime / osc.decayTime
								freq = osc.initialFreq * pow(osc.finalFreq / osc.initialFreq, ratio)
							} else {
								freq = osc.finalFreq
							}
						}
                        
						phasesBar[phaseIdx] += twoPi * freq / sampleRate
						if phasesBar[phaseIdx] > twoPi { phasesBar[phaseIdx] -= twoPi }
                    
						sampleValue += Double(osc.volume * envValue) * generateWaveform(osc.waveform, phase: phasesBar[phaseIdx])
					}
				}
			}
            
			out[i] = Float(sampleValue)
		}
        
		renderedBuffer = buffer
		return duration
	}
  
	public func play() {
		guard let buffer = renderedBuffer else { return }
		configureEngine()
        
		if playerNode.isPlaying { playerNode.stop() }
		playerNode.scheduleBuffer(buffer, at: nil, options: [])
		playerNode.play()
	}
    
	public func stop() {
		playerNode.stop()
	}
    
	public var isPlaying: Bool {
		return playerNode.isPlaying
	}
}
