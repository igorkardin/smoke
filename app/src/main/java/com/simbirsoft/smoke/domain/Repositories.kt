package com.simbirsoft.smoke.domain

import com.simbirsoft.smoke.data.sources.FirebasePagingSource

interface Repository<T : Any, K : DataSourceParams> {
    val defaultParams: K
    fun provideDataSource(params: K = defaultParams): FirebasePagingSource<T>
}

interface AddRepository<T : Any, K : DataSourceParams> : Repository<T, K> {
    suspend fun add(t: T)

    object EmptyParams : DataSourceParams
}

class IdSelectParams(val id: String? = null) : DataSourceParams

