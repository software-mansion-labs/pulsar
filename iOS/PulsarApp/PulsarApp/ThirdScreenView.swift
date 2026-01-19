import SwiftUI

struct ThirdScreenView: View {
    @State private var pressCount = 0
    
    var body: some View {
        VStack {
            Text("Third Screen")
                .font(.largeTitle)
                .padding()
            
            Button(action: handlePress) {
                Text("Press Me")
                    .font(.title2)
                    .padding()
                    .background(Color.blue)
                    .foregroundColor(.white)
                    .cornerRadius(10)
            }
            
            Text("Pressed \(pressCount) times")
                .font(.body)
                .foregroundColor(.gray)
                .padding(.top, 10)
            
            Spacer()
        }
        .padding()
    }
    
    func handlePress() {
        pressCount += 1
        print("Button pressed! Count: \(pressCount)")
    }
}

#Preview {
    ThirdScreenView()
}
