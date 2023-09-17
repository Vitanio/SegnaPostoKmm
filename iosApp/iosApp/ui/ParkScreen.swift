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
        viewModel.state.collect(
            collector: Collector<ParkState> { state in onStateReceived(state: state) }
        ) { error in
            print("Error ocurred during state collection")
        }
    }
    
    private func onStateReceived(state: ParkState) {
        self.state.value = state
    }
    
    var body: some View {
        List {
            Button("Next") { viewModel.onParkClicked() }
            Text("Counter: \(state.value.test)")
        }
    }
}

extension ParkViewModel {
    func observableState() -> ObservableParkState {
        return (state.value as! ParkState).wrapAsObservable()
    }
}
