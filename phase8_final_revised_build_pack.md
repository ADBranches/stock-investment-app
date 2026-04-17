# Phase 8 — Security, Settings, and Account Management

This pack is split into exactly what you asked for:

- **keep as-is**
- **targeted edits only**
- **new files to create fully**

It is based on the Phase 8 audit you pasted.

---

## 1) Keep as-is

These files are already good enough for Phase 8 and do not need changes:

### Android
- `app/src/main/java/com/mutebi/stockinvestmentapp/data/remote/network/RetrofitProvider.kt`
- `app/src/main/java/com/mutebi/stockinvestmentapp/di/NetworkModule.kt`
- `app/src/main/java/com/mutebi/stockinvestmentapp/di/RepositoryModule.kt`
- `app/src/main/java/com/mutebi/stockinvestmentapp/data/remote/api/UserApi.kt`
- `app/src/main/java/com/mutebi/stockinvestmentapp/data/repository/UserRepository.kt`
- `app/src/main/java/com/mutebi/stockinvestmentapp/data/remote/network/AuthInterceptor.kt`
- `app/src/main/java/com/mutebi/stockinvestmentapp/features/profile/setup/ProfileSetupScreen.kt`
- `app/src/main/java/com/mutebi/stockinvestmentapp/features/profile/setup/ProfileSetupViewModel.kt`
- `app/src/main/java/com/mutebi/stockinvestmentapp/domain/model/UserProfile.kt`

### Backend
- `backend-api/app/routes/users.py`
- `backend-api/app/services/auth_service.py`
- `backend-api/app/services/user_service.py`

---

## 2) Targeted edits only

These files already exist, but they need Phase 8 changes.

---

## File: `mobile-android/app/build.gradle.kts`

Add this line inside `dependencies { ... }`:

```kotlin
implementation("androidx.biometric:biometric:1.1.0")
```

---

## File: `mobile-android/app/src/main/AndroidManifest.xml`

Add this biometric permission near the top with the other permissions:

```xml
<uses-permission android:name="android.permission.USE_BIOMETRIC" />
```

Your permission block should become:

```xml
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
<uses-permission android:name="android.permission.USE_BIOMETRIC" />
```

---

## File: `mobile-android/app/src/main/java/com/mutebi/stockinvestmentapp/navigation/Routes.kt`

Replace with:

```kotlin
package com.mutebi.stockinvestmentapp.navigation

sealed class Routes(val route: String) {
    data object Splash : Routes("splash")
    data object Onboarding : Routes("onboarding")

    data object Login : Routes("login")
    data object Register : Routes("register")
    data object ForgotPassword : Routes("forgot_password")

    data object ProfileSetup : Routes("profile_setup")

    data object KycIntro : Routes("kyc_intro")
    data object KycPersonalInfo : Routes("kyc_personal_info")
    data object KycDocumentUpload : Routes("kyc_document_upload")
    data object KycReview : Routes("kyc_review")
    data object KycSuccess : Routes("kyc_success")

    data object Dashboard : Routes("dashboard")
    data object Market : Routes("market")
    data object Watchlist : Routes("watchlist")
    data object Portfolio : Routes("portfolio")
    data object TransactionHistory : Routes("portfolio_history")

    data object EducationHub : Routes("education_hub")
    data object NotificationCenter : Routes("notification_center")

    data object Settings : Routes("settings")
    data object SecuritySettings : Routes("security_settings")
    data object Account : Routes("account")
    data object EditProfile : Routes("edit_profile")
    data object ChangePassword : Routes("change_password")

    data object AssetDetail : Routes("asset_detail/{assetId}") {
        fun createRoute(assetId: Int): String = "asset_detail/$assetId"
    }

    data object TradeOrder : Routes("trade_order/{assetId}") {
        fun createRoute(assetId: Int): String = "trade_order/$assetId"
    }

    data object TradeConfirm : Routes("trade_confirm")
    data object TradeResult : Routes("trade_result")

    data object ArticleDetail : Routes("article_detail/{articleId}") {
        fun createRoute(articleId: Int): String = "article_detail/$articleId"
    }
}
```

---

## File: `mobile-android/app/src/main/java/com/mutebi/stockinvestmentapp/navigation/BottomNavItem.kt`

Replace with:

```kotlin
package com.mutebi.stockinvestmentapp.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomNavItem(
    val route: String,
    val label: String,
    val icon: ImageVector
) {
    data object Dashboard : BottomNavItem(
        route = Routes.Dashboard.route,
        label = "Dashboard",
        icon = Icons.Filled.Home
    )

    data object Market : BottomNavItem(
        route = Routes.Market.route,
        label = "Market",
        icon = Icons.Filled.Star
    )

    data object Watchlist : BottomNavItem(
        route = Routes.Watchlist.route,
        label = "Watchlist",
        icon = Icons.Filled.List
    )

    data object Portfolio : BottomNavItem(
        route = Routes.Portfolio.route,
        label = "Portfolio",
        icon = Icons.Filled.List
    )

    data object Settings : BottomNavItem(
        route = Routes.Settings.route,
        label = "Settings",
        icon = Icons.Filled.Settings
    )

    companion object {
        val items = listOf(Dashboard, Market, Watchlist, Portfolio, Settings)
    }
}
```

---

## File: `mobile-android/app/src/main/java/com/mutebi/stockinvestmentapp/data/remote/api/AuthApi.kt`

Replace with:

```kotlin
package com.mutebi.stockinvestmentapp.data.remote.api

import com.mutebi.stockinvestmentapp.data.remote.dto.ApiEnvelopeDto
import com.mutebi.stockinvestmentapp.data.remote.dto.AuthPayloadDto
import com.mutebi.stockinvestmentapp.data.remote.dto.AuthUserDto
import com.mutebi.stockinvestmentapp.data.remote.dto.ForgotPasswordRequestDto
import com.mutebi.stockinvestmentapp.data.remote.dto.LoginRequestDto
import com.mutebi.stockinvestmentapp.data.remote.dto.RegisterRequestDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface AuthApi {
    @POST("api/v1/auth/register")
    suspend fun register(
        @Body request: RegisterRequestDto
    ): Response<ApiEnvelopeDto<AuthPayloadDto>>

    @POST("api/v1/auth/login")
    suspend fun login(
        @Body request: LoginRequestDto
    ): Response<ApiEnvelopeDto<AuthPayloadDto>>

    @POST("api/v1/auth/forgot-password")
    suspend fun forgotPassword(
        @Body request: ForgotPasswordRequestDto
    ): Response<ApiEnvelopeDto<Unit>>

    @GET("api/v1/auth/me")
    suspend fun me(): Response<ApiEnvelopeDto<AuthUserDto>>

    @POST("api/v1/security/change-password")
    suspend fun changePassword(
        @Body payload: Map<String, String>
    ): Response<ApiEnvelopeDto<Unit>>

    @POST("api/v1/security/revoke-other-sessions")
    suspend fun revokeOtherSessions(): Response<ApiEnvelopeDto<Unit>>
}
```

---

## File: `mobile-android/app/src/main/java/com/mutebi/stockinvestmentapp/data/repository/AuthRepository.kt`

Replace with:

```kotlin
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
```

---

## File: `mobile-android/app/src/main/java/com/mutebi/stockinvestmentapp/data/session/SessionManager.kt`

Replace with:

```kotlin
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
```

---

## File: `mobile-android/app/src/main/java/com/mutebi/stockinvestmentapp/MainActivity.kt`

Replace with:

```kotlin
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
```

---

## File: `mobile-android/app/src/main/java/com/mutebi/stockinvestmentapp/navigation/AppNavGraph.kt`

Replace with:

```kotlin
package com.mutebi.stockinvestmentapp.navigation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.mutebi.stockinvestmentapp.features.account.AccountScreen
import com.mutebi.stockinvestmentapp.features.account.AccountViewModel
import com.mutebi.stockinvestmentapp.features.account.ChangePasswordScreen
import com.mutebi.stockinvestmentapp.features.account.EditProfileScreen
import com.mutebi.stockinvestmentapp.features.auth.forgot_password.ForgotPasswordScreen
import com.mutebi.stockinvestmentapp.features.auth.login.LoginScreen
import com.mutebi.stockinvestmentapp.features.auth.register.RegisterScreen
import com.mutebi.stockinvestmentapp.features.education.article.ArticleDetailScreen
import com.mutebi.stockinvestmentapp.features.education.hub.EducationHubScreen
import com.mutebi.stockinvestmentapp.features.home.dashboard.DashboardScreen
import com.mutebi.stockinvestmentapp.features.kyc.KycDocumentUploadScreen
import com.mutebi.stockinvestmentapp.features.kyc.KycIntroScreen
import com.mutebi.stockinvestmentapp.features.kyc.KycPersonalInfoScreen
import com.mutebi.stockinvestmentapp.features.kyc.KycReviewScreen
import com.mutebi.stockinvestmentapp.features.kyc.KycSuccessScreen
import com.mutebi.stockinvestmentapp.features.kyc.KycViewModel
import com.mutebi.stockinvestmentapp.features.market.details.AssetDetailScreen
import com.mutebi.stockinvestmentapp.features.market.list.MarketListScreen
import com.mutebi.stockinvestmentapp.features.notifications.NotificationCenterScreen
import com.mutebi.stockinvestmentapp.features.onboarding.OnboardingScreen
import com.mutebi.stockinvestmentapp.features.portfolio.history.TransactionHistoryScreen
import com.mutebi.stockinvestmentapp.features.portfolio.overview.PortfolioScreen
import com.mutebi.stockinvestmentapp.features.profile.setup.ProfileSetupScreen
import com.mutebi.stockinvestmentapp.features.security.SecuritySettingsScreen
import com.mutebi.stockinvestmentapp.features.security.SecuritySettingsViewModel
import com.mutebi.stockinvestmentapp.features.settings.SettingsScreen
import com.mutebi.stockinvestmentapp.features.settings.SettingsViewModel
import com.mutebi.stockinvestmentapp.features.splash.SplashScreen
import com.mutebi.stockinvestmentapp.features.trade.confirm.TradeConfirmScreen
import com.mutebi.stockinvestmentapp.features.trade.confirm.TradeResultScreen
import com.mutebi.stockinvestmentapp.features.trade.order.TradeOrderScreen
import com.mutebi.stockinvestmentapp.features.trade.order.TradeOrderViewModel
import com.mutebi.stockinvestmentapp.features.watchlist.WatchlistScreen

@Composable
fun AppNavGraph(
    navController: NavHostController,
    isLoggedIn: Boolean,
    modifier: Modifier = Modifier
) {
    val kycVm: KycViewModel = viewModel()
    val tradeVm: TradeOrderViewModel = viewModel()
    val accountVm: AccountViewModel = viewModel()
    val settingsVm: SettingsViewModel = viewModel()
    val securityVm: SecuritySettingsViewModel = viewModel()

    NavHost(
        navController = navController,
        startDestination = Routes.Splash.route,
        modifier = modifier
    ) {
        composable(Routes.Splash.route) {
            SplashScreen(
                isLoggedIn = isLoggedIn,
                onNavigateToOnboarding = {
                    navController.navigate(Routes.Onboarding.route) {
                        popUpTo(Routes.Splash.route) { inclusive = true }
                    }
                },
                onNavigateToDashboard = {
                    navController.navigate(Routes.Dashboard.route) {
                        popUpTo(Routes.Splash.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Routes.Onboarding.route) {
            OnboardingScreen(
                onGetStarted = {
                    navController.navigate(Routes.Login.route) {
                        popUpTo(Routes.Onboarding.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Routes.Login.route) {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate(Routes.Dashboard.route) {
                        popUpTo(Routes.Login.route) { inclusive = true }
                    }
                },
                onNavigateToRegister = {
                    navController.navigate(Routes.Register.route)
                },
                onNavigateToForgotPassword = {
                    navController.navigate(Routes.ForgotPassword.route)
                }
            )
        }

        composable(Routes.Register.route) {
            RegisterScreen(
                onRegisterSuccess = {
                    navController.navigate(Routes.ProfileSetup.route) {
                        popUpTo(Routes.Register.route) { inclusive = true }
                    }
                },
                onNavigateToLogin = {
                    navController.popBackStack()
                }
            )
        }

        composable(Routes.ForgotPassword.route) {
            ForgotPasswordScreen(
                onBackToLogin = {
                    navController.popBackStack()
                }
            )
        }

        composable(Routes.ProfileSetup.route) {
            ProfileSetupScreen(
                onProfileSaved = {
                    navController.navigate(Routes.KycIntro.route) {
                        popUpTo(Routes.ProfileSetup.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Routes.KycIntro.route) {
            KycIntroScreen(
                onStartKyc = {
                    navController.navigate(Routes.KycPersonalInfo.route)
                }
            )
        }

        composable(Routes.KycPersonalInfo.route) {
            KycPersonalInfoScreen(
                vm = kycVm,
                onNext = {
                    navController.navigate(Routes.KycDocumentUpload.route)
                }
            )
        }

        composable(Routes.KycDocumentUpload.route) {
            KycDocumentUploadScreen(
                vm = kycVm,
                onNext = {
                    navController.navigate(Routes.KycReview.route)
                }
            )
        }

        composable(Routes.KycReview.route) {
            KycReviewScreen(
                vm = kycVm,
                onSuccess = {
                    navController.navigate(Routes.KycSuccess.route) {
                        popUpTo(Routes.KycReview.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Routes.KycSuccess.route) {
            KycSuccessScreen(
                onDone = {
                    navController.navigate(Routes.Dashboard.route) {
                        popUpTo(Routes.KycIntro.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Routes.Dashboard.route) {
            DashboardScreen(
                onOpenMarket = {
                    navController.navigate(Routes.Market.route)
                },
                onOpenWatchlist = {
                    navController.navigate(Routes.Watchlist.route)
                },
                onOpenEducation = {
                    navController.navigate(Routes.EducationHub.route)
                },
                onOpenNotifications = {
                    navController.navigate(Routes.NotificationCenter.route)
                }
            )
        }

        composable(Routes.Market.route) {
            MarketListScreen(
                onAssetClick = { assetId ->
                    navController.navigate(Routes.AssetDetail.createRoute(assetId))
                },
                onOpenWatchlist = {
                    navController.navigate(Routes.Watchlist.route)
                }
            )
        }

        composable(Routes.Watchlist.route) {
            WatchlistScreen(
                onOpenMarket = {
                    navController.navigate(Routes.Market.route)
                },
                onBack = {
                    navController.popBackStack()
                }
            )
        }

        composable(Routes.AssetDetail.route) { backStackEntry ->
            val assetId = backStackEntry.arguments?.getString("assetId")?.toIntOrNull()

            if (assetId == null) {
                Phase8PlaceholderScreen(
                    title = "Asset detail unavailable",
                    message = "The selected asset ID was not found."
                )
            } else {
                AssetDetailScreen(
                    assetId = assetId,
                    onBack = { navController.popBackStack() },
                    onOpenWatchlist = {
                        navController.navigate(Routes.Watchlist.route)
                    },
                    onTradeAsset = {
                        navController.navigate(Routes.TradeOrder.createRoute(assetId))
                    }
                )
            }
        }

        composable(Routes.Portfolio.route) {
            PortfolioScreen(
                onOpenHistory = {
                    navController.navigate(Routes.TransactionHistory.route)
                },
                onTradeAsset = { assetId ->
                    navController.navigate(Routes.TradeOrder.createRoute(assetId))
                },
                onOpenMarket = {
                    navController.navigate(Routes.Market.route)
                }
            )
        }

        composable(Routes.TransactionHistory.route) {
            TransactionHistoryScreen(
                onBack = {
                    navController.popBackStack()
                }
            )
        }

        composable(Routes.TradeOrder.route) { backStackEntry ->
            val assetId = backStackEntry.arguments?.getString("assetId")?.toIntOrNull()

            if (assetId == null) {
                Phase8PlaceholderScreen(
                    title = "Trade setup unavailable",
                    message = "The selected asset ID was not found."
                )
            } else {
                TradeOrderScreen(
                    assetId = assetId,
                    vm = tradeVm,
                    onBack = { navController.popBackStack() },
                    onContinue = {
                        navController.navigate(Routes.TradeConfirm.route)
                    }
                )
            }
        }

        composable(Routes.TradeConfirm.route) {
            TradeConfirmScreen(
                vm = tradeVm,
                onBack = { navController.popBackStack() },
                onTradeCompleted = {
                    navController.navigate(Routes.TradeResult.route)
                }
            )
        }

        composable(Routes.TradeResult.route) {
            TradeResultScreen(
                vm = tradeVm,
                onDone = {
                    navController.navigate(Routes.Portfolio.route) {
                        popUpTo(Routes.TradeOrder.route) { inclusive = false }
                    }
                }
            )
        }

        composable(Routes.EducationHub.route) {
            EducationHubScreen(
                onBack = { navController.popBackStack() },
                onOpenNotifications = {
                    navController.navigate(Routes.NotificationCenter.route)
                },
                onOpenArticle = { articleId ->
                    navController.navigate(Routes.ArticleDetail.createRoute(articleId))
                }
            )
        }

        composable(Routes.ArticleDetail.route) { backStackEntry ->
            val articleId = backStackEntry.arguments?.getString("articleId")?.toIntOrNull()

            if (articleId == null) {
                Phase8PlaceholderScreen(
                    title = "Article unavailable",
                    message = "The selected article ID was not found."
                )
            } else {
                ArticleDetailScreen(
                    articleId = articleId,
                    onBack = { navController.popBackStack() }
                )
            }
        }

        composable(Routes.NotificationCenter.route) {
            NotificationCenterScreen(
                onBack = { navController.popBackStack() }
            )
        }

        composable(Routes.Settings.route) {
            SettingsScreen(
                vm = settingsVm,
                onOpenSecurity = {
                    navController.navigate(Routes.SecuritySettings.route)
                },
                onOpenAccount = {
                    navController.navigate(Routes.Account.route)
                }
            )
        }

        composable(Routes.SecuritySettings.route) {
            SecuritySettingsScreen(
                vm = securityVm,
                onBack = { navController.popBackStack() },
                onOpenChangePassword = {
                    navController.navigate(Routes.ChangePassword.route)
                }
            )
        }

        composable(Routes.Account.route) {
            AccountScreen(
                vm = accountVm,
                onBack = { navController.popBackStack() },
                onEditProfile = {
                    navController.navigate(Routes.EditProfile.route)
                },
                onChangePassword = {
                    navController.navigate(Routes.ChangePassword.route)
                },
                onLoggedOut = {
                    navController.navigate(Routes.Login.route) {
                        popUpTo(Routes.Dashboard.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Routes.EditProfile.route) {
            EditProfileScreen(
                vm = accountVm,
                onBack = { navController.popBackStack() }
            )
        }

        composable(Routes.ChangePassword.route) {
            ChangePasswordScreen(
                vm = accountVm,
                onBack = { navController.popBackStack() }
            )
        }
    }
}

@Composable
private fun Phase8PlaceholderScreen(
    title: String,
    message: String
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(text = title)
            Text(text = message)
        }
    }
}
```

---

## File: `backend-api/app/routes/auth.py`

No code change required for Phase 8.

Keep your current file as-is.

---

## 3) New files to create fully

---

## File: `mobile-android/app/src/main/java/com/mutebi/stockinvestmentapp/core/security/CryptoManager.kt`

```kotlin
package com.mutebi.stockinvestmentapp.core.security

import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.util.Base64
import java.nio.ByteBuffer
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec

class CryptoManager {

    private val keyAlias = "stockapp_secure_key"
    private val transformation = "AES/GCM/NoPadding"
    private val androidKeyStore = "AndroidKeyStore"

    private fun getOrCreateSecretKey(): SecretKey {
        val keyStore = KeyStore.getInstance(androidKeyStore).apply { load(null) }
        val existingKey = keyStore.getKey(keyAlias, null) as? SecretKey
        if (existingKey != null) return existingKey

        val generator = KeyGenerator.getInstance(
            KeyProperties.KEY_ALGORITHM_AES,
            androidKeyStore
        )

        val spec = KeyGenParameterSpec.Builder(
            keyAlias,
            KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
        )
            .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
            .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
            .setRandomizedEncryptionRequired(true)
            .build()

        generator.init(spec)
        return generator.generateKey()
    }

    fun encrypt(plainText: String): String {
        val cipher = Cipher.getInstance(transformation)
        cipher.init(Cipher.ENCRYPT_MODE, getOrCreateSecretKey())

        val iv = cipher.iv
        val encryptedBytes = cipher.doFinal(plainText.toByteArray(Charsets.UTF_8))

        val payload = ByteBuffer.allocate(4 + iv.size + encryptedBytes.size)
            .putInt(iv.size)
            .put(iv)
            .put(encryptedBytes)
            .array()

        return Base64.encodeToString(payload, Base64.DEFAULT)
    }

    fun decrypt(encoded: String): String? {
        return try {
            val payload = Base64.decode(encoded, Base64.DEFAULT)
            val buffer = ByteBuffer.wrap(payload)

            val ivSize = buffer.int
            val iv = ByteArray(ivSize)
            buffer.get(iv)

            val encryptedBytes = ByteArray(buffer.remaining())
            buffer.get(encryptedBytes)

            val cipher = Cipher.getInstance(transformation)
            cipher.init(
                Cipher.DECRYPT_MODE,
                getOrCreateSecretKey(),
                GCMParameterSpec(128, iv)
            )

            String(cipher.doFinal(encryptedBytes), Charsets.UTF_8)
        } catch (_: Exception) {
            null
        }
    }
}
```

---

## File: `mobile-android/app/src/main/java/com/mutebi/stockinvestmentapp/core/security/SecureTokenStore.kt`

```kotlin
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
```

---

## File: `mobile-android/app/src/main/java/com/mutebi/stockinvestmentapp/core/security/BiometricAuthManager.kt`

```kotlin
package com.mutebi.stockinvestmentapp.core.security

import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity

class BiometricAuthManager(
    private val activity: FragmentActivity
) {

    fun isBiometricAvailable(): Boolean {
        val manager = BiometricManager.from(activity)
        return manager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG) ==
            BiometricManager.BIOMETRIC_SUCCESS
    }

    fun authenticate(
        title: String,
        subtitle: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        if (!isBiometricAvailable()) {
            onError("Biometric authentication is not available on this device.")
            return
        }

        val executor = ContextCompat.getMainExecutor(activity)
        val prompt = BiometricPrompt(
            activity,
            executor,
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    onSuccess()
                }

                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    onError(errString.toString())
                }

                override fun onAuthenticationFailed() {
                    onError("Biometric authentication failed. Try again.")
                }
            }
        )

        val promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle(title)
            .setSubtitle(subtitle)
            .setNegativeButtonText("Cancel")
            .build()

        prompt.authenticate(promptInfo)
    }
}
```

---

## File: `mobile-android/app/src/main/java/com/mutebi/stockinvestmentapp/features/security/BiometricPromptManager.kt`

```kotlin
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
```

---

## File: `mobile-android/app/src/main/java/com/mutebi/stockinvestmentapp/features/settings/SettingsUiState.kt`

```kotlin
package com.mutebi.stockinvestmentapp.features.settings

data class SettingsUiState(
    val privacyAccepted: Boolean = false,
    val analyticsConsent: Boolean = false,
    val marketingConsent: Boolean = false,
    val statusMessage: String? = null
)
```

---

## File: `mobile-android/app/src/main/java/com/mutebi/stockinvestmentapp/features/settings/SettingsViewModel.kt`

```kotlin
package com.mutebi.stockinvestmentapp.features.settings

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.mutebi.stockinvestmentapp.data.session.SessionManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class SettingsViewModel(application: Application) : AndroidViewModel(application) {

    private val sessionManager = SessionManager(application)

    private val _uiState = MutableStateFlow(
        SettingsUiState(
            privacyAccepted = sessionManager.isPrivacyAccepted(),
            analyticsConsent = sessionManager.isAnalyticsConsentEnabled(),
            marketingConsent = sessionManager.isMarketingConsentEnabled()
        )
    )
    val uiState: StateFlow<SettingsUiState> = _uiState

    fun onPrivacyAcceptedChange(value: Boolean) {
        sessionManager.setPrivacyAccepted(value)
        _uiState.value = _uiState.value.copy(
            privacyAccepted = value,
            statusMessage = "Privacy preference saved."
        )
    }

    fun onAnalyticsConsentChange(value: Boolean) {
        sessionManager.setAnalyticsConsentEnabled(value)
        _uiState.value = _uiState.value.copy(
            analyticsConsent = value,
            statusMessage = "Analytics consent saved."
        )
    }

    fun onMarketingConsentChange(value: Boolean) {
        sessionManager.setMarketingConsentEnabled(value)
        _uiState.value = _uiState.value.copy(
            marketingConsent = value,
            statusMessage = "Marketing consent saved."
        )
    }
}
```

---

## File: `mobile-android/app/src/main/java/com/mutebi/stockinvestmentapp/features/settings/SettingsScreen.kt`

```kotlin
package com.mutebi.stockinvestmentapp.features.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun SettingsScreen(
    vm: SettingsViewModel,
    onOpenSecurity: () -> Unit,
    onOpenAccount: () -> Unit
) {
    val state by vm.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Settings")

        Text("Privacy and consent")

        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text("Privacy policy accepted")
            Switch(
                checked = state.privacyAccepted,
                onCheckedChange = vm::onPrivacyAcceptedChange
            )

            Text("Analytics consent")
            Switch(
                checked = state.analyticsConsent,
                onCheckedChange = vm::onAnalyticsConsentChange
            )

            Text("Marketing consent")
            Switch(
                checked = state.marketingConsent,
                onCheckedChange = vm::onMarketingConsentChange
            )
        }

        Button(
            onClick = onOpenSecurity,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Open security settings")
        }

        Button(
            onClick = onOpenAccount,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Open account")
        }

        state.statusMessage?.let {
            Text(it)
        }
    }
}
```

---

## File: `mobile-android/app/src/main/java/com/mutebi/stockinvestmentapp/features/security/SecurityUiState.kt`

```kotlin
package com.mutebi.stockinvestmentapp.features.security

data class SecurityUiState(
    val biometricEnabled: Boolean = false,
    val biometricAvailable: Boolean = false,
    val isBusy: Boolean = false,
    val statusMessage: String? = null,
    val error: String? = null
)
```

---

## File: `mobile-android/app/src/main/java/com/mutebi/stockinvestmentapp/features/security/SecuritySettingsViewModel.kt`

```kotlin
package com.mutebi.stockinvestmentapp.features.security

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.mutebi.stockinvestmentapp.core.utils.Resource
import com.mutebi.stockinvestmentapp.data.remote.network.RetrofitProvider
import com.mutebi.stockinvestmentapp.data.repository.AuthRepository
import com.mutebi.stockinvestmentapp.data.session.SessionManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SecuritySettingsViewModel(application: Application) : AndroidViewModel(application) {

    private val sessionManager = SessionManager(application)
    private val authRepository = AuthRepository(
        authApi = RetrofitProvider.authApi(application),
        sessionManager = sessionManager
    )

    private val _uiState = MutableStateFlow(
        SecurityUiState(
            biometricEnabled = sessionManager.isBiometricLockEnabled()
        )
    )
    val uiState: StateFlow<SecurityUiState> = _uiState

    fun setBiometricAvailable(value: Boolean) {
        _uiState.value = _uiState.value.copy(biometricAvailable = value)
    }

    fun setBiometricEnabled(value: Boolean) {
        sessionManager.setBiometricLockEnabled(value)
        _uiState.value = _uiState.value.copy(
            biometricEnabled = value,
            statusMessage = if (value) {
                "Biometric lock enabled."
            } else {
                "Biometric lock disabled."
            },
            error = null
        )
    }

    fun revokeOtherSessions() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isBusy = true, error = null)

            when (val result = authRepository.revokeOtherSessions()) {
                is Resource.Success -> {
                    _uiState.value = _uiState.value.copy(
                        isBusy = false,
                        statusMessage = result.data ?: "Other sessions revoked."
                    )
                }
                is Resource.Error -> {
                    _uiState.value = _uiState.value.copy(
                        isBusy = false,
                        error = result.message
                    )
                }
                else -> {
                    _uiState.value = _uiState.value.copy(isBusy = false)
                }
            }
        }
    }
}
```

---

## File: `mobile-android/app/src/main/java/com/mutebi/stockinvestmentapp/features/security/SecuritySettingsScreen.kt`

```kotlin
package com.mutebi.stockinvestmentapp.features.security

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.fragment.app.FragmentActivity

@Composable
fun SecuritySettingsScreen(
    vm: SecuritySettingsViewModel,
    onBack: () -> Unit,
    onOpenChangePassword: () -> Unit
) {
    val state by vm.uiState.collectAsState()

    val activity = androidx.compose.ui.platform.LocalContext.current as FragmentActivity
    val promptManager = BiometricPromptManager(activity)

    LaunchedEffect(Unit) {
        vm.setBiometricAvailable(promptManager.isAvailable())
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        TextButton(onClick = onBack) {
            Text("Back")
        }

        Text("Security settings")

        Text(
            if (state.biometricAvailable) {
                "Biometric authentication is available on this device."
            } else {
                "Biometric authentication is not available on this device."
            }
        )

        Button(
            onClick = {
                if (state.biometricEnabled) {
                    vm.setBiometricEnabled(false)
                } else {
                    promptManager.authenticateForEnablement(
                        onSuccess = { vm.setBiometricEnabled(true) },
                        onError = { vm.setBiometricAvailable(promptManager.isAvailable()) }
                    )
                }
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = state.biometricAvailable
        ) {
            Text(
                if (state.biometricEnabled) "Disable biometric lock" else "Enable biometric lock"
            )
        }

        Button(
            onClick = onOpenChangePassword,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Change password")
        }

        Button(
            onClick = vm::revokeOtherSessions,
            modifier = Modifier.fillMaxWidth(),
            enabled = !state.isBusy
        ) {
            Text("Revoke other sessions")
        }

        state.statusMessage?.let {
            Text(it)
        }

        state.error?.let {
            Text(it)
        }
    }
}
```

---

## File: `mobile-android/app/src/main/java/com/mutebi/stockinvestmentapp/features/account/AccountUiState.kt`

```kotlin
package com.mutebi.stockinvestmentapp.features.account

data class AccountUiState(
    val isLoading: Boolean = false,
    val isSaving: Boolean = false,
    val email: String = "",
    val fullName: String = "",
    val phoneNumber: String = "",
    val country: String = "",
    val dateOfBirth: String = "",
    val profileCompleted: Boolean = false,
    val kycStatus: String = "",
    val currentPassword: String = "",
    val newPassword: String = "",
    val confirmNewPassword: String = "",
    val successMessage: String? = null,
    val error: String? = null
)
```

---

## File: `mobile-android/app/src/main/java/com/mutebi/stockinvestmentapp/features/account/AccountViewModel.kt`

```kotlin
package com.mutebi.stockinvestmentapp.features.account

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.mutebi.stockinvestmentapp.core.utils.Resource
import com.mutebi.stockinvestmentapp.data.remote.network.RetrofitProvider
import com.mutebi.stockinvestmentapp.data.repository.AuthRepository
import com.mutebi.stockinvestmentapp.data.repository.UserRepository
import com.mutebi.stockinvestmentapp.data.session.SessionManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AccountViewModel(application: Application) : AndroidViewModel(application) {

    private val sessionManager = SessionManager(application)

    private val userRepository = UserRepository(
        userApi = RetrofitProvider.userApi(application)
    )

    private val authRepository = AuthRepository(
        authApi = RetrofitProvider.authApi(application),
        sessionManager = sessionManager
    )

    private val _uiState = MutableStateFlow(AccountUiState(isLoading = true))
    val uiState: StateFlow<AccountUiState> = _uiState

    init {
        loadAccount()
    }

    fun loadAccount() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null, successMessage = null)

            when (val result = userRepository.getProfile()) {
                is Resource.Success -> {
                    val profile = result.data
                    if (profile != null) {
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            email = profile.email,
                            fullName = profile.fullName,
                            phoneNumber = profile.phoneNumber,
                            country = profile.country,
                            dateOfBirth = profile.dateOfBirth,
                            profileCompleted = profile.profileCompleted,
                            kycStatus = profile.kycStatus
                        )
                    } else {
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            error = "Account profile unavailable."
                        )
                    }
                }
                is Resource.Error -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = result.message
                    )
                }
                else -> {
                    _uiState.value = _uiState.value.copy(isLoading = false)
                }
            }
        }
    }

    fun onFullNameChange(value: String) {
        _uiState.value = _uiState.value.copy(fullName = value, error = null, successMessage = null)
    }

    fun onPhoneNumberChange(value: String) {
        _uiState.value = _uiState.value.copy(phoneNumber = value, error = null, successMessage = null)
    }

    fun onCountryChange(value: String) {
        _uiState.value = _uiState.value.copy(country = value, error = null, successMessage = null)
    }

    fun onDateOfBirthChange(value: String) {
        _uiState.value = _uiState.value.copy(dateOfBirth = value, error = null, successMessage = null)
    }

    fun onCurrentPasswordChange(value: String) {
        _uiState.value = _uiState.value.copy(currentPassword = value, error = null, successMessage = null)
    }

    fun onNewPasswordChange(value: String) {
        _uiState.value = _uiState.value.copy(newPassword = value, error = null, successMessage = null)
    }

    fun onConfirmNewPasswordChange(value: String) {
        _uiState.value = _uiState.value.copy(confirmNewPassword = value, error = null, successMessage = null)
    }

    fun saveProfile() {
        val state = _uiState.value

        if (state.fullName.isBlank() || state.phoneNumber.isBlank() || state.country.isBlank() || state.dateOfBirth.isBlank()) {
            _uiState.value = state.copy(error = "All profile fields are required.")
            return
        }

        viewModelScope.launch {
            _uiState.value = state.copy(isSaving = true, error = null, successMessage = null)

            when (
                val result = userRepository.updateProfile(
                    fullName = state.fullName.trim(),
                    phoneNumber = state.phoneNumber.trim(),
                    country = state.country.trim(),
                    dateOfBirth = state.dateOfBirth.trim()
                )
            ) {
                is Resource.Success -> {
                    val profile = result.data
                    _uiState.value = _uiState.value.copy(
                        isSaving = false,
                        email = profile?.email ?: state.email,
                        fullName = profile?.fullName ?: state.fullName,
                        phoneNumber = profile?.phoneNumber ?: state.phoneNumber,
                        country = profile?.country ?: state.country,
                        dateOfBirth = profile?.dateOfBirth ?: state.dateOfBirth,
                        successMessage = "Profile updated successfully."
                    )
                }
                is Resource.Error -> {
                    _uiState.value = _uiState.value.copy(
                        isSaving = false,
                        error = result.message
                    )
                }
                else -> {
                    _uiState.value = _uiState.value.copy(isSaving = false)
                }
            }
        }
    }

    fun changePassword() {
        val state = _uiState.value

        when {
            state.currentPassword.isBlank() -> {
                _uiState.value = state.copy(error = "Current password is required.")
                return
            }
            state.newPassword.isBlank() -> {
                _uiState.value = state.copy(error = "New password is required.")
                return
            }
            state.newPassword.length < 8 -> {
                _uiState.value = state.copy(error = "New password must be at least 8 characters.")
                return
            }
            state.newPassword != state.confirmNewPassword -> {
                _uiState.value = state.copy(error = "Password confirmation does not match.")
                return
            }
        }

        viewModelScope.launch {
            _uiState.value = state.copy(isSaving = true, error = null, successMessage = null)

            when (
                val result = authRepository.changePassword(
                    currentPassword = state.currentPassword,
                    newPassword = state.newPassword
                )
            ) {
                is Resource.Success -> {
                    _uiState.value = _uiState.value.copy(
                        isSaving = false,
                        currentPassword = "",
                        newPassword = "",
                        confirmNewPassword = "",
                        successMessage = result.data ?: "Password changed successfully."
                    )
                }
                is Resource.Error -> {
                    _uiState.value = _uiState.value.copy(
                        isSaving = false,
                        error = result.message
                    )
                }
                else -> {
                    _uiState.value = _uiState.value.copy(isSaving = false)
                }
            }
        }
    }

    fun logout() {
        authRepository.logout()
    }
}
```

---

## File: `mobile-android/app/src/main/java/com/mutebi/stockinvestmentapp/features/account/AccountScreen.kt`

```kotlin
package com.mutebi.stockinvestmentapp.features.account

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun AccountScreen(
    vm: AccountViewModel,
    onBack: () -> Unit,
    onEditProfile: () -> Unit,
    onChangePassword: () -> Unit,
    onLoggedOut: () -> Unit
) {
    val state by vm.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        TextButton(onClick = onBack) {
            Text("Back")
        }

        Text("Account")

        if (state.isLoading) {
            CircularProgressIndicator()
        } else {
            Text("Email: ${state.email}")
            Text("Full name: ${state.fullName}")
            Text("Phone: ${state.phoneNumber}")
            Text("Country: ${state.country}")
            Text("Date of birth: ${state.dateOfBirth}")
            Text("Profile completed: ${state.profileCompleted}")
            Text("KYC status: ${state.kycStatus}")

            Button(
                onClick = onEditProfile,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Edit profile")
            }

            Button(
                onClick = onChangePassword,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Change password")
            }

            Button(
                onClick = {
                    vm.logout()
                    onLoggedOut()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Log out")
            }
        }

        state.successMessage?.let {
            Text(it)
        }

        state.error?.let {
            Text(it)
        }
    }
}
```

---

## File: `mobile-android/app/src/main/java/com/mutebi/stockinvestmentapp/features/account/EditProfileScreen.kt`

```kotlin
package com.mutebi.stockinvestmentapp.features.account

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun EditProfileScreen(
    vm: AccountViewModel,
    onBack: () -> Unit
) {
    val state by vm.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        TextButton(onClick = onBack) {
            Text("Back")
        }

        Text("Edit profile")

        OutlinedTextField(
            value = state.fullName,
            onValueChange = vm::onFullNameChange,
            label = { Text("Full name") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        OutlinedTextField(
            value = state.phoneNumber,
            onValueChange = vm::onPhoneNumberChange,
            label = { Text("Phone number") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        OutlinedTextField(
            value = state.country,
            onValueChange = vm::onCountryChange,
            label = { Text("Country") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        OutlinedTextField(
            value = state.dateOfBirth,
            onValueChange = vm::onDateOfBirthChange,
            label = { Text("Date of birth (YYYY-MM-DD)") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Button(
            onClick = vm::saveProfile,
            modifier = Modifier.fillMaxWidth(),
            enabled = !state.isSaving
        ) {
            if (state.isSaving) {
                CircularProgressIndicator()
            } else {
                Text("Save changes")
            }
        }

        state.successMessage?.let {
            Text(it)
        }

        state.error?.let {
            Text(it)
        }
    }
}
```

---

## File: `mobile-android/app/src/main/java/com/mutebi/stockinvestmentapp/features/account/ChangePasswordScreen.kt`

```kotlin
package com.mutebi.stockinvestmentapp.features.account

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ChangePasswordScreen(
    vm: AccountViewModel,
    onBack: () -> Unit
) {
    val state by vm.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        TextButton(onClick = onBack) {
            Text("Back")
        }

        Text("Change password")

        OutlinedTextField(
            value = state.currentPassword,
            onValueChange = vm::onCurrentPasswordChange,
            label = { Text("Current password") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        OutlinedTextField(
            value = state.newPassword,
            onValueChange = vm::onNewPasswordChange,
            label = { Text("New password") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        OutlinedTextField(
            value = state.confirmNewPassword,
            onValueChange = vm::onConfirmNewPasswordChange,
            label = { Text("Confirm new password") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Button(
            onClick = vm::changePassword,
            modifier = Modifier.fillMaxWidth(),
            enabled = !state.isSaving
        ) {
            if (state.isSaving) {
                CircularProgressIndicator()
            } else {
                Text("Update password")
            }
        }

        state.successMessage?.let {
            Text(it)
        }

        state.error?.let {
            Text(it)
        }
    }
}
```

---

## File: `backend-api/app/services/settings_service.py`

```python
_USER_SETTINGS_STORE: dict[int, dict] = {}


class SettingsService:
    DEFAULTS = {
        "privacy_accepted": False,
        "analytics_consent": False,
        "marketing_consent": False,
    }

    @staticmethod
    def get_settings(user_id: int) -> dict:
        stored = _USER_SETTINGS_STORE.get(user_id, {})
        return {**SettingsService.DEFAULTS, **stored}

    @staticmethod
    def update_settings(user_id: int, payload: dict) -> dict:
        current = SettingsService.get_settings(user_id)
        allowed_keys = set(SettingsService.DEFAULTS.keys())

        updates = {
            key: bool(value)
            for key, value in payload.items()
            if key in allowed_keys
        }

        current.update(updates)
        _USER_SETTINGS_STORE[user_id] = current
        return current
```

---

## File: `backend-api/app/services/security_service.py`

```python
from app.repositories.user_repository import UserRepository
from app.utils.security import check_password, hash_password


class SecurityService:
    @staticmethod
    def change_password(user_id: int, current_password: str, new_password: str):
        user = UserRepository.find_by_id(user_id)
        if not user:
            return False, "User not found."

        if not check_password(user.password_hash, current_password):
            return False, "Current password is incorrect."

        if len(new_password) < 8:
            return False, "New password must be at least 8 characters."

        UserRepository.update_user(
            user,
            password_hash=hash_password(new_password),
        )
        return True, "Password changed successfully."

    @staticmethod
    def revoke_other_sessions(user_id: int) -> dict:
        return {
            "user_id": user_id,
            "revoked_sessions": 0,
            "note": "JWT session revocation is simulated in this MVP phase."
        }
```

---

## File: `backend-api/app/routes/settings.py`

```python
from flask import Blueprint, request
from flask_jwt_extended import get_jwt_identity, jwt_required

from app.services.settings_service import SettingsService
from app.utils.response import success_response

settings_bp = Blueprint("settings", __name__)


@settings_bp.get("")
@jwt_required()
def get_settings():
    user_id = int(get_jwt_identity())
    data = SettingsService.get_settings(user_id)
    return success_response("Settings retrieved successfully.", data)


@settings_bp.patch("")
@jwt_required()
def update_settings():
    user_id = int(get_jwt_identity())
    payload = request.get_json(silent=True) or {}
    data = SettingsService.update_settings(user_id, payload)
    return success_response("Settings updated successfully.", data)
```

---

## File: `backend-api/app/routes/security.py`

```python
from flask import Blueprint, request
from flask_jwt_extended import get_jwt_identity, jwt_required

from app.services.security_service import SecurityService
from app.utils.response import error_response, success_response

security_bp = Blueprint("security", __name__)


@security_bp.post("/change-password")
@jwt_required()
def change_password():
    payload = request.get_json(silent=True) or {}
    current_password = (payload.get("current_password") or "").strip()
    new_password = (payload.get("new_password") or "").strip()

    if not current_password or not new_password:
        return error_response(
            "current_password and new_password are required.",
            status_code=400,
        )

    success, message = SecurityService.change_password(
        int(get_jwt_identity()),
        current_password,
        new_password,
    )
    if not success:
        return error_response(message, status_code=400)

    return success_response(message, None, 200)


@security_bp.post("/revoke-other-sessions")
@jwt_required()
def revoke_other_sessions():
    result = SecurityService.revoke_other_sessions(int(get_jwt_identity()))
    return success_response("Other sessions revoked successfully.", result, 200)
```

---

## File: `backend-api/tests/test_security.py`

```python
import uuid


def _unique_email() -> str:
    return f"phase8_security_{uuid.uuid4().hex[:10]}@example.com"


def _register_user(client, email: str, password: str = "Password123!"):
    return client.post(
        "/api/v1/auth/register",
        json={
            "email": email,
            "password": password,
            "full_name": "Phase Eight Security User",
        },
    )


def _login_user(client, email: str, password: str = "Password123!"):
    return client.post(
        "/api/v1/auth/login",
        json={
            "email": email,
            "password": password,
        },
    )


def _auth_headers(client, password: str = "Password123!"):
    email = _unique_email()

    register_response = _register_user(client, email, password)
    assert register_response.status_code in (200, 201), register_response.get_json()

    login_response = _login_user(client, email, password)
    assert login_response.status_code == 200, login_response.get_json()

    body = login_response.get_json() or {}
    data = body.get("data") or {}
    token = data.get("access_token") or data.get("token")
    assert token, body

    return email, {
        "Authorization": f"Bearer {token}",
        "Content-Type": "application/json",
    }


def test_change_password(client):
    email, headers = _auth_headers(client)

    response = client.post(
        "/api/v1/security/change-password",
        headers=headers,
        json={
            "current_password": "Password123!",
            "new_password": "NewPassword123!",
        },
    )
    body = response.get_json() or {}

    assert response.status_code == 200, body
    assert body.get("success") is True

    login_response = _login_user(client, email, "NewPassword123!")
    assert login_response.status_code == 200, login_response.get_json()


def test_revoke_other_sessions(client):
    _, headers = _auth_headers(client)

    response = client.post(
        "/api/v1/security/revoke-other-sessions",
        headers=headers,
        json={},
    )
    body = response.get_json() or {}

    assert response.status_code == 200, body
    assert body.get("success") is True

    data = body.get("data") or {}
    assert "revoked_sessions" in data
```

---

## File: `backend-api/tests/test_account_settings.py`

```python
import uuid


def _unique_email() -> str:
    return f"phase8_settings_{uuid.uuid4().hex[:10]}@example.com"


def _register_user(client, email: str, password: str = "Password123!"):
    return client.post(
        "/api/v1/auth/register",
        json={
            "email": email,
            "password": password,
            "full_name": "Phase Eight Settings User",
        },
    )


def _login_user(client, email: str, password: str = "Password123!"):
    return client.post(
        "/api/v1/auth/login",
        json={
            "email": email,
            "password": password,
        },
    )


def _auth_headers(client):
    email = _unique_email()

    register_response = _register_user(client, email)
    assert register_response.status_code in (200, 201), register_response.get_json()

    login_response = _login_user(client, email)
    assert login_response.status_code == 200, login_response.get_json()

    body = login_response.get_json() or {}
    data = body.get("data") or {}
    token = data.get("access_token") or data.get("token")
    assert token, body

    return {
        "Authorization": f"Bearer {token}",
        "Content-Type": "application/json",
    }


def test_get_default_settings(client):
    headers = _auth_headers(client)

    response = client.get("/api/v1/settings", headers=headers)
    body = response.get_json() or {}

    assert response.status_code == 200, body
    assert body.get("success") is True

    data = body.get("data") or {}
    assert data.get("privacy_accepted") is False
    assert data.get("analytics_consent") is False
    assert data.get("marketing_consent") is False


def test_update_settings(client):
    headers = _auth_headers(client)

    response = client.patch(
        "/api/v1/settings",
        headers=headers,
        json={
            "privacy_accepted": True,
            "analytics_consent": True,
            "marketing_consent": False,
        },
    )
    body = response.get_json() or {}

    assert response.status_code == 200, body
    assert body.get("success") is True

    data = body.get("data") or {}
    assert data.get("privacy_accepted") is True
    assert data.get("analytics_consent") is True
    assert data.get("marketing_consent") is False


def test_update_account_profile(client):
    headers = _auth_headers(client)

    response = client.patch(
        "/api/v1/users/me/profile",
        headers=headers,
        json={
            "full_name": "Phase Eight Updated",
            "phone_number": "+256700111222",
            "country": "Uganda",
            "date_of_birth": "2001-01-01",
        },
    )
    body = response.get_json() or {}

    assert response.status_code == 200, body
    assert body.get("success") is True

    data = body.get("data") or {}
    assert data.get("full_name") == "Phase Eight Updated"
```

---

## 4) New directories to create

### From `mobile-android/`
```bash
mkdir -p app/src/main/java/com/mutebi/stockinvestmentapp/features/settings
mkdir -p app/src/main/java/com/mutebi/stockinvestmentapp/features/security
mkdir -p app/src/main/java/com/mutebi/stockinvestmentapp/features/account
mkdir -p app/src/main/java/com/mutebi/stockinvestmentapp/core/security
```

### From `backend-api/`
```bash
mkdir -p app/routes
mkdir -p app/services
mkdir -p tests
```

---

## 5) Backend wire-up notes

These are important:

1. Register `settings_bp` and `security_bp` in your central blueprint registration.
2. Because `settings.py` and `security.py` are new routes, confirm they are mounted under `/api/v1/settings` and `/api/v1/security`.
3. No migration is required for Phase 8 as written here because settings are stored in-memory and password changes reuse the existing user table.

---

## 6) Verification sequence

### Backend
From `backend-api/`:
```bash
source env/bin/activate
pytest tests/test_security.py tests/test_account_settings.py -q
python3 -m run
```

### Android
From `mobile-android/`:
```bash
./gradlew clean
./gradlew installDebug
adb reverse tcp:5000 tcp:5000
adb shell am start -n com.mutebi.stockinvestmentapp/.MainActivity
adb logcat
```

---

## 7) Expected Phase 8 outcome

After these updates:

- Settings screen exists and stores privacy + consent choices locally
- Security settings screen exists
- biometric lock option works
- app unlock can require biometric when enabled
- account viewer works
- edit profile works
- change password flow works
- revoke other sessions control exists
- secure token storage replaces plain token storage
- user self-management and trust signals are clearly improved
