package com.week4.milestones16untilmilestone0isapproved

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.week4.milestones16untilmilestone0isapproved.presentation.ContactViewModel
import com.week4.milestones16untilmilestone0isapproved.presentation.ContactViewModelFactory
import com.week4.milestones16untilmilestone0isapproved.ui.screen.ContactScreen

class MainActivity : ComponentActivity() {

    private val viewModel: ContactViewModel by viewModels {
        ContactViewModelFactory((application as ContactApplication).repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ContactScreen(viewModel = viewModel)
        }
    }
}