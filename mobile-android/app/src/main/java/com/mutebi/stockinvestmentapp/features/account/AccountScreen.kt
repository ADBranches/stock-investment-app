package com.mutebi.stockinvestmentapp.features.account

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun AccountScreen(
    vm: AccountViewModel,
    onBack: () -> Unit,
    onEditProfile: () -> Unit,
    onChangePassword: () -> Unit,
    onLoggedOut: () -> Unit
) {
    val state by vm.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        TextButton(onClick = onBack) {
            Text("Back")
        }

        Text("Account")

        if (state.isLoading) {
            CircularProgressIndicator()
        } else {
            Text("Email: ${state.email}")
            Text("Full name: ${state.fullName}")
            Text("Phone: ${state.phoneNumber}")
            Text("Country: ${state.country}")
            Text("Date of birth: ${state.dateOfBirth}")
            Text("Profile completed: ${state.profileCompleted}")
            Text("KYC status: ${state.kycStatus}")

            Button(
                onClick = onEditProfile,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Edit profile")
            }

            Button(
                onClick = onChangePassword,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Change password")
            }

            Button(
                onClick = {
                    vm.logout()
                    onLoggedOut()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Log out")
            }
        }

        state.successMessage?.let {
            Text(it)
        }

        state.error?.let {
            Text(it)
        }
    }
}