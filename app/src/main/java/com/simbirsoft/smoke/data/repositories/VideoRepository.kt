package com.simbirsoft.smoke.data.repositories

import com.google.firebase.firestore.FirebaseFirestore
import com.simbirsoft.smoke.data.sources.VideoPagingSource
import com.simbirsoft.smoke.domain.AddRepository
import com.simbirsoft.smoke.domain.Repository
import com.simbirsoft.smoke.domain.Video

class VideoRepository(private val store: FirebaseFirestore) : Repository<Video, AddRepository.EmptyParams> {
    override val defaultParams: AddRepository.EmptyParams = AddRepository.EmptyParams
    override fun provideDataSource(params: AddRepository.EmptyParams) = VideoPagingSource(store)
}