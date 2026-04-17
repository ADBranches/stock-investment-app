# Phase 4 Final Revised Close-Out Pack (Android + Backend)

This pack is based on the **actual files you shared**, not guessed replacements.

---

## 1) Keep these files as-is

These are already populated well enough for Phase 4 and do **not** need wholesale replacement:

### Android
- `app/src/main/java/com/mutebi/stockinvestmentapp/data/remote/api/AuthApi.kt`
- `app/src/main/java/com/mutebi/stockinvestmentapp/data/repository/AuthRepository.kt`
- `app/src/main/java/com/mutebi/stockinvestmentapp/features/auth/forgot_password/ForgotPasswordScreen.kt`
- `app/src/main/java/com/mutebi/stockinvestmentapp/features/auth/forgot_password/ForgotPasswordViewModel.kt`
- `app/src/main/java/com/mutebi/stockinvestmentapp/features/auth/register/RegisterScreen.kt`
- `app/src/main/java/com/mutebi/stockinvestmentapp/features/auth/register/RegisterViewModel.kt`
- `app/src/main/java/com/mutebi/stockinvestmentapp/features/auth/login/LoginUiState.kt`
- `app/src/main/java/com/mutebi/stockinvestmentapp/features/auth/register/RegisterUiState.kt`
- `app/src/main/java/com/mutebi/stockinvestmentapp/features/auth/forgot_password/ForgotPasswordUiState.kt`
- `app/src/main/java/com/mutebi/stockinvestmentapp/features/profile/setup/ProfileSetupScreen.kt`
- `app/src/main/java/com/mutebi/stockinvestmentapp/features/profile/setup/ProfileSetupViewModel.kt`
- `app/src/main/java/com/mutebi/stockinvestmentapp/features/profile/setup/ProfileSetupUiState.kt`
- `app/src/main/java/com/mutebi/stockinvestmentapp/features/profile/setup/components/ProfileAvatarPicker.kt`
- `app/src/main/java/com/mutebi/stockinvestmentapp/features/profile/setup/components/ProfileForm.kt`
- `app/src/main/java/com/mutebi/stockinvestmentapp/features/kyc/KycIntroScreen.kt`
- `app/src/main/java/com/mutebi/stockinvestmentapp/features/kyc/KycSuccessScreen.kt`
- `app/src/main/java/com/mutebi/stockinvestmentapp/data/remote/api/UserApi.kt`
- `app/src/main/java/com/mutebi/stockinvestmentapp/data/remote/api/KycApi.kt`
- `app/src/main/java/com/mutebi/stockinvestmentapp/data/repository/UserRepository.kt`
- `app/src/main/java/com/mutebi/stockinvestmentapp/data/repository/KycRepository.kt`
- `app/src/main/java/com/mutebi/stockinvestmentapp/data/remote/dto/UserDtos.kt`
- `app/src/main/java/com/mutebi/stockinvestmentapp/data/remote/dto/KycDtos.kt`
- `app/src/main/java/com/mutebi/stockinvestmentapp/domain/model/UserProfile.kt`
- `app/src/main/java/com/mutebi/stockinvestmentapp/domain/model/KycProfile.kt`
- `app/src/main/java/com/mutebi/stockinvestmentapp/di/NetworkModule.kt`
- `app/src/main/java/com/mutebi/stockinvestmentapp/di/RepositoryModule.kt`

### Backend
- `backend-api/app/services/user_service.py`

---

## 2) Replace these Android files

---

## File: `mobile-android/app/src/main/java/com/mutebi/stockinvestmentapp/features/kyc/KycUiState.kt`

```kotlin
package com.mutebi.stockinvestmentapp.features.kyc

data class KycUiState(
    val firstName: String = "",
    val lastName: String = "",
    val nationalIdNumber: String = "",
    val documentType: String = "",
    val documentNumber: String = "",
    val status: String = "not_started",
    val isLoading: Boolean = false,
    val error: String? = null,
    val success: Boolean = false
)
```

---

## File: `mobile-android/app/src/main/java/com/mutebi/stockinvestmentapp/features/kyc/KycPersonalInfoScreen.kt`

```kotlin
package com.mutebi.stockinvestmentapp.features.kyc

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mutebi.stockinvestmentapp.features.auth.components.AuthHeader

@Composable
fun KycPersonalInfoScreen(
    onNext: () -> Unit,
    vm: KycViewModel
) {
    val state by vm.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center
    ) {
        AuthHeader(
            title = "Personal information",
            subtitle = "Enter your legal identity details"
        )

        OutlinedTextField(
            value = state.firstName,
            onValueChange = vm::onFirstNameChange,
            modifier = Modifier.fillMaxWidth(),
            label = { Text("First name") },
            singleLine = true
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = state.lastName,
            onValueChange = vm::onLastNameChange,
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Last name") },
            singleLine = true
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = state.nationalIdNumber,
            onValueChange = vm::onNationalIdNumberChange,
            modifier = Modifier.fillMaxWidth(),
            label = { Text("National ID number") },
            singleLine = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = onNext, modifier = Modifier.fillMaxWidth()) {
            Text("Next")
        }
    }
}
```

---

## File: `mobile-android/app/src/main/java/com/mutebi/stockinvestmentapp/features/kyc/KycDocumentUploadScreen.kt`

```kotlin
package com.mutebi.stockinvestmentapp.features.kyc

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mutebi.stockinvestmentapp.features.auth.components.AuthHeader

@Composable
fun KycDocumentUploadScreen(
    onNext: () -> Unit,
    vm: KycViewModel
) {
    val state by vm.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center
    ) {
        AuthHeader(
            title = "Document details",
            subtitle = "Provide the document information for verification"
        )

        OutlinedTextField(
            value = state.documentType,
            onValueChange = vm::onDocumentTypeChange,
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Document type") },
            singleLine = true
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = state.documentNumber,
            onValueChange = vm::onDocumentNumberChange,
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Document number") },
            singleLine = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = onNext, modifier = Modifier.fillMaxWidth()) {
            Text("Next")
        }
    }
}
```

---

## File: `mobile-android/app/src/main/java/com/mutebi/stockinvestmentapp/features/kyc/KycReviewScreen.kt`

```kotlin
package com.mutebi.stockinvestmentapp.features.kyc

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun KycReviewScreen(
    onSuccess: () -> Unit,
    vm: KycViewModel
) {
    val state by vm.uiState.collectAsState()

    LaunchedEffect(state.success) {
        if (state.success) onSuccess()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text("Review your information")
        Spacer(modifier = Modifier.height(8.dp))
        Text("First name: ${state.firstName}")
        Text("Last name: ${state.lastName}")
        Text("National ID: ${state.nationalIdNumber}")
        Text("Document type: ${state.documentType}")
        Text("Document number: ${state.documentNumber}")

        Spacer(modifier = Modifier.height(12.dp))

        state.error?.let {
            Text(text = it)
            Spacer(modifier = Modifier.height(8.dp))
        }

        Button(
            onClick = vm::submit,
            enabled = !state.isLoading,
            modifier = Modifier.fillMaxWidth()
        ) {
            if (state.isLoading) {
                CircularProgressIndicator()
            } else {
                Text("Submit KYC")
            }
        }
    }
}
```

---

## File: `mobile-android/app/src/main/java/com/mutebi/stockinvestmentapp/navigation/AppNavGraph.kt`

```kotlin
package com.mutebi.stockinvestmentapp.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
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
import com.mutebi.stockinvestmentapp.features.kyc.KycViewModel
import com.mutebi.stockinvestmentapp.features.onboarding.OnboardingScreen
import com.mutebi.stockinvestmentapp.features.profile.setup.ProfileSetupScreen
import com.mutebi.stockinvestmentapp.features.splash.SplashScreen

@Composable
fun AppNavGraph(
    navController: NavHostController,
    isLoggedIn: Boolean,
    modifier: Modifier = Modifier
) {
    val kycViewModel: KycViewModel = viewModel()

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
                    kycViewModel.loadKyc()
                    navController.navigate(Routes.KycPersonalInfo.route)
                }
            )
        }

        composable(Routes.KycPersonalInfo.route) {
            KycPersonalInfoScreen(
                vm = kycViewModel,
                onNext = {
                    navController.navigate(Routes.KycDocumentUpload.route)
                }
            )
        }

        composable(Routes.KycDocumentUpload.route) {
            KycDocumentUploadScreen(
                vm = kycViewModel,
                onNext = {
                    navController.navigate(Routes.KycReview.route)
                }
            )
        }

        composable(Routes.KycReview.route) {
            KycReviewScreen(
                vm = kycViewModel,
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
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text("Dashboard comes in Phase 5")
            }
        }

        composable(Routes.Market.route) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text("Market comes in Phase 5")
            }
        }

        composable(Routes.Watchlist.route) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text("Watchlist comes in Phase 5")
            }
        }

        composable(Routes.Portfolio.route) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text("Portfolio comes in Phase 6")
            }
        }
    }
}
```

---

## 3) Replace these backend files

These are the actual backend Phase 4 gaps:
- users route path mismatch with Android (`PATCH /users/me/profile` expected)
- KYC route mismatch with Android (`GET /kyc/me` and `PATCH /kyc/me` expected)
- KYC service needs an explicit update method
- `test_users.py` and `test_kyc.py` are missing

---

## File: `backend-api/app/routes/users.py`

```python
from flask import Blueprint, request
from flask_jwt_extended import get_jwt_identity, jwt_required

from app.schemas.user_schema import UpdateUserSchema
from app.services.user_service import UserService
from app.utils.response import error_response, success_response
from app.utils.validators import load_schema_or_errors

users_bp = Blueprint("users", __name__)
update_user_schema = UpdateUserSchema()


@users_bp.get("/me")
@jwt_required()
def me():
    user = UserService.get_user_profile(int(get_jwt_identity()))
    if not user:
        return error_response("User not found.", status_code=404)
    return success_response("User profile retrieved successfully.", user.to_dict())


@users_bp.patch("/me/profile")
@users_bp.put("/profile")
@jwt_required()
def update_profile():
    payload = request.get_json(silent=True) or {}
    data, errors = load_schema_or_errors(update_user_schema, payload)
    if errors:
        return error_response("Validation failed.", errors, 400)

    user = UserService.update_user_profile(int(get_jwt_identity()), data)
    if not user:
        return error_response("User not found.", status_code=404)

    return success_response("Profile updated successfully.", user.to_dict())
```

---

## File: `backend-api/app/routes/kyc.py`

```python
from flask import Blueprint, request
from flask_jwt_extended import get_jwt_identity, jwt_required

from app.schemas.kyc_schema import KYCSubmissionSchema
from app.services.kyc_service import KYCService
from app.services.notification_service import NotificationService
from app.utils.enums import NotificationType
from app.utils.response import error_response, success_response
from app.utils.validators import load_schema_or_errors

kyc_bp = Blueprint("kyc", __name__)
kyc_submission_schema = KYCSubmissionSchema()


@kyc_bp.post("/submit")
@jwt_required()
def submit_kyc():
    payload = request.get_json(silent=True) or {}
    data, errors = load_schema_or_errors(kyc_submission_schema, payload)
    if errors:
        return error_response("Validation failed.", errors, 400)

    submission = KYCService.submit_kyc(int(get_jwt_identity()), data)

    NotificationService.create_notification(
        user_id=int(get_jwt_identity()),
        title="KYC submitted",
        body="Your KYC details have been submitted and are pending review.",
        notification_type=NotificationType.KYC.value,
    )

    return success_response("KYC submitted successfully.", submission.to_dict(), 201)


@kyc_bp.get("/me")
@kyc_bp.get("/status")
@jwt_required()
def get_kyc_status():
    submission = KYCService.get_kyc_status(int(get_jwt_identity()))
    if not submission:
        return error_response("KYC record not found.", status_code=404)

    return success_response("KYC status retrieved successfully.", submission.to_dict())


@kyc_bp.patch("/me")
@jwt_required()
def update_kyc():
    payload = request.get_json(silent=True) or {}
    data, errors = load_schema_or_errors(kyc_submission_schema, payload)
    if errors:
        return error_response("Validation failed.", errors, 400)

    submission = KYCService.update_kyc(int(get_jwt_identity()), data)
    if not submission:
        return error_response("KYC record not found.", status_code=404)

    return success_response("KYC updated successfully.", submission.to_dict())
```

---

## File: `backend-api/app/services/kyc_service.py`

```python
from app.extensions import db
from app.models.kyc import KYCSubmission
from app.utils.enums import KYCStatus


class KYCService:
    @staticmethod
    def submit_kyc(user_id: int, data: dict):
        submission = KYCSubmission.query.filter_by(user_id=user_id).first()
        if submission:
            for key, value in data.items():
                setattr(submission, key, value)
            submission.status = KYCStatus.PENDING.value
        else:
            submission = KYCSubmission(
                user_id=user_id,
                status=KYCStatus.PENDING.value,
                **data,
            )
            db.session.add(submission)

        db.session.commit()
        return submission

    @staticmethod
    def update_kyc(user_id: int, data: dict):
        submission = KYCSubmission.query.filter_by(user_id=user_id).first()
        if not submission:
            return None

        for key, value in data.items():
            setattr(submission, key, value)
        submission.status = KYCStatus.PENDING.value

        db.session.commit()
        return submission

    @staticmethod
    def get_kyc_status(user_id: int):
        return KYCSubmission.query.filter_by(user_id=user_id).first()
```

---

## 4) Add these missing backend test files

---

## File: `backend-api/tests/test_users.py`

```python
import uuid


def _unique_email() -> str:
    return f"phase4_users_{uuid.uuid4().hex[:10]}@example.com"


def _register_user(client, email: str, password: str = "Password123!"):
    return client.post(
        "/api/v1/auth/register",
        json={
            "email": email,
            "password": password,
            "full_name": "Phase Four User",
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


def test_get_current_user_profile(client):
    headers = _auth_headers(client)

    response = client.get("/api/v1/users/me", headers=headers)
    body = response.get_json() or {}

    assert response.status_code == 200, body
    assert body.get("success") is True
    assert body.get("data") is not None


def test_update_current_user_profile(client):
    headers = _auth_headers(client)

    response = client.patch(
        "/api/v1/users/me/profile",
        headers=headers,
        json={
            "full_name": "Phase Four Updated",
            "phone_number": "+256700000000",
            "country": "Uganda",
            "date_of_birth": "2000-01-01",
        },
    )
    body = response.get_json() or {}

    assert response.status_code == 200, body
    assert body.get("success") is True

    data = body.get("data") or {}
    assert data.get("full_name") == "Phase Four Updated"
    assert data.get("phone_number") == "+256700000000"
    assert data.get("country") == "Uganda"
    assert data.get("date_of_birth") == "2000-01-01"
```

---

## File: `backend-api/tests/test_kyc.py`

```python
import uuid


def _unique_email() -> str:
    return f"phase4_kyc_{uuid.uuid4().hex[:10]}@example.com"


def _register_user(client, email: str, password: str = "Password123!"):
    return client.post(
        "/api/v1/auth/register",
        json={
            "email": email,
            "password": password,
            "full_name": "KYC Test User",
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


def test_submit_kyc(client):
    headers = _auth_headers(client)

    response = client.post(
        "/api/v1/kyc/submit",
        headers=headers,
        json={
            "first_name": "Phase",
            "last_name": "User",
            "national_id_number": "CM1234567890AA",
            "document_type": "national_id",
            "document_number": "ID-12345678",
        },
    )
    body = response.get_json() or {}

    assert response.status_code in (200, 201), body
    assert body.get("success") is True

    data = body.get("data") or {}
    assert data.get("first_name") == "Phase"
    assert data.get("last_name") == "User"
    assert data.get("document_type") == "national_id"


def test_get_my_kyc(client):
    headers = _auth_headers(client)

    submit_response = client.post(
        "/api/v1/kyc/submit",
        headers=headers,
        json={
            "first_name": "Phase",
            "last_name": "User",
            "national_id_number": "CM1234567890AA",
            "document_type": "national_id",
            "document_number": "ID-12345678",
        },
    )
    assert submit_response.status_code in (200, 201), submit_response.get_json()

    response = client.get("/api/v1/kyc/me", headers=headers)
    body = response.get_json() or {}

    assert response.status_code == 200, body
    assert body.get("success") is True
    assert body.get("data") is not None


def test_update_my_kyc(client):
    headers = _auth_headers(client)

    submit_response = client.post(
        "/api/v1/kyc/submit",
        headers=headers,
        json={
            "first_name": "Phase",
            "last_name": "User",
            "national_id_number": "CM1234567890AA",
            "document_type": "national_id",
            "document_number": "ID-12345678",
        },
    )
    assert submit_response.status_code in (200, 201), submit_response.get_json()

    response = client.patch(
        "/api/v1/kyc/me",
        headers=headers,
        json={
            "first_name": "Updated",
            "last_name": "User",
            "national_id_number": "CM1234567890AA",
            "document_type": "passport",
            "document_number": "P-99887766",
        },
    )
    body = response.get_json() or {}

    assert response.status_code == 200, body
    assert body.get("success") is True

    data = body.get("data") or {}
    assert data.get("first_name") == "Updated"
    assert data.get("document_type") == "passport"
    assert data.get("document_number") == "P-99887766"
```

---

## 5) Recommended verification steps

### Backend
From `backend-api/`:

```bash
source env/bin/activate
pytest tests/test_users.py tests/test_kyc.py -q
python3 -m run
```

### Manual backend checks
```bash
curl -s http://127.0.0.1:5000/api/v1/users/me -H "Authorization: Bearer $TOKEN"
curl -s -X PATCH http://127.0.0.1:5000/api/v1/users/me/profile \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "full_name":"Phase Four Updated",
    "phone_number":"+256700000000",
    "country":"Uganda",
    "date_of_birth":"2000-01-01"
  }'

curl -s -X POST http://127.0.0.1:5000/api/v1/kyc/submit \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "first_name":"Phase",
    "last_name":"User",
    "national_id_number":"CM1234567890AA",
    "document_type":"national_id",
    "document_number":"ID-12345678"
  }'

curl -s http://127.0.0.1:5000/api/v1/kyc/me -H "Authorization: Bearer $TOKEN"

curl -s -X PATCH http://127.0.0.1:5000/api/v1/kyc/me \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "first_name":"Updated",
    "last_name":"User",
    "national_id_number":"CM1234567890AA",
    "document_type":"passport",
    "document_number":"P-99887766"
  }'
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

## 6) Expected Phase 4 outcome

After these patches:

- register works
- login works
- forgot password works if your backend `auth.py` already has that route
- profile setup saves through `/api/v1/users/me/profile`
- KYC submit works through `/api/v1/kyc/submit`
- KYC reload works through `/api/v1/kyc/me`
- KYC update works through `/api/v1/kyc/me`
- login no longer drops into a blank dashboard immediately
- dashboard remains a placeholder until Phase 5
