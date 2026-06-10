package com.week4.milestones16untilmilestone0isapproved

import android.app.Application
import androidx.room.Room
import com.week4.milestones16untilmilestone0isapproved.data.AppDatabase
import com.week4.milestones16untilmilestone0isapproved.repository.RoomContactRepository

/**
 * ContactApplication — custom Application for lazy initialization of the database and repository.
 * Used to provide a single global data store for the whole app.
 */
class ContactApplication : Application() {

    // Lazily create the Room database instance.
    val database by lazy {
        Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "contacts_db"
        ).build()
    }

    // Lazily create the repository using the database DAO.
    val repository by lazy {
        RoomContactRepository(database.contactDao())
    }
}