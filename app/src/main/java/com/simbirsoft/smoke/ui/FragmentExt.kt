package com.simbirsoft.smoke.ui

import android.content.Context
import android.content.Intent
import android.net.Uri

fun Context.openMap(latitude: Double, longitude: Double) {
    val uri = "geo:$latitude, $longitude"
    startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(uri)))
}

fun Context.openVideo(url: String = "ы") = startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))