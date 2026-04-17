package com.mutebi.stockinvestmentapp.features.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun SettingsScreen(
    vm: SettingsViewModel,
    onOpenSecurity: () -> Unit,
    onOpenAccount: () -> Unit
) {
    val state by vm.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Settings")

        Text("Privacy and consent")

        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text("Privacy policy accepted")
            Switch(
                checked = state.privacyAccepted,
                onCheckedChange = vm::onPrivacyAcceptedChange
            )

            Text("Analytics consent")
            Switch(
                checked = state.analyticsConsent,
                onCheckedChange = vm::onAnalyticsConsentChange
            )

            Text("Marketing consent")
            Switch(
                checked = state.marketingConsent,
                onCheckedChange = vm::onMarketingConsentChange
            )
        }

        Button(
            onClick = onOpenSecurity,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Open security settings")
        }

        Button(
            onClick = onOpenAccount,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Open account")
        }

        state.statusMessage?.let {
            Text(it)
        }
    }
}