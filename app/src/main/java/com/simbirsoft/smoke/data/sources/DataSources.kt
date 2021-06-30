package com.simbirsoft.smoke.data.sources

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.simbirsoft.smoke.data.*
import com.simbirsoft.smoke.domain.*

const val HOOKAH_COLLECTION = "hookah"
const val REVIEW_COLLECTION = "review"
const val SHOPS_COLLECTION = "shops"
const val DISCOUNT_COLLECTION = "discounts"
const val VIDEO_COLLECTION = "videos"

class HookahPagingSource(val store: FirebaseFirestore) : FirebasePagingSource<Hookah>() {
    override val mapper: (DocumentSnapshot) -> Hookah = { it.toHookah() }
    override val collectionReference: CollectionReference = store.collection(HOOKAH_COLLECTION)
}

class ShopPagingSource(val store: FirebaseFirestore) : FirebasePagingSource<Shop>() {
    override val mapper: (DocumentSnapshot) -> Shop = { it.toShop() }
    override val collectionReference: CollectionReference = store.collection(SHOPS_COLLECTION)
}

class VideoPagingSource(val store: FirebaseFirestore) : FirebasePagingSource<Video>() {
    override val mapper: (DocumentSnapshot) -> Video = { it.toVideo() }
    override val collectionReference: CollectionReference = store.collection(VIDEO_COLLECTION)
}

class ReviewPagingSource(
    val store: FirebaseFirestore,
    selectId: String? = null
) : ChildPagingSource<Review>(selectId) {
    override val parentField: String = "hookah"
    override val collectionReference: CollectionReference = store.collection(REVIEW_COLLECTION)
    override val mapper: (DocumentSnapshot) -> Review = { it.toReview() }
}

class DiscountPagingSource(
    val store: FirebaseFirestore,
    selectId: String? = null,
) : ChildPagingSource<Discount>(selectId) {
    override val parentField: String = "shop"
    override val mapper: (DocumentSnapshot) -> Discount = { it.toDiscount() }
    override val collectionReference: CollectionReference = store.collection(DISCOUNT_COLLECTION)

}

fun Hookah.updateRating(newReview: Review): Hookah {
    val newAvg = (rating.average * rating.count + newReview.rating) / (rating.count + 1)
    return copy(rating = HookahRating(rating.count + 1, newAvg))
}