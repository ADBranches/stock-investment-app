package com.mutebi.stockinvestmentapp.features.notifications

import com.mutebi.stockinvestmentapp.domain.model.AppNotification

data class NotificationCenterUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val notifications: List<AppNotification> = emptyList()
)