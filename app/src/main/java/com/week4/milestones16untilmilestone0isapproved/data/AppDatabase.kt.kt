package com.week4.milestones16untilmilestone0isapproved.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [Contact::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun contactDao(): ContactDao
}