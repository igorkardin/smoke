package com.simbirsoft.smoke.di

import com.google.firebase.firestore.FirebaseFirestore
import com.simbirsoft.smoke.data.repositories.HookahRepository
import com.simbirsoft.smoke.presentation.HookahViewModel
import com.simbirsoft.smoke.ui.hookah.HookahFragment
import com.simbirsoft.smoke.ui.shops.ShopsFragment
import dagger.Component
import dagger.Module
import dagger.Provides

@Component(modules = [HookahModule::class], dependencies = [AppComponent::class])
@LowerScope
interface HookahComponent {
    fun inject(hookahFragment: HookahFragment)
}

@Module
class HookahModule {
    @Provides
    @LowerScope
    fun hookahRepository(store: FirebaseFirestore) = HookahRepository(store)

    @Provides
    @LowerScope
    fun hookahViewModelFactory(hookahRepository: HookahRepository) = HookahViewModel.Factory(hookahRepository)
}