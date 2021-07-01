package com.simbirsoft.smoke.di

import com.google.firebase.firestore.FirebaseFirestore
import com.simbirsoft.smoke.data.repositories.VideoRepository
import com.simbirsoft.smoke.presentation.VideoViewModel
import com.simbirsoft.smoke.ui.video.VideoFragment
import dagger.Component
import dagger.Module
import dagger.Provides

@LowerScope
@Component(modules = [VideoModule::class], dependencies = [AppComponent::class])
interface VideoComponent {
    fun inject(videoFragment: VideoFragment)
}

@Module
class VideoModule {
    @Provides
    @LowerScope
    fun videoRepository(store: FirebaseFirestore) = VideoRepository(store)

    @Provides
    @LowerScope
    fun videoViewModelFactory(videoRepository: VideoRepository) = VideoViewModel.Factory(videoRepository)
}