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
    
    @Published var authorizationStatus : CLAuthorizationStatus = .notDetermined
    private let locationManager = CLLocationManager()
    @Published var cordinates : CLLocationCoordinate2D?
    
    override init() {
        super.init()
        locationManager.delegate=self
        locationManager.desiredAccuracy=kCLLocationAccuracyBest
        locationManager.startUpdatingLocation()
    }
    
    func requestLocationPermission()  {
        locationManager.requestWhenInUseAuthorization()
    }
    
    func locationManagerDidChangeAuthorization(_ manager: CLLocationManager) {
        authorizationStatus = manager.authorizationStatus
    }
    
    func locationManager(_ manager: CLLocationManager, didUpdateLocations locations: [CLLocation]) {
        guard let location = locations.last else {return}
        
        cordinates = location.coordinate
    }
    
}
