import SwiftUI
import shared

@main
struct iOSApp: App {
    let databaseModule = DatabaseModule()
    
	var body: some Scene {
		WindowGroup {
            ContentView(parkDataSource: databaseModule.parkDataSource)
		}
	}
}
