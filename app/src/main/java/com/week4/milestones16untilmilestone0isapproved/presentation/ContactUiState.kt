package com.week4.milestones16untilmilestone0isapproved.presentation

import com.week4.milestones16untilmilestone0isapproved.data.Contact

/**
 * ContactUiState — UI state model for the contacts screen.
 * Contains the list of contacts and current input field values (`name`, `phone`).
 */
data class ContactUiState(
    val contacts: List<Contact> = emptyList(),
    val name: String = "",
    val phone: String = ""
)