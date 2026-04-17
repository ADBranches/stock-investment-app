package com.mutebi.stockinvestmentapp.data.repository

import com.mutebi.stockinvestmentapp.core.utils.Resource
import com.mutebi.stockinvestmentapp.data.remote.api.AuthApi
import com.mutebi.stockinvestmentapp.data.remote.dto.ForgotPasswordRequestDto
import com.mutebi.stockinvestmentapp.data.remote.dto.LoginRequestDto
import com.mutebi.stockinvestmentapp.data.remote.dto.RegisterRequestDto
import com.mutebi.stockinvestmentapp.data.session.AuthHolder
import com.mutebi.stockinvestmentapp.data.session.SessionManager
import com.mutebi.stockinvestmentapp.domain.model.UserProfile

class AuthRepository(
    private val authApi: AuthApi,
    private val sessionManager: SessionManager
) {
    suspend fun register(
        email: String,
        password: String,
        fullName: String
    ): Resource<UserProfile> {
        return try {
            val response = authApi.register(
                RegisterRequestDto(
                    email = email,
                    password = password,
                    fullName = fullName
                )
            )

            val body = response.body()
            if (response.isSuccessful && body?.success == true) {
                val token = body.data?.token ?: body.data?.accessToken ?: body.token
                val user = body.data?.user

                if (!token.isNullOrBlank()) {
                    sessionManager.saveSession(token, user?.id, user?.email)
                    AuthHolder.setSession(token, user?.id, user?.email)
                }

                if (user != null) {
                    Resource.Success(user.toDomain())
                } else {
                    Resource.Error(body.message.ifBlank { "Registration failed: missing user data" })
                }
            } else {
                Resource.Error(body?.message ?: "Registration failed")
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Registration failed")
        }
    }

    suspend fun login(
        email: String,
        password: String
    ): Resource<UserProfile> {
        return try {
            val response = authApi.login(LoginRequestDto(email, password))
            val body = response.body()

            if (response.isSuccessful && body?.success == true) {
                val token = body.data?.token ?: body.data?.accessToken ?: body.token
                val user = body.data?.user

                if (!token.isNullOrBlank()) {
                    sessionManager.saveSession(token, user?.id, user?.email)
                    AuthHolder.setSession(token, user?.id, user?.email)
                }

                if (user != null) {
                    Resource.Success(user.toDomain())
                } else {
                    Resource.Error("Missing user data")
                }
            } else {
                Resource.Error(body?.message ?: "Login failed")
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Login failed")
        }
    }

    suspend fun forgotPassword(email: String): Resource<String> {
        return try {
            val response = authApi.forgotPassword(ForgotPasswordRequestDto(email))
            val body = response.body()

            if (response.isSuccessful && body?.success == true) {
                Resource.Success(body.message.ifBlank { "Reset request submitted" })
            } else {
                Resource.Error(body?.message ?: "Reset request failed")
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Reset request failed")
        }
    }

    suspend fun getCurrentUser(): Resource<UserProfile> {
        return try {
            val response = authApi.me()
            val body = response.body()

            if (response.isSuccessful && body?.success == true) {
                val user = body.data
                if (user != null) {
                    Resource.Success(user.toDomain())
                } else {
                    Resource.Error(body.message.ifBlank { "Failed to fetch user" })
                }
            } else {
                Resource.Error(body?.message ?: "Failed to fetch user")
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Failed to fetch user")
        }
    }

    suspend fun changePassword(
        currentPassword: String,
        newPassword: String
    ): Resource<String> {
        return try {
            val response = authApi.changePassword(
                mapOf(
                    "current_password" to currentPassword,
                    "new_password" to newPassword
                )
            )
            val body = response.body()

            if (response.isSuccessful && body?.success == true) {
                Resource.Success(body.message.ifBlank { "Password changed successfully." })
            } else {
                Resource.Error(body?.message ?: "Password change failed")
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Password change failed")
        }
    }

    suspend fun revokeOtherSessions(): Resource<String> {
        return try {
            val response = authApi.revokeOtherSessions()
            val body = response.body()

            if (response.isSuccessful && body?.success == true) {
                Resource.Success(body.message.ifBlank { "Other sessions revoked successfully." })
            } else {
                Resource.Error(body?.message ?: "Failed to revoke other sessions")
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Failed to revoke other sessions")
        }
    }

    fun restoreSession() {
        val token = sessionManager.getToken()
        if (!token.isNullOrBlank()) {
            AuthHolder.setSession(
                token = token,
                userId = sessionManager.getUserId(),
                email = sessionManager.getEmail()
            )
        }
    }

    fun isLoggedIn(): Boolean = sessionManager.hasSession()

    fun logout() {
        sessionManager.clearSession()
        AuthHolder.clear()
    }
}