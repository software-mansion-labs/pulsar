import SwiftUI
import Pulsar

struct ThirdScreenView: View {
    private var pulsar = Pulsar()
    private var simulator = AudioSimulator()
    @State var tmp: PatternComposer?

    var body: some View {
        VStack {
            Text("Third Screen")
                .font(.largeTitle)
                .padding()
            
            Button("Click") {
                pulsar.getPresets().success()
            }

            Spacer()

            Button("Play Example Pattern") {
                let amplitude: [ValuePoint] = [
                    ValuePoint(time: 0.0,   value: 0.538),
                    ValuePoint(time: 0.997, value: 1.0),
                    ValuePoint(time: 1.015, value: 0.0),
                ]
                let frequency: [ValuePoint] = [
                    ValuePoint(time: 0.0,   value: 0.675),
                    ValuePoint(time: 0.996, value: 0.478),
                ]
                let discretePattern: [DiscdetePoint] = [
                    DiscdetePoint(time: 0.1, amplitude: 0.8, frequency: 0.5),
                    DiscdetePoint(time: 0.3, amplitude: 0.6, frequency: 0.7),
                    DiscdetePoint(time: 0.6, amplitude: 0.9, frequency: 0.3),
                    DiscdetePoint(time: 0.9, amplitude: 0.7, frequency: 0.8),
                ]
                let data = PatternData(continuousPattern: ContinuousPattern(amplitude: amplitude, frequency: frequency), discretePattern: discretePattern)

                tmp = pulsar.getPatternComposer()
                tmp?.parsePattern(hapticsData: data)
                tmp?.play()
                simulator.play(buffer: simulator.parsePattern(from: data))
            }
            
            Spacer()
        }
        .padding()
    }
}

#Preview {
    ThirdScreenView()
}
