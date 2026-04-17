package com.mutebi.stockinvestmentapp.features.kyc

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mutebi.stockinvestmentapp.features.auth.components.AuthHeader

@Composable
fun KycPersonalInfoScreen(
    onNext: () -> Unit,
    vm: KycViewModel
) {
    val state by vm.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center
    ) {
        AuthHeader(
            title = "Personal information",
            subtitle = "Enter your legal identity details"
        )

        OutlinedTextField(
            value = state.firstName,
            onValueChange = vm::onFirstNameChange,
            modifier = Modifier.fillMaxWidth(),
            label = { Text("First name") },
            singleLine = true
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = state.lastName,
            onValueChange = vm::onLastNameChange,
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Last name") },
            singleLine = true
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = state.nationalIdNumber,
            onValueChange = vm::onNationalIdNumberChange,
            modifier = Modifier.fillMaxWidth(),
            label = { Text("National ID number") },
            singleLine = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = onNext, modifier = Modifier.fillMaxWidth()) {
            Text("Next")
        }
    }
}  