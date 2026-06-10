package com.week4.milestones16untilmilestone0isapproved.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.week4.milestones16untilmilestone0isapproved.data.Contact
import com.week4.milestones16untilmilestone0isapproved.repository.ContactRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * ContactViewModel — owns UI state and interacts with the repository.
 *
 * Responsibilities:
 * - Store and expose state via `StateFlow` (`_uiState` / `uiState`).
 * - Subscribe to the contacts Flow from `ContactRepository` inside `viewModelScope` (lifecycle-aware).
 * - Perform simple validation and delegate saving to the repository.
 */
class ContactViewModel(
    private val repository: ContactRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ContactUiState())
    val uiState: StateFlow<ContactUiState> = _uiState.asStateFlow()

    init {
        // Launch a coroutine in viewModelScope — automatically cancelled when the ViewModel is cleared.
        viewModelScope.launch {
            // Collect the Flow of contacts from the repository.
            // Any updates will be delivered here and reflected in the UI via the StateFlow.
            repository.getAllContacts().collect { contacts ->
                // Atomically update the state with the new contacts list.
                _uiState.update { it.copy(contacts = contacts) }
            }
        }
    }

    fun onNameChange(name: String) {
        // Update the name field in the UI state when the input changes.
        _uiState.update { it.copy(name = name) }
    }

    fun onPhoneChange(phone: String) {
        // Update the phone field in the UI state when the input changes.
        _uiState.update { it.copy(phone = phone) }
    }

    fun saveContact() {
        val current = _uiState.value
        // Simple validation: both fields must be non-blank.
        if (current.name.isBlank() || current.phone.isBlank()) return

        // Perform the insert operation in a coroutine on viewModelScope (asynchronously).
        viewModelScope.launch {
            repository.insertContact(
                Contact(
                    name = current.name,
                    phone = current.phone
                )
            )

            // After successful insert, clear the input fields in the UI state.
            _uiState.update { it.copy(name = "", phone = "") }
        }
    }
}