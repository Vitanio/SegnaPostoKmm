import SwiftUI
import shared

struct ContentView: View {
	let greet = Greeting().greet()
    
    var parkDataSource: ParkDataSource?

    init(parkDataSource: ParkDataSource? = nil) {
        self.parkDataSource = parkDataSource
    }
    
    var park = Park(id: nil, title: "Mountain Bike", description: "Mountain Bike", number: "Mountain Bike", latitude: 12.2, longitude: 12.3, date: "Mountain Bike")
    
	var body: some View {
		Text(greet)
        Button(action: addPark) {
            Text("Add Park")
        }

	}
    func addPark() {
        parkDataSource?.insertPark(park: park, completionHandler: { _ in })
    }
}
