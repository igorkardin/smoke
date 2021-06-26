package com.simbirsoft.smoke.di

import com.google.firebase.firestore.FirebaseFirestore
import com.simbirsoft.smoke.data.HookahPagingSource
import com.simbirsoft.smoke.presentation.HookahViewModel
import com.simbirsoft.smoke.ui.hookah.HookahFragment
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
    fun hookahSource(firestore: FirebaseFirestore) = HookahPagingSource(firestore)

    @Provides
    @LowerScope
    fun hookahViewModelFactory(hookahPagingSource: HookahPagingSource) = HookahViewModel.Factory(hookahPagingSource)
}