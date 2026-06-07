# Architectural Literacy Audit: Repository + Room – Individual Assignment

**Student:** Henry Kirykovich

**Repository:** https://github.com/HenryKirykovich/Architectural_Literacy_Audit

## Introduction

This document analyzes the architecture of a Repository + Room project developed during Milestones 1–6 of the course. The purpose of this audit is to examine how the application implements the MVVM architectural pattern, a Repository layer, Room database persistence, coroutines, and reactive state management.

The project was created as part of Milestones 1–6 and later moved into a dedicated GitHub repository named Architectural_Literacy_Audit for review and evaluation.

The application follows a layered architecture consisting of:

- UI Layer (Compose UI and Activity)
- ViewModel Layer
- Repository Layer
- DAO Layer
- Room Database Layer

The goal of this analysis is not to demonstrate application features, but to explain how responsibilities are separated between layers, how data flows through the application, and how architectural decisions support maintainability, lifecycle awareness, and reactive UI behavior.

---

## Question 1 – ViewModel State Ownership

The application state is defined inside the `ContactViewModel` class:

```kotlin
private val _uiState = MutableStateFlow(ContactUiState())
val uiState: StateFlow<ContactUiState> = _uiState.asStateFlow()
```

The public state exposed to the UI is `uiState` of type `StateFlow<ContactUiState>`. This state contains the current list of contacts and the transient input fields (`name` and `phone`) being entered by the user. The `ContactUiState` data class is defined in `ContactUiState.kt`.

If this state were stored inside a Composable or directly in the Activity, the entered values could be lost during configuration changes (for example, screen rotation). The `ViewModel` survives configuration changes and continues to hold the current state. Because of this, the UI can reconnect to the existing `StateFlow` and continue displaying the correct information without losing transient user input.

## Question 2 – Repository and Database Operations

The Repository layer consists of the `ContactRepository` interface and the `RoomContactRepository` implementation. Database operations are performed through the repository methods such as:

```kotlin
override suspend fun insertContact(contact: Contact) {
    contactDao.insert(contact)
}

override fun getAllContacts(): Flow<List<Contact>> {
    return contactDao.getAllContacts()
}
```

The Repository acts as an architectural boundary between the `ViewModel` and the DAO. The `ViewModel` communicates only with the Repository and never accesses the DAO directly. In this implementation, database write operations are executed from `viewModelScope.launch { }` inside the `ViewModel`, preventing blocking work from occurring on the main UI thread.

Removing the Repository would tightly couple the presentation layer to the persistence implementation. That would make future changes—such as adding a network source—harder and would violate separation of concerns. The Repository isolates database logic from presentation logic and simplifies maintenance.

## Question 3 – DAO Functions, Flow, and Suspend Operations

The DAO contains a write operation and a reactive query:

```kotlin
@Insert
suspend fun insert(contact: Contact)

@Query("SELECT * FROM contacts")
fun getAllContacts(): Flow<List<Contact>>
```

The `insert` function is `suspend` because database write operations should not execute on the main thread. The query returns `Flow<List<Contact>>` because the UI needs to automatically react when the database changes. When a new contact is inserted, Room emits an updated list via the `Flow`.

If the query returned `List<Contact>` instead of `Flow<List<Contact>>`, the UI would receive only a single snapshot and would not automatically update when the database changed.

## Question 4 – Reactive Data Flow from DAO to Composable

The reactive data flow follows this path:

ContactDao.getAllContacts()
→ RoomContactRepository.getAllContacts()
→ ContactViewModel
→ `uiState` `StateFlow`
→ `ContactScreen`
→ `collectAsState()`
→ Compose UI

The DAO exposes `getAllContacts(): Flow<List<Contact>>`. The Repository returns that `Flow`. Inside `ContactViewModel`, the `Flow` is collected in the `init` block:

```kotlin
viewModelScope.launch {
    repository.getAllContacts().collect { contacts ->
        _uiState.update { it.copy(contacts = contacts) }
    }
}
```

The UI collects the ViewModel state with:

```kotlin
val uiState by viewModel.uiState.collectAsState()
```

Compose recomposes automatically when `uiState` changes. When a new contact is inserted in Room, Room emits a new value, the ViewModel updates the `StateFlow`, and Compose updates the screen.

## Question 5 – State That Would Reset Without ViewModel

If the `ViewModel` were removed, transient UI state would reset during configuration changes. The state stored in `ContactUiState` includes:

- `contacts: List<Contact> = emptyList()`
- `name: String = ""`
- `phone: String = ""`

The text currently entered into the Name and Phone fields would be lost if the Activity and Composables were recreated. The `ViewModel` preserves `ContactUiState` across configuration changes so that the UI can reconnect to the existing state.

## Question 6 – Function That Would Become Architecturally Incorrect in the UI Layer

The `saveContact()` function in `ContactViewModel.kt` performs validation, launches a coroutine, and invokes the Repository to insert a contact:

```kotlin
fun saveContact() {
    val current = _uiState.value
    if (current.name.isBlank() || current.phone.isBlank()) return

    viewModelScope.launch {
        repository.insertContact(
            Contact(name = current.name, phone = current.phone)
        )
        _uiState.update { it.copy(name = "", phone = "") }
    }
}
```

Moving this function into an Activity or Composable would make the UI layer responsible for database operations and coroutine management. That would violate MVVM separation of concerns. Keeping `saveContact()` in the `ViewModel` preserves lifecycle-aware coroutine usage (`viewModelScope`) and keeps persistence logic out of the UI.

## Question 7 – Architectural Weakness

One potential weakness is how the `ViewModel` currently collects the Repository `Flow` manually in the `init` block:

```kotlin
init {
    viewModelScope.launch {
        repository.getAllContacts().collect { contacts ->
            _uiState.update { it.copy(contacts = contacts) }
        }
    }
}
```

While this approach works for a small app, it creates manual state synchronization. A more scalable approach would be converting the repository `Flow` into a `StateFlow` using `stateIn()` inside the ViewModel or repository. This would reduce boilerplate and make it easier to combine multiple data sources.

## Question 8 – Annotated Code Excerpt

The following excerpt demonstrates how the `ViewModel` receives data from the Repository and updates application state using coroutines and `StateFlow`:

```kotlin
init {                                          // ViewModel Layer
    viewModelScope.launch {               // Coroutine launched in ViewModel scope
        repository.getAllContacts()               // Repository Layer
            .collect { contacts ->               // Collecting Flow data
                _uiState.update {            // State Management Layer
                    it.copy(                 // Creating updated state object
                        contacts = contacts      // Updating contact list
                    )
                }
            }
    }
}

fun saveContact() {                              // ViewModel Business Logic
    val current = _uiState.value                // Reading current UI state

    if (current.name.isBlank() || current.phone.isBlank())
        return                                    // Validation Logic

    viewModelScope.launch {                       // Coroutine execution

        repository.insertContact(                 // Repository Layer
            Contact(                              // Entity Creation
                name = current.name,
                phone = current.phone
            )
        )

        _uiState.update {                         // State Update
            it.copy(
                name = "",                        // Clear input field
                phone = ""                        // Clear input field
            )
        }
    }
}
```

This excerpt shows several architectural responsibilities: the `ViewModel` owns application state and performs validation; coroutines are launched in `viewModelScope` (lifecycle-aware); database operations go through the Repository; and `StateFlow` is used so the UI reacts to data changes. These responsibilities follow MVVM architecture and support maintainability and reactive UI behavior.

---

If you want, I can also:

- add more inline comments to other files (for example `RoomContactRepository.kt` or `ContactDao.kt`),
- run the Gradle build or unit tests locally, or
- create a short README linking this audit to the repo.

