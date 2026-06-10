package com.week4.milestones16untilmilestone0isapproved.data

import androidx.room.Database
import androidx.room.RoomDatabase

/**
 * AppDatabase — abstract Room database for the app.
 * Declares entities and the schema version.
 */
@Database(
    entities = [Contact::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    // Returns the DAO for working with contacts.
    abstract fun contactDao(): ContactDao
}