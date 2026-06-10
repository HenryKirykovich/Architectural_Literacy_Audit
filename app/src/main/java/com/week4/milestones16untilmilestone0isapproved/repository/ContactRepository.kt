package com.week4.milestones16untilmilestone0isapproved.repository

import com.week4.milestones16untilmilestone0isapproved.data.Contact
import kotlinx.coroutines.flow.Flow

/**
 * ContactRepository — repository layer abstraction.
 * The ViewModel interacts with the repository instead of the DAO,
 * which simplifies testing and allows swapping data sources (for example, adding a network source).
 */
interface ContactRepository {
    // Inserts a contact into storage (implementation may be synchronous or asynchronous).
    suspend fun insertContact(contact: Contact)

    // Returns a Flow of contacts for reactive UI updates.
    fun getAllContacts(): Flow<List<Contact>>
}