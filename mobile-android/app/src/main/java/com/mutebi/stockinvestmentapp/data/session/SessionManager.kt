package com.mutebi.stockinvestmentapp.data.session

import android.content.Context
import android.content.SharedPreferences
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class SessionManager @Inject constructor(
    @ApplicationContext context: Context
) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences("stockapp_session", Context.MODE_PRIVATE)

    fun saveSession(token: String, userId: Int?, email: String?) {
        prefs.edit()
            .putString(KEY_TOKEN, token)
            .putInt(KEY_USER_ID, userId ?: -1)
            .putString(KEY_EMAIL, email)
            .apply()
    }

    fun getToken(): String? = prefs.getString(KEY_TOKEN, null)

    fun getUserId(): Int? {
        val value = prefs.getInt(KEY_USER_ID, -1)
        return if (value == -1) null else value
    }

    fun getEmail(): String? = prefs.getString(KEY_EMAIL, null)

    fun hasSession(): Boolean = !getToken().isNullOrBlank()

    fun restoreAuthState(): AuthState {
        val token = getToken()
        return AuthState(
            token = token,
            userId = getUserId(),
            email = getEmail(),
            isLoggedIn = !token.isNullOrBlank()
        )
    }

    fun clearSession() {
        prefs.edit().clear().apply()
    }

    companion object {
        private const val KEY_TOKEN = "token"
        private const val KEY_USER_ID = "user_id"
        private const val KEY_EMAIL = "email"
    }
}