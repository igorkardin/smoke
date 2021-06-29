package com.simbirsoft.smoke.data

import android.util.Log
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.simbirsoft.smoke.data.repositories.HookahRepository
import com.simbirsoft.smoke.data.repositories.ReviewRepository
import com.simbirsoft.smoke.data.repositories.ShopRepository
import com.simbirsoft.smoke.domain.Hookah
import com.simbirsoft.smoke.domain.HookahRating
import com.simbirsoft.smoke.domain.Review
import com.simbirsoft.smoke.domain.Shop
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.random.Random


fun addHookah() {
    val repository = HookahRepository(Firebase.firestore)
    CoroutineScope(Dispatchers.IO).launch {
        for (i in 0..40) {
            repository.add(
                Hookah(
                    rating = HookahRating(0, 0.0),
                    name = "Hookah$i",
                    description = "Ашшалеть какой дымный",
                    price = 5000,
                    picture = "https://i.pinimg.com/originals/9b/97/dc/9b97dc4e6f89e78ad531db5f2a3ca33b.jpg"
                )
            )
            Log.e("ADDHK", "added $i")
        }
    }
}

fun addReviews() {
    val reviewRepository = ReviewRepository(Firebase.firestore)
    addThreeReviewsToHookah("0OnIXptKWkYu78BfOeN4", reviewRepository)
}


fun addThreeReviewsToHookah(hookahId: String, repository: ReviewRepository) {
    fun getReview() = Review(
        hookahId = hookahId,
        author = "Leonid",
        body = "Нормальный кальян, всем советую, не лучший, но нормально",
        rating = Random.nextLong(1, 5)
    )

    CoroutineScope(Dispatchers.IO).launch {
        for (i in 0 until 4) repository.add(getReview())
    }
}

fun addShops() {
    val dataSource = ShopRepository(Firebase.firestore)
    CoroutineScope(Dispatchers.IO).launch {
        for (i in 0 until 10) {
            val shop = Shop(
                picture = "https://chichamaps.s3.amazonaws.com/uploads/logo-hookah-shop.jpg",
                name = "Shop $i",
                longitude = 10.0,
                latitude = 20.0
            )
            dataSource.add(shop)
        }
    }
}