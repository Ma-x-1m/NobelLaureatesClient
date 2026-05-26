package com.example.nobellaureatesclient.di

import com.example.nobellaureatesclient.data.repository.NobelPrizesRepositoryImpl
import com.example.nobellaureatesclient.domain.repository.NobelPrizesRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindNobelPrizesRepository(
        impl: NobelPrizesRepositoryImpl
    ): NobelPrizesRepository
}
