package com.mutebi.stockinvestmentapp.data.remote.api

import com.mutebi.stockinvestmentapp.data.remote.dto.ApiEnvelopeDto
import com.mutebi.stockinvestmentapp.data.remote.dto.AppNotificationDto
import com.mutebi.stockinvestmentapp.data.remote.dto.NotificationListPayloadDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.Path

interface NotificationApi {
    @GET("api/v1/notifications")
    suspend fun getNotifications(): Response<ApiEnvelopeDto<NotificationListPayloadDto>>

    @PATCH("api/v1/notifications/{notificationId}/read")
    suspend fun markAsRead(
        @Path("notificationId") notificationId: Int
    ): Response<ApiEnvelopeDto<AppNotificationDto>>
}