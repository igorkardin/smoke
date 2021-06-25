package com.simbirsoft.smoke.data

import android.util.Log
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.simbirsoft.smoke.domain.Hookah
import com.simbirsoft.smoke.domain.HookahRating
import com.simbirsoft.smoke.domain.Review
import com.simbirsoft.smoke.domain.Shop
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlin.random.Random


fun addHookah() {
    val hookahSource = HookahPagingSource(Firebase.firestore)
    CoroutineScope(Dispatchers.IO).launch {
        for (i in 0..40) {
            hookahSource.addHookah(
                Hookah(
                    rating = HookahRating(0, 0.0),
                    name = "Hookah$i",
                    description = "Ашшалеть какой дымный",
                    price = 5000,
                    picture = "https://i.pinimg.com/originals/9b/97/dc/9b97dc4e6f89e78ad531db5f2a3ca33b.jpg"
                )
            ).collect {
                Log.e("ADDHK", "added $i")
            }
        }
    }
}

fun addReviews() {
    val reviewPagingSource = ReviewPagingSource(Firebase.firestore)
    addThreeReviewsToHookah(
        Hookah(
            "0OnIXptKWkYu78BfOeN4", HookahRating(0, 0.0), "HookahName",
            "https://shutniki.club/wp-content/uploads/2020/04/smeshnye_kartinki_kalyan_6_27153457.jpg",
            10000,
            "Супер дымный с экстра сырным"
        ),
        reviewPagingSource
    )
}


fun addThreeReviewsToHookah(hookah: Hookah, pagingSource: ReviewPagingSource) {
    fun getReview() = Review(
        hookahId = hookah.id,
        author = "Leonid",
        body = "Нормальный кальян, всем советую, не лучший, но нормально",
        rating = Random.nextLong(1, 5)
    )

    var updatedHookah = hookah
    CoroutineScope(Dispatchers.IO).launch {
        for (i in 0 until 4) {
            val review = getReview()
            pagingSource.addReviewToHookah(
                updatedHookah,
                review
            ).collect {
                Log.e("ADDHK", "added $i")
            }
            updatedHookah = updatedHookah.updateRating(review)
        }
    }
}

fun addShops() {
    val dataSource = ShopPagingSource(Firebase.firestore)
    CoroutineScope(Dispatchers.IO).launch {
        for (i in 0 until 10) {
            val shop = Shop(
                picture = "https://chichamaps.s3.amazonaws.com/uploads/logo-hookah-shop.jpg",
                name = "Shop $i",
                longitude = 10.0,
                latitude = 20.0
            )
            dataSource.addShop(shop).collect()
        }
    }
}