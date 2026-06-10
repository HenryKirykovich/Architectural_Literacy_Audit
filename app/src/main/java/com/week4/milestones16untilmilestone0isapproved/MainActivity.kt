package com.week4.milestones16untilmilestone0isapproved

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.week4.milestones16untilmilestone0isapproved.presentation.ContactViewModel
import com.week4.milestones16untilmilestone0isapproved.presentation.ContactViewModelFactory
import com.week4.milestones16untilmilestone0isapproved.ui.screen.ContactScreen

/**
 * MainActivity — the application's entry point.
 * Responsible for initializing the UI and creating the ViewModel.
 */
class MainActivity : ComponentActivity() {

    /**
     * The ViewModel instance provided by the factory.
     * Connects the application to the repository via `ContactApplication`.
     */
    private val viewModel: ContactViewModel by viewModels {
        ContactViewModelFactory((application as ContactApplication).repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Set the Compose content and pass the ViewModel to the screen.
        setContent {
            ContactScreen(viewModel = viewModel)
        }
    }
}