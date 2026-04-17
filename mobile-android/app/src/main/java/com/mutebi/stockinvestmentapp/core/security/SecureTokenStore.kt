package com.mutebi.stockinvestmentapp.core.security

import android.content.Context
import android.content.SharedPreferences

class SecureTokenStore(
    context: Context,
    private val cryptoManager: CryptoManager
) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences("stockapp_secure_store", Context.MODE_PRIVATE)

    fun saveToken(token: String) {
        val encrypted = cryptoManager.encrypt(token)
        prefs.edit().putString(KEY_ENCRYPTED_TOKEN, encrypted).apply()
    }

    fun getToken(): String? {
        val encrypted = prefs.getString(KEY_ENCRYPTED_TOKEN, null) ?: return null
        return cryptoManager.decrypt(encrypted)
    }

    fun clearToken() {
        prefs.edit().remove(KEY_ENCRYPTED_TOKEN).apply()
    }

    companion object {
        private const val KEY_ENCRYPTED_TOKEN = "encrypted_token"
    }
}