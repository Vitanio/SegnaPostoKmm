//
//  LocationPermission.swift
//  iosApp
//
//  Created by Daniele Vitanio on 23/09/23.
//  Copyright © 2023 orgName. All rights reserved.
//

import Foundation
import CoreLocation


class LocationPermission:NSObject, ObservableObject, CLLocationManagerDelegate {
    
    let function: () -> Void
    
    @Published var authorizationStatus : CLAuthorizationStatus = .notDetermined
    private let locationManager = CLLocationManager()
    @Published var cordinates : CLLocationCoordinate2D?
    
    init(addParkFunction: @escaping () -> Void) {
        self.function = addParkFunction
        super.init() 
        
        locationManager.delegate=self
        locationManager.desiredAccuracy=kCLLocationAccuracyBest
        locationManager.startUpdatingLocation()
    }
    
    func locationManagerDidChangeAuthorization(_ manager: CLLocationManager) {
        authorizationStatus = manager.authorizationStatus
        
        switch authorizationStatus{
            case .authorizedAlways:
                self.function()
            case .authorizedWhenInUse:
                self.function()
            default:
                break;
            }
    }
    
    func locationManager(_ manager: CLLocationManager, didUpdateLocations locations: [CLLocation]) {
        guard let location = locations.last else {return}
        
        cordinates = location.coordinate
    }
    
}
