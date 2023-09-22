import SwiftUI
import shared

private let ParkRoute = "park_screen"

struct ContentView: View {
    @State private var route = [String]();
    
    let databaseModule: DatabaseModule
    let repository: ParkRepository
    let permissionsUtil: PermissionsUtil
    let viewModel:ParkViewModel
    
    init() {
        self.databaseModule = DatabaseModule()
        self.repository = ParkRepository(driver: databaseModule.driver)
        self.permissionsUtil = PermissionsUtil()
        self.viewModel = ParkViewModel(repository: repository, permissionsUtil: permissionsUtil)
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
