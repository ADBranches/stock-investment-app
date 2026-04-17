package com.mutebi.stockinvestmentapp.features.account

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ChangePasswordScreen(
    vm: AccountViewModel,
    onBack: () -> Unit
) {
    val state by vm.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        TextButton(onClick = onBack) {
            Text("Back")
        }

        Text("Change password")

        OutlinedTextField(
            value = state.currentPassword,
            onValueChange = vm::onCurrentPasswordChange,
            label = { Text("Current password") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        OutlinedTextField(
            value = state.newPassword,
            onValueChange = vm::onNewPasswordChange,
            label = { Text("New password") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        OutlinedTextField(
            value = state.confirmNewPassword,
            onValueChange = vm::onConfirmNewPasswordChange,
            label = { Text("Confirm new password") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Button(
            onClick = vm::changePassword,
            modifier = Modifier.fillMaxWidth(),
            enabled = !state.isSaving
        ) {
            if (state.isSaving) {
                CircularProgressIndicator()
            } else {
                Text("Update password")
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