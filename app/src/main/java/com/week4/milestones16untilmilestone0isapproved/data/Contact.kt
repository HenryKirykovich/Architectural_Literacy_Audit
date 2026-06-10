package com.week4.milestones16untilmilestone0isapproved.data

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Contact — Room entity representing a contact in the local database.
 *
 * Fields:
 * - id: primary key, auto-generated.
 * - name: contact name.
 * - phone: contact phone number.
 */
@Entity(tableName = "contacts")
data class Contact(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val name: String,
    val phone: String
)