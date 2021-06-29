package com.simbirsoft.smoke

import android.app.Application
import com.simbirsoft.smoke.di.*

class App : Application() {
    lateinit var appComponent: AppComponent
    var hookahComponent: HookahComponent? = null
    var reviewComponent: ReviewComponent? = null
    var shopComponent: ShopComponent? = null
    var discountComponent: DiscountComponent? = null

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.builder().appModule(AppModule(this)).build()
        hookahComponent =
            DaggerHookahComponent.builder()
                .appComponent(appComponent)
                .hookahModule(HookahModule())
                .build()
        reviewComponent = DaggerReviewComponent.builder()
            .appComponent(appComponent)
            .reviewModule(ReviewModule())
            .build()
        shopComponent = DaggerShopComponent.builder()
            .appComponent(appComponent)
            .shopModule(ShopModule())
            .build()
        discountComponent = DaggerDiscountComponent.builder()
            .appComponent(appComponent)
            .discountModule(DiscountModule())
            .build()
    }
}