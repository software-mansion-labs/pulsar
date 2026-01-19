import SwiftUI
import CoreHaptics

struct NewScreenView: View {
    @State private var engine: CHHapticEngine?
    @State private var continuousPlayer: CHHapticAdvancedPatternPlayer?
    @State private var dragLocation: CGPoint = .zero
    @State private var isDragging = false
    
    var body: some View {
        VStack {
            Text("Gesture Haptics")
                .font(.largeTitle)
                .padding()
            
            Text(isDragging ? 
                 "Dragging - X: \(String(format: "%.2f", dragLocation.x)), Y: \(String(format: "%.2f", dragLocation.y))" : 
                 "Tap or drag on the square")
                .font(.caption)
                .foregroundColor(.gray)
                .padding(.bottom, 20)
            
            // Haptic Square
            RoundedRectangle(cornerRadius: 20)
                .fill(
                    LinearGradient(
                        colors: isDragging ? 
                            [Color.blue.opacity(0.3), Color.purple.opacity(0.3)] :
                            [Color.gray.opacity(0.2), Color.gray.opacity(0.1)],
                        startPoint: .topLeading,
                        endPoint: .bottomTrailing
                    )
                )
                .frame(width: 300, height: 300)
                .overlay(
                    RoundedRectangle(cornerRadius: 20)
                        .stroke(isDragging ? Color.blue : Color.gray, lineWidth: 3)
                )
                .overlay(
                    Circle()
                        .fill(Color.red.opacity(0.7))
                        .frame(width: 20, height: 20)
                        .position(x: dragLocation.x, y: dragLocation.y)
                        .opacity(isDragging ? 1 : 0)
                )
                .gesture(
                    DragGesture(minimumDistance: 0)
                        .onChanged { value in
                             handleDrag(at: value.location)
                        }
                        .onEnded { _ in
                             handleDragEnd()
                        }
                )
                .simultaneousGesture(
                    SpatialTapGesture()
                        .onEnded { event in
//                            handleTap(at: event.location)
                        }
                )
            
            VStack(alignment: .leading, spacing: 10) {
                Text("Intensity: \(String(format: "%.2f", getIntensity()))")
                Text("Sharpness: \(String(format: "%.2f", getSharpness()))")
            }
            .font(.system(.body, design: .monospaced))
            .padding()
            
            Spacer()
        }
        .padding()
        .onAppear {
            prepareHaptics()
        }
    }
    
    // Calculate intensity based on Y position (0 to 1, bottom to top)
    func getIntensity(at location: CGPoint? = nil) -> Float {
        let point = location ?? dragLocation
        return Float(min(max((300.0 - point.y) / 300.0, 0), 1))
    }
    
    // Calculate sharpness based on X position (0 to 1, left to right)
    func getSharpness(at location: CGPoint? = nil) -> Float {
        let point = location ?? dragLocation
        return Float(min(max(point.x / 300.0, 0), 1))
    }
    
    func prepareHaptics() {
        guard CHHapticEngine.capabilitiesForHardware().supportsHaptics else { return }
        
        do {
            engine = try CHHapticEngine()
            try engine?.start()
        } catch {
            print("Failed to start haptic engine: \(error)")
        }
    }
    
    func handleTap(at location: CGPoint) {
        guard CHHapticEngine.capabilitiesForHardware().supportsHaptics else { return }
        
        // Create a transient haptic event using tap location
        let intensity = CHHapticEventParameter(parameterID: .hapticIntensity, value: getIntensity(at: location))
        let sharpness = CHHapticEventParameter(parameterID: .hapticSharpness, value: getSharpness(at: location))
        
        let event = CHHapticEvent(eventType: .hapticTransient, parameters: [intensity, sharpness], relativeTime: 0)
        
        do {
            let pattern = try CHHapticPattern(events: [event], parameters: [])
            let player = try engine?.makePlayer(with: pattern)
            try player?.start(atTime: 0)
        } catch {
            print("Failed to play haptic: \(error)")
        }
    }
    
    func handleDrag(at location: CGPoint) {
        dragLocation = location
        
        if !isDragging {
            // Start continuous haptic
            isDragging = true
            startContinuousHaptic()
        } else {
            // Update continuous haptic parameters
            updateContinuousHaptic()
        }
    }
    
    func handleDragEnd() {
        isDragging = false
        stopContinuousHaptic()
    }
    
    func startContinuousHaptic() {
        guard CHHapticEngine.capabilitiesForHardware().supportsHaptics else { return }
        
        let intensity = CHHapticEventParameter(parameterID: .hapticIntensity, value: getIntensity())
        let sharpness = CHHapticEventParameter(parameterID: .hapticSharpness, value: getSharpness())
        
        let event = CHHapticEvent(
            eventType: .hapticContinuous,
            parameters: [intensity, sharpness],
            relativeTime: 0,
            duration: 100 // Long duration for continuous feedback
        )
        
        do {
            let pattern = try CHHapticPattern(events: [event], parameters: [])
            continuousPlayer = try engine?.makeAdvancedPlayer(with: pattern)
            try continuousPlayer?.start(atTime: 0)
        } catch {
            print("Failed to start continuous haptic: \(error)")
        }
    }
    
    func updateContinuousHaptic() {
        guard let player = continuousPlayer else { return }
        
        let intensityParam = CHHapticDynamicParameter(
            parameterID: .hapticIntensityControl,
            value: getIntensity(),
            relativeTime: 0
        )
        
        let sharpnessParam = CHHapticDynamicParameter(
            parameterID: .hapticSharpnessControl,
            value: getSharpness(),
            relativeTime: 0
        )
        
        do {
            try player.sendParameters([intensityParam, sharpnessParam], atTime: 0)
        } catch {
            print("Failed to update haptic parameters: \(error)")
        }
    }
    
    func stopContinuousHaptic() {
        do {
            try continuousPlayer?.stop(atTime: 0)
        } catch {
            print("Failed to stop continuous haptic: \(error)")
        }
        continuousPlayer = nil
    }
}

#Preview {
    NewScreenView()
}
