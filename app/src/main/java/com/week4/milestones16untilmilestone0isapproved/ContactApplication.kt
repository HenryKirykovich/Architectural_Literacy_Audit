package com.week4.milestones16untilmilestone0isapproved

import android.app.Application
import androidx.room.Room
import com.week4.milestones16untilmilestone0isapproved.data.AppDatabase
import com.week4.milestones16untilmilestone0isapproved.repository.RoomContactRepository

class ContactApplication : Application() {

    val database by lazy {
        Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "contacts_db"
        ).build()
    }

    val repository by lazy {
        RoomContactRepository(database.contactDao())
    }
}