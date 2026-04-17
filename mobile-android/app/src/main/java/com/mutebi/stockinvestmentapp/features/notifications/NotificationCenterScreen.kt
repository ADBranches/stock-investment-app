package com.mutebi.stockinvestmentapp.features.notifications

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun NotificationCenterScreen(
    onBack: () -> Unit,
    vm: NotificationCenterViewModel = viewModel()
) {
    val state by vm.uiState.collectAsState()

    LazyColumn(
        contentPadding = PaddingValues(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            TextButton(onClick = onBack) {
                Text("Back")
            }
        }

        item {
            Text("Notification center")
        }

        if (state.isLoading) {
            item {
                CircularProgressIndicator()
            }
        } else if (state.notifications.isEmpty()) {
            item {
                Text("No notifications yet.")
            }
        } else {
            items(state.notifications, key = { it.id }) { notification ->
                androidx.compose.material3.Card {
                    androidx.compose.foundation.layout.Column(
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(notification.title)
                        Text(notification.body)
                        Text("Type: ${notification.notificationType}")
                        Text("Created: ${notification.createdAt}")
                        Text(if (notification.isRead) "Read" else "Unread")

                        if (!notification.isRead) {
                            Button(onClick = { vm.markAsRead(notification.id) }) {
                                Text("Mark as read")
                            }
                        }
                    }
                }
            }
        }

        state.error?.let { errorMessage ->
            item {
                Text(errorMessage)
            }
        }
    }
}