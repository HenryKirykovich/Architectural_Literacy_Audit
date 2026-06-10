package com.week4.milestones16untilmilestone0isapproved.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

/**
 * ContactDao — Data Access Object (DAO) for the `contacts` table.
 *
 * Provides basic operations:
 * - insert contact (`suspend`) — runs in a coroutine;
 * - reactive query for all contacts (`Flow<List<Contact>>`) — enables the UI to react to changes automatically.
 */
@Dao
interface ContactDao {

    // Inserts a new contact into the database (suspend — does not block the main thread).
    @Insert
    suspend fun insert(contact: Contact)

    // Returns a Flow of contact lists — Room emits updates automatically when data changes.
    @Query("SELECT * FROM contacts")
    fun getAllContacts(): Flow<List<Contact>>
}

