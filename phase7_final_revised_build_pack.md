# Phase 7 — Education Hub, Notifications, and Responsible Investing Layer

This pack follows your requested structure:

- **keep as-is**
- **targeted edits only**
- **new files to create fully**

It is written against your current Phase 6 codebase and keeps previous phases intact unless Phase 7 needs a real update.

---

## 1) Keep as-is

These files already fit Phase 7 well enough and do **not** need changes right now:

### Android
- `app/src/main/java/com/mutebi/stockinvestmentapp/navigation/BottomNavItem.kt`
- `app/src/main/java/com/mutebi/stockinvestmentapp/features/home/dashboard/DashboardViewModel.kt`
- `app/src/main/java/com/mutebi/stockinvestmentapp/features/trade/order/TradeOrderViewModel.kt`
- `app/src/main/java/com/mutebi/stockinvestmentapp/features/trade/order/TradeOrderUiState.kt`

### Backend
- `backend-api/app/models/notification.py`
- `backend-api/app/routes/notifications.py`
- `backend-api/app/services/notification_service.py`

---

## 2) Targeted edits only

These files already exist, but they need Phase 7 updates.

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
                Phase7PlaceholderScreen(
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
                Phase7PlaceholderScreen(
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
                Phase7PlaceholderScreen(
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
    }
}

@Composable
private fun Phase7PlaceholderScreen(
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

## File: `mobile-android/app/src/main/java/com/mutebi/stockinvestmentapp/data/remote/network/RetrofitProvider.kt`

Replace with:

```kotlin
package com.mutebi.stockinvestmentapp.data.remote.network

import android.app.Application
import com.google.gson.GsonBuilder
import com.mutebi.stockinvestmentapp.core.constants.ApiConstants
import com.mutebi.stockinvestmentapp.data.remote.api.AssetApi
import com.mutebi.stockinvestmentapp.data.remote.api.AuthApi
import com.mutebi.stockinvestmentapp.data.remote.api.EducationApi
import com.mutebi.stockinvestmentapp.data.remote.api.KycApi
import com.mutebi.stockinvestmentapp.data.remote.api.NotificationApi
import com.mutebi.stockinvestmentapp.data.remote.api.PortfolioApi
import com.mutebi.stockinvestmentapp.data.remote.api.TradeApi
import com.mutebi.stockinvestmentapp.data.remote.api.TransactionApi
import com.mutebi.stockinvestmentapp.data.remote.api.UserApi
import com.mutebi.stockinvestmentapp.data.remote.api.WatchlistApi
import com.mutebi.stockinvestmentapp.data.session.SessionManager
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitProvider {

    fun provideRetrofit(authInterceptor: AuthInterceptor): Retrofit {
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        val okHttpClient = OkHttpClient.Builder()
            .connectTimeout(ApiConstants.TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .readTimeout(ApiConstants.TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .writeTimeout(ApiConstants.TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .addInterceptor(authInterceptor)
            .addInterceptor(logging)
            .build()

        return Retrofit.Builder()
            .baseUrl(ApiConstants.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(
                GsonConverterFactory.create(GsonBuilder().create())
            )
            .build()
    }

    private fun retrofit(application: Application): Retrofit {
        return provideRetrofit(
            AuthInterceptor(SessionManager(application))
        )
    }

    fun authApi(application: Application): AuthApi =
        retrofit(application).create(AuthApi::class.java)

    fun userApi(application: Application): UserApi =
        retrofit(application).create(UserApi::class.java)

    fun kycApi(application: Application): KycApi =
        retrofit(application).create(KycApi::class.java)

    fun assetApi(application: Application): AssetApi =
        retrofit(application).create(AssetApi::class.java)

    fun watchlistApi(application: Application): WatchlistApi =
        retrofit(application).create(WatchlistApi::class.java)

    fun portfolioApi(application: Application): PortfolioApi =
        retrofit(application).create(PortfolioApi::class.java)

    fun tradeApi(application: Application): TradeApi =
        retrofit(application).create(TradeApi::class.java)

    fun transactionApi(application: Application): TransactionApi =
        retrofit(application).create(TransactionApi::class.java)

    fun educationApi(application: Application): EducationApi =
        retrofit(application).create(EducationApi::class.java)

    fun notificationApi(application: Application): NotificationApi =
        retrofit(application).create(NotificationApi::class.java)
}
```

---

## File: `mobile-android/app/src/main/java/com/mutebi/stockinvestmentapp/di/NetworkModule.kt`

Replace with:

```kotlin
package com.mutebi.stockinvestmentapp.di

import com.mutebi.stockinvestmentapp.data.remote.api.AssetApi
import com.mutebi.stockinvestmentapp.data.remote.api.AuthApi
import com.mutebi.stockinvestmentapp.data.remote.api.EducationApi
import com.mutebi.stockinvestmentapp.data.remote.api.KycApi
import com.mutebi.stockinvestmentapp.data.remote.api.NotificationApi
import com.mutebi.stockinvestmentapp.data.remote.api.PortfolioApi
import com.mutebi.stockinvestmentapp.data.remote.api.TradeApi
import com.mutebi.stockinvestmentapp.data.remote.api.TransactionApi
import com.mutebi.stockinvestmentapp.data.remote.api.UserApi
import com.mutebi.stockinvestmentapp.data.remote.api.WatchlistApi
import com.mutebi.stockinvestmentapp.data.remote.network.AuthInterceptor
import com.mutebi.stockinvestmentapp.data.remote.network.RetrofitProvider
import com.mutebi.stockinvestmentapp.data.session.SessionManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideAuthInterceptor(
        sessionManager: SessionManager
    ): AuthInterceptor = AuthInterceptor(sessionManager)

    @Provides
    @Singleton
    fun provideRetrofit(
        authInterceptor: AuthInterceptor
    ): Retrofit = RetrofitProvider.provideRetrofit(authInterceptor)

    @Provides
    fun provideAuthApi(retrofit: Retrofit): AuthApi =
        retrofit.create(AuthApi::class.java)

    @Provides
    fun provideUserApi(retrofit: Retrofit): UserApi =
        retrofit.create(UserApi::class.java)

    @Provides
    fun provideKycApi(retrofit: Retrofit): KycApi =
        retrofit.create(KycApi::class.java)

    @Provides
    fun provideAssetApi(retrofit: Retrofit): AssetApi =
        retrofit.create(AssetApi::class.java)

    @Provides
    fun providePortfolioApi(retrofit: Retrofit): PortfolioApi =
        retrofit.create(PortfolioApi::class.java)

    @Provides
    fun provideWatchlistApi(retrofit: Retrofit): WatchlistApi =
        retrofit.create(WatchlistApi::class.java)

    @Provides
    fun provideTradeApi(retrofit: Retrofit): TradeApi =
        retrofit.create(TradeApi::class.java)

    @Provides
    fun provideTransactionApi(retrofit: Retrofit): TransactionApi =
        retrofit.create(TransactionApi::class.java)

    @Provides
    fun provideEducationApi(retrofit: Retrofit): EducationApi =
        retrofit.create(EducationApi::class.java)

    @Provides
    fun provideNotificationApi(retrofit: Retrofit): NotificationApi =
        retrofit.create(NotificationApi::class.java)
}
```

---

## File: `mobile-android/app/src/main/java/com/mutebi/stockinvestmentapp/di/RepositoryModule.kt`

Replace with:

```kotlin
package com.mutebi.stockinvestmentapp.di

import com.mutebi.stockinvestmentapp.data.remote.api.AssetApi
import com.mutebi.stockinvestmentapp.data.remote.api.AuthApi
import com.mutebi.stockinvestmentapp.data.remote.api.EducationApi
import com.mutebi.stockinvestmentapp.data.remote.api.KycApi
import com.mutebi.stockinvestmentapp.data.remote.api.NotificationApi
import com.mutebi.stockinvestmentapp.data.remote.api.PortfolioApi
import com.mutebi.stockinvestmentapp.data.remote.api.TradeApi
import com.mutebi.stockinvestmentapp.data.remote.api.TransactionApi
import com.mutebi.stockinvestmentapp.data.remote.api.UserApi
import com.mutebi.stockinvestmentapp.data.remote.api.WatchlistApi
import com.mutebi.stockinvestmentapp.data.repository.AssetRepository
import com.mutebi.stockinvestmentapp.data.repository.AuthRepository
import com.mutebi.stockinvestmentapp.data.repository.EducationRepository
import com.mutebi.stockinvestmentapp.data.repository.KycRepository
import com.mutebi.stockinvestmentapp.data.repository.NotificationRepository
import com.mutebi.stockinvestmentapp.data.repository.PortfolioRepository
import com.mutebi.stockinvestmentapp.data.repository.TradeRepository
import com.mutebi.stockinvestmentapp.data.repository.TransactionRepository
import com.mutebi.stockinvestmentapp.data.repository.UserRepository
import com.mutebi.stockinvestmentapp.data.repository.WatchlistRepository
import com.mutebi.stockinvestmentapp.data.session.SessionManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    fun provideAuthRepository(
        authApi: AuthApi,
        sessionManager: SessionManager
    ): AuthRepository = AuthRepository(authApi, sessionManager)

    @Provides
    fun provideAssetRepository(
        assetApi: AssetApi
    ): AssetRepository = AssetRepository(assetApi)

    @Provides
    fun providePortfolioRepository(
        portfolioApi: PortfolioApi
    ): PortfolioRepository = PortfolioRepository(portfolioApi)

    @Provides
    fun provideWatchlistRepository(
        watchlistApi: WatchlistApi
    ): WatchlistRepository = WatchlistRepository(watchlistApi)

    @Provides
    fun provideUserRepository(
        userApi: UserApi
    ): UserRepository = UserRepository(userApi)

    @Provides
    fun provideKycRepository(
        kycApi: KycApi
    ): KycRepository = KycRepository(kycApi)

    @Provides
    fun provideTradeRepository(
        tradeApi: TradeApi
    ): TradeRepository = TradeRepository(tradeApi)

    @Provides
    fun provideTransactionRepository(
        transactionApi: TransactionApi
    ): TransactionRepository = TransactionRepository(transactionApi)

    @Provides
    fun provideEducationRepository(
        educationApi: EducationApi
    ): EducationRepository = EducationRepository(educationApi)

    @Provides
    fun provideNotificationRepository(
        notificationApi: NotificationApi
    ): NotificationRepository = NotificationRepository(notificationApi)
}
```

---

## File: `mobile-android/app/src/main/java/com/mutebi/stockinvestmentapp/features/home/dashboard/DashboardScreen.kt`

Replace with:

```kotlin
package com.mutebi.stockinvestmentapp.features.home.dashboard

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.mutebi.stockinvestmentapp.features.home.components.EducationPromptCard
import com.mutebi.stockinvestmentapp.features.home.components.MarketHighlightsCard
import com.mutebi.stockinvestmentapp.features.home.components.PortfolioSummaryCard
import com.mutebi.stockinvestmentapp.features.home.components.WatchlistPreviewCard

@Composable
fun DashboardScreen(
    onOpenMarket: () -> Unit,
    onOpenWatchlist: () -> Unit,
    onOpenEducation: () -> Unit,
    onOpenNotifications: () -> Unit,
    vm: DashboardViewModel = hiltViewModel()
) {
    val state by vm.uiState.collectAsState()

    if (state.isLoading) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            CircularProgressIndicator()
            Text("Loading dashboard...")
        }
        return
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Dashboard")

        PortfolioSummaryCard(
            trackedAssets = state.topAssets.size,
            watchlistCount = state.watchlistItems.size,
            onOpenWatchlist = onOpenWatchlist
        )

        MarketHighlightsCard(
            assets = state.topAssets,
            onOpenMarket = onOpenMarket
        )

        WatchlistPreviewCard(
            items = state.watchlistItems,
            onOpenWatchlist = onOpenWatchlist
        )

        EducationPromptCard(
            onOpenEducation = onOpenEducation,
            onOpenNotifications = onOpenNotifications
        )

        state.error?.let { errorMessage ->
            Text(
                text = errorMessage,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}
```

---

## File: `mobile-android/app/src/main/java/com/mutebi/stockinvestmentapp/features/home/components/EducationPromptCard.kt`

Replace with:

```kotlin
package com.mutebi.stockinvestmentapp.features.home.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun EducationPromptCard(
    onOpenEducation: () -> Unit,
    onOpenNotifications: () -> Unit
) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text("Responsible investing")
            Text("Learn before acting. Good investing starts with understanding risk, time horizon, and diversification.")
            Text("Beginner reminder: never invest money you cannot afford to keep invested for the long term.")

            Button(onClick = onOpenEducation) {
                Text("Open education hub")
            }

            Button(onClick = onOpenNotifications) {
                Text("Open notifications")
            }
        }
    }
}
```

---

## File: `mobile-android/app/src/main/java/com/mutebi/stockinvestmentapp/features/trade/order/TradeOrderScreen.kt`

Replace with:

```kotlin
package com.mutebi.stockinvestmentapp.features.trade.order

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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mutebi.stockinvestmentapp.features.education.components.RiskNoticeBanner
import com.mutebi.stockinvestmentapp.features.trade.components.OrderSummaryCard
import com.mutebi.stockinvestmentapp.features.trade.components.QuantitySelector
import com.mutebi.stockinvestmentapp.features.trade.components.TradeActionTabs

@Composable
fun TradeOrderScreen(
    assetId: Int,
    vm: TradeOrderViewModel,
    onBack: () -> Unit,
    onContinue: () -> Unit
) {
    val state by vm.uiState.collectAsState()

    LaunchedEffect(assetId) {
        vm.clearResult()
        vm.loadAsset(assetId)
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

        Text("Trade order")

        RiskNoticeBanner(
            text = "Risk warning: market prices can move quickly. Review your trade size carefully and make sure it matches your goals."
        )

        if (state.isLoading) {
            CircularProgressIndicator()
        } else {
            val asset = state.asset
            if (asset == null) {
                Text(state.error ?: "Asset unavailable.")
            } else {
                Text("${asset.symbol} • ${asset.name}")
                Text("Current price: $${"%.2f".format(asset.price)}")

                TradeActionTabs(
                    selected = state.tradeType,
                    onSelectedChange = vm::onTradeTypeChange
                )

                QuantitySelector(
                    value = state.quantityText,
                    onValueChange = vm::onQuantityChange
                )

                OrderSummaryCard(
                    asset = asset,
                    tradeType = state.tradeType,
                    quantityText = state.quantityText
                )

                Button(
                    onClick = {
                        if (vm.canProceedToConfirm()) {
                            onContinue()
                        } else {
                            vm.onQuantityChange(state.quantityText)
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Continue")
                }
            }
        }

        state.error?.let {
            Text(it)
        }
    }
}
```

---

## 3) New files to create fully

---

## File: `mobile-android/app/src/main/java/com/mutebi/stockinvestmentapp/data/remote/api/EducationApi.kt`

```kotlin
package com.mutebi.stockinvestmentapp.data.remote.api

import com.mutebi.stockinvestmentapp.data.remote.dto.ApiEnvelopeDto
import com.mutebi.stockinvestmentapp.data.remote.dto.EducationArticleDto
import com.mutebi.stockinvestmentapp.data.remote.dto.EducationListPayloadDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface EducationApi {
    @GET("api/v1/education")
    suspend fun getArticles(
        @Query("q") query: String? = null,
        @Query("topic") topic: String? = null
    ): Response<ApiEnvelopeDto<EducationListPayloadDto>>

    @GET("api/v1/education/{articleId}")
    suspend fun getArticleById(
        @Path("articleId") articleId: Int
    ): Response<ApiEnvelopeDto<EducationArticleDto>>
}
```

---

## File: `mobile-android/app/src/main/java/com/mutebi/stockinvestmentapp/data/remote/api/NotificationApi.kt`

```kotlin
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
```

---

## File: `mobile-android/app/src/main/java/com/mutebi/stockinvestmentapp/data/remote/dto/EducationDtos.kt`

```kotlin
package com.mutebi.stockinvestmentapp.data.remote.dto

import com.google.gson.annotations.SerializedName
import com.mutebi.stockinvestmentapp.domain.model.EducationArticle

data class EducationListPayloadDto(
    @SerializedName("items")
    val items: List<EducationArticleDto> = emptyList()
)

data class EducationArticleDto(
    @SerializedName("id")
    val id: Int = 0,
    @SerializedName("title")
    val title: String = "",
    @SerializedName("summary")
    val summary: String = "",
    @SerializedName("content")
    val content: String = "",
    @SerializedName("topic")
    val topic: String = "",
    @SerializedName("risk_warning")
    val riskWarning: String? = null,
    @SerializedName("is_beginner_friendly")
    val isBeginnerFriendly: Boolean = true
) {
    fun toDomain(): EducationArticle {
        return EducationArticle(
            id = id,
            title = title,
            summary = summary,
            content = content,
            topic = topic,
            riskWarning = riskWarning.orEmpty(),
            isBeginnerFriendly = isBeginnerFriendly
        )
    }
}
```

---

## File: `mobile-android/app/src/main/java/com/mutebi/stockinvestmentapp/data/remote/dto/NotificationDtos.kt`

```kotlin
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
```

---

## File: `mobile-android/app/src/main/java/com/mutebi/stockinvestmentapp/data/repository/EducationRepository.kt`

```kotlin
package com.mutebi.stockinvestmentapp.data.repository

import com.mutebi.stockinvestmentapp.core.utils.Resource
import com.mutebi.stockinvestmentapp.data.remote.api.EducationApi
import com.mutebi.stockinvestmentapp.domain.model.EducationArticle
import javax.inject.Inject

class EducationRepository @Inject constructor(
    private val api: EducationApi
) {
    suspend fun getArticles(
        query: String? = null,
        topic: String? = null
    ): Resource<List<EducationArticle>> {
        return try {
            val response = api.getArticles(query = query, topic = topic)
            val body = response.body()
            val items = body?.data?.items.orEmpty().map { it.toDomain() }

            if (response.isSuccessful && body?.success == true) {
                Resource.Success(items)
            } else {
                Resource.Error(body?.message ?: "Unable to load education articles")
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Unable to load education articles")
        }
    }

    suspend fun getArticleById(articleId: Int): Resource<EducationArticle> {
        return try {
            val response = api.getArticleById(articleId)
            val body = response.body()
            val dto = body?.data

            if (response.isSuccessful && body?.success == true && dto != null) {
                Resource.Success(dto.toDomain())
            } else {
                Resource.Error(body?.message ?: "Unable to load article")
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Unable to load article")
        }
    }
}
```

---

## File: `mobile-android/app/src/main/java/com/mutebi/stockinvestmentapp/data/repository/NotificationRepository.kt`

```kotlin
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
```

---

## File: `mobile-android/app/src/main/java/com/mutebi/stockinvestmentapp/domain/model/EducationArticle.kt`

```kotlin
package com.mutebi.stockinvestmentapp.domain.model

data class EducationArticle(
    val id: Int = 0,
    val title: String = "",
    val summary: String = "",
    val content: String = "",
    val topic: String = "",
    val riskWarning: String = "",
    val isBeginnerFriendly: Boolean = true
)
```

---

## File: `mobile-android/app/src/main/java/com/mutebi/stockinvestmentapp/domain/model/AppNotification.kt`

```kotlin
package com.mutebi.stockinvestmentapp.domain.model

data class AppNotification(
    val id: Int = 0,
    val title: String = "",
    val body: String = "",
    val notificationType: String = "",
    val isRead: Boolean = false,
    val createdAt: String = ""
)
```

---

## File: `mobile-android/app/src/main/java/com/mutebi/stockinvestmentapp/features/education/hub/EducationHubUiState.kt`

```kotlin
package com.mutebi.stockinvestmentapp.features.education.hub

import com.mutebi.stockinvestmentapp.domain.model.EducationArticle

data class EducationHubUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val query: String = "",
    val selectedTopic: String = "All",
    val articles: List<EducationArticle> = emptyList(),
    val visibleArticles: List<EducationArticle> = emptyList(),
    val topics: List<String> = listOf("All")
)
```

---

## File: `mobile-android/app/src/main/java/com/mutebi/stockinvestmentapp/features/education/hub/EducationHubViewModel.kt`

```kotlin
package com.mutebi.stockinvestmentapp.features.education.hub

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.mutebi.stockinvestmentapp.core.utils.Resource
import com.mutebi.stockinvestmentapp.data.remote.network.RetrofitProvider
import com.mutebi.stockinvestmentapp.data.repository.EducationRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class EducationHubViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = EducationRepository(
        RetrofitProvider.educationApi(application)
    )

    private val _uiState = MutableStateFlow(EducationHubUiState(isLoading = true))
    val uiState: StateFlow<EducationHubUiState> = _uiState

    init {
        loadArticles()
    }

    fun loadArticles() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)

            when (val result = repository.getArticles()) {
                is Resource.Success -> {
                    val items = result.data.orEmpty()
                    val topics = listOf("All") + items.map { it.topic }.filter { it.isNotBlank() }.distinct().sorted()

                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        articles = items,
                        topics = topics
                    )
                    applyFilters()
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

    fun onQueryChange(value: String) {
        _uiState.value = _uiState.value.copy(query = value)
        applyFilters()
    }

    fun onTopicChange(value: String) {
        _uiState.value = _uiState.value.copy(selectedTopic = value)
        applyFilters()
    }

    private fun applyFilters() {
        val current = _uiState.value
        val query = current.query.trim().lowercase()

        val filtered = current.articles.filter { article ->
            val matchesQuery = query.isBlank() ||
                article.title.lowercase().contains(query) ||
                article.summary.lowercase().contains(query) ||
                article.content.lowercase().contains(query)

            val matchesTopic = current.selectedTopic == "All" || article.topic == current.selectedTopic

            matchesQuery && matchesTopic
        }

        _uiState.value = current.copy(visibleArticles = filtered)
    }
}
```

---

## File: `mobile-android/app/src/main/java/com/mutebi/stockinvestmentapp/features/education/hub/EducationHubScreen.kt`

```kotlin
package com.mutebi.stockinvestmentapp.features.education.hub

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mutebi.stockinvestmentapp.features.education.components.EducationCard
import com.mutebi.stockinvestmentapp.features.education.components.RiskNoticeBanner
import com.mutebi.stockinvestmentapp.features.education.components.TopicChipRow

@Composable
fun EducationHubScreen(
    onBack: () -> Unit,
    onOpenNotifications: () -> Unit,
    onOpenArticle: (Int) -> Unit,
    vm: EducationHubViewModel = viewModel()
) {
    val state by vm.uiState.collectAsState()

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            TextButton(onClick = onBack) {
                Text("Back")
            }
        }

        item {
            Text("Education hub")
        }

        item {
            RiskNoticeBanner(
                text = "Responsible investing reminder: education should guide decisions, not replace careful judgment or personal affordability checks."
            )
        }

        item {
            OutlinedTextField(
                value = state.query,
                onValueChange = vm::onQueryChange,
                label = { Text("Search lessons and tips") },
                singleLine = true
            )
        }

        item {
            TopicChipRow(
                topics = state.topics,
                selectedTopic = state.selectedTopic,
                onTopicSelected = vm::onTopicChange
            )
        }

        item {
            TextButton(onClick = onOpenNotifications) {
                Text("Open notification center")
            }
        }

        if (state.isLoading) {
            item {
                CircularProgressIndicator()
            }
        } else if (state.visibleArticles.isEmpty()) {
            item {
                Text("No education articles available yet.")
            }
        } else {
            items(state.visibleArticles, key = { it.id }) { article ->
                EducationCard(
                    article = article,
                    onClick = { onOpenArticle(article.id) }
                )
            }
        }

        state.error?.let { errorMessage ->
            item {
                Text(errorMessage)
            }
        }
    }
}
```

---

## File: `mobile-android/app/src/main/java/com/mutebi/stockinvestmentapp/features/education/article/ArticleDetailViewModel.kt`

```kotlin
package com.mutebi.stockinvestmentapp.features.education.article

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.mutebi.stockinvestmentapp.core.utils.Resource
import com.mutebi.stockinvestmentapp.data.remote.network.RetrofitProvider
import com.mutebi.stockinvestmentapp.data.repository.EducationRepository
import com.mutebi.stockinvestmentapp.domain.model.EducationArticle
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class ArticleDetailUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val article: EducationArticle? = null
)

class ArticleDetailViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = EducationRepository(
        RetrofitProvider.educationApi(application)
    )

    private val _uiState = MutableStateFlow(ArticleDetailUiState(isLoading = true))
    val uiState: StateFlow<ArticleDetailUiState> = _uiState

    fun loadArticle(articleId: Int) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)

            when (val result = repository.getArticleById(articleId)) {
                is Resource.Success -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        article = result.data
                    )
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
}
```

---

## File: `mobile-android/app/src/main/java/com/mutebi/stockinvestmentapp/features/education/article/ArticleDetailScreen.kt`

```kotlin
package com.mutebi.stockinvestmentapp.features.education.article

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mutebi.stockinvestmentapp.features.education.components.RiskNoticeBanner

@Composable
fun ArticleDetailScreen(
    articleId: Int,
    onBack: () -> Unit,
    vm: ArticleDetailViewModel = viewModel()
) {
    val state by vm.uiState.collectAsState()

    LaunchedEffect(articleId) {
        vm.loadArticle(articleId)
    }

    if (state.isLoading) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            CircularProgressIndicator()
            Text("Loading article...")
        }
        return
    }

    val article = state.article
    if (article == null) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(state.error ?: "Article unavailable.")
            TextButton(onClick = onBack) {
                Text("Back")
            }
        }
        return
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        TextButton(onClick = onBack) {
            Text("Back")
        }

        Text(article.title)
        Text("Topic: ${article.topic}")

        if (article.isBeginnerFriendly) {
            Text("Beginner-friendly")
        }

        if (article.riskWarning.isNotBlank()) {
            RiskNoticeBanner(text = article.riskWarning)
        }

        Text(article.summary)
        Text(article.content)
    }
}
```

---

## File: `mobile-android/app/src/main/java/com/mutebi/stockinvestmentapp/features/education/components/EducationCard.kt`

```kotlin
package com.mutebi.stockinvestmentapp.features.education.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.mutebi.stockinvestmentapp.domain.model.EducationArticle

@Composable
fun EducationCard(
    article: EducationArticle,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(article.title)
            Text(article.summary)
            Text("Topic: ${article.topic}")
            if (article.isBeginnerFriendly) {
                Text("Beginner-friendly")
            }
        }
    }
}
```

---

## File: `mobile-android/app/src/main/java/com/mutebi/stockinvestmentapp/features/education/components/TopicChipRow.kt`

```kotlin
package com.mutebi.stockinvestmentapp.features.education.components

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun TopicChipRow(
    topics: List<String>,
    selectedTopic: String,
    onTopicSelected: (String) -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.horizontalScroll(rememberScrollState())
    ) {
        topics.forEach { topic ->
            FilterChip(
                selected = selectedTopic == topic,
                onClick = { onTopicSelected(topic) },
                label = { Text(topic) }
            )
        }
    }
}
```

---

## File: `mobile-android/app/src/main/java/com/mutebi/stockinvestmentapp/features/education/components/RiskNoticeBanner.kt`

```kotlin
package com.mutebi.stockinvestmentapp.features.education.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun RiskNoticeBanner(
    text: String
) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Text(text)
    }
}
```

---

## File: `mobile-android/app/src/main/java/com/mutebi/stockinvestmentapp/features/notifications/NotificationCenterUiState.kt`

```kotlin
package com.mutebi.stockinvestmentapp.features.notifications

import com.mutebi.stockinvestmentapp.domain.model.AppNotification

data class NotificationCenterUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val notifications: List<AppNotification> = emptyList()
)
```

---

## File: `mobile-android/app/src/main/java/com/mutebi/stockinvestmentapp/features/notifications/NotificationCenterViewModel.kt`

```kotlin
package com.mutebi.stockinvestmentapp.features.notifications

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.mutebi.stockinvestmentapp.core.utils.Resource
import com.mutebi.stockinvestmentapp.data.remote.network.RetrofitProvider
import com.mutebi.stockinvestmentapp.data.repository.NotificationRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class NotificationCenterViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = NotificationRepository(
        RetrofitProvider.notificationApi(application)
    )

    private val _uiState = MutableStateFlow(NotificationCenterUiState(isLoading = true))
    val uiState: StateFlow<NotificationCenterUiState> = _uiState

    init {
        loadNotifications()
    }

    fun loadNotifications() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)

            when (val result = repository.getNotifications()) {
                is Resource.Success -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        notifications = result.data.orEmpty()
                    )
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

    fun markAsRead(notificationId: Int) {
        viewModelScope.launch {
            when (val result = repository.markAsRead(notificationId)) {
                is Resource.Success -> {
                    val updated = result.data
                    _uiState.value = _uiState.value.copy(
                        notifications = _uiState.value.notifications.map {
                            if (it.id == updated?.id) updated else it
                        }
                    )
                }
                is Resource.Error -> {
                    _uiState.value = _uiState.value.copy(error = result.message)
                }
                else -> Unit
            }
        }
    }
}
```

---

## File: `mobile-android/app/src/main/java/com/mutebi/stockinvestmentapp/features/notifications/NotificationCenterScreen.kt`

```kotlin
package com.mutebi.stockinvestmentapp.features.notifications

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun NotificationCenterScreen(
    onBack: () -> Unit,
    vm: NotificationCenterViewModel = viewModel()
) {
    val state by vm.uiState.collectAsState()

    LazyColumn(
        contentPadding = PaddingValues(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            TextButton(onClick = onBack) {
                Text("Back")
            }
        }

        item {
            Text("Notification center")
        }

        if (state.isLoading) {
            item {
                CircularProgressIndicator()
            }
        } else if (state.notifications.isEmpty()) {
            item {
                Text("No notifications yet.")
            }
        } else {
            items(state.notifications, key = { it.id }) { notification ->
                androidx.compose.material3.Card {
                    androidx.compose.foundation.layout.Column(
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(notification.title)
                        Text(notification.body)
                        Text("Type: ${notification.notificationType}")
                        Text("Created: ${notification.createdAt}")
                        Text(if (notification.isRead) "Read" else "Unread")

                        if (!notification.isRead) {
                            Button(onClick = { vm.markAsRead(notification.id) }) {
                                Text("Mark as read")
                            }
                        }
                    }
                }
            }
        }

        state.error?.let { errorMessage ->
            item {
                Text(errorMessage)
            }
        }
    }
}
```

---

## File: `backend-api/app/models/education.py`

```python
from datetime import datetime

from app.extensions import db


class EducationArticle(db.Model):
    __tablename__ = "education_articles"

    id = db.Column(db.Integer, primary_key=True)
    title = db.Column(db.String(255), nullable=False)
    summary = db.Column(db.Text, nullable=False)
    content = db.Column(db.Text, nullable=False)
    topic = db.Column(db.String(100), nullable=False, default="General")
    risk_warning = db.Column(db.Text, nullable=True)
    is_beginner_friendly = db.Column(db.Boolean, nullable=False, default=True)
    created_at = db.Column(db.DateTime, nullable=False, default=datetime.utcnow)
    updated_at = db.Column(
        db.DateTime,
        nullable=False,
        default=datetime.utcnow,
        onupdate=datetime.utcnow,
    )

    def to_dict(self) -> dict:
        return {
            "id": self.id,
            "title": self.title,
            "summary": self.summary,
            "content": self.content,
            "topic": self.topic,
            "risk_warning": self.risk_warning,
            "is_beginner_friendly": self.is_beginner_friendly,
            "created_at": self.created_at.isoformat() if self.created_at else None,
            "updated_at": self.updated_at.isoformat() if self.updated_at else None,
        }
```

---

## File: `backend-api/app/routes/education.py`

```python
from flask import Blueprint, request

from app.services.education_service import EducationService
from app.utils.response import error_response, success_response

education_bp = Blueprint("education", __name__)


@education_bp.get("")
def list_education():
    query = request.args.get("q")
    topic = request.args.get("topic")

    articles = EducationService.list_articles(
        query=query,
        topic=topic,
    )

    return success_response(
        "Education articles retrieved successfully.",
        {"items": [article.to_dict() for article in articles]},
    )


@education_bp.get("/<int:article_id>")
def get_education_article(article_id: int):
    article = EducationService.get_article(article_id)
    if not article:
        return error_response("Education article not found.", status_code=404)

    return success_response(
        "Education article retrieved successfully.",
        article.to_dict(),
    )
```

---

## File: `backend-api/app/services/education_service.py`

```python
from sqlalchemy import or_

from app.models.education import EducationArticle


class EducationService:
    @staticmethod
    def list_articles(query: str | None = None, topic: str | None = None):
        db_query = EducationArticle.query

        if topic:
            db_query = db_query.filter(EducationArticle.topic.ilike(topic))

        if query:
            db_query = db_query.filter(
                or_(
                    EducationArticle.title.ilike(f"%{query}%"),
                    EducationArticle.summary.ilike(f"%{query}%"),
                    EducationArticle.content.ilike(f"%{query}%"),
                )
            )

        return db_query.order_by(EducationArticle.created_at.desc()).all()

    @staticmethod
    def get_article(article_id: int):
        return EducationArticle.query.get(article_id)
```

---

## File: `backend-api/tests/test_education.py`

```python
from app.extensions import db
from app.models.education import EducationArticle


def test_list_education_articles(client):
    article = EducationArticle(
        title="Responsible Investing Basics",
        summary="A starter lesson for new investors.",
        content="Long-form lesson content goes here.",
        topic="Beginner",
        risk_warning="Markets can rise and fall. Learn before acting.",
        is_beginner_friendly=True,
    )
    db.session.add(article)
    db.session.commit()

    response = client.get("/api/v1/education")
    body = response.get_json() or {}

    assert response.status_code == 200, body
    assert body.get("success") is True

    data = body.get("data") or {}
    items = data.get("items") or []
    assert any(item.get("title") == "Responsible Investing Basics" for item in items)


def test_get_single_education_article(client):
    article = EducationArticle(
        title="Understanding Risk",
        summary="A short article about investment risk.",
        content="Diversification, time horizon, and volatility matter.",
        topic="Risk",
        risk_warning="Past performance does not guarantee future results.",
        is_beginner_friendly=True,
    )
    db.session.add(article)
    db.session.commit()

    response = client.get(f"/api/v1/education/{article.id}")
    body = response.get_json() or {}

    assert response.status_code == 200, body
    assert body.get("success") is True

    data = body.get("data") or {}
    assert data.get("id") == article.id
    assert data.get("title") == "Understanding Risk"
```

---

## File: `backend-api/tests/test_notifications.py`

```python
import uuid

from app.services.notification_service import NotificationService


def _unique_email() -> str:
    return f"phase7_notifications_{uuid.uuid4().hex[:10]}@example.com"


def _register_user(client, email: str, password: str = "Password123!"):
    return client.post(
        "/api/v1/auth/register",
        json={
            "email": email,
            "password": password,
            "full_name": "Phase Seven Notification User",
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


def _auth_context(client):
    email = _unique_email()

    register_response = _register_user(client, email)
    assert register_response.status_code in (200, 201), register_response.get_json()

    login_response = _login_user(client, email)
    assert login_response.status_code == 200, login_response.get_json()

    body = login_response.get_json() or {}
    data = body.get("data") or {}
    token = data.get("access_token") or data.get("token")
    user = data.get("user") or {}
    user_id = user.get("id")
    assert token and user_id, body

    headers = {
        "Authorization": f"Bearer {token}",
        "Content-Type": "application/json",
    }
    return headers, user_id


def test_list_notifications(client):
    headers, user_id = _auth_context(client)

    NotificationService.create_notification(
        user_id=user_id,
        title="Trade completed",
        body="Your recent trade has been completed.",
        notification_type="trade",
    )

    response = client.get("/api/v1/notifications", headers=headers)
    body = response.get_json() or {}

    assert response.status_code == 200, body
    assert body.get("success") is True

    data = body.get("data") or {}
    items = data.get("items") or []
    assert len(items) >= 1


def test_mark_notification_read(client):
    headers, user_id = _auth_context(client)

    notification = NotificationService.create_notification(
        user_id=user_id,
        title="KYC submitted",
        body="Your KYC review is pending.",
        notification_type="kyc",
    )

    response = client.patch(
        f"/api/v1/notifications/{notification.id}/read",
        headers=headers,
    )
    body = response.get_json() or {}

    assert response.status_code == 200, body
    assert body.get("success") is True

    data = body.get("data") or {}
    assert data.get("id") == notification.id
    assert data.get("is_read") is True
```

---

## 4) New directories to create

### From `mobile-android/`
```bash
mkdir -p app/src/main/java/com/mutebi/stockinvestmentapp/features/education/hub
mkdir -p app/src/main/java/com/mutebi/stockinvestmentapp/features/education/article
mkdir -p app/src/main/java/com/mutebi/stockinvestmentapp/features/education/components
mkdir -p app/src/main/java/com/mutebi/stockinvestmentapp/features/notifications
mkdir -p app/src/main/java/com/mutebi/stockinvestmentapp/data/remote/api
mkdir -p app/src/main/java/com/mutebi/stockinvestmentapp/data/remote/dto
mkdir -p app/src/main/java/com/mutebi/stockinvestmentapp/data/repository
mkdir -p app/src/main/java/com/mutebi/stockinvestmentapp/domain/model
```

### From `backend-api/`
```bash
mkdir -p app/models
mkdir -p app/routes
mkdir -p app/services
mkdir -p tests
```

---

## 5) Backend wire-up notes

These are small but important:

1. If your Flask app uses a central blueprint registration file, register `education_bp` there.
2. If your backend imports models centrally, make sure `EducationArticle` is imported so migrations can see it.
3. Run a migration after adding `education.py`.

---

## 6) Verification sequence

### Backend
From `backend-api/`:
```bash
source env/bin/activate
pytest tests/test_education.py tests/test_notifications.py -q
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

## 7) Expected Phase 7 outcome

After these updates:

- education hub opens and lists articles
- article detail opens
- dashboard links into education hub and notification center
- trade flow shows a visible risk notice
- notification center loads notifications and can mark them as read
- education content makes the app feel guided, not only transactional
- project becomes more aligned with its responsible-investing purpose
