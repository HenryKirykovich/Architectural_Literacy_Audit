## Question 8 – Annotated Code Excerpt

Ниже приведён аннотированный фрагмент кода из `ContactViewModel.kt` и краткий анализ архитектурных обязанностей.

```kotlin
init {                                                // ViewModel Layer
    viewModelScope.launch {                           // Coroutine launched in ViewModel scope
        repository.getAllContacts()                   // Repository Layer
            .collect { contacts ->                    // Collecting Flow data
                _uiState.update {                     // State Management Layer
                    it.copy(                          // Creating updated state object
                        contacts = contacts           // Updating contact list
                    )
                }
            }
    }
}

fun saveContact() {                                   // ViewModel Business Logic
    val current = _uiState.value                      // Reading current UI state

    if (current.name.isBlank() || current.phone.isBlank())
        return                                        // Validation Logic

    viewModelScope.launch {                           // Coroutine execution

        repository.insertContact(                     // Repository Layer
            Contact(                                  // Entity Creation
                name = current.name,
                phone = current.phone
            )
        )

        _uiState.update {                             // State Update
            it.copy(
                name = "",                            // Clear input field
                phone = ""                            // Clear input field
            )
        }
    }
}
```

Анализ (кратко):

- **Владелец состояния:** `ContactViewModel` владеет UI-состоянием (`MutableStateFlow`), предоставляет `StateFlow` для UI.
- **Lifecycle-aware корутины:** все асинхронные операции запускаются в `viewModelScope`, поэтому они отменяются при уничтожении ViewModel.
- **Слой репозитория:** операции с БД/персистентностью выполняются через `ContactRepository`, а не напрямую через DAO.
- **Реактивное обновление UI:** StateFlow используется для хранения состояния, и UI автоматически реагирует на его изменения.
- **Валидация во ViewModel:** простая проверка полей выполняется на уровне ViewModel перед сохранением.

Push target (git remote):

```
git remote add origin https://github.com/HenryKirykovich/Architectural_Literacy_Audit.git
```

Файл создан для домашней работы — содержит аннотированный фрагмент и краткий архитектурный анализ.
