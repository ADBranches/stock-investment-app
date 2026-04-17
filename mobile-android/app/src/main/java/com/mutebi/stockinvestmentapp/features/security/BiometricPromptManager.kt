package com.mutebi.stockinvestmentapp.features.security

import androidx.fragment.app.FragmentActivity
import com.mutebi.stockinvestmentapp.core.security.BiometricAuthManager

class BiometricPromptManager(
    private val activity: FragmentActivity
) {

    private val biometricAuthManager = BiometricAuthManager(activity)

    fun isAvailable(): Boolean = biometricAuthManager.isBiometricAvailable()

    fun authenticateForEnablement(
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        biometricAuthManager.authenticate(
            title = "Enable biometric lock",
            subtitle = "Confirm your biometric to enable app lock",
            onSuccess = onSuccess,
            onError = onError
        )
    }
}