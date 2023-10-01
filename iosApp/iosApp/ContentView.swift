import SwiftUI
import shared

private let ParkRoute = "park_screen"

struct ContentView: View {
    @State private var route = [String]();
    
    let databaseModule: DatabaseModule
    let repository: ParkRepository
    let locationManager: LocationManager
    let viewModel:ParkViewModel
    
    init() {
        self.databaseModule = DatabaseModule()
        self.repository = ParkRepository(driver: databaseModule.driver)
        self.locationManager = LocationManager()
        self.viewModel = ParkViewModel(repository: repository, locationManager: locationManager)
    }
        
    var body: some View {
        NavigationStack(path: $route) {
            ParkScreen(
                viewModel: viewModel,
                onNext: { route.append(ParkRoute) }
            )
        }
    }
}
