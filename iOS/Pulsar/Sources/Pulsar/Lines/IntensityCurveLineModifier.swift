import CoreHaptics

@available(iOS 13.0, macOS 10.15, *)
public class IntensityCurveLineModifier : CurveLineModifier {
  public var getCurve: CHHapticParameterCurve {
    return CHHapticParameterCurve(
      parameterID: .hapticIntensityControl,
      controlPoints: points,
      relativeTime: 0
    )
  }
}
