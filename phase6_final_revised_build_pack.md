# Phase 6 — Final Revised Build Pack

This pack is based on the **actual current files you shared** for Phase 6.

It follows your rule:
- existing files get **targeted/full updates only where necessary**
- brand-new Phase 6 files are created fully
- previous-phase files are not repeated unless the current audited state shows they still need updating for Phase 6 to compile and work

---

## 1) Audit summary

### Keep as-is
These backend files are already good enough for Phase 6 and do not need changes:

- `backend-api/app/models/portfolio.py`
- `backend-api/app/models/holding.py`
- `backend-api/app/models/trade.py`
- `backend-api/app/routes/portfolio.py`
- `backend-api/app/routes/trades.py`
- `backend-api/app/services/portfolio_service.py`
- `backend-api/app/services/trade_service.py`
- `backend-api/app/repositories/portfolio_repository.py`
- `backend-api/app/repositories/trade_repository.py`

These Android files are fine as-is for Phase 6:

- `app/src/main/java/com/mutebi/stockinvestmentapp/navigation/BottomNavItem.kt`
- `app/src/main/java/com/mutebi/stockinvestmentapp/features/home/dashboard/DashboardScreen.kt`
- `app/src/main/java/com/mutebi/stockinvestmentapp/features/market/list/MarketListScreen.kt`

### Existing files that must be updated
These are present in your current codebase, but their current shape is not sufficient for Phase 6:

- `navigation/Routes.kt`
- `navigation/AppNavGraph.kt`
- `data/remote/network/RetrofitProvider.kt`
- `di/NetworkModule.kt`
- `di/RepositoryModule.kt`
- `data/remote/api/AssetApi.kt`
- `data/remote/api/WatchlistApi.kt`
- `data/remote/api/PortfolioApi.kt`
- `data/remote/dto/AssetDtos.kt`
- `data/remote/dto/WatchlistDtos.kt`
- `data/remote/dto/PortfolioDtos.kt`
- `data/repository/AssetRepository.kt`
- `data/repository/WatchlistRepository.kt`
- `data/repository/PortfolioRepository.kt`
- `domain/model/Portfolio.kt`
- `domain/model/TradeOrder.kt`
- `features/market/details/AssetDetailScreen.kt`

### New files to create
These were missing in the audit and should be created now:

- `data/remote/api/TradeApi.kt`
- `data/remote/api/TransactionApi.kt`
- `data/remote/dto/TradeDtos.kt`
- `data/remote/dto/TransactionDtos.kt`
- `data/repository/TradeRepository.kt`
- `data/repository/TransactionRepository.kt`
- `domain/model/Holding.kt`
- `domain/model/Transaction.kt`
- `domain/model/PortfolioSummary.kt`
- `features/portfolio/overview/*`
- `features/portfolio/history/*`
- `features/portfolio/components/*`
- `features/trade/order/*`
- `features/trade/confirm/*`
- `features/trade/components/*`
- `backend-api/tests/test_portfolio.py`
- `backend-api/tests/test_trades.py`

---

## 2) Replace these existing Android files

---

## File: `mobile-android/app/src/main/java/com/mutebi/stockinvestmentapp/navigation/Routes.kt`

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

    data object AssetDetail : Routes("asset_detail/{assetId}") {
        fun createRoute(assetId: Int): String = "asset_detail/$assetId"
    }

    data object TradeOrder : Routes("trade_order/{assetId}") {
        fun createRoute(assetId: Int): String = "trade_order/$assetId"
    }

    data object TradeConfirm : Routes("trade_confirm")
    data object TradeResult : Routes("trade_result")
}
```

---

## File: `mobile-android/app/src/main/java/com/mutebi/stockinvestmentapp/navigation/AppNavGraph.kt`

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
import com.mutebi.stockinvestmentapp.features.home.dashboard.DashboardScreen
import com.mutebi.stockinvestmentapp.features.kyc.KycDocumentUploadScreen
import com.mutebi.stockinvestmentapp.features.kyc.KycIntroScreen
import com.mutebi.stockinvestmentapp.features.kyc.KycPersonalInfoScreen
import com.mutebi.stockinvestmentapp.features.kyc.KycReviewScreen
import com.mutebi.stockinvestmentapp.features.kyc.KycSuccessScreen
import com.mutebi.stockinvestmentapp.features.kyc.KycViewModel
import com.mutebi.stockinvestmentapp.features.market.details.AssetDetailScreen
import com.mutebi.stockinvestmentapp.features.market.list.MarketListScreen
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
                Phase6PlaceholderScreen(
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
                Phase6PlaceholderScreen(
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
    }
}

@Composable
private fun Phase6PlaceholderScreen(
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

```kotlin
package com.mutebi.stockinvestmentapp.data.remote.network

import android.app.Application
import com.google.gson.GsonBuilder
import com.mutebi.stockinvestmentapp.core.constants.ApiConstants
import com.mutebi.stockinvestmentapp.data.remote.api.AssetApi
import com.mutebi.stockinvestmentapp.data.remote.api.AuthApi
import com.mutebi.stockinvestmentapp.data.remote.api.KycApi
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
}
```

---

## File: `mobile-android/app/src/main/java/com/mutebi/stockinvestmentapp/di/NetworkModule.kt`

```kotlin
package com.mutebi.stockinvestmentapp.di

import com.mutebi.stockinvestmentapp.data.remote.api.AssetApi
import com.mutebi.stockinvestmentapp.data.remote.api.AuthApi
import com.mutebi.stockinvestmentapp.data.remote.api.KycApi
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
}
```

---

## File: `mobile-android/app/src/main/java/com/mutebi/stockinvestmentapp/di/RepositoryModule.kt`

```kotlin
package com.mutebi.stockinvestmentapp.di

import com.mutebi.stockinvestmentapp.data.remote.api.AssetApi
import com.mutebi.stockinvestmentapp.data.remote.api.AuthApi
import com.mutebi.stockinvestmentapp.data.remote.api.KycApi
import com.mutebi.stockinvestmentapp.data.remote.api.PortfolioApi
import com.mutebi.stockinvestmentapp.data.remote.api.TradeApi
import com.mutebi.stockinvestmentapp.data.remote.api.TransactionApi
import com.mutebi.stockinvestmentapp.data.remote.api.UserApi
import com.mutebi.stockinvestmentapp.data.remote.api.WatchlistApi
import com.mutebi.stockinvestmentapp.data.repository.AssetRepository
import com.mutebi.stockinvestmentapp.data.repository.AuthRepository
import com.mutebi.stockinvestmentapp.data.repository.KycRepository
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
}
```

---

## File: `mobile-android/app/src/main/java/com/mutebi/stockinvestmentapp/data/remote/dto/AssetDtos.kt`

```kotlin
package com.mutebi.stockinvestmentapp.data.remote.dto

import com.google.gson.annotations.SerializedName
import com.mutebi.stockinvestmentapp.domain.model.Asset

data class AssetListPayloadDto(
    @SerializedName("assets")
    val assets: List<AssetDto> = emptyList()
)

data class AssetDto(
    @SerializedName("id")
    val id: Int = 0,
    @SerializedName("symbol")
    val symbol: String = "",
    @SerializedName("name")
    val name: String = "",
    @SerializedName("price")
    val price: Double = 0.0,
    @SerializedName("change_percent")
    val changePercent: Double = 0.0
) {
    fun toDomain(): Asset {
        return Asset(
            id = id,
            symbol = symbol,
            name = name,
            price = price,
            changePercent = changePercent
        )
    }
}
```

---

## File: `mobile-android/app/src/main/java/com/mutebi/stockinvestmentapp/data/remote/dto/WatchlistDtos.kt`

```kotlin
package com.mutebi.stockinvestmentapp.data.remote.dto

import com.google.gson.annotations.SerializedName
import com.mutebi.stockinvestmentapp.domain.model.WatchlistItem

data class WatchlistListPayloadDto(
    @SerializedName("items")
    val items: List<WatchlistItemDto> = emptyList()
)

data class AddWatchlistRequestDto(
    @SerializedName("asset_id")
    val assetId: Int
)

data class WatchlistItemDto(
    @SerializedName("asset_id")
    val assetId: Int = 0,
    @SerializedName("symbol")
    val symbol: String = "",
    @SerializedName("name")
    val name: String = "",
    @SerializedName("price")
    val price: Double = 0.0
) {
    fun toDomain(): WatchlistItem {
        return WatchlistItem(
            assetId = assetId,
            symbol = symbol,
            name = name,
            price = price
        )
    }
}
```

---

## File: `mobile-android/app/src/main/java/com/mutebi/stockinvestmentapp/data/remote/api/AssetApi.kt`

```kotlin
package com.mutebi.stockinvestmentapp.data.remote.api

import com.mutebi.stockinvestmentapp.data.remote.dto.ApiEnvelopeDto
import com.mutebi.stockinvestmentapp.data.remote.dto.AssetDto
import com.mutebi.stockinvestmentapp.data.remote.dto.AssetListPayloadDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface AssetApi {
    @GET("api/v1/assets")
    suspend fun getAssets(
        @Query("q") query: String? = null,
        @Query("active") active: Boolean? = true
    ): Response<ApiEnvelopeDto<AssetListPayloadDto>>

    @GET("api/v1/assets/{assetId}")
    suspend fun getAssetById(
        @Path("assetId") assetId: Int
    ): Response<ApiEnvelopeDto<AssetDto>>
}
```

---

## File: `mobile-android/app/src/main/java/com/mutebi/stockinvestmentapp/data/remote/api/WatchlistApi.kt`

```kotlin
package com.mutebi.stockinvestmentapp.data.remote.api

import com.mutebi.stockinvestmentapp.data.remote.dto.AddWatchlistRequestDto
import com.mutebi.stockinvestmentapp.data.remote.dto.ApiEnvelopeDto
import com.mutebi.stockinvestmentapp.data.remote.dto.WatchlistItemDto
import com.mutebi.stockinvestmentapp.data.remote.dto.WatchlistListPayloadDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface WatchlistApi {
    @GET("api/v1/watchlist")
    suspend fun getWatchlist(): Response<ApiEnvelopeDto<WatchlistListPayloadDto>>

    @POST("api/v1/watchlist")
    suspend fun addToWatchlist(
        @Body request: AddWatchlistRequestDto
    ): Response<ApiEnvelopeDto<WatchlistItemDto>>

    @DELETE("api/v1/watchlist/{assetId}")
    suspend fun removeFromWatchlist(
        @Path("assetId") assetId: Int
    ): Response<ApiEnvelopeDto<Unit>>
}
```

---

## File: `mobile-android/app/src/main/java/com/mutebi/stockinvestmentapp/data/remote/api/PortfolioApi.kt`

```kotlin
package com.mutebi.stockinvestmentapp.data.remote.api

import com.mutebi.stockinvestmentapp.data.remote.dto.ApiEnvelopeDto
import com.mutebi.stockinvestmentapp.data.remote.dto.PortfolioDataDto
import retrofit2.Response
import retrofit2.http.GET

interface PortfolioApi {
    @GET("api/v1/portfolio")
    suspend fun getPortfolio(): Response<ApiEnvelopeDto<PortfolioDataDto>>
}
```

---

## File: `mobile-android/app/src/main/java/com/mutebi/stockinvestmentapp/data/remote/dto/PortfolioDtos.kt`

```kotlin
package com.mutebi.stockinvestmentapp.data.remote.dto

import com.google.gson.annotations.SerializedName
import com.mutebi.stockinvestmentapp.domain.model.Holding
import com.mutebi.stockinvestmentapp.domain.model.Portfolio
import com.mutebi.stockinvestmentapp.domain.model.PortfolioSummary

data class PortfolioCoreDto(
    @SerializedName("id")
    val id: Int = 0,
    @SerializedName("user_id")
    val userId: Int = 0,
    @SerializedName("name")
    val name: String = "",
    @SerializedName("description")
    val description: String? = null,
    @SerializedName("cash_balance")
    val cashBalance: Double = 0.0
)

data class HoldingDto(
    @SerializedName("id")
    val id: Int = 0,
    @SerializedName("portfolio_id")
    val portfolioId: Int = 0,
    @SerializedName("quantity")
    val quantity: Double = 0.0,
    @SerializedName("average_price")
    val averagePrice: Double = 0.0,
    @SerializedName("market_value")
    val marketValue: Double = 0.0,
    @SerializedName("asset")
    val asset: AssetDto? = null
) {
    fun toDomain(): Holding {
        return Holding(
            id = id,
            portfolioId = portfolioId,
            quantity = quantity,
            averagePrice = averagePrice,
            marketValue = marketValue,
            asset = asset?.toDomain()
        )
    }
}

data class PortfolioDataDto(
    @SerializedName("portfolio")
    val portfolio: PortfolioCoreDto? = null,
    @SerializedName("total_value")
    val totalValue: Double = 0.0,
    @SerializedName("holdings_count")
    val holdingsCount: Int = 0,
    @SerializedName("holdings")
    val holdings: List<HoldingDto> = emptyList(),
    @SerializedName("trades")
    val trades: List<TransactionDto> = emptyList()
) {
    fun toDomain(): Portfolio {
        return Portfolio(
            summary = PortfolioSummary(
                totalValue = totalValue,
                cashBalance = portfolio?.cashBalance ?: 0.0,
                holdingsCount = holdingsCount
            ),
            holdings = holdings.map { it.toDomain() },
            transactions = trades.map { it.toDomain() }
        )
    }
}
```

---

## File: `mobile-android/app/src/main/java/com/mutebi/stockinvestmentapp/data/repository/AssetRepository.kt`

```kotlin
package com.mutebi.stockinvestmentapp.data.repository

import com.mutebi.stockinvestmentapp.core.utils.Resource
import com.mutebi.stockinvestmentapp.data.remote.api.AssetApi
import com.mutebi.stockinvestmentapp.domain.model.Asset
import javax.inject.Inject

class AssetRepository @Inject constructor(
    private val assetApi: AssetApi
) {
    suspend fun getAssets(): Resource<List<Asset>> {
        return listAssets()
    }

    suspend fun listAssets(
        search: String? = null,
        active: Boolean? = true
    ): Resource<List<Asset>> {
        return try {
            val response = assetApi.getAssets(query = search, active = active)
            val body = response.body()
            val assets = body?.data?.assets.orEmpty().map { it.toDomain() }

            if (response.isSuccessful && body?.success == true) {
                Resource.Success(assets)
            } else {
                Resource.Error(body?.message ?: "Unable to load assets")
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Unable to load assets")
        }
    }

    suspend fun getAssetById(assetId: Int): Resource<Asset> {
        return try {
            val response = assetApi.getAssetById(assetId)
            val body = response.body()
            val dto = body?.data

            if (response.isSuccessful && body?.success == true && dto != null) {
                Resource.Success(dto.toDomain())
            } else {
                Resource.Error(body?.message ?: "Unable to load asset details")
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Unable to load asset details")
        }
    }
}
```

---

## File: `mobile-android/app/src/main/java/com/mutebi/stockinvestmentapp/data/repository/WatchlistRepository.kt`

```kotlin
package com.mutebi.stockinvestmentapp.data.repository

import com.mutebi.stockinvestmentapp.core.utils.Resource
import com.mutebi.stockinvestmentapp.data.remote.api.WatchlistApi
import com.mutebi.stockinvestmentapp.data.remote.dto.AddWatchlistRequestDto
import com.mutebi.stockinvestmentapp.domain.model.WatchlistItem
import javax.inject.Inject

class WatchlistRepository @Inject constructor(
    private val api: WatchlistApi
) {
    suspend fun getWatchlist(): Resource<List<WatchlistItem>> {
        return try {
            val response = api.getWatchlist()
            val body = response.body()
            val items = body?.data?.items.orEmpty().map { it.toDomain() }

            if (response.isSuccessful && body?.success == true) {
                Resource.Success(items)
            } else {
                Resource.Error(body?.message ?: "Unable to load watchlist")
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Unable to load watchlist")
        }
    }

    suspend fun addToWatchlist(assetId: Int): Resource<WatchlistItem> {
        return try {
            val response = api.addToWatchlist(AddWatchlistRequestDto(assetId))
            val body = response.body()
            val dto = body?.data

            if (response.isSuccessful && body?.success == true && dto != null) {
                Resource.Success(dto.toDomain())
            } else {
                Resource.Error(body?.message ?: "Unable to add asset to watchlist")
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Unable to add asset to watchlist")
        }
    }

    suspend fun removeFromWatchlist(assetId: Int): Resource<String> {
        return try {
            val response = api.removeFromWatchlist(assetId)
            val body = response.body()

            if (response.isSuccessful && body?.success == true) {
                Resource.Success(body?.message ?: "Removed from watchlist")
            } else {
                Resource.Error(body?.message ?: "Unable to remove asset from watchlist")
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Unable to remove asset from watchlist")
        }
    }
}
```

---

## File: `mobile-android/app/src/main/java/com/mutebi/stockinvestmentapp/data/repository/PortfolioRepository.kt`

```kotlin
package com.mutebi.stockinvestmentapp.data.repository

import com.mutebi.stockinvestmentapp.core.utils.Resource
import com.mutebi.stockinvestmentapp.data.remote.api.PortfolioApi
import com.mutebi.stockinvestmentapp.domain.model.Portfolio
import javax.inject.Inject

class PortfolioRepository @Inject constructor(
    private val portfolioApi: PortfolioApi
) {
    suspend fun getPortfolio(): Resource<Portfolio> {
        return try {
            val response = portfolioApi.getPortfolio()
            val body = response.body()
            val dto = body?.data

            if (response.isSuccessful && body?.success == true && dto != null) {
                Resource.Success(dto.toDomain())
            } else {
                Resource.Error(body?.message ?: "Unable to load portfolio")
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Unable to load portfolio")
        }
    }
}
```

---

## File: `mobile-android/app/src/main/java/com/mutebi/stockinvestmentapp/domain/model/Portfolio.kt`

```kotlin
package com.mutebi.stockinvestmentapp.domain.model

data class Portfolio(
    val summary: PortfolioSummary = PortfolioSummary(),
    val holdings: List<Holding> = emptyList(),
    val transactions: List<Transaction> = emptyList()
)
```

---

## File: `mobile-android/app/src/main/java/com/mutebi/stockinvestmentapp/domain/model/TradeOrder.kt`

```kotlin
package com.mutebi.stockinvestmentapp.domain.model

data class TradeOrder(
    val assetId: Int,
    val tradeType: String,
    val quantity: Double
)
```

---

## File: `mobile-android/app/src/main/java/com/mutebi/stockinvestmentapp/features/market/details/AssetDetailScreen.kt`

```kotlin
package com.mutebi.stockinvestmentapp.features.market.details

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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mutebi.stockinvestmentapp.features.market.components.AssetPriceRow

@Composable
fun AssetDetailScreen(
    assetId: Int,
    onBack: () -> Unit,
    onOpenWatchlist: () -> Unit,
    onTradeAsset: () -> Unit,
    vm: AssetDetailViewModel = viewModel()
) {
    val state by vm.uiState.collectAsState()

    LaunchedEffect(assetId) {
        vm.loadAsset(assetId)
    }

    if (state.isLoading) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            CircularProgressIndicator()
            Text("Loading asset details...")
        }
        return
    }

    val asset = state.asset
    if (asset == null) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(state.error ?: "Asset details unavailable.")
            TextButton(onClick = onBack) {
                Text("Go back")
            }
        }
        return
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

        AssetPriceRow(asset = asset)

        Text("Asset detail")
        Text("Symbol: ${asset.symbol}")
        Text("Name: ${asset.name}")
        Text("Current price: $${"%.2f".format(asset.price)}")
        Text("Change: ${"%.2f".format(asset.changePercent)}%")

        Button(
            onClick = vm::toggleWatchlist,
            modifier = Modifier.fillMaxWidth(),
            enabled = !state.actionLoading
        ) {
            if (state.actionLoading) {
                CircularProgressIndicator()
            } else {
                Text(if (state.isInWatchlist) "Remove from watchlist" else "Add to watchlist")
            }
        }

        Button(
            onClick = onTradeAsset,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Trade this asset")
        }

        TextButton(onClick = onOpenWatchlist) {
            Text("Open watchlist")
        }

        state.error?.let {
            Text(it)
        }
    }
}
```

---

## 3) Create these new Android files

---

## File: `mobile-android/app/src/main/java/com/mutebi/stockinvestmentapp/data/remote/api/TradeApi.kt`

```kotlin
package com.mutebi.stockinvestmentapp.data.remote.api

import com.mutebi.stockinvestmentapp.data.remote.dto.ApiEnvelopeDto
import com.mutebi.stockinvestmentapp.data.remote.dto.CreateTradeRequestDto
import com.mutebi.stockinvestmentapp.data.remote.dto.TradeResponseDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface TradeApi {
    @POST("api/v1/trades")
    suspend fun createTrade(
        @Body request: CreateTradeRequestDto
    ): Response<ApiEnvelopeDto<TradeResponseDto>>
}
```

---

## File: `mobile-android/app/src/main/java/com/mutebi/stockinvestmentapp/data/remote/api/TransactionApi.kt`

```kotlin
package com.mutebi.stockinvestmentapp.data.remote.api

import com.mutebi.stockinvestmentapp.data.remote.dto.ApiEnvelopeDto
import com.mutebi.stockinvestmentapp.data.remote.dto.TransactionListPayloadDto
import retrofit2.Response
import retrofit2.http.GET

interface TransactionApi {
    @GET("api/v1/portfolio/transactions")
    suspend fun getTransactions(): Response<ApiEnvelopeDto<TransactionListPayloadDto>>
}
```

---

## File: `mobile-android/app/src/main/java/com/mutebi/stockinvestmentapp/data/remote/dto/TradeDtos.kt`

```kotlin
package com.mutebi.stockinvestmentapp.data.remote.dto

import com.google.gson.annotations.SerializedName
import com.mutebi.stockinvestmentapp.domain.model.Transaction

data class CreateTradeRequestDto(
    @SerializedName("asset_id")
    val assetId: Int,
    @SerializedName("trade_type")
    val tradeType: String,
    @SerializedName("quantity")
    val quantity: Double
)

data class TradeResponseDto(
    @SerializedName("id")
    val id: Int = 0,
    @SerializedName("asset_id")
    val assetId: Int = 0,
    @SerializedName("trade_type")
    val tradeType: String = "",
    @SerializedName("quantity")
    val quantity: Double = 0.0,
    @SerializedName("price")
    val price: Double = 0.0,
    @SerializedName("status")
    val status: String = "",
    @SerializedName("created_at")
    val createdAt: String = "",
    @SerializedName("asset")
    val asset: AssetDto? = null
) {
    fun toDomain(): Transaction {
        return Transaction(
            id = id,
            assetId = assetId,
            tradeType = tradeType,
            quantity = quantity,
            price = price,
            status = status,
            createdAt = createdAt,
            asset = asset?.toDomain()
        )
    }
}
```

---

## File: `mobile-android/app/src/main/java/com/mutebi/stockinvestmentapp/data/remote/dto/TransactionDtos.kt`

```kotlin
package com.mutebi.stockinvestmentapp.data.remote.dto

import com.google.gson.annotations.SerializedName
import com.mutebi.stockinvestmentapp.domain.model.Transaction

data class TransactionListPayloadDto(
    @SerializedName("trades")
    val trades: List<TransactionDto> = emptyList()
)

data class TransactionDto(
    @SerializedName("id")
    val id: Int = 0,
    @SerializedName("asset_id")
    val assetId: Int = 0,
    @SerializedName("trade_type")
    val tradeType: String = "",
    @SerializedName("quantity")
    val quantity: Double = 0.0,
    @SerializedName("price")
    val price: Double = 0.0,
    @SerializedName("status")
    val status: String = "",
    @SerializedName("created_at")
    val createdAt: String = "",
    @SerializedName("asset")
    val asset: AssetDto? = null
) {
    fun toDomain(): Transaction {
        return Transaction(
            id = id,
            assetId = assetId,
            tradeType = tradeType,
            quantity = quantity,
            price = price,
            status = status,
            createdAt = createdAt,
            asset = asset?.toDomain()
        )
    }
}
```

---

## File: `mobile-android/app/src/main/java/com/mutebi/stockinvestmentapp/data/repository/TradeRepository.kt`

```kotlin
package com.mutebi.stockinvestmentapp.data.repository

import com.mutebi.stockinvestmentapp.core.utils.Resource
import com.mutebi.stockinvestmentapp.data.remote.api.TradeApi
import com.mutebi.stockinvestmentapp.data.remote.dto.CreateTradeRequestDto
import com.mutebi.stockinvestmentapp.domain.model.Transaction
import javax.inject.Inject

class TradeRepository @Inject constructor(
    private val tradeApi: TradeApi
) {
    suspend fun createTrade(
        assetId: Int,
        tradeType: String,
        quantity: Double
    ): Resource<Transaction> {
        return try {
            val response = tradeApi.createTrade(
                CreateTradeRequestDto(
                    assetId = assetId,
                    tradeType = tradeType,
                    quantity = quantity
                )
            )
            val body = response.body()
            val dto = body?.data

            if (response.isSuccessful && body?.success == true && dto != null) {
                Resource.Success(dto.toDomain())
            } else {
                Resource.Error(body?.message ?: "Unable to submit trade")
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Unable to submit trade")
        }
    }
}
```

---

## File: `mobile-android/app/src/main/java/com/mutebi/stockinvestmentapp/data/repository/TransactionRepository.kt`

```kotlin
package com.mutebi.stockinvestmentapp.data.repository

import com.mutebi.stockinvestmentapp.core.utils.Resource
import com.mutebi.stockinvestmentapp.data.remote.api.TransactionApi
import com.mutebi.stockinvestmentapp.domain.model.Transaction
import javax.inject.Inject

class TransactionRepository @Inject constructor(
    private val transactionApi: TransactionApi
) {
    suspend fun getTransactions(): Resource<List<Transaction>> {
        return try {
            val response = transactionApi.getTransactions()
            val body = response.body()
            val transactions = body?.data?.trades.orEmpty().map { it.toDomain() }

            if (response.isSuccessful && body?.success == true) {
                Resource.Success(transactions)
            } else {
                Resource.Error(body?.message ?: "Unable to load transactions")
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Unable to load transactions")
        }
    }
}
```

---

## File: `mobile-android/app/src/main/java/com/mutebi/stockinvestmentapp/domain/model/Holding.kt`

```kotlin
package com.mutebi.stockinvestmentapp.domain.model

data class Holding(
    val id: Int = 0,
    val portfolioId: Int = 0,
    val quantity: Double = 0.0,
    val averagePrice: Double = 0.0,
    val marketValue: Double = 0.0,
    val asset: Asset? = null
)
```

---

## File: `mobile-android/app/src/main/java/com/mutebi/stockinvestmentapp/domain/model/Transaction.kt`

```kotlin
package com.mutebi.stockinvestmentapp.domain.model

data class Transaction(
    val id: Int = 0,
    val assetId: Int = 0,
    val tradeType: String = "",
    val quantity: Double = 0.0,
    val price: Double = 0.0,
    val status: String = "",
    val createdAt: String = "",
    val asset: Asset? = null
)
```

---

## File: `mobile-android/app/src/main/java/com/mutebi/stockinvestmentapp/domain/model/PortfolioSummary.kt`

```kotlin
package com.mutebi.stockinvestmentapp.domain.model

data class PortfolioSummary(
    val totalValue: Double = 0.0,
    val cashBalance: Double = 0.0,
    val holdingsCount: Int = 0
)
```

---

## File: `mobile-android/app/src/main/java/com/mutebi/stockinvestmentapp/features/portfolio/overview/PortfolioUiState.kt`

```kotlin
package com.mutebi.stockinvestmentapp.features.portfolio.overview

import com.mutebi.stockinvestmentapp.domain.model.Holding
import com.mutebi.stockinvestmentapp.domain.model.PortfolioSummary
import com.mutebi.stockinvestmentapp.domain.model.Transaction

data class PortfolioUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val summary: PortfolioSummary = PortfolioSummary(),
    val holdings: List<Holding> = emptyList(),
    val recentTransactions: List<Transaction> = emptyList()
)
```

---

## File: `mobile-android/app/src/main/java/com/mutebi/stockinvestmentapp/features/portfolio/overview/PortfolioViewModel.kt`

```kotlin
package com.mutebi.stockinvestmentapp.features.portfolio.overview

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.mutebi.stockinvestmentapp.core.utils.Resource
import com.mutebi.stockinvestmentapp.data.remote.network.RetrofitProvider
import com.mutebi.stockinvestmentapp.data.repository.PortfolioRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class PortfolioViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = PortfolioRepository(
        RetrofitProvider.portfolioApi(application)
    )

    private val _uiState = MutableStateFlow(PortfolioUiState(isLoading = true))
    val uiState: StateFlow<PortfolioUiState> = _uiState

    init {
        loadPortfolio()
    }

    fun loadPortfolio() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)

            when (val result = repository.getPortfolio()) {
                is Resource.Success -> {
                    val portfolio = result.data
                    if (portfolio != null) {
                        _uiState.value = PortfolioUiState(
                            isLoading = false,
                            summary = portfolio.summary,
                            holdings = portfolio.holdings,
                            recentTransactions = portfolio.transactions.take(5)
                        )
                    } else {
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            error = "Portfolio data unavailable"
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
}
```

---

## File: `mobile-android/app/src/main/java/com/mutebi/stockinvestmentapp/features/portfolio/overview/PortfolioScreen.kt`

```kotlin
package com.mutebi.stockinvestmentapp.features.portfolio.overview

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mutebi.stockinvestmentapp.features.portfolio.components.AllocationChart
import com.mutebi.stockinvestmentapp.features.portfolio.components.HoldingCard
import com.mutebi.stockinvestmentapp.features.portfolio.components.TransactionRow

@Composable
fun PortfolioScreen(
    onOpenHistory: () -> Unit,
    onTradeAsset: (Int) -> Unit,
    onOpenMarket: () -> Unit,
    vm: PortfolioViewModel = viewModel()
) {
    val state by vm.uiState.collectAsState()

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text("Portfolio")
        }

        if (state.isLoading) {
            item {
                CircularProgressIndicator()
            }
        } else {
            item {
                Text("Total value: $${"%.2f".format(state.summary.totalValue)}")
            }

            item {
                Text("Cash balance: $${"%.2f".format(state.summary.cashBalance)}")
            }

            item {
                Text("Holdings count: ${state.summary.holdingsCount}")
            }

            item {
                AllocationChart(holdings = state.holdings)
            }

            item {
                Button(onClick = onOpenHistory) {
                    Text("View transaction history")
                }
            }

            item {
                TextButton(onClick = onOpenMarket) {
                    Text("Browse market")
                }
            }

            if (state.holdings.isEmpty()) {
                item {
                    Text("No holdings yet.")
                }
            } else {
                items(state.holdings, key = { it.id }) { holding ->
                    HoldingCard(
                        holding = holding,
                        onTrade = {
                            val assetId = holding.asset?.id ?: 0
                            if (assetId > 0) onTradeAsset(assetId)
                        }
                    )
                }
            }

            if (state.recentTransactions.isNotEmpty()) {
                item {
                    Text("Recent transactions")
                }

                items(state.recentTransactions, key = { it.id }) { transaction ->
                    TransactionRow(transaction = transaction)
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

## File: `mobile-android/app/src/main/java/com/mutebi/stockinvestmentapp/features/portfolio/history/TransactionHistoryUiState.kt`

```kotlin
package com.mutebi.stockinvestmentapp.features.portfolio.history

import com.mutebi.stockinvestmentapp.domain.model.Transaction

data class TransactionHistoryUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val transactions: List<Transaction> = emptyList()
)
```

---

## File: `mobile-android/app/src/main/java/com/mutebi/stockinvestmentapp/features/portfolio/history/TransactionHistoryViewModel.kt`

```kotlin
package com.mutebi.stockinvestmentapp.features.portfolio.history

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.mutebi.stockinvestmentapp.core.utils.Resource
import com.mutebi.stockinvestmentapp.data.remote.network.RetrofitProvider
import com.mutebi.stockinvestmentapp.data.repository.TransactionRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class TransactionHistoryViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = TransactionRepository(
        RetrofitProvider.transactionApi(application)
    )

    private val _uiState = MutableStateFlow(TransactionHistoryUiState(isLoading = true))
    val uiState: StateFlow<TransactionHistoryUiState> = _uiState

    init {
        loadTransactions()
    }

    fun loadTransactions() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)

            when (val result = repository.getTransactions()) {
                is Resource.Success -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        transactions = result.data.orEmpty()
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

## File: `mobile-android/app/src/main/java/com/mutebi/stockinvestmentapp/features/portfolio/history/TransactionHistoryScreen.kt`

```kotlin
package com.mutebi.stockinvestmentapp.features.portfolio.history

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mutebi.stockinvestmentapp.features.portfolio.components.TransactionRow

@Composable
fun TransactionHistoryScreen(
    onBack: () -> Unit,
    vm: TransactionHistoryViewModel = viewModel()
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
            Text("Transaction history")
        }

        if (state.isLoading) {
            item {
                CircularProgressIndicator()
            }
        } else if (state.transactions.isEmpty()) {
            item {
                Text("No transactions yet.")
            }
        } else {
            items(state.transactions, key = { it.id }) { transaction ->
                TransactionRow(transaction = transaction)
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

## File: `mobile-android/app/src/main/java/com/mutebi/stockinvestmentapp/features/portfolio/components/HoldingCard.kt`

```kotlin
package com.mutebi.stockinvestmentapp.features.portfolio.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.mutebi.stockinvestmentapp.features.market.components.AssetPriceRow
import com.mutebi.stockinvestmentapp.domain.model.Holding

@Composable
fun HoldingCard(
    holding: Holding,
    onTrade: () -> Unit
) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            holding.asset?.let { asset ->
                AssetPriceRow(asset = asset)
            } ?: Text("Asset unavailable")

            Text("Quantity: ${"%.2f".format(holding.quantity)}")
            Text("Average price: $${"%.2f".format(holding.averagePrice)}")
            Text("Market value: $${"%.2f".format(holding.marketValue)}")

            Button(onClick = onTrade) {
                Text("Trade")
            }
        }
    }
}
```

---

## File: `mobile-android/app/src/main/java/com/mutebi/stockinvestmentapp/features/portfolio/components/AllocationChart.kt`

```kotlin
package com.mutebi.stockinvestmentapp.features.portfolio.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Card
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mutebi.stockinvestmentapp.domain.model.Holding

@Composable
fun AllocationChart(
    holdings: List<Holding>
) {
    val total = holdings.sumOf { it.marketValue }.takeIf { it > 0.0 } ?: 1.0

    Card(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text("Allocation")

            if (holdings.isEmpty()) {
                Text("No allocation to display yet.")
            } else {
                holdings.forEach { holding ->
                    val label = holding.asset?.symbol ?: "Unknown"
                    val fraction = (holding.marketValue / total).toFloat().coerceIn(0f, 1f)

                    Text("$label • $${"%.2f".format(holding.marketValue)}")
                    LinearProgressIndicator(
                        progress = { fraction },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp)
                    )
                }
            }
        }
    }
}
```

---

## File: `mobile-android/app/src/main/java/com/mutebi/stockinvestmentapp/features/portfolio/components/TransactionRow.kt`

```kotlin
package com.mutebi.stockinvestmentapp.features.portfolio.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.mutebi.stockinvestmentapp.domain.model.Transaction

@Composable
fun TransactionRow(
    transaction: Transaction
) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text("${transaction.tradeType.uppercase()} • ${transaction.asset?.symbol ?: transaction.assetId}")
            Text("Quantity: ${"%.2f".format(transaction.quantity)}")
            Text("Price: $${"%.2f".format(transaction.price)}")
            Text("Status: ${transaction.status}")
            Text("Created: ${transaction.createdAt}")
        }
    }
}
```

---

## File: `mobile-android/app/src/main/java/com/mutebi/stockinvestmentapp/features/trade/order/TradeOrderUiState.kt`

```kotlin
package com.mutebi.stockinvestmentapp.features.trade.order

import com.mutebi.stockinvestmentapp.domain.model.Asset
import com.mutebi.stockinvestmentapp.domain.model.Transaction

data class TradeOrderUiState(
    val assetId: Int? = null,
    val asset: Asset? = null,
    val tradeType: String = "buy",
    val quantityText: String = "1",
    val isLoading: Boolean = false,
    val isSubmitting: Boolean = false,
    val error: String? = null,
    val submittedTransaction: Transaction? = null,
    val successMessage: String? = null
)
```

---

## File: `mobile-android/app/src/main/java/com/mutebi/stockinvestmentapp/features/trade/order/TradeOrderViewModel.kt`

```kotlin
package com.mutebi.stockinvestmentapp.features.trade.order

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.mutebi.stockinvestmentapp.core.utils.Resource
import com.mutebi.stockinvestmentapp.data.remote.network.RetrofitProvider
import com.mutebi.stockinvestmentapp.data.repository.AssetRepository
import com.mutebi.stockinvestmentapp.data.repository.TradeRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class TradeOrderViewModel(application: Application) : AndroidViewModel(application) {

    private val assetRepository = AssetRepository(
        RetrofitProvider.assetApi(application)
    )
    private val tradeRepository = TradeRepository(
        RetrofitProvider.tradeApi(application)
    )

    private val _uiState = MutableStateFlow(TradeOrderUiState())
    val uiState: StateFlow<TradeOrderUiState> = _uiState

    fun loadAsset(assetId: Int) {
        if (_uiState.value.assetId == assetId && _uiState.value.asset != null) return

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true,
                error = null,
                assetId = assetId,
                submittedTransaction = null,
                successMessage = null
            )

            when (val result = assetRepository.getAssetById(assetId)) {
                is Resource.Success -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        asset = result.data
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

    fun onTradeTypeChange(value: String) {
        _uiState.value = _uiState.value.copy(
            tradeType = value,
            error = null
        )
    }

    fun onQuantityChange(value: String) {
        _uiState.value = _uiState.value.copy(
            quantityText = value,
            error = null
        )
    }

    fun clearResult() {
        _uiState.value = _uiState.value.copy(
            submittedTransaction = null,
            successMessage = null,
            error = null
        )
    }

    fun canProceedToConfirm(): Boolean {
        return validateQuantity() != null && _uiState.value.asset != null
    }

    fun submitTrade() {
        val current = _uiState.value
        val assetId = current.assetId ?: return
        val quantity = validateQuantity()

        if (quantity == null) {
            _uiState.value = current.copy(error = "Enter a valid quantity greater than zero")
            return
        }

        viewModelScope.launch {
            _uiState.value = current.copy(
                isSubmitting = true,
                error = null,
                submittedTransaction = null,
                successMessage = null
            )

            when (
                val result = tradeRepository.createTrade(
                    assetId = assetId,
                    tradeType = current.tradeType,
                    quantity = quantity
                )
            ) {
                is Resource.Success -> {
                    _uiState.value = _uiState.value.copy(
                        isSubmitting = false,
                        submittedTransaction = result.data,
                        successMessage = "Trade submitted successfully."
                    )
                }
                is Resource.Error -> {
                    _uiState.value = _uiState.value.copy(
                        isSubmitting = false,
                        error = result.message
                    )
                }
                else -> {
                    _uiState.value = _uiState.value.copy(isSubmitting = false)
                }
            }
        }
    }

    private fun validateQuantity(): Double? {
        val quantity = _uiState.value.quantityText.trim().toDoubleOrNull()
        return quantity?.takeIf { it > 0.0 }
    }
}
```

---

## File: `mobile-android/app/src/main/java/com/mutebi/stockinvestmentapp/features/trade/order/TradeOrderScreen.kt`

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

## File: `mobile-android/app/src/main/java/com/mutebi/stockinvestmentapp/features/trade/confirm/TradeConfirmScreen.kt`

```kotlin
package com.mutebi.stockinvestmentapp.features.trade.confirm

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
import com.mutebi.stockinvestmentapp.features.trade.components.OrderSummaryCard
import com.mutebi.stockinvestmentapp.features.trade.order.TradeOrderViewModel

@Composable
fun TradeConfirmScreen(
    vm: TradeOrderViewModel,
    onBack: () -> Unit,
    onTradeCompleted: () -> Unit
) {
    val state by vm.uiState.collectAsState()

    LaunchedEffect(state.submittedTransaction) {
        if (state.submittedTransaction != null) {
            onTradeCompleted()
        }
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

        Text("Confirm trade")

        val asset = state.asset
        if (asset == null) {
            Text(state.error ?: "Asset unavailable.")
        } else {
            OrderSummaryCard(
                asset = asset,
                tradeType = state.tradeType,
                quantityText = state.quantityText
            )

            Button(
                onClick = vm::submitTrade,
                enabled = !state.isSubmitting,
                modifier = Modifier.fillMaxWidth()
            ) {
                if (state.isSubmitting) {
                    CircularProgressIndicator()
                } else {
                    Text("Submit trade")
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

## File: `mobile-android/app/src/main/java/com/mutebi/stockinvestmentapp/features/trade/confirm/TradeResultScreen.kt`

```kotlin
package com.mutebi.stockinvestmentapp.features.trade.confirm

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mutebi.stockinvestmentapp.features.trade.order.TradeOrderViewModel

@Composable
fun TradeResultScreen(
    vm: TradeOrderViewModel,
    onDone: () -> Unit
) {
    val state by vm.uiState.collectAsState()
    val transaction = state.submittedTransaction

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Trade result")

        if (transaction != null) {
            Text(state.successMessage ?: "Trade submitted successfully.")
            Text("Type: ${transaction.tradeType}")
            Text("Quantity: ${"%.2f".format(transaction.quantity)}")
            Text("Price: $${"%.2f".format(transaction.price)}")
            Text("Status: ${transaction.status}")
        } else {
            Text(state.error ?: "Trade result unavailable.")
        }

        Button(
            onClick = onDone,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Done")
        }
    }
}
```

---

## File: `mobile-android/app/src/main/java/com/mutebi/stockinvestmentapp/features/trade/components/QuantitySelector.kt`

```kotlin
package com.mutebi.stockinvestmentapp.features.trade.components

import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun QuantitySelector(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        label = { Text("Quantity") },
        singleLine = true
    )
}
```

---

## File: `mobile-android/app/src/main/java/com/mutebi/stockinvestmentapp/features/trade/components/OrderSummaryCard.kt`

```kotlin
package com.mutebi.stockinvestmentapp.features.trade.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.mutebi.stockinvestmentapp.domain.model.Asset

@Composable
fun OrderSummaryCard(
    asset: Asset,
    tradeType: String,
    quantityText: String
) {
    val quantity = quantityText.toDoubleOrNull() ?: 0.0
    val estimatedValue = quantity * asset.price

    Card(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text("Order summary")
            Text("Asset: ${asset.symbol} • ${asset.name}")
            Text("Trade type: ${tradeType.uppercase()}")
            Text("Quantity: ${"%.2f".format(quantity)}")
            Text("Unit price: $${"%.2f".format(asset.price)}")
            Text("Estimated value: $${"%.2f".format(estimatedValue)}")
        }
    }
}
```

---

## File: `mobile-android/app/src/main/java/com/mutebi/stockinvestmentapp/features/trade/components/TradeActionTabs.kt`

```kotlin
package com.mutebi.stockinvestmentapp.features.trade.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp

@Composable
fun TradeActionTabs(
    selected: String,
    onSelectedChange: (String) -> Unit
) {
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        listOf("buy", "sell").forEach { action ->
            FilterChip(
                selected = selected == action,
                onClick = { onSelectedChange(action) },
                label = { Text(action.uppercase()) }
            )
        }
    }
}
```

---

## 4) Create these new backend test files

---

## File: `backend-api/tests/test_portfolio.py`

```python
import uuid

from app.extensions import db
from app.models.portfolio import Portfolio
from app.repositories.asset_repository import AssetRepository
from app.repositories.portfolio_repository import PortfolioRepository


def _unique_email() -> str:
    return f"phase6_portfolio_{uuid.uuid4().hex[:10]}@example.com"


def _register_user(client, email: str, password: str = "Password123!"):
    return client.post(
        "/api/v1/auth/register",
        json={
            "email": email,
            "password": password,
            "full_name": "Phase Six Portfolio User",
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


def _ensure_portfolio(user_id: int):
    portfolio = PortfolioRepository.get_primary_for_user(user_id)
    if portfolio:
        if portfolio.cash_balance < 10000:
            portfolio.cash_balance = 10000.0
            db.session.commit()
        return portfolio

    portfolio = Portfolio(
        user_id=user_id,
        name="Main Portfolio",
        cash_balance=10000.0,
    )
    db.session.add(portfolio)
    db.session.commit()
    return portfolio


def _seed_asset():
    suffix = uuid.uuid4().hex[:6].upper()
    return AssetRepository.create_asset(
        symbol=f"PF{suffix}",
        name=f"Portfolio Asset {suffix}",
        price=120.0,
        change_percent=2.5,
        is_active=True,
    )


def test_get_portfolio_summary(client):
    headers, user_id = _auth_context(client)
    _ensure_portfolio(user_id)
    asset = _seed_asset()

    trade_response = client.post(
        "/api/v1/trades",
        headers=headers,
        json={
            "asset_id": asset.id,
            "trade_type": "buy",
            "quantity": 3,
        },
    )
    assert trade_response.status_code in (200, 201), trade_response.get_json()

    response = client.get("/api/v1/portfolio", headers=headers)
    body = response.get_json() or {}

    assert response.status_code == 200, body
    assert body.get("success") is True

    data = body.get("data") or {}
    assert data.get("portfolio") is not None
    assert data.get("holdings") is not None
    assert data.get("trades") is not None
    assert data.get("total_value") is not None


def test_get_portfolio_transactions(client):
    headers, user_id = _auth_context(client)
    _ensure_portfolio(user_id)
    asset = _seed_asset()

    trade_response = client.post(
        "/api/v1/trades",
        headers=headers,
        json={
            "asset_id": asset.id,
            "trade_type": "buy",
            "quantity": 2,
        },
    )
    assert trade_response.status_code in (200, 201), trade_response.get_json()

    response = client.get("/api/v1/portfolio/transactions", headers=headers)
    body = response.get_json() or {}

    assert response.status_code == 200, body
    assert body.get("success") is True

    data = body.get("data") or {}
    trades = data.get("trades") or []
    assert len(trades) >= 1
```

---

## File: `backend-api/tests/test_trades.py`

```python
import uuid

from app.extensions import db
from app.models.holding import Holding
from app.models.portfolio import Portfolio
from app.repositories.asset_repository import AssetRepository
from app.repositories.portfolio_repository import PortfolioRepository


def _unique_email() -> str:
    return f"phase6_trades_{uuid.uuid4().hex[:10]}@example.com"


def _register_user(client, email: str, password: str = "Password123!"):
    return client.post(
        "/api/v1/auth/register",
        json={
            "email": email,
            "password": password,
            "full_name": "Phase Six Trade User",
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


def _ensure_portfolio(user_id: int):
    portfolio = PortfolioRepository.get_primary_for_user(user_id)
    if portfolio:
        if portfolio.cash_balance < 10000:
            portfolio.cash_balance = 10000.0
            db.session.commit()
        return portfolio

    portfolio = Portfolio(
        user_id=user_id,
        name="Main Portfolio",
        cash_balance=10000.0,
    )
    db.session.add(portfolio)
    db.session.commit()
    return portfolio


def _seed_asset():
    suffix = uuid.uuid4().hex[:6].upper()
    return AssetRepository.create_asset(
        symbol=f"TR{suffix}",
        name=f"Trade Asset {suffix}",
        price=100.0,
        change_percent=1.8,
        is_active=True,
    )


def test_create_buy_trade(client):
    headers, user_id = _auth_context(client)
    portfolio = _ensure_portfolio(user_id)
    asset = _seed_asset()

    response = client.post(
        "/api/v1/trades",
        headers=headers,
        json={
            "asset_id": asset.id,
            "trade_type": "buy",
            "quantity": 5,
        },
    )
    body = response.get_json() or {}

    assert response.status_code in (200, 201), body
    assert body.get("success") is True

    data = body.get("data") or {}
    assert data.get("trade_type") == "buy"
    assert data.get("asset_id") == asset.id

    holding = Holding.query.filter_by(portfolio_id=portfolio.id, asset_id=asset.id).first()
    assert holding is not None
    assert holding.quantity == 5


def test_create_sell_trade(client):
    headers, user_id = _auth_context(client)
    portfolio = _ensure_portfolio(user_id)
    asset = _seed_asset()

    buy_response = client.post(
        "/api/v1/trades",
        headers=headers,
        json={
            "asset_id": asset.id,
            "trade_type": "buy",
            "quantity": 5,
        },
    )
    assert buy_response.status_code in (200, 201), buy_response.get_json()

    sell_response = client.post(
        "/api/v1/trades",
        headers=headers,
        json={
            "asset_id": asset.id,
            "trade_type": "sell",
            "quantity": 2,
        },
    )
    body = sell_response.get_json() or {}

    assert sell_response.status_code in (200, 201), body
    assert body.get("success") is True

    data = body.get("data") or {}
    assert data.get("trade_type") == "sell"

    holding = Holding.query.filter_by(portfolio_id=portfolio.id, asset_id=asset.id).first()
    assert holding is not None
    assert holding.quantity == 3
```

---

## 5) Directory creation commands

### From `mobile-android/`
```bash
mkdir -p app/src/main/java/com/mutebi/stockinvestmentapp/data/remote/api
mkdir -p app/src/main/java/com/mutebi/stockinvestmentapp/data/remote/dto
mkdir -p app/src/main/java/com/mutebi/stockinvestmentapp/data/repository
mkdir -p app/src/main/java/com/mutebi/stockinvestmentapp/domain/model
mkdir -p app/src/main/java/com/mutebi/stockinvestmentapp/features/portfolio/overview
mkdir -p app/src/main/java/com/mutebi/stockinvestmentapp/features/portfolio/history
mkdir -p app/src/main/java/com/mutebi/stockinvestmentapp/features/portfolio/components
mkdir -p app/src/main/java/com/mutebi/stockinvestmentapp/features/trade/order
mkdir -p app/src/main/java/com/mutebi/stockinvestmentapp/features/trade/confirm
mkdir -p app/src/main/java/com/mutebi/stockinvestmentapp/features/trade/components
```

### From `backend-api/`
```bash
mkdir -p tests
```

---

## 6) Verification sequence

### Backend
From `backend-api/`:
```bash
source env/bin/activate
pytest tests/test_portfolio.py tests/test_trades.py -q
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

## 7) Expected Phase 6 outcome

After these updates:

- Portfolio route is no longer a placeholder
- portfolio summary loads
- holdings display
- transaction history screen works
- asset detail can open a trade flow
- trade order -> confirm -> result flow works
- backend tests for portfolio and trades exist
- the app reaches the MVP line for portfolio + trade flow
