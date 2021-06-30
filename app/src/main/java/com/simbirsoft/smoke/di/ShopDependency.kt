package com.simbirsoft.smoke.di

import com.google.firebase.firestore.FirebaseFirestore
import com.simbirsoft.smoke.data.repositories.ShopRepository
import com.simbirsoft.smoke.presentation.ShopViewModel
import com.simbirsoft.smoke.ui.shops.ShopsFragment
import dagger.Component
import dagger.Module
import dagger.Provides

@Component(modules = [ShopModule::class], dependencies = [AppComponent::class])
@LowerScope
interface ShopComponent {
    fun inject(shopsFragment: ShopsFragment)
}

@Module
class ShopModule {
    @Provides
    @LowerScope
    fun shopRepository(store: FirebaseFirestore) = ShopRepository(store)

    @Provides
    @LowerScope
    fun shopViewModelFactory(shopRepository: ShopRepository) = ShopViewModel.Factory(shopRepository)
}