package com.mutebi.stockinvestmentapp.features.profile.setup

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mutebi.stockinvestmentapp.features.auth.components.AuthHeader
import com.mutebi.stockinvestmentapp.features.profile.setup.components.ProfileAvatarPicker
import com.mutebi.stockinvestmentapp.features.profile.setup.components.ProfileForm

@Composable
fun ProfileSetupScreen(
    onProfileSaved: () -> Unit,
    vm: ProfileSetupViewModel = viewModel()
) {
    val state by vm.uiState.collectAsState()

    LaunchedEffect(state.success) {
        if (state.success) onProfileSaved()
    }

    LaunchedEffect(Unit) {
        vm.loadProfile()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center
    ) {
        AuthHeader(
            title = "Complete your profile",
            subtitle = "Tell us a bit more about yourself"
        )

        ProfileAvatarPicker()

        Spacer(modifier = Modifier.height(16.dp))

        ProfileForm(
            fullName = state.fullName,
            phoneNumber = state.phoneNumber,
            country = state.country,
            dateOfBirth = state.dateOfBirth,
            onFullNameChange = vm::onFullNameChange,
            onPhoneNumberChange = vm::onPhoneNumberChange,
            onCountryChange = vm::onCountryChange,
            onDateOfBirthChange = vm::onDateOfBirthChange
        )

        Spacer(modifier = Modifier.height(12.dp))

        state.error?.let {
            Text(text = it)
            Spacer(modifier = Modifier.height(8.dp))
        }

        Button(
            onClick = vm::submit,
            enabled = !state.isLoading,
            modifier = Modifier.fillMaxWidth()
        ) {
            if (state.isLoading) {
                CircularProgressIndicator(modifier = Modifier.height(18.dp))
            } else {
                Text("Save profile")
            }
        }
    }
}