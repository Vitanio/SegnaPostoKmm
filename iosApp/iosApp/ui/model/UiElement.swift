//
//  UiElement.swift
//  iosApp
//
//  Created by Daniele Vitanio on 23/09/23.
//  Copyright © 2023 orgName. All rights reserved.
//

import Foundation


struct UiElement{
    let requestPermission: Bool
    let showPermissionDialog: Bool
    
    init(requestPermission: Bool = false, showPermissionDialog: Bool = false) {
        self.requestPermission = requestPermission
        self.showPermissionDialog = showPermissionDialog
    }
}
