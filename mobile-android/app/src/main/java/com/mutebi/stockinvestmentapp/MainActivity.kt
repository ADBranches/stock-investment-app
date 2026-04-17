package com.mutebi.stockinvestmentapp

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.fragment.app.FragmentActivity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import com.mutebi.stockinvestmentapp.core.security.BiometricAuthManager
import com.mutebi.stockinvestmentapp.core.ui.theme.StockAppTheme
import com.mutebi.stockinvestmentapp.data.session.SessionManager
import com.mutebi.stockinvestmentapp.navigation.AppNavGraph
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : FragmentActivity() {

    @Inject
    lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val isLoggedIn = sessionManager.hasSession()
            val requiresBiometric = isLoggedIn && sessionManager.isBiometricLockEnabled()

            var appUnlocked by remember { mutableStateOf(!requiresBiometric) }
            var biometricMessage by remember { mutableStateOf<String?>(null) }

            val navController = rememberNavController()
            val biometricAuthManager = remember { BiometricAuthManager(this@MainActivity) }

            fun requestUnlock() {
                biometricAuthManager.authenticate(
                    title = "Unlock StockApp",
                    subtitle = "Use your biometric to continue",
                    onSuccess = {
                        appUnlocked = true
                        biometricMessage = null
                    },
                    onError = { error ->
                        biometricMessage = error
                    }
                )
            }

            LaunchedEffect(requiresBiometric) {
                if (requiresBiometric && !appUnlocked) {
                    requestUnlock()
                }
            }

            StockAppTheme {
                Surface {
                    if (requiresBiometric && !appUnlocked) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(24.dp),
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            Text("Security lock enabled")
                            Text("This app is protected with biometric lock.")

                            biometricMessage?.let {
                                Text(it)
                            }

                            Button(onClick = ::requestUnlock) {
                                Text("Unlock")
                            }
                        }
                    } else {
                        AppNavGraph(
                            navController = navController,
                            isLoggedIn = isLoggedIn
                        )
                    }
                }
            }
        }
    }
}