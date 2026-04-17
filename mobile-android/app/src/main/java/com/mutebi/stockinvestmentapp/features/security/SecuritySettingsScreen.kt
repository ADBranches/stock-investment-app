package com.mutebi.stockinvestmentapp.features.security

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.fragment.app.FragmentActivity

@Composable
fun SecuritySettingsScreen(
    vm: SecuritySettingsViewModel,
    onBack: () -> Unit,
    onOpenChangePassword: () -> Unit
) {
    val state by vm.uiState.collectAsState()

    val activity = androidx.compose.ui.platform.LocalContext.current as FragmentActivity
    val promptManager = BiometricPromptManager(activity)

    LaunchedEffect(Unit) {
        vm.setBiometricAvailable(promptManager.isAvailable())
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        TextButton(onClick = onBack) {
            Text("Back")
        }

        Text("Security settings")

        Text(
            if (state.biometricAvailable) {
                "Biometric authentication is available on this device."
            } else {
                "Biometric authentication is not available on this device."
            }
        )

        Button(
            onClick = {
                if (state.biometricEnabled) {
                    vm.setBiometricEnabled(false)
                } else {
                    promptManager.authenticateForEnablement(
                        onSuccess = { vm.setBiometricEnabled(true) },
                        onError = { vm.setBiometricAvailable(promptManager.isAvailable()) }
                    )
                }
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = state.biometricAvailable
        ) {
            Text(
                if (state.biometricEnabled) "Disable biometric lock" else "Enable biometric lock"
            )
        }

        Button(
            onClick = onOpenChangePassword,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Change password")
        }

        Button(
            onClick = vm::revokeOtherSessions,
            modifier = Modifier.fillMaxWidth(),
            enabled = !state.isBusy
        ) {
            Text("Revoke other sessions")
        }

        state.statusMessage?.let {
            Text(it)
        }

        state.error?.let {
            Text(it)
        }
    }
}