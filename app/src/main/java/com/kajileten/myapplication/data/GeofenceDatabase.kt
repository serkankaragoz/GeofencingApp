package com.kajileten.myapplication.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters

@Database(
    entities = [GeofenceEntity::class],
    version = 1,
    exportSchema = false

)
@TypeConverters(Converters::class)
abstract class GeofenceDatabase : RoomDatabase() {

    abstract fun geofenceDao(): GeofenceDao
}