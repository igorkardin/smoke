package com.simbirsoft.smoke.di

import com.google.firebase.firestore.FirebaseFirestore
import com.simbirsoft.smoke.data.repositories.ReviewRepository
import com.simbirsoft.smoke.presentation.ReviewViewModel
import com.simbirsoft.smoke.ui.hookah.HookahDetailsFragment
import dagger.Component
import dagger.Module
import dagger.Provides

@Component(modules = [ReviewModule::class], dependencies = [AppComponent::class])
@LowerScope
interface ReviewComponent {
    fun inject(hookahDetails: HookahDetailsFragment)
}

@Module
class ReviewModule {
    @Provides
    @LowerScope
    fun reviewRepository(store: FirebaseFirestore) = ReviewRepository(store)

    @Provides
    @LowerScope
    fun reviewViewModelFactory(hookahRepository: ReviewRepository) = ReviewViewModel.Factory(hookahRepository)
}