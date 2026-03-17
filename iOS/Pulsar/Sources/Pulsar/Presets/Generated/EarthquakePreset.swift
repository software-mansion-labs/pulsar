import UIKit
import Foundation

@objc public class EarthquakePreset : Player, Preset {
  public static let name: String = "Earthquake"

  @objc public init(_ haptics: Pulsar) {
    super.init(
      haptics, 
      rawContinuousPattern: [
        [[0, 0.0], [300, 0.8], [300, 0.0], [400, 0.0], [600, 0.8], [600, 0.0]],
        [[0, 0.8], [600, 0.8]],
      ], 
      rawDiscretePattern: [
        [0, 1.0, 1.0]
      ]
    )
  }
  
  public static func getInstance(haptics: Pulsar) -> Preset {
    return EarthquakePreset(haptics)
  }
}
