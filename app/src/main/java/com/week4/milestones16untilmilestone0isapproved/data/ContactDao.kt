package com.week4.milestones16untilmilestone0isapproved.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ContactDao {

    @Insert
    suspend fun insert(contact: Contact)

    @Query("SELECT * FROM contacts")
    fun getAllContacts(): Flow<List<Contact>>
}

