//
//  ObservableUiEventState.swift
//  iosApp
//
//  Created by Daniele Vitanio on 23/09/23.
//  Copyright © 2023 orgName. All rights reserved.
//

import shared

class ObservableUiEventState : ObservableObject {
    @Published var state: ParkScreenEvent
    
    init(state: ParkScreenEvent) {
        self.state = state
    }
}

extension ParkScreenEvent {
    func wrapAsObservable() -> ObservableUiEventState {
        return ObservableUiEventState(state: self)
    }
}
