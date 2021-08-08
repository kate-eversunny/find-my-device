package com.androidgang.findmydevice.models

data class Smartphone(
    var id : Int = 0,
    var name: String = "",
    var imei : String = "",
    var lastSeen: Long = 0,
    var longitude: Double = 0.0,
    var latitude: Double = 0.0
)
