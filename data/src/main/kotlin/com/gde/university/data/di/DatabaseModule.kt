package com.gde.university.data.di

import android.content.Context
import androidx.room.Room
import com.gde.university.data.database.DatabaseConstant.DATABASE_NAME
import com.gde.university.data.database.UniversityDao
import com.gde.university.data.database.UniversityDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): UniversityDatabase {
        return Room.databaseBuilder(
            context,
            UniversityDatabase::class.java,
            DATABASE_NAME
        ).build()
    }

    @Provides
    @Singleton
    fun provideUniversityDao(database: UniversityDatabase): UniversityDao {
        return database.universityDao()
    }
}
