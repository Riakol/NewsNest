package com.riakol.data.di

import com.riakol.data.repository.NewsRepositoryImpl
import com.riakol.domain.repository.NewsRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindNewsRepository(
        impl: NewsRepositoryImpl
    ): NewsRepository
}