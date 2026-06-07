package com.week4.milestones16untilmilestone0isapproved.presentation

import com.week4.milestones16untilmilestone0isapproved.data.Contact

data class ContactUiState(
    val contacts: List<Contact> = emptyList(),
    val name: String = "",
    val phone: String = ""
)