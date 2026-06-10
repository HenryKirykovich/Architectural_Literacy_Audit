package com.week4.milestones16untilmilestone0isapproved.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.week4.milestones16untilmilestone0isapproved.presentation.ContactViewModel

/**
 * ContactScreen — Composable that displays the contact list and input fields.
 *
 * The UI subscribes to `viewModel.uiState` via `collectAsState()` and reacts to changes.
 * All user actions (field changes, save) are delegated to the ViewModel.
 */
@Composable
fun ContactScreen(viewModel: ContactViewModel) {
    // Subscribe to the ViewModel state — Compose will recompose the UI automatically on changes.
    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("Contacts", style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(16.dp))

        // Name input field — updates are delegated to the ViewModel via onNameChange.
        OutlinedTextField(
            value = uiState.name,
            onValueChange = viewModel::onNameChange,
            label = { Text("Name") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Phone input field — updates are delegated to the ViewModel via onPhoneChange.
        OutlinedTextField(
            value = uiState.phone,
            onValueChange = viewModel::onPhoneChange,
            label = { Text("Phone") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Save button calls saveContact() on the ViewModel.
        Button(
            onClick = viewModel::saveContact,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Save")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Contact list — render each contact as simple text.
        LazyColumn {
            items(uiState.contacts) { contact ->
                Text("${contact.name} — ${contact.phone}")
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}