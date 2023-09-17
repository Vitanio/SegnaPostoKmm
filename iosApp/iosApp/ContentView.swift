import SwiftUI
import shared

private let ParkRoute = "park_screen"

struct ContentView: View {
    @State private var route = [String]()
    
    var body: some View {
        NavigationStack(path: $route) {
            ParkScreen(
                viewModel: ParkViewModel(),
                onNext: { route.append(ParkRoute) }
            )
        }
    }
}
