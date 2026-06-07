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
 * ContactViewModel — владеет UI-состоянием и взаимодействует с репозиторием.
 *
 * Ответственности:
 * - Хранение и публикация состояния через `StateFlow` (`_uiState` / `uiState`).
 * - Подписка на Flow контактов из `ContactRepository` внутри `viewModelScope` (lifecycle-aware).
 * - Простая валидация и делегирование сохранения в репозиторий.
 */
class ContactViewModel(
    private val repository: ContactRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ContactUiState())
    val uiState: StateFlow<ContactUiState> = _uiState.asStateFlow()

    init {
        // Запускаем корутину в viewModelScope — автоматически отменяется при уничтожении ViewModel.
        viewModelScope.launch {
            // Получаем Flow списка контактов из репозитория и собираем его.
            // Любые обновления списка будут поступать сюда и отражаться в UI через StateFlow.
            repository.getAllContacts().collect { contacts ->
                // Атомарно обновляем состояние, подставляя новый список контактов.
                _uiState.update { it.copy(contacts = contacts) }
            }
        }
    }

    fun onNameChange(name: String) {
        // Обновляем поле имени в UI-состоянии при изменении ввода.
        _uiState.update { it.copy(name = name) }
    }

    fun onPhoneChange(phone: String) {
        // Обновляем поле телефона в UI-состоянии при изменении ввода.
        _uiState.update { it.copy(phone = phone) }
    }

    fun saveContact() {
        val current = _uiState.value
        // Простая валидация: оба поля должны быть заполнены.
        if (current.name.isBlank() || current.phone.isBlank()) return

        // Выполнение операции вставки в БД в корутине viewModelScope (асинхронно).
        viewModelScope.launch {
            repository.insertContact(
                Contact(
                    name = current.name,
                    phone = current.phone
                )
            )

            // После успешной вставки очищаем поля ввода в UI-состоянии.
            _uiState.update { it.copy(name = "", phone = "") }
        }
    }
}