//
//  ParkScreen.swift
//  iosApp
//
//  Created by Daniele Vitanio on 17/09/23.
//  Copyright © 2023 orgName. All rights reserved.
//

import SwiftUI
import shared
import MapKit
import CoreLocation

struct ParkScreen: View {
    @ObservedObject private var state: ObservableParkState
    @ObservedObject private var uiEvent: ObservableUiEventState
    
    @ObservedObject private var uiElements: ObservableUiElementState
    
    
    @ObservedObject private var locationPermission:LocationPermission
    
    private let viewModel: ParkViewModel
    private let onNext: () -> Void
    
    init(
        viewModel: ParkViewModel,
        onNext: @escaping () -> Void
    ) {
        self.viewModel = viewModel
        self.onNext = onNext
        self.state = viewModel.observableState()
        self.uiEvent = ObservableUiEventState(state: ParkScreenEvent.InitialState())
        self.uiElements = ObservableUiElementState(value: UiElement(requestPermission: false, showPermissionDialog: false))
        self.locationPermission = LocationPermission{
            viewModel.onEvent(event: ParkEvent.OnAddParkClicked())
            
        }
        observeState()
    }
    
    private func observeState() {
        
        viewModel.parkState.collect(collector: Collector<ParkState> { state in onStateReceived(state: state) }) { error in
            print("Error ocurred during state collection")
        }
        
        viewModel.uiEvent.collect(collector: Collector<ParkScreenEvent> { state in onUiEventReceived(uiEvent: state)}) { error in
            print("Error ocurred during state collection")
        }
        
    }
    
    
    private func onStateReceived(state: ParkState) {
        self.state.value = state
    }
    
    private func onUiEventReceived(uiEvent: ParkScreenEvent) {
        self.uiEvent.state = uiEvent
        
        switch self.uiEvent.state {
        case _ as ParkScreenEvent.ShowPermissionDialog:
            self.uiElements.value = UiElement(requestPermission: false, showPermissionDialog: true)
       // case _ as ParkScreenEvent.ShowMapDialkog:
       //     self.uiElements.value = UiElement(requestPermission: false, showPermissionDialog: true)
        default:
            break
        }
    }
    
    @State private var isViewAppeared = false
    
    var body: some View {
        
        if(self.uiElements.value.showPermissionDialog){
            PermissionDialog(
                title: "Access location?",
                message: "This lets you choose which photos you want to add to this project.",
                buttonTitle: "Go to Settings") {
                    viewModel.onEvent(event: ParkEvent.OnGoToSettingsClicked())
                    self.uiElements.value = UiElement(requestPermission: false, showPermissionDialog: false)
                }
                .navigationTitle("Segna Posto")
        }else{
            ScrollView{
                VStack {
                    ForEach(
                        self.state.value.parkHistory,
                        id: \.self
                    ) { element in

                        ParkCardView(element: element,
                                     navigationFunction: {
                            viewModel.onEvent(event: ParkEvent.OnStartNavigationClicked(park: element))
                        },
                        deleteFunction: {
                            viewModel.onEvent(event: ParkEvent.OnDeleteParkClicked(park: element))
                        })}

                    }
                    //.frame(width: (UIScreen.screenWidth) / 2, height: 150, alignment: .leading)
                }
            }
            
            
            Button("Add Park Button") { viewModel.onEvent(event: ParkEvent.OnAddParkClicked()) }
            
                .listRowBackground(Color.clear)
                .onAppear {
                    if !isViewAppeared {
                        viewModel.onEvent(event: ParkEvent.OnScreenResumed())
                        isViewAppeared = true
                    }
                }
                .onDisappear {
                    // Handle the ON_STOP event here if needed
                }
                .navigationTitle("Segna Posto")

        }
        
    }



extension ParkViewModel {
    
    func observableState() -> ObservableParkState {
        return (parkState.value as! ParkState).wrapAsObservable()
    }
}

struct ParkCardView: View {
    let element: Park
    let navigationFunction: () -> Void
    let deleteFunction: () -> Void

    var body: some View {
        var parkMarker = CLLocationCoordinate2D(latitude: element.latitude, longitude: element.longitude)
        
        VStack(alignment: .leading) {
            VStack(alignment: .leading) {
                if #available(iOS 17.0, *) {
                    Map {
                        Marker("Tower Bridge", coordinate: parkMarker)
                    }
                } else {
                    Map(coordinateRegion: .constant(
                        MKCoordinateRegion(
                            center: parkMarker,
                            latitudinalMeters: CLLocationDistance(exactly: 200)!, longitudinalMeters: CLLocationDistance(exactly: 200)!)
                    ))
                }
            }
            .frame(maxWidth: .infinity, minHeight: 250, alignment: .leading)
            .cornerRadius(20)

            VStack(alignment: .leading) {
                HStack{
                    VStack(alignment: .leading) {
                        Text(element.title)
                        Text(element.description_! + " - " + element.number!)
                        
                    }.frame(maxWidth: .infinity, alignment: .leading)
                        Image(systemName: "minus.circle.fill")
                            .resizable()
                            .frame(width: 20, height: 20, alignment: .trailing)
                            .foregroundColor(Color(UIColor.systemRed))
                            .onTapGesture(perform: {deleteFunction()})
                            
                }
                .frame(maxWidth: .infinity, alignment: .leading)

                Button(action: { navigationFunction() }, label: {
                    Text("Navigate")
                        .frame(maxWidth: .infinity, alignment: .center)
                        .padding(10)
                        .background(Color(UIColor.systemTeal))
                        .foregroundColor(.white)
                        .font(.system(size: 18, weight: .semibold, design: .default))
                        
                }).cornerRadius(10)
            }
            .padding([.leading, .trailing, .bottom], 10)
        }
        .frame(maxWidth: .infinity, alignment: .leading)
        .background(Color(UIColor.systemGray5))
        .cornerRadius(20)
        .padding([.leading, .trailing], 10)
        .padding([.top, .bottom], 10)
        
    }
    
}

extension UIScreen{
    static let screenWidth = UIScreen.main.bounds.size.width
    static let screenHeight = UIScreen.main.bounds.size.height
    static let screenSize = UIScreen.main.bounds.size
}
