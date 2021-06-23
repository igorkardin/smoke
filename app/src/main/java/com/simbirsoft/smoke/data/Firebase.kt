package com.simbirsoft.smoke.data

import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

object FirebaseProvider {
    private val db by lazy { Firebase.firestore }
    val hookahDatabase by lazy {
        db.collection(HOOKAH_ID)
    }
    private const val HOOKAH_ID = "hookah"
}