package com.mutebi.stockinvestmentapp.data.remote.dto

import com.google.gson.annotations.SerializedName
import com.mutebi.stockinvestmentapp.domain.model.AppNotification

data class NotificationListPayloadDto(
    @SerializedName("items")
    val items: List<AppNotificationDto> = emptyList()
)

data class AppNotificationDto(
    @SerializedName("id")
    val id: Int = 0,
    @SerializedName("title")
    val title: String = "",
    @SerializedName("body")
    val body: String = "",
    @SerializedName("notification_type")
    val notificationType: String = "",
    @SerializedName("is_read")
    val isRead: Boolean = false,
    @SerializedName("created_at")
    val createdAt: String = ""
) {
    fun toDomain(): AppNotification {
        return AppNotification(
            id = id,
            title = title,
            body = body,
            notificationType = notificationType,
            isRead = isRead,
            createdAt = createdAt
        )
    }
}