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
    
    private let viewModel: ParkViewModel
    private let onNext: () -> Void
    
    init(
        viewModel: ParkViewModel,
        onNext: @escaping () -> Void
    ) {
        self.viewModel = viewModel
        self.onNext = onNext
        self.state = viewModel.observableState()
        observeState()
    }
    
    private func observeState() {
        
        viewModel.parkState.collect(
            collector: Collector<ParkState> { state in onStateReceived(state: state) }
        ) { error in
            print("Error ocurred during state collection")
        }
        
        viewModel.uiEvent.collect(
            collector: Collector<ParkScreenEvent> { state
                in onUiEventReceived(uiEvent: state)
            }
        ) { error in
            print("Error ocurred during state collection")
        }
    }
    
    
    private func onStateReceived(state: ParkState) {
        self.state.value = state
    }
    
    private func onUiEventReceived(uiEvent: ParkScreenEvent) {
        switch uiEvent {
        case let event as ParkScreenEvent.RequestPermission:
            print("Received event: RequestPermission")
        case let event as ParkScreenEvent.ShowPermissionDialog:
            print("Received event: ShowPermissionDialog")
        default:
            break
        }
    }
    
    @State private var isViewAppeared = false
    
    var body: some View {
        
        Button("Add Park Button") { viewModel.onEvent(event: ParkEvent.OnAddParkClicked()) }
        List(self.state.value.parkHistory, id: \.self) { element in
            CardView(element: element)
        }
        .onAppear {
            if !isViewAppeared {
                viewModel.onEvent(event: ParkEvent.OnScreenResumed())
                isViewAppeared = true
            }
        }
        .onDisappear {
            // Handle the ON_STOP event here if needed
        }
    }
}
    
extension ParkViewModel {
    
    func observableState() -> ObservableParkState {
        return (parkState.value as! ParkState).wrapAsObservable()
    }
}

struct CardView: View {
    let element: Park

    var body: some View {
        VStack(alignment: .leading, spacing: 10) {
            HStack {
                Text("Id: \(element.id)")
                Spacer()
                Text("Title: \(element.title)")
            }

            HStack {
                Text("Latitude: \(element.latitude)")
                Spacer()
                Text("Longitude: \(element.longitude)")
            }
        }
        .padding(10)
        .background(Color.white)
        .cornerRadius(10)
        .shadow(radius: 5)
        .padding([.leading, .trailing], 10)
        .padding(.top, 5)
    }
}
