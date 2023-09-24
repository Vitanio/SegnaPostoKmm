//
//  ObservableUiElementState.swift
//  iosApp
//
//  Created by Daniele Vitanio on 23/09/23.
//  Copyright © 2023 orgName. All rights reserved.
//

import Foundation


class ObservableUiElementState : ObservableObject {
    @Published var value: UiElement
    
    init(value: UiElement) {
        self.value = value
    }
}

extension UiElement {
    func wrapAsObservable() -> ObservableUiElementState {
        return ObservableUiElementState(value: self)
    }
}

