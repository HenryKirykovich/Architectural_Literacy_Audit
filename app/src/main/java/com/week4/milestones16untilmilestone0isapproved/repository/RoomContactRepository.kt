package com.week4.milestones16untilmilestone0isapproved.repository

import com.week4.milestones16untilmilestone0isapproved.data.Contact
import com.week4.milestones16untilmilestone0isapproved.data.ContactDao
import kotlinx.coroutines.flow.Flow

/**
 * RoomContactRepository — concrete `ContactRepository` implementation that uses a Room DAO.
 * Delegates repository operations to the `ContactDao`.
 */
class RoomContactRepository(
    private val contactDao: ContactDao
) : ContactRepository {

    // Delegate the contact insertion to the DAO.
    override suspend fun insertContact(contact: Contact) {
        contactDao.insert(contact)
    }

    // Return the contact Flow from the DAO.
    override fun getAllContacts(): Flow<List<Contact>> {
        return contactDao.getAllContacts()
    }
}