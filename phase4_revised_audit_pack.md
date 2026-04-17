# Phase 4 Revised Audit Pack — Responsible Stock Trading & Investment App

This revised pack is **audit-first**.

It is based on the files you actually shared from your current Android codebase, so it avoids replacing files that are already populated and working.

---

## 1) Phase 4 scope we are targeting

Phase 4 is for:

- onboarding UI
- sign up
- login
- forgot password
- profile completion
- KYC submission screens
- auth endpoint integration
- session persistence

It is **not** the phase for the real dashboard. Dashboard belongs to **Phase 5**.

---

## 2) Audit verdict from the files you shared

### A. Already populated — keep these as they are

Do **not** replace these files wholesale.

```text
mobile-android/app/src/main/java/com/mutebi/stockinvestmentapp/data/remote/api/AuthApi.kt
mobile-android/app/src/main/java/com/mutebi/stockinvestmentapp/data/repository/AuthRepository.kt
mobile-android/app/src/main/java/com/mutebi/stockinvestmentapp/features/auth/forgot_password/ForgotPasswordScreen.kt
mobile-android/app/src/main/java/com/mutebi/stockinvestmentapp/features/auth/forgot_password/ForgotPasswordViewModel.kt
mobile-android/app/src/main/java/com/mutebi/stockinvestmentapp/features/auth/register/RegisterScreen.kt
mobile-android/app/src/main/java/com/mutebi/stockinvestmentapp/features/auth/register/RegisterViewModel.kt
mobile-android/app/src/main/java/com/mutebi/stockinvestmentapp/features/profile/setup/ProfileSetupScreen.kt
mobile-android/app/src/main/java/com/mutebi/stockinvestmentapp/features/profile/setup/ProfileSetupViewModel.kt
mobile-android/app/src/main/java/com/mutebi/stockinvestmentapp/features/kyc/KycViewModel.kt
mobile-android/app/src/main/java/com/mutebi/stockinvestmentapp/data/remote/api/UserApi.kt
mobile-android/app/src/main/java/com/mutebi/stockinvestmentapp/data/remote/api/KycApi.kt
mobile-android/app/src/main/java/com/mutebi/stockinvestmentapp/data/repository/UserRepository.kt
mobile-android/app/src/main/java/com/mutebi/stockinvestmentapp/data/repository/KycRepository.kt
mobile-android/app/src/main/java/com/mutebi/stockinvestmentapp/di/NetworkModule.kt
mobile-android/app/src/main/java/com/mutebi/stockinvestmentapp/di/RepositoryModule.kt
```

### B. Phase 4 blocker identified

Your current `AppNavGraph.kt` sends:

- splash logged-in users -> `Dashboard`
- login success -> `Dashboard`
- KYC done -> `Dashboard`

But `Dashboard`, `Market`, `Watchlist`, and `Portfolio` are still empty TODO destinations. That is why you got the blank dark screen.

### C. Minimal patch strategy

For this revised pack:

- keep the already populated files above
- patch navigation so Phase 4 routes do not land on a blank Phase 5 screen
- keep DTO/API/repository naming consistent with your existing codebase
- add only safe backend close-out snippets instead of blind full-file replacement

---

## 3) Files to patch now

Only these are included as patch targets in this revised pack:

```text
mobile-android/app/src/main/java/com/mutebi/stockinvestmentapp/navigation/AppNavGraph.kt
mobile-android/app/src/main/java/com/mutebi/stockinvestmentapp/data/remote/dto/AuthDtos.kt
mobile-android/app/src/main/java/com/mutebi/stockinvestmentapp/data/remote/network/RetrofitProvider.kt
backend-api/app/__init__.py
backend-api/app/routes/auth.py
backend-api/.env
```

---

## 4) Replace this file — AppNavGraph.kt

**File:** `mobile-android/app/src/main/java/com/mutebi/stockinvestmentapp/navigation/AppNavGraph.kt`

This version fixes the blank-screen problem without dragging Phase 5 implementation into Phase 4.

```kotlin
package com.mutebi.stockinvestmentapp.navigation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.mutebi.stockinvestmentapp.features.auth.forgot_password.ForgotPasswordScreen
import com.mutebi.stockinvestmentapp.features.auth.login.LoginScreen
import com.mutebi.stockinvestmentapp.features.auth.register.RegisterScreen
import com.mutebi.stockinvestmentapp.features.kyc.KycDocumentUploadScreen
import com.mutebi.stockinvestmentapp.features.kyc.KycIntroScreen
import com.mutebi.stockinvestmentapp.features.kyc.KycPersonalInfoScreen
import com.mutebi.stockinvestmentapp.features.kyc.KycReviewScreen
import com.mutebi.stockinvestmentapp.features.kyc.KycSuccessScreen
import com.mutebi.stockinvestmentapp.features.onboarding.OnboardingScreen
import com.mutebi.stockinvestmentapp.features.profile.setup.ProfileSetupScreen
import com.mutebi.stockinvestmentapp.features.splash.SplashScreen

@Composable
fun AppNavGraph(
    navController: NavHostController,
    isLoggedIn: Boolean,
    modifier: Modifier = Modifier
) {
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
                    navController.navigate(Routes.ProfileSetup.route) {
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
                    navController.navigate(Routes.ProfileSetup.route) {
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
                onNext = {
                    navController.navigate(Routes.KycDocumentUpload.route)
                }
            )
        }

        composable(Routes.KycDocumentUpload.route) {
            KycDocumentUploadScreen(
                onNext = {
                    navController.navigate(Routes.KycReview.route)
                }
            )
        }

        composable(Routes.KycReview.route) {
            KycReviewScreen(
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
            Phase4PlaceholderScreen(
                title = "Dashboard comes in Phase 5",
                message = "Phase 4 is complete up to auth, profile setup, and KYC."
            )
        }

        composable(Routes.Market.route) {
            Phase4PlaceholderScreen(
                title = "Market comes in Phase 5",
                message = "This route is intentionally deferred until Market Discovery."
            )
        }

        composable(Routes.Watchlist.route) {
            Phase4PlaceholderScreen(
                title = "Watchlist comes in Phase 5",
                message = "This route is intentionally deferred until Market Discovery."
            )
        }

        composable(Routes.Portfolio.route) {
            Phase4PlaceholderScreen(
                title = "Portfolio comes in Phase 6",
                message = "This route is intentionally deferred until Portfolio and Trade Flow."
            )
        }
    }
}

@Composable
private fun Phase4PlaceholderScreen(
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

## 5) Replace this file only if it still matches your earlier version — AuthDtos.kt

**File:** `mobile-android/app/src/main/java/com/mutebi/stockinvestmentapp/data/remote/dto/AuthDtos.kt`

Do **not** rename your DTO classes.
Do **not** switch to `AuthEnvelope`, `LoginRequest`, or `RegisterRequest`.
Keep your current names and only add support for backend validation `errors`.

```kotlin
package com.mutebi.stockinvestmentapp.data.remote.dto

import com.google.gson.annotations.SerializedName
import com.mutebi.stockinvestmentapp.domain.model.UserProfile

data class ApiEnvelopeDto<T>(
    @SerializedName("success")
    val success: Boolean = false,
    @SerializedName("message")
    val message: String = "",
    @SerializedName("data")
    val data: T? = null,
    @SerializedName("token")
    val token: String? = null,
    @SerializedName("errors")
    val errors: Map<String, List<String>>? = null
)

data class LoginRequestDto(
    @SerializedName("email")
    val email: String,
    @SerializedName("password")
    val password: String
)

data class RegisterRequestDto(
    @SerializedName("email")
    val email: String,
    @SerializedName("password")
    val password: String,
    @SerializedName("full_name")
    val fullName: String
)

data class ForgotPasswordRequestDto(
    @SerializedName("email")
    val email: String
)

data class AuthUserDto(
    @SerializedName("id")
    val id: Int,
    @SerializedName("email")
    val email: String,
    @SerializedName("full_name")
    val fullName: String? = null,
    @SerializedName("profile_completed")
    val profileCompleted: Boolean = false,
    @SerializedName("kyc_status")
    val kycStatus: String? = null
) {
    fun toDomain(): UserProfile {
        return UserProfile(
            id = id,
            email = email,
            fullName = fullName.orEmpty(),
            phoneNumber = "",
            country = "",
            dateOfBirth = "",
            profileCompleted = profileCompleted,
            kycStatus = kycStatus.orEmpty()
        )
    }
}

data class AuthPayloadDto(
    @SerializedName("token")
    val token: String? = null,
    @SerializedName("access_token")
    val accessToken: String? = null,
    @SerializedName("user")
    val user: AuthUserDto? = null
)

/**
 * Keep this temporarily if some API calls still expect the old flat response.
 */
data class AuthResponseDto(
    @SerializedName("success")
    val success: Boolean = false,
    @SerializedName("message")
    val message: String = "",
    @SerializedName("token")
    val token: String? = null,
    @SerializedName("user")
    val user: AuthUserDto? = null
)

data class GenericMessageResponseDto(
    @SerializedName("success")
    val success: Boolean = false,
    @SerializedName("message")
    val message: String = ""
)
```

---

## 6) Patch this file if it still has a dead local BASE_URL — RetrofitProvider.kt

**File:** `mobile-android/app/src/main/java/com/mutebi/stockinvestmentapp/data/remote/network/RetrofitProvider.kt`

What to enforce:

- remove any local `private const val BASE_URL = ...`
- keep a single source of truth in `ApiConstants.BASE_URL`
- continue using your existing `AuthInterceptor`

Your Retrofit builder should use this pattern:

```kotlin
.baseUrl(ApiConstants.BASE_URL)
```

Not this pattern:

```kotlin
.baseUrl(BASE_URL)
```

---

## 7) Backend additive patch — __init__.py

**File:** `backend-api/app/__init__.py`

Do not replace the whole file.
Add the missing import near the top:

```python
from app.utils.response import error_response
```

Then directly after `jwt.init_app(app)` inside your extension registration area, add:

```python
@jwt.unauthorized_loader
def handle_missing_jwt(reason):
    return error_response("Authentication required.", status_code=401)

@jwt.invalid_token_loader
def handle_invalid_jwt(reason):
    return error_response("Invalid or malformed access token.", status_code=401)

@jwt.expired_token_loader
def handle_expired_jwt(jwt_header, jwt_payload):
    return error_response("Session expired. Please log in again.", status_code=401)

@jwt.revoked_token_loader
def handle_revoked_jwt(jwt_header, jwt_payload):
    return error_response("This session is no longer valid.", status_code=401)
```

---

## 8) Backend additive patch — auth.py forgot-password route

**File:** `backend-api/app/routes/auth.py`

Do not replace the whole file.
Insert this block below `login()` and above `@auth_bp.get("/me")` if it is still missing:

```python
@auth_bp.post("/forgot-password")
def forgot_password():
    payload = request.get_json(silent=True) or {}
    email = (payload.get("email") or "").strip().lower()

    if not email:
        return error_response("Email is required.", status_code=400)

    return success_response(
        "If an account exists for that email, a reset link has been sent.",
        None,
        200,
    )
```

---

## 9) Backend env patch

**File:** `backend-api/.env`

Ensure your JWT secret is strong and at least 32 characters.

Example:

```env
JWT_SECRET_KEY=stock-investment-app-super-secret-jwt-key-2026-strong
```

---

## 10) Files audited but intentionally not replaced in this pack

These are already populated enough for Phase 4 and should stay as-is unless a real bug appears during testing:

```text
AuthApi.kt
AuthRepository.kt
ForgotPasswordScreen.kt
ForgotPasswordViewModel.kt
RegisterScreen.kt
RegisterViewModel.kt
ProfileSetupScreen.kt
ProfileSetupViewModel.kt
KycViewModel.kt
UserApi.kt
KycApi.kt
UserRepository.kt
KycRepository.kt
NetworkModule.kt
RepositoryModule.kt
```

---

## 11) Files still not audited in this revised pass

These may already be fine, but they were not fully inspected in this audit batch, so do not replace them blindly.

```text
mobile-android/app/src/main/java/com/mutebi/stockinvestmentapp/features/auth/login/LoginUiState.kt
mobile-android/app/src/main/java/com/mutebi/stockinvestmentapp/features/auth/register/RegisterUiState.kt
mobile-android/app/src/main/java/com/mutebi/stockinvestmentapp/features/auth/forgot_password/ForgotPasswordUiState.kt
mobile-android/app/src/main/java/com/mutebi/stockinvestmentapp/features/profile/setup/ProfileSetupUiState.kt
mobile-android/app/src/main/java/com/mutebi/stockinvestmentapp/features/profile/setup/components/ProfileAvatarPicker.kt
mobile-android/app/src/main/java/com/mutebi/stockinvestmentapp/features/profile/setup/components/ProfileForm.kt
mobile-android/app/src/main/java/com/mutebi/stockinvestmentapp/features/kyc/KycIntroScreen.kt
mobile-android/app/src/main/java/com/mutebi/stockinvestmentapp/features/kyc/KycPersonalInfoScreen.kt
mobile-android/app/src/main/java/com/mutebi/stockinvestmentapp/features/kyc/KycDocumentUploadScreen.kt
mobile-android/app/src/main/java/com/mutebi/stockinvestmentapp/features/kyc/KycReviewScreen.kt
mobile-android/app/src/main/java/com/mutebi/stockinvestmentapp/features/kyc/KycSuccessScreen.kt
mobile-android/app/src/main/java/com/mutebi/stockinvestmentapp/features/kyc/KycUiState.kt
mobile-android/app/src/main/java/com/mutebi/stockinvestmentapp/data/remote/dto/UserDtos.kt
mobile-android/app/src/main/java/com/mutebi/stockinvestmentapp/data/remote/dto/KycDtos.kt
mobile-android/app/src/main/java/com/mutebi/stockinvestmentapp/domain/model/UserProfile.kt
mobile-android/app/src/main/java/com/mutebi/stockinvestmentapp/domain/model/KycProfile.kt
backend-api/app/routes/users.py
backend-api/app/routes/kyc.py
backend-api/app/services/user_service.py
backend-api/app/services/kyc_service.py
backend-api/tests/test_users.py
backend-api/tests/test_kyc.py
```

If you want a second revised pack after this one, the next audit batch should be exactly the files above.

---

## 12) Exact device test sequence after applying only the patches above

### Backend

```bash
cd ~/Downloads/sem32/Mutebi/stock-investment-app/backend-api
source env/bin/activate
alembic upgrade head
python3 run.py
```

### Auth checks

```bash
curl -s -X POST http://127.0.0.1:5000/api/v1/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "email": "phase4_test_user@example.com",
    "password": "Password123!",
    "full_name": "Phase Four User"
  }'

TOKEN=$(curl -s -X POST http://127.0.0.1:5000/api/v1/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "phase4_test_user@example.com",
    "password": "Password123!"
  }' | python3 -c 'import sys, json; print((json.load(sys.stdin).get("data") or {}).get("access_token", ""))')

curl -s http://127.0.0.1:5000/api/v1/auth/me \
  -H "Authorization: Bearer $TOKEN"

curl -s -X POST http://127.0.0.1:5000/api/v1/auth/forgot-password \
  -H "Content-Type: application/json" \
  -d '{"email":"phase4_test_user@example.com"}'
```

### Profile and KYC checks

```bash
curl -s http://127.0.0.1:5000/api/v1/users/me \
  -H "Authorization: Bearer $TOKEN"

curl -s -X PATCH http://127.0.0.1:5000/api/v1/users/me/profile \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "full_name": "Phase Four Updated",
    "phone_number": "+256700000000",
    "country": "Uganda",
    "date_of_birth": "2000-01-01"
  }'

curl -s -X POST http://127.0.0.1:5000/api/v1/kyc/submit \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "first_name": "Phase",
    "last_name": "User",
    "national_id_number": "CM1234567890AA",
    "document_type": "national_id",
    "document_number": "ID-12345678"
  }'

curl -s http://127.0.0.1:5000/api/v1/kyc/me \
  -H "Authorization: Bearer $TOKEN"
```

### Android device flow

```bash
cd ~/Downloads/sem32/Mutebi/stock-investment-app/mobile-android
adb devices
adb reverse tcp:5000 tcp:5000
adb reverse --list
./gradlew clean
./gradlew installDebug
adb shell am start -n com.mutebi.stockinvestmentapp/.MainActivity
adb logcat
```

---

## 13) Phase 4 done criteria after this revised pack

Declare Phase 4 done only when all of these pass:

- register works
- login works
- forgot password returns `200` with a safe message
- session persists after reopen
- profile setup saves successfully
- KYC submit works
- KYC reload works
- no blank screen after login
- no raw JWT framework errors leak into normal UI flow

---

## 14) Next phase after this revised pack

Only after the checklist above passes should you move to:

- `DashboardScreen.kt`
- `MarketListScreen.kt`
- `AssetDetailScreen.kt`
- `WatchlistScreen.kt`

Those belong to **Phase 5**, not Phase 4.
