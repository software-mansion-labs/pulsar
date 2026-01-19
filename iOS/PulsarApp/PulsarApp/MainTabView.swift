import SwiftUI

struct MainTabView: View {
    var body: some View {
        TabView {
            ContentView()
                .tabItem {
                    Label("Playground", systemImage: "gamecontroller.fill")
                }
            
            NewScreenView()
                .tabItem {
                    Label("New Screen", systemImage: "star.fill")
                }
            
            ThirdScreenView()
                .tabItem {
                    Label("Third", systemImage: "circle.fill")
                }
        }
    }
}

#Preview {
    MainTabView()
}
