package com.mutebi.stockinvestmentapp.features.kyc

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mutebi.stockinvestmentapp.features.auth.components.AuthHeader

@Composable
fun KycIntroScreen(
    onStartKyc: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center
    ) {
        AuthHeader(
            title = "Verify your identity",
            subtitle = "KYC helps secure your account and unlock features"
        )

        Button(onClick = onStart) {
            Text("Start KYC")
        }
    }
}