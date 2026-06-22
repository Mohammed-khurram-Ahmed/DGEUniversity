package com.gde.university.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.gde.university.data.database.DatabaseConstant.DATABASE_VERSION

@Database(entities = [UniversityEntity::class], version = DATABASE_VERSION, exportSchema = false)
@TypeConverters(Converters::class)
abstract class UniversityDatabase : RoomDatabase() {
    abstract fun universityDao(): UniversityDao
}
