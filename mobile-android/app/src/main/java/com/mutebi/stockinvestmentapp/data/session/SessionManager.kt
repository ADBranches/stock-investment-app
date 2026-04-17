package com.mutebi.stockinvestmentapp.data.session

import android.content.Context
import android.content.SharedPreferences
import com.mutebi.stockinvestmentapp.core.security.CryptoManager
import com.mutebi.stockinvestmentapp.core.security.SecureTokenStore
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class SessionManager @Inject constructor(
    @ApplicationContext context: Context
) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences("stockapp_session", Context.MODE_PRIVATE)

    private val secureTokenStore = SecureTokenStore(
        context = context,
        cryptoManager = CryptoManager()
    )

    fun saveSession(token: String, userId: Int?, email: String?) {
        secureTokenStore.saveToken(token)
        prefs.edit()
            .putInt(KEY_USER_ID, userId ?: -1)
            .putString(KEY_EMAIL, email)
            .remove(KEY_TOKEN_LEGACY)
            .apply()
    }

    fun getToken(): String? {
        val secure = secureTokenStore.getToken()
        if (!secure.isNullOrBlank()) return secure

        val legacy = prefs.getString(KEY_TOKEN_LEGACY, null)
        if (!legacy.isNullOrBlank()) {
            secureTokenStore.saveToken(legacy)
            prefs.edit().remove(KEY_TOKEN_LEGACY).apply()
            return legacy
        }

        return null
    }

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

    fun isBiometricLockEnabled(): Boolean =
        prefs.getBoolean(KEY_BIOMETRIC_LOCK_ENABLED, false)

    fun setBiometricLockEnabled(enabled: Boolean) {
        prefs.edit().putBoolean(KEY_BIOMETRIC_LOCK_ENABLED, enabled).apply()
    }

    fun isPrivacyAccepted(): Boolean =
        prefs.getBoolean(KEY_PRIVACY_ACCEPTED, false)

    fun setPrivacyAccepted(value: Boolean) {
        prefs.edit().putBoolean(KEY_PRIVACY_ACCEPTED, value).apply()
    }

    fun isAnalyticsConsentEnabled(): Boolean =
        prefs.getBoolean(KEY_ANALYTICS_CONSENT, false)

    fun setAnalyticsConsentEnabled(value: Boolean) {
        prefs.edit().putBoolean(KEY_ANALYTICS_CONSENT, value).apply()
    }

    fun isMarketingConsentEnabled(): Boolean =
        prefs.getBoolean(KEY_MARKETING_CONSENT, false)

    fun setMarketingConsentEnabled(value: Boolean) {
        prefs.edit().putBoolean(KEY_MARKETING_CONSENT, value).apply()
    }

    fun clearSession() {
        secureTokenStore.clearToken()
        prefs.edit()
            .remove(KEY_USER_ID)
            .remove(KEY_EMAIL)
            .remove(KEY_TOKEN_LEGACY)
            .apply()
    }

    companion object {
        private const val KEY_TOKEN_LEGACY = "token"
        private const val KEY_USER_ID = "user_id"
        private const val KEY_EMAIL = "email"

        private const val KEY_BIOMETRIC_LOCK_ENABLED = "biometric_lock_enabled"
        private const val KEY_PRIVACY_ACCEPTED = "privacy_accepted"
        private const val KEY_ANALYTICS_CONSENT = "analytics_consent"
        private const val KEY_MARKETING_CONSENT = "marketing_consent"
    }
}