package com.mutebi.stockinvestmentapp.features.profile.setup.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ProfileAvatarPicker() {
    Surface(
        modifier = Modifier.size(88.dp),
        shape = CircleShape
    ) {
        Box(contentAlignment = Alignment.Center) {
            Text("Avatar")
        }
    }
}