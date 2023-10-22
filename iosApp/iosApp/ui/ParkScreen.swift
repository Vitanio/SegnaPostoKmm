//
//  ParkScreen.swift
//  iosApp
//
//  Created by Daniele Vitanio on 17/09/23.
//  Copyright © 2023 orgName. All rights reserved.
//

import SwiftUI
import shared

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
        default:
            break
        }
    }
    
    @State private var isViewAppeared = false
    
    var body: some View {
        
        let columns = [GridItem(.flexible()), GridItem(.flexible())]
        
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
                
                if(self.state.value.parkHistory.first != nil){
                    PrimaryCardView(element: self.state.value.parkHistory.first!) { viewModel.onEvent(event: ParkEvent.OnDeleteParkClicked(park: self.state.value.parkHistory.first!))
                    }
                }
                
                LazyVGrid(
                    columns: columns,
                    alignment: .center,
                    spacing: 10
                ) {
                    ForEach(
                        self.state.value.parkHistory,
                        id: \.self
                    ) { element in
                        SecondaryCardView(element: element) { viewModel.onEvent(event: ParkEvent.OnDeleteParkClicked(park: element)) }
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
    
}

extension ParkViewModel {
    
    func observableState() -> ObservableParkState {
        return (parkState.value as! ParkState).wrapAsObservable()
    }
}

struct PrimaryCardView: View {
    let element: Park
    let deleteFunction: () -> Void
    
    var body: some View {
        
        VStack(alignment: .leading, spacing: 10) {
            HStack{
                Image(uiImage: UIImage(named: "MapsIcon")!).resizable().scaledToFit().frame(width: 50, height: 50)
                VStack(alignment: .leading, spacing: 10){
                    Text(element.title)
                    if(element.description_ != nil){
                        Text(element.description_!)
                    }
                }
            }

            
            VStack(alignment: .leading, spacing: 10) {
                Text("Latitude: \(element.latitude)")
                Text("Longitude: \(element.longitude)")
            }
            
            Text("Id: \(element.id)")
            
            Button(action: { deleteFunction() }, label: {
                Text("Delete")
                    .padding(10)
                    .background(Color.black)
                    .foregroundColor(.white)
            }).cornerRadius(20)
            
        }
        .frame(maxWidth: .infinity, alignment: .leading)
        .padding(10)
        .background(Color.blue)
        .cornerRadius(10)
        .padding([.leading, .trailing], 10)
        .padding(.top, 5)
        
    }
}

struct SecondaryCardView: View {
    let element: Park
    let deleteFunction: () -> Void
    
    var body: some View {
        
        
        VStack(alignment: .leading, spacing: 10) {
            
            HStack{
                Image(uiImage: UIImage(named: "MapsIcon")!).resizable().scaledToFit().frame(width: 50, height: 50)
                VStack(alignment: .leading, spacing: 10){
                    Text(element.title)
                    if(element.description_ != nil){
                        Text(element.description_!)
                    }
                }
            }
            
            VStack(alignment: .leading, spacing: 10) {
                Text("Latitude: \(element.latitude)")
                Text("Longitude: \(element.longitude)")
            }
            
            
            Text("Id: \(element.id)")
            
            
            Button(action: { deleteFunction() }, label: {
                Text("Delete")
                    .padding(10)
                    .background(Color.black)
                    .foregroundColor(.white)
            }).cornerRadius(20)
        }
        .frame(maxWidth: .infinity, alignment: .leading)
        .padding(10)
        .background(Color.blue)
        .cornerRadius(10)
        .padding([.leading, .trailing], 10)
        .padding(.top, 5)
        
        
    }
}


extension UIScreen{
    static let screenWidth = UIScreen.main.bounds.size.width
    static let screenHeight = UIScreen.main.bounds.size.height
    static let screenSize = UIScreen.main.bounds.size
}
