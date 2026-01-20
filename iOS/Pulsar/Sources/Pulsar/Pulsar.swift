import CoreHaptics
import UIKit

@objc public class Pulsar: NSObject {
  private var presets: PresetsWrapper?
  private var patternComposer: PatternComposerImpl = PatternComposerImpl()
  private var realtimeComposer: RealtimeComposerImpl = RealtimeComposerImpl()
  private var audioSimulator: AudioSimulator = AudioSimulator()
  
  @objc public func Presets() -> PresetsWrapper {
    if (presets == nil) {
      presets = PresetsWrapper(haptics: self)
    }
    return presets!
  }
  
  @objc public func PatternComposer() -> PatternComposerImpl {
    return patternComposer
  }
  
  @objc public func RealtimeComposer() -> RealtimeComposerImpl {
    return realtimeComposer
  }
  
  public func getAudioSimulator() -> AudioSimulator {
    return audioSimulator
  }
  
}
