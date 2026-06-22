package com.gde.university.data.di

import com.gde.university.data.repository.UniversityRepositoryImpl
import com.gde.university.domain.repository.UniversityRepository
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
    abstract fun bindUniversityRepository(
        universityRepositoryImpl: UniversityRepositoryImpl
    ): UniversityRepository
}
