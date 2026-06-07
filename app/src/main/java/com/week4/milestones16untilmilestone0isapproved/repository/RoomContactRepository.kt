package com.week4.milestones16untilmilestone0isapproved.repository

import com.week4.milestones16untilmilestone0isapproved.data.Contact
import com.week4.milestones16untilmilestone0isapproved.data.ContactDao
import kotlinx.coroutines.flow.Flow

class RoomContactRepository(
    private val contactDao: ContactDao
) : ContactRepository {

    override suspend fun insertContact(contact: Contact) {
        contactDao.insert(contact)
    }

    override fun getAllContacts(): Flow<List<Contact>> {
        return contactDao.getAllContacts()
    }
}