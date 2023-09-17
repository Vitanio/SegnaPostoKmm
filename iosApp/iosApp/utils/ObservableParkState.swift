//
//  ObservableParkState.swift
//  iosApp
//
//  Created by Daniele Vitanio on 17/09/23.
//  Copyright © 2023 orgName. All rights reserved.
//

import shared

class ObservableParkState : ObservableObject {
    @Published var value: ParkState
    
    init(value: ParkState) {
        self.value = value
    }
}

extension ParkState {
    func wrapAsObservable() -> ObservableParkState {
        return ObservableParkState(value: self)
    }
}
