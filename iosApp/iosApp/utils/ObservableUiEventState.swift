//
//  ObservableParkState.swift
//  iosApp
//
//  Created by Daniele Vitanio on 17/09/23.
//  Copyright © 2023 orgName. All rights reserved.
//

import shared

class ObservableUiEventState : ObservableObject {
    @Published var value: ParkScreenEvent
    
    init(value: ParkScreenEvent) {
        self.value = value
    }
}

extension ParkScreenEvent {
    func wrapAsObservable() -> ObservableUiEventState {
        return ObservableUiEventState(value: self)
    }
}
