package com.week4.milestones16untilmilestone0isapproved.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "contacts")
data class Contact(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val name: String,
    val phone: String
)