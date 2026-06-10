package com.week4.milestones16untilmilestone0isapproved.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.week4.milestones16untilmilestone0isapproved.repository.ContactRepository

/**
 * ContactViewModelFactory — factory for creating a `ContactViewModel` with a `ContactRepository` dependency.
 * Used to pass the repository into the ViewModel when creating it via `viewModels {}`.
 */
class ContactViewModelFactory(
    private val repository: ContactRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ContactViewModel::class.java)) {
            return ContactViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}