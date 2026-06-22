package com.gde.university.data.di

import com.gde.university.data.datasource.local.UniversityLocalDataSource
import com.gde.university.data.datasource.local.UniversityLocalDataSourceImpl
import com.gde.university.data.datasource.remote.UniversityRemoteDataSource
import com.gde.university.data.datasource.remote.UniversityRemoteDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DataSourceModule {

    @Binds
    @Singleton
    abstract fun bindUniversityRemoteDataSource(
        impl: UniversityRemoteDataSourceImpl
    ): UniversityRemoteDataSource

    @Binds
    @Singleton
    abstract fun bindUniversityLocalDataSource(
        impl: UniversityLocalDataSourceImpl
    ): UniversityLocalDataSource
}
