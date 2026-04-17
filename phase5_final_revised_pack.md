# Phase 5 — Final Revised Build Pack

This pack is based on the files you actually shared for Phase 5.

It follows your rule:
- **existing files** get **targeted/full updates only where necessary**
- **new Phase 5 files** are created fully
- **Phase 4 files are not repeated** unless Phase 5 requires a real update

Your Phase 5 timeline is for dashboard, market discovery, asset details, search/filter, and watchlist add/remove, with backend support for assets and watchlist. fileciteturn24file11turn24file15

---

## 1) Phase 5 audit result

### Keep as-is
These are already good enough for Phase 5 and do not need changes:

- `app/src/main/java/com/mutebi/stockinvestmentapp/navigation/BottomNavItem.kt`
- `app/src/main/java/com/mutebi/stockinvestmentapp/domain/model/Asset.kt`
- `app/src/main/java/com/mutebi/stockinvestmentapp/domain/model/WatchlistItem.kt`
- `../backend-api/app/routes/assets.py`
- `../backend-api/app/services/asset_service.py`
- `../backend-api/app/repositories/asset_repository.py`
- `../backend-api/app/repositories/watchlist_repository.py`

Those files already exist in your current project state. fileciteturn24file6turn24file8turn24file9

### Existing files that must be updated for Phase 5
These exist, but their current shape is not enough for Phase 5:

- `navigation/Routes.kt`
- `navigation/AppNavGraph.kt`
- `data/remote/dto/AssetDtos.kt`
- `data/remote/dto/WatchlistDtos.kt`
- `data/remote/api/AssetApi.kt`
- `data/remote/api/WatchlistApi.kt`
- `data/repository/AssetRepository.kt`
- `data/repository/WatchlistRepository.kt`
- `../backend-api/app/routes/watchlist.py`

The main reason is that the current Android asset/watchlist API layer still uses the old non-envelope shapes and wrong endpoint paths like `"assets"` / `"watchlist"` instead of the `/api/v1/...` routes your backend exposes. Also, `WatchlistRepository.kt` is still effectively empty. fileciteturn24file6turn24file8

### New files to create
These were missing from the audit and should now be created for Phase 5:

- `features/home/dashboard/*`
- `features/home/components/*`
- `features/market/list/*`
- `features/market/details/*`
- `features/market/components/*`
- `features/watchlist/*`
- `core/ui/components/MiniLineChart.kt`
- `core/ui/components/PercentageBadge.kt`
- `../backend-api/app/services/watchlist_service.py`
- `../backend-api/tests/test_market_assets.py`
- `../backend-api/tests/test_watchlist.py`

The audit output explicitly showed these files were missing. fileciteturn24file7turn24file10

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

    data object AssetDetail : Routes("asset_detail/{assetId}") {
        fun createRoute(assetId: Int): String = "asset_detail/$assetId"
    }
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
import com.mutebi.stockinvestmentapp.features.profile.setup.ProfileSetupScreen
import com.mutebi.stockinvestmentapp.features.splash.SplashScreen
import com.mutebi.stockinvestmentapp.features.watchlist.WatchlistScreen

@Composable
fun AppNavGraph(
    navController: NavHostController,
    isLoggedIn: Boolean,
    modifier: Modifier = Modifier
) {
    val kycVm: KycViewModel = viewModel()

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
                Phase5PlaceholderScreen(
                    title = "Asset detail unavailable",
                    message = "The selected asset ID was not found."
                )
            } else {
                AssetDetailScreen(
                    assetId = assetId,
                    onBack = { navController.popBackStack() },
                    onOpenWatchlist = {
                        navController.navigate(Routes.Watchlist.route)
                    }
                )
            }
        }

        composable(Routes.Portfolio.route) {
            Phase5PlaceholderScreen(
                title = "Portfolio comes in Phase 6",
                message = "This route is intentionally deferred until Portfolio and Trade Flow."
            )
        }
    }
}

@Composable
private fun Phase5PlaceholderScreen(
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

## File: `backend-api/app/routes/watchlist.py`

```python
from flask import Blueprint, request
from flask_jwt_extended import get_jwt_identity, jwt_required

from app.services.watchlist_service import WatchlistService
from app.utils.response import error_response, success_response

watchlist_bp = Blueprint("watchlist", __name__)


@watchlist_bp.get("")
@jwt_required()
def get_watchlist():
    items = WatchlistService.list_watchlist(int(get_jwt_identity()))
    return success_response(
        "Watchlist retrieved successfully.",
        {"items": items},
    )


@watchlist_bp.post("")
@jwt_required()
def add_to_watchlist():
    payload = request.get_json(silent=True) or {}
    asset_id = payload.get("asset_id")
    if not asset_id:
        return error_response("asset_id is required.", status_code=400)

    item, status_code, message = WatchlistService.add_asset(
        int(get_jwt_identity()),
        int(asset_id),
    )
    if item is None:
        return error_response(message, status_code=status_code)

    return success_response("Asset added to watchlist successfully.", item, 201)


@watchlist_bp.delete("/<int:asset_id>")
@jwt_required()
def remove_from_watchlist(asset_id: int):
    removed, status_code, message = WatchlistService.remove_asset(
        int(get_jwt_identity()),
        asset_id,
    )
    if not removed:
        return error_response(message, status_code=status_code)

    return success_response("Asset removed from watchlist successfully.")
```

---

## 3) Create these new backend files

---

## File: `backend-api/app/services/watchlist_service.py`

```python
from app.repositories.asset_repository import AssetRepository
from app.repositories.watchlist_repository import WatchlistRepository


class WatchlistService:
    @staticmethod
    def _serialize_item(item):
        asset = AssetRepository.get_by_id(item.asset_id)

        asset_dict = {}
        if asset and hasattr(asset, "to_dict"):
            asset_dict = asset.to_dict() or {}

        return {
            "asset_id": item.asset_id,
            "symbol": asset_dict.get("symbol", getattr(asset, "symbol", "")),
            "name": asset_dict.get("name", getattr(asset, "name", "")),
            "price": asset_dict.get("price", getattr(asset, "price", 0.0)),
        }

    @staticmethod
    def list_watchlist(user_id: int):
        items = WatchlistRepository.list_for_user(user_id)
        return [WatchlistService._serialize_item(item) for item in items]

    @staticmethod
    def add_asset(user_id: int, asset_id: int):
        asset = AssetRepository.get_by_id(asset_id)
        if not asset:
            return None, 404, "Asset not found."

        existing = WatchlistRepository.find_item(user_id, asset_id)
        if existing:
            return None, 409, "Asset already exists in watchlist."

        item = WatchlistRepository.add_item(user_id, asset_id)
        return WatchlistService._serialize_item(item), 201, "Asset added to watchlist successfully."

    @staticmethod
    def remove_asset(user_id: int, asset_id: int):
        item = WatchlistRepository.find_item(user_id, asset_id)
        if not item:
            return False, 404, "Watchlist item not found."

        WatchlistRepository.remove_item(item)
        return True, 200, "Asset removed from watchlist successfully."
```

---

## File: `backend-api/tests/test_market_assets.py`

```python
import uuid

from app.repositories.asset_repository import AssetRepository


def _seed_asset(symbol: str, name: str, price: float, change_percent: float, is_active: bool = True):
    return AssetRepository.create_asset(
        symbol=symbol,
        name=name,
        price=price,
        change_percent=change_percent,
        is_active=is_active,
    )


def test_list_market_assets(client):
    suffix = uuid.uuid4().hex[:6].upper()
    _seed_asset(f"MK{suffix}", f"Market Asset {suffix}", 125.50, 2.4, True)

    response = client.get("/api/v1/assets")
    body = response.get_json() or {}

    assert response.status_code == 200, body
    assert body.get("success") is True

    data = body.get("data") or {}
    assets = data.get("assets") or []
    assert len(assets) >= 1


def test_search_market_assets(client):
    suffix = uuid.uuid4().hex[:6].upper()
    symbol = f"SR{suffix}"
    _seed_asset(symbol, f"Search Asset {suffix}", 98.10, -1.1, True)

    response = client.get(f"/api/v1/assets?q={symbol}")
    body = response.get_json() or {}

    assert response.status_code == 200, body
    assert body.get("success") is True

    data = body.get("data") or {}
    assets = data.get("assets") or []
    assert any(asset.get("symbol") == symbol for asset in assets), assets


def test_get_single_market_asset(client):
    suffix = uuid.uuid4().hex[:6].upper()
    asset = _seed_asset(f"DT{suffix}", f"Detail Asset {suffix}", 77.00, 0.8, True)

    response = client.get(f"/api/v1/assets/{asset.id}")
    body = response.get_json() or {}

    assert response.status_code == 200, body
    assert body.get("success") is True

    data = body.get("data") or {}
    assert data.get("id") == asset.id
    assert data.get("symbol") == asset.symbol
```

---

## File: `backend-api/tests/test_watchlist.py`

```python
import uuid

from app.repositories.asset_repository import AssetRepository


def _unique_email() -> str:
    return f"phase5_watchlist_{uuid.uuid4().hex[:10]}@example.com"


def _register_user(client, email: str, password: str = "Password123!"):
    return client.post(
        "/api/v1/auth/register",
        json={
            "email": email,
            "password": password,
            "full_name": "Phase Five User",
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


def _seed_asset():
    suffix = uuid.uuid4().hex[:6].upper()
    return AssetRepository.create_asset(
        symbol=f"WL{suffix}",
        name=f"Watchlist Asset {suffix}",
        price=150.75,
        change_percent=3.2,
        is_active=True,
    )


def test_add_to_watchlist(client):
    headers = _auth_headers(client)
    asset = _seed_asset()

    response = client.post(
        "/api/v1/watchlist",
        headers=headers,
        json={"asset_id": asset.id},
    )
    body = response.get_json() or {}

    assert response.status_code in (200, 201), body
    assert body.get("success") is True

    data = body.get("data") or {}
    assert data.get("asset_id") == asset.id
    assert data.get("symbol") == asset.symbol


def test_get_watchlist(client):
    headers = _auth_headers(client)
    asset = _seed_asset()

    add_response = client.post(
        "/api/v1/watchlist",
        headers=headers,
        json={"asset_id": asset.id},
    )
    assert add_response.status_code in (200, 201), add_response.get_json()

    response = client.get("/api/v1/watchlist", headers=headers)
    body = response.get_json() or {}

    assert response.status_code == 200, body
    assert body.get("success") is True

    data = body.get("data") or {}
    items = data.get("items") or []
    assert any(item.get("asset_id") == asset.id for item in items), items


def test_remove_from_watchlist(client):
    headers = _auth_headers(client)
    asset = _seed_asset()

    add_response = client.post(
        "/api/v1/watchlist",
        headers=headers,
        json={"asset_id": asset.id},
    )
    assert add_response.status_code in (200, 201), add_response.get_json()

    response = client.delete(f"/api/v1/watchlist/{asset.id}", headers=headers)
    body = response.get_json() or {}

    assert response.status_code == 200, body
    assert body.get("success") is True
```

---

## 4) Create these new Android files

---

## File: `mobile-android/app/src/main/java/com/mutebi/stockinvestmentapp/features/home/dashboard/DashboardUiState.kt`

```kotlin
package com.mutebi.stockinvestmentapp.features.home.dashboard

import com.mutebi.stockinvestmentapp.domain.model.Asset
import com.mutebi.stockinvestmentapp.domain.model.WatchlistItem

data class DashboardUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val topAssets: List<Asset> = emptyList(),
    val watchlistItems: List<WatchlistItem> = emptyList()
)
```

---

## File: `mobile-android/app/src/main/java/com/mutebi/stockinvestmentapp/features/home/dashboard/DashboardViewModel.kt`

```kotlin
package com.mutebi.stockinvestmentapp.features.home.dashboard

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.mutebi.stockinvestmentapp.core.utils.Resource
import com.mutebi.stockinvestmentapp.data.remote.network.RetrofitProvider
import com.mutebi.stockinvestmentapp.data.repository.AssetRepository
import com.mutebi.stockinvestmentapp.data.repository.WatchlistRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class DashboardViewModel(application: Application) : AndroidViewModel(application) {

    private val assetRepository = AssetRepository(
        RetrofitProvider.assetApi(application)
    )

    private val watchlistRepository = WatchlistRepository(
        RetrofitProvider.watchlistApi(application)
    )

    private val _uiState = MutableStateFlow(DashboardUiState(isLoading = true))
    val uiState: StateFlow<DashboardUiState> = _uiState

    init {
        refresh()
    }

    fun refresh() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)

            val assetsResult = assetRepository.listAssets(active = true)
            val watchlistResult = watchlistRepository.getWatchlist()

            val assets = when (assetsResult) {
                is Resource.Success -> assetsResult.data.orEmpty()
                else -> emptyList()
            }

            val watchlistItems = when (watchlistResult) {
                is Resource.Success -> watchlistResult.data.orEmpty()
                else -> emptyList()
            }

            val errorMessage = when {
                assetsResult is Resource.Error -> assetsResult.message
                watchlistResult is Resource.Error -> watchlistResult.message
                else -> null
            }

            _uiState.value = DashboardUiState(
                isLoading = false,
                error = errorMessage,
                topAssets = assets.sortedByDescending { it.changePercent }.take(5),
                watchlistItems = watchlistItems.take(4)
            )
        }
    }
}
```

---

## File: `mobile-android/app/src/main/java/com/mutebi/stockinvestmentapp/features/home/dashboard/DashboardScreen.kt`

```kotlin
package com.mutebi.stockinvestmentapp.features.home.dashboard

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.item
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mutebi.stockinvestmentapp.features.home.components.EducationPromptCard
import com.mutebi.stockinvestmentapp.features.home.components.MarketHighlightsCard
import com.mutebi.stockinvestmentapp.features.home.components.PortfolioSummaryCard
import com.mutebi.stockinvestmentapp.features.home.components.WatchlistPreviewCard

@Composable
fun DashboardScreen(
    onOpenMarket: () -> Unit,
    onOpenWatchlist: () -> Unit,
    vm: DashboardViewModel = viewModel()
) {
    val state by vm.uiState.collectAsState()

    if (state.isLoading) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item { CircularProgressIndicator() }
            item { Text("Loading dashboard...") }
        }
        return
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text("Dashboard")
        }

        item {
            PortfolioSummaryCard(
                trackedAssets = state.topAssets.size,
                watchlistCount = state.watchlistItems.size,
                onOpenWatchlist = onOpenWatchlist
            )
        }

        item {
            MarketHighlightsCard(
                assets = state.topAssets,
                onOpenMarket = onOpenMarket
            )
        }

        item {
            WatchlistPreviewCard(
                items = state.watchlistItems,
                onOpenWatchlist = onOpenWatchlist
            )
        }

        item {
            EducationPromptCard()
        }

        state.error?.let { errorMessage ->
            item {
                Text(
                    text = errorMessage,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
        }
    }
}
```

---

## File: `mobile-android/app/src/main/java/com/mutebi/stockinvestmentapp/features/home/components/PortfolioSummaryCard.kt`

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
fun PortfolioSummaryCard(
    trackedAssets: Int,
    watchlistCount: Int,
    onOpenWatchlist: () -> Unit
) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text("Portfolio summary")
            Text("Portfolio holdings arrive in Phase 6.")
            Text("Tracked market highlights: $trackedAssets")
            Text("Watchlist items: $watchlistCount")

            Button(onClick = onOpenWatchlist) {
                Text("Open watchlist")
            }
        }
    }
}
```

---

## File: `mobile-android/app/src/main/java/com/mutebi/stockinvestmentapp/features/home/components/MarketHighlightsCard.kt`

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
import com.mutebi.stockinvestmentapp.domain.model.Asset

@Composable
fun MarketHighlightsCard(
    assets: List<Asset>,
    onOpenMarket: () -> Unit
) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text("Market highlights")

            if (assets.isEmpty()) {
                Text("No market highlights yet.")
            } else {
                assets.take(3).forEach { asset ->
                    Text("${asset.symbol} • ${asset.name} • $${"%.2f".format(asset.price)} • ${"%.2f".format(asset.changePercent)}%")
                }
            }

            Button(onClick = onOpenMarket) {
                Text("Browse market")
            }
        }
    }
}
```

---

## File: `mobile-android/app/src/main/java/com/mutebi/stockinvestmentapp/features/home/components/EducationPromptCard.kt`

```kotlin
package com.mutebi.stockinvestmentapp.features.home.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun EducationPromptCard() {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text("Investor tip")
            Text("Diversify carefully, research assets, and avoid acting only on short-term price swings.")
        }
    }
}
```

---

## File: `mobile-android/app/src/main/java/com/mutebi/stockinvestmentapp/features/home/components/WatchlistPreviewCard.kt`

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
import com.mutebi.stockinvestmentapp.domain.model.WatchlistItem

@Composable
fun WatchlistPreviewCard(
    items: List<WatchlistItem>,
    onOpenWatchlist: () -> Unit
) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text("Watchlist preview")

            if (items.isEmpty()) {
                Text("Your watchlist is empty.")
            } else {
                items.take(3).forEach { item ->
                    Text("${item.symbol} • ${item.name} • $${"%.2f".format(item.price)}")
                }
            }

            Button(onClick = onOpenWatchlist) {
                Text("View all")
            }
        }
    }
}
```

---

## File: `mobile-android/app/src/main/java/com/mutebi/stockinvestmentapp/features/market/list/MarketListUiState.kt`

```kotlin
package com.mutebi.stockinvestmentapp.features.market.list

import com.mutebi.stockinvestmentapp.domain.model.Asset

data class MarketListUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val searchQuery: String = "",
    val selectedFilter: String = "All",
    val assets: List<Asset> = emptyList(),
    val visibleAssets: List<Asset> = emptyList(),
    val watchlistIds: Set<Int> = emptySet()
)
```

---

## File: `mobile-android/app/src/main/java/com/mutebi/stockinvestmentapp/features/market/list/MarketListViewModel.kt`

```kotlin
package com.mutebi.stockinvestmentapp.features.market.list

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.mutebi.stockinvestmentapp.core.utils.Resource
import com.mutebi.stockinvestmentapp.data.remote.network.RetrofitProvider
import com.mutebi.stockinvestmentapp.data.repository.AssetRepository
import com.mutebi.stockinvestmentapp.data.repository.WatchlistRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MarketListViewModel(application: Application) : AndroidViewModel(application) {

    private val assetRepository = AssetRepository(
        RetrofitProvider.assetApi(application)
    )
    private val watchlistRepository = WatchlistRepository(
        RetrofitProvider.watchlistApi(application)
    )

    private val _uiState = MutableStateFlow(MarketListUiState(isLoading = true))
    val uiState: StateFlow<MarketListUiState> = _uiState

    init {
        refresh()
    }

    fun refresh() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)

            val assetsResult = assetRepository.listAssets(active = true)
            val watchlistResult = watchlistRepository.getWatchlist()

            val assets = when (assetsResult) {
                is Resource.Success -> assetsResult.data.orEmpty()
                else -> emptyList()
            }

            val watchlistIds = when (watchlistResult) {
                is Resource.Success -> watchlistResult.data.orEmpty().map { it.assetId }.toSet()
                else -> emptySet()
            }

            val errorMessage = when {
                assetsResult is Resource.Error -> assetsResult.message
                watchlistResult is Resource.Error -> watchlistResult.message
                else -> null
            }

            _uiState.value = _uiState.value.copy(
                isLoading = false,
                error = errorMessage,
                assets = assets,
                watchlistIds = watchlistIds
            )

            applyFilters()
        }
    }

    fun onSearchQueryChange(value: String) {
        _uiState.value = _uiState.value.copy(searchQuery = value)
        applyFilters()
    }

    fun onFilterChange(value: String) {
        _uiState.value = _uiState.value.copy(selectedFilter = value)
        applyFilters()
    }

    fun toggleWatchlist(assetId: Int) {
        viewModelScope.launch {
            val watchlistIds = _uiState.value.watchlistIds

            if (assetId in watchlistIds) {
                val result = watchlistRepository.removeFromWatchlist(assetId)
                if (result is Resource.Success) {
                    _uiState.value = _uiState.value.copy(
                        watchlistIds = _uiState.value.watchlistIds - assetId,
                        error = null
                    )
                } else if (result is Resource.Error) {
                    _uiState.value = _uiState.value.copy(error = result.message)
                }
            } else {
                val result = watchlistRepository.addToWatchlist(assetId)
                if (result is Resource.Success) {
                    _uiState.value = _uiState.value.copy(
                        watchlistIds = _uiState.value.watchlistIds + assetId,
                        error = null
                    )
                } else if (result is Resource.Error) {
                    _uiState.value = _uiState.value.copy(error = result.message)
                }
            }
        }
    }

    private fun applyFilters() {
        val current = _uiState.value
        val query = current.searchQuery.trim().lowercase()

        val filtered = current.assets.filter { asset ->
            val matchesQuery = query.isBlank() ||
                asset.symbol.lowercase().contains(query) ||
                asset.name.lowercase().contains(query)

            val matchesFilter = when (current.selectedFilter) {
                "Gainers" -> asset.changePercent >= 0
                "Decliners" -> asset.changePercent < 0
                else -> true
            }

            matchesQuery && matchesFilter
        }

        _uiState.value = current.copy(visibleAssets = filtered)
    }
}
```

---

## File: `mobile-android/app/src/main/java/com/mutebi/stockinvestmentapp/features/market/list/MarketListScreen.kt`

```kotlin
package com.mutebi.stockinvestmentapp.features.market.list

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
import com.mutebi.stockinvestmentapp.features.market.components.AssetCard
import com.mutebi.stockinvestmentapp.features.market.components.FilterChipGroup
import com.mutebi.stockinvestmentapp.features.market.components.MarketSearchBar

@Composable
fun MarketListScreen(
    onAssetClick: (Int) -> Unit,
    onOpenWatchlist: () -> Unit,
    vm: MarketListViewModel = viewModel()
) {
    val state by vm.uiState.collectAsState()

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text("Market")
        }

        item {
            MarketSearchBar(
                value = state.searchQuery,
                onValueChange = vm::onSearchQueryChange
            )
        }

        item {
            FilterChipGroup(
                selected = state.selectedFilter,
                onSelectedChange = vm::onFilterChange
            )
        }

        item {
            TextButton(onClick = onOpenWatchlist) {
                Text("Open watchlist")
            }
        }

        if (state.isLoading) {
            item {
                CircularProgressIndicator()
            }
        } else {
            items(state.visibleAssets, key = { it.id }) { asset ->
                AssetCard(
                    asset = asset,
                    isInWatchlist = asset.id in state.watchlistIds,
                    onClick = { onAssetClick(asset.id) },
                    onWatchlistClick = { vm.toggleWatchlist(asset.id) }
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

## File: `mobile-android/app/src/main/java/com/mutebi/stockinvestmentapp/features/market/details/AssetDetailUiState.kt`

```kotlin
package com.mutebi.stockinvestmentapp.features.market.details

import com.mutebi.stockinvestmentapp.domain.model.Asset

data class AssetDetailUiState(
    val isLoading: Boolean = false,
    val actionLoading: Boolean = false,
    val error: String? = null,
    val asset: Asset? = null,
    val isInWatchlist: Boolean = false
)
```

---

## File: `mobile-android/app/src/main/java/com/mutebi/stockinvestmentapp/features/market/details/AssetDetailViewModel.kt`

```kotlin
package com.mutebi.stockinvestmentapp.features.market.details

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.mutebi.stockinvestmentapp.core.utils.Resource
import com.mutebi.stockinvestmentapp.data.remote.network.RetrofitProvider
import com.mutebi.stockinvestmentapp.data.repository.AssetRepository
import com.mutebi.stockinvestmentapp.data.repository.WatchlistRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AssetDetailViewModel(application: Application) : AndroidViewModel(application) {

    private val assetRepository = AssetRepository(
        RetrofitProvider.assetApi(application)
    )
    private val watchlistRepository = WatchlistRepository(
        RetrofitProvider.watchlistApi(application)
    )

    private val _uiState = MutableStateFlow(AssetDetailUiState(isLoading = true))
    val uiState: StateFlow<AssetDetailUiState> = _uiState

    fun loadAsset(assetId: Int) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)

            val assetResult = assetRepository.getAssetById(assetId)
            val watchlistResult = watchlistRepository.getWatchlist()

            val asset = when (assetResult) {
                is Resource.Success -> assetResult.data
                else -> null
            }

            val watchlistIds = when (watchlistResult) {
                is Resource.Success -> watchlistResult.data.orEmpty().map { it.assetId }.toSet()
                else -> emptySet()
            }

            val errorMessage = when {
                assetResult is Resource.Error -> assetResult.message
                watchlistResult is Resource.Error -> watchlistResult.message
                else -> null
            }

            _uiState.value = _uiState.value.copy(
                isLoading = false,
                error = errorMessage,
                asset = asset,
                isInWatchlist = asset?.id in watchlistIds
            )
        }
    }

    fun toggleWatchlist() {
        val asset = _uiState.value.asset ?: return

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(actionLoading = true, error = null)

            val result = if (_uiState.value.isInWatchlist) {
                watchlistRepository.removeFromWatchlist(asset.id)
            } else {
                watchlistRepository.addToWatchlist(asset.id)
            }

            when (result) {
                is Resource.Success -> {
                    _uiState.value = _uiState.value.copy(
                        actionLoading = false,
                        isInWatchlist = !_uiState.value.isInWatchlist
                    )
                }
                is Resource.Error -> {
                    _uiState.value = _uiState.value.copy(
                        actionLoading = false,
                        error = result.message
                    )
                }
                else -> {
                    _uiState.value = _uiState.value.copy(actionLoading = false)
                }
            }
        }
    }
}
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

## File: `mobile-android/app/src/main/java/com/mutebi/stockinvestmentapp/features/market/components/AssetPriceRow.kt`

```kotlin
package com.mutebi.stockinvestmentapp.features.market.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.mutebi.stockinvestmentapp.core.ui.components.PercentageBadge
import com.mutebi.stockinvestmentapp.domain.model.Asset

@Composable
fun AssetPriceRow(asset: Asset) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text("${asset.symbol} • ${asset.name}")
        PercentageBadge(value = asset.changePercent)
    }
}
```

---

## File: `mobile-android/app/src/main/java/com/mutebi/stockinvestmentapp/features/market/components/MarketSearchBar.kt`

```kotlin
package com.mutebi.stockinvestmentapp.features.market.components

import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun MarketSearchBar(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        label = { Text("Search by symbol or name") },
        singleLine = true
    )
}
```

---

## File: `mobile-android/app/src/main/java/com/mutebi/stockinvestmentapp/features/market/components/FilterChipGroup.kt`

```kotlin
package com.mutebi.stockinvestmentapp.features.market.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp

@Composable
fun FilterChipGroup(
    selected: String,
    onSelectedChange: (String) -> Unit
) {
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        listOf("All", "Gainers", "Decliners").forEach { label ->
            FilterChip(
                selected = selected == label,
                onClick = { onSelectedChange(label) },
                label = { Text(label) }
            )
        }
    }
}
```

---

## File: `mobile-android/app/src/main/java/com/mutebi/stockinvestmentapp/features/market/components/AssetCard.kt`

```kotlin
package com.mutebi.stockinvestmentapp.features.market.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mutebi.stockinvestmentapp.core.ui.components.MiniLineChart
import com.mutebi.stockinvestmentapp.domain.model.Asset

@Composable
fun AssetCard(
    asset: Asset,
    isInWatchlist: Boolean,
    onClick: () -> Unit,
    onWatchlistClick: () -> Unit
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
            AssetPriceRow(asset = asset)
            Text("Price: $${"%.2f".format(asset.price)}")

            MiniLineChart(
                values = listOf(
                    (asset.price * 0.96).toFloat(),
                    (asset.price * 1.01).toFloat(),
                    (asset.price * 0.99).toFloat(),
                    asset.price.toFloat()
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
            )

            Button(onClick = onWatchlistClick) {
                Text(if (isInWatchlist) "Remove from watchlist" else "Add to watchlist")
            }
        }
    }
}
```

---

## File: `mobile-android/app/src/main/java/com/mutebi/stockinvestmentapp/features/watchlist/WatchlistUiState.kt`

```kotlin
package com.mutebi.stockinvestmentapp.features.watchlist

import com.mutebi.stockinvestmentapp.domain.model.WatchlistItem

data class WatchlistUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val items: List<WatchlistItem> = emptyList()
)
```

---

## File: `mobile-android/app/src/main/java/com/mutebi/stockinvestmentapp/features/watchlist/WatchlistViewModel.kt`

```kotlin
package com.mutebi.stockinvestmentapp.features.watchlist

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.mutebi.stockinvestmentapp.core.utils.Resource
import com.mutebi.stockinvestmentapp.data.remote.network.RetrofitProvider
import com.mutebi.stockinvestmentapp.data.repository.WatchlistRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class WatchlistViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = WatchlistRepository(
        RetrofitProvider.watchlistApi(application)
    )

    private val _uiState = MutableStateFlow(WatchlistUiState(isLoading = true))
    val uiState: StateFlow<WatchlistUiState> = _uiState

    init {
        loadWatchlist()
    }

    fun loadWatchlist() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)

            when (val result = repository.getWatchlist()) {
                is Resource.Success -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        items = result.data.orEmpty()
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

    fun removeItem(assetId: Int) {
        viewModelScope.launch {
            when (val result = repository.removeFromWatchlist(assetId)) {
                is Resource.Success -> {
                    _uiState.value = _uiState.value.copy(
                        items = _uiState.value.items.filterNot { it.assetId == assetId },
                        error = null
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

## File: `mobile-android/app/src/main/java/com/mutebi/stockinvestmentapp/features/watchlist/WatchlistScreen.kt`

```kotlin
package com.mutebi.stockinvestmentapp.features.watchlist

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

@Composable
fun WatchlistScreen(
    onOpenMarket: () -> Unit,
    onBack: () -> Unit,
    vm: WatchlistViewModel = viewModel()
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
            Text("Watchlist")
        }

        item {
            Button(onClick = onOpenMarket) {
                Text("Browse market")
            }
        }

        if (state.isLoading) {
            item {
                CircularProgressIndicator()
            }
        } else if (state.items.isEmpty()) {
            item {
                Text("Your watchlist is empty.")
            }
        } else {
            items(state.items, key = { it.assetId }) { item ->
                androidx.compose.material3.Card {
                    androidx.compose.foundation.layout.Column(
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text("${item.symbol} • ${item.name}")
                        Text("Price: $${"%.2f".format(item.price)}")
                        Button(onClick = { vm.removeItem(item.assetId) }) {
                            Text("Remove")
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

## File: `mobile-android/app/src/main/java/com/mutebi/stockinvestmentapp/core/ui/components/MiniLineChart.kt`

```kotlin
package com.mutebi.stockinvestmentapp.core.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke

@Composable
fun MiniLineChart(
    values: List<Float>,
    modifier: Modifier = Modifier
) {
    Canvas(modifier = modifier) {
        if (values.size < 2) return@Canvas

        val min = values.minOrNull() ?: 0f
        val max = values.maxOrNull() ?: 0f
        val range = (max - min).takeIf { it != 0f } ?: 1f
        val stepX = size.width / (values.size - 1).coerceAtLeast(1)

        val path = Path()
        values.forEachIndexed { index, value ->
            val x = index * stepX
            val y = size.height - ((value - min) / range) * size.height
            val point = Offset(x, y)
            if (index == 0) path.moveTo(point.x, point.y) else path.lineTo(point.x, point.y)
        }

        drawPath(path = path, style = Stroke(width = 4f))
    }
}
```

---

## File: `mobile-android/app/src/main/java/com/mutebi/stockinvestmentapp/core/ui/components/PercentageBadge.kt`

```kotlin
package com.mutebi.stockinvestmentapp.core.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun PercentageBadge(value: Double) {
    val positive = value >= 0

    Surface(
        tonalElevation = 2.dp,
        color = if (positive) {
            MaterialTheme.colorScheme.primaryContainer
        } else {
            MaterialTheme.colorScheme.errorContainer
        }
    ) {
        Text(
            text = "${if (positive) "+" else ""}${"%.2f".format(value)}%",
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
        )
    }
}
```

---

## 5) Directory creation commands

### From `mobile-android/`
```bash
mkdir -p app/src/main/java/com/mutebi/stockinvestmentapp/features/home/dashboard
mkdir -p app/src/main/java/com/mutebi/stockinvestmentapp/features/home/components
mkdir -p app/src/main/java/com/mutebi/stockinvestmentapp/features/market/list
mkdir -p app/src/main/java/com/mutebi/stockinvestmentapp/features/market/details
mkdir -p app/src/main/java/com/mutebi/stockinvestmentapp/features/market/components
mkdir -p app/src/main/java/com/mutebi/stockinvestmentapp/features/watchlist
mkdir -p app/src/main/java/com/mutebi/stockinvestmentapp/core/ui/components
```

### From `backend-api/`
```bash
mkdir -p app/services
mkdir -p tests
```

---

## 6) Verification sequence

### Backend
From `backend-api/`:

```bash
source env/bin/activate
pytest tests/test_market_assets.py tests/test_watchlist.py -q
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

## 7) Expected Phase 5 outcome

After these updates:

- splash/login land on a real dashboard instead of a placeholder
- dashboard shows welcome-market-watchlist summary
- market list loads assets from backend
- local search and filter work
- asset detail screen opens
- watchlist add/remove works from Android
- watchlist screen renders and remove works
- backend tests for assets and watchlist now exist

That matches the Phase 5 goals and deliverables in your timeline. fileciteturn24file11turn24file15
