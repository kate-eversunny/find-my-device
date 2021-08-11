package com.androidgang.findmydevice.helpers

import com.androidgang.findmydevice.models.Smartphone
import java.text.SimpleDateFormat

object Constants {
    val phones = listOf(
        Smartphone(id = 0, name = "Samsung Galaxy S6", imei = "78451584548474", lastSeen = 1628392345L, 14.45, 16.85),
        Smartphone(id =1, name = "LG K52 Lite", imei = "18484511518489", lastSeen = 1628401581L, 45.45, 67.85),
        Smartphone(id =2, name = "Asus Zenfone Max Pro M1 ZB602KL",imei = "38458652548455", lastSeen = 1628412347L, 38.45, 52.85)
    )
    val sdf = SimpleDateFormat("dd.MM.yyyy HH:mm")

    const val TAG_BUNDLE_DEVICE_ID = "idDevice"

    const val LOCATION_REQUEST_INTERVAL: Long = 30000

    const val LOCATION_REQUEST_FASTEST_INTERVAL: Long = 5000

    const val PERMISSIONS_FINE_LOCATION = 100

    const val FIREBASE_DATABASE_URL = "https://findmydeviceapp-4dbac-default-rtdb.europe-west1.firebasedatabase.app/"

}