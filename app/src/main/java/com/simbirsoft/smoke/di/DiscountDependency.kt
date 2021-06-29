package com.simbirsoft.smoke.di

import com.google.firebase.firestore.FirebaseFirestore
import com.simbirsoft.smoke.data.repositories.DiscountRepository
import com.simbirsoft.smoke.presentation.DiscountViewModel
import com.simbirsoft.smoke.ui.shops.ShopDetailsFragment
import dagger.Component
import dagger.Module
import dagger.Provides

@Component(modules = [DiscountModule::class], dependencies = [AppComponent::class])
@LowerScope
interface DiscountComponent {
    fun inject(shopsFragment: ShopDetailsFragment)
}

@Module
class DiscountModule {
    @Provides
    @LowerScope
    fun discountDepository(store: FirebaseFirestore) = DiscountRepository(store)

    @Provides
    @LowerScope
    fun discountViewModelFactory(discountRepository: DiscountRepository) = DiscountViewModel.Factory(discountRepository)
}