package com.example.segnaposto.dialog

interface TextProvider {
    fun getTitleText(): String
    fun getDescriptionText(): String
    fun getPrimaryButtonText(): String
}

open class BaseTextProvider: TextProvider {
    override fun getTitleText() = ""
    override fun getDescriptionText() = ""
    override fun getPrimaryButtonText() = ""
}

class LocationPowerStatusTextProvider: BaseTextProvider() {
    override fun getTitleText(): String {
        return "Gps off"
    }
    override fun getDescriptionText(): String {
        return "This app needs access to your location so that you can save " +
                "the position of your park."
    }
    override fun getPrimaryButtonText(): String {
        return "Ok"
    }
}