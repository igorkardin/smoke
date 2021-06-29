package com.simbirsoft.smoke.ui

import android.content.Context
import android.content.Intent
import android.net.Uri

 fun Context.openMap(latitude: Double, longitude: Double) {
    val uri = "geo:$latitude, $longitude"
    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(uri))
    this.startActivity(intent)
}

 fun Context.openVideo(url: String) {
    val uri = " e"
    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(uri))
    this.startActivity(intent)
}