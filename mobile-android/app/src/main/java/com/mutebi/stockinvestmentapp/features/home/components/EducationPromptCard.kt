package com.mutebi.stockinvestmentapp.features.home.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun EducationPromptCard(
    onOpenEducation: () -> Unit,
    onOpenNotifications: () -> Unit
) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text("Responsible investing")
            Text("Learn before acting. Good investing starts with understanding risk, time horizon, and diversification.")
            Text("Beginner reminder: never invest money you cannot afford to keep invested for the long term.")

            Button(onClick = onOpenEducation) {
                Text("Open education hub")
            }

            Button(onClick = onOpenNotifications) {
                Text("Open notifications")
            }
        }
    }
}