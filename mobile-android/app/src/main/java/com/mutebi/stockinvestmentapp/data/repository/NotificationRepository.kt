package com.mutebi.stockinvestmentapp.data.repository

import com.mutebi.stockinvestmentapp.core.utils.Resource
import com.mutebi.stockinvestmentapp.data.remote.api.NotificationApi
import com.mutebi.stockinvestmentapp.domain.model.AppNotification
import javax.inject.Inject

class NotificationRepository @Inject constructor(
    private val api: NotificationApi
) {
    suspend fun getNotifications(): Resource<List<AppNotification>> {
        return try {
            val response = api.getNotifications()
            val body = response.body()
            val items = body?.data?.items.orEmpty().map { it.toDomain() }

            if (response.isSuccessful && body?.success == true) {
                Resource.Success(items)
            } else {
                Resource.Error(body?.message ?: "Unable to load notifications")
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Unable to load notifications")
        }
    }

    suspend fun markAsRead(notificationId: Int): Resource<AppNotification> {
        return try {
            val response = api.markAsRead(notificationId)
            val body = response.body()
            val dto = body?.data

            if (response.isSuccessful && body?.success == true && dto != null) {
                Resource.Success(dto.toDomain())
            } else {
                Resource.Error(body?.message ?: "Unable to update notification")
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Unable to update notification")
        }
    }
}