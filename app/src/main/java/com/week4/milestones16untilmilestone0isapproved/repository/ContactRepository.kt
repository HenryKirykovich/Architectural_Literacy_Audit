package com.week4.milestones16untilmilestone0isapproved.repository

import com.week4.milestones16untilmilestone0isapproved.data.Contact
import kotlinx.coroutines.flow.Flow

interface ContactRepository {
    suspend fun insertContact(contact: Contact)
    fun getAllContacts(): Flow<List<Contact>>
}