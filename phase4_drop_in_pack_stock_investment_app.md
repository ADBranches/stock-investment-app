
# Phase 4 Drop-In Pack — Android + Backend Close-Out

This markdown pack is a **best-effort, copy/paste-ready Phase 4 bundle** aligned to your phase plan and the close-out guide.

It is designed to solve the current blockers you have already hit:

- working login but **blank screen after login**
- incomplete **Profile Setup / KYC flow**
- missing **forgot password** path
- missing **UserApi / KycApi / repositories / models**
- need for a **temporary post-login route** that does not jump into the empty Phase 5 dashboard

## Important assumptions

This pack assumes your current project already has these items:

- `Resource` sealed class under `com.mutebi.stockinvestmentapp.core.utils.Resource`
- `SessionManager` under `com.mutebi.stockinvestmentapp.data.session.SessionManager`
- `ApiConstants` under `com.mutebi.stockinvestmentapp.core.constants.ApiConstants`
- `AuthInterceptor` already existing and able to attach the bearer token
- `AuthRepository.login(...)` and `AuthRepository.register(...)` already existing or replaced with the version below
- backend helpers `success_response(...)` and `error_response(...)`
- backend JWT setup already working

---

## Paste order

1. **Supporting required edits**
2. **Android auth / profile / KYC files**
3. **Backend close-out edits**
4. **Test commands**

---

# 1) SUPPORTING REQUIRED EDITS

These are not all Phase 4 brand-new files, but they are required so the Phase 4 files below actually work.

---

## File: `mobile-android/app/src/main/java/com/mutebi/stockinvestmentapp/navigation/AppNavGraph.kt`

> Replace the file with this version.  
> This fixes the blank-screen issue by routing login into **Phase 4 screens**, not the still-empty Phase 5 dashboard.

```kotlin
package com.mutebi.stockinvestmentapp.navigation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
    var kycFirstName by rememberSaveable { mutableStateOf("") }
    var kycLastName by rememberSaveable { mutableStateOf("") }
    var kycNationalIdNumber by rememberSaveable { mutableStateOf("") }
    var kycDocumentType by rememberSaveable { mutableStateOf("national_id") }
    var kycDocumentNumber by rememberSaveable { mutableStateOf("") }

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
                firstName = kycFirstName,
                lastName = kycLastName,
                nationalIdNumber = kycNationalIdNumber,
                onFirstNameChange = { kycFirstName = it },
                onLastNameChange = { kycLastName = it },
                onNationalIdNumberChange = { kycNationalIdNumber = it },
                onNext = {
                    navController.navigate(Routes.KycDocumentUpload.route)
                }
            )
        }

        composable(Routes.KycDocumentUpload.route) {
            KycDocumentUploadScreen(
                documentType = kycDocumentType,
                documentNumber = kycDocumentNumber,
                onDocumentTypeChange = { kycDocumentType = it },
                onDocumentNumberChange = { kycDocumentNumber = it },
                onNext = {
                    navController.navigate(Routes.KycReview.route)
                }
            )
        }

        composable(Routes.KycReview.route) {
            KycReviewScreen(
                firstName = kycFirstName,
                lastName = kycLastName,
                nationalIdNumber = kycNationalIdNumber,
                documentType = kycDocumentType,
                documentNumber = kycDocumentNumber,
                onBack = {
                    navController.popBackStack()
                },
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
            PlaceholderPhase5Screen(
                title = "Dashboard coming in Phase 5",
                buttonText = "Go to Market placeholder",
                onClick = {
                    navController.navigate(Routes.Market.route)
                }
            )
        }

        composable(Routes.Market.route) {
            PlaceholderPhase5Screen(
                title = "Market screen coming in Phase 5",
                buttonText = "Go to Watchlist placeholder",
                onClick = {
                    navController.navigate(Routes.Watchlist.route)
                }
            )
        }

        composable(Routes.Watchlist.route) {
            PlaceholderPhase5Screen(
                title = "Watchlist screen coming in Phase 5",
                buttonText = "Go to Portfolio placeholder",
                onClick = {
                    navController.navigate(Routes.Portfolio.route)
                }
            )
        }

        composable(Routes.Portfolio.route) {
            PlaceholderPhase5Screen(
                title = "Portfolio screen coming in Phase 5",
                buttonText = "Back to Dashboard placeholder",
                onClick = {
                    navController.navigate(Routes.Dashboard.route)
                }
            )
        }
    }
}

@Composable
private fun PlaceholderPhase5Screen(
    title: String,
    buttonText: String,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = title)
            Button(onClick = onClick) {
                Text(buttonText)
            }
        }
    }
}
```

> Add this missing import if Android Studio/VS Code complains:

```kotlin
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.unit.dp
```

---

## File: `mobile-android/app/src/main/java/com/mutebi/stockinvestmentapp/data/remote/api/AuthApi.kt`

> Replace or update to include forgot-password support.

```kotlin
package com.mutebi.stockinvestmentapp.data.remote.api

import com.mutebi.stockinvestmentapp.data.remote.dto.AuthEnvelope
import com.mutebi.stockinvestmentapp.data.remote.dto.ForgotPasswordRequest
import com.mutebi.stockinvestmentapp.data.remote.dto.LoginRequest
import com.mutebi.stockinvestmentapp.data.remote.dto.LoginResponseDto
import com.mutebi.stockinvestmentapp.data.remote.dto.RegisterRequest
import com.mutebi.stockinvestmentapp.data.remote.dto.UserSummaryDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface AuthApi {

    @POST("api/v1/auth/register")
    suspend fun register(
        @Body request: RegisterRequest
    ): Response<AuthEnvelope<UserSummaryDto>>

    @POST("api/v1/auth/login")
    suspend fun login(
        @Body request: LoginRequest
    ): Response<AuthEnvelope<LoginResponseDto>>

    @POST("api/v1/auth/forgot-password")
    suspend fun forgotPassword(
        @Body request: ForgotPasswordRequest
    ): Response<AuthEnvelope<Nothing?>>

    @GET("api/v1/auth/me")
    suspend fun me(): Response<AuthEnvelope<UserSummaryDto>>
}
```

---

## File: `mobile-android/app/src/main/java/com/mutebi/stockinvestmentapp/data/remote/dto/AuthDtos.kt`

> Replace or merge with this version if your current DTO file is incomplete.

```kotlin
package com.mutebi.stockinvestmentapp.data.remote.dto

data class AuthEnvelope<T>(
    val message: String? = null,
    val data: T? = null,
    val errors: Map<String, List<String>>? = null
)

data class LoginRequest(
    val email: String,
    val password: String
)

data class RegisterRequest(
    val email: String,
    val password: String,
    val full_name: String
)

data class ForgotPasswordRequest(
    val email: String
)

data class UserSummaryDto(
    val id: Int? = null,
    val email: String? = null,
    val full_name: String? = null
)

data class LoginResponseDto(
    val access_token: String? = null,
    val user: UserSummaryDto? = null
)
```

---

## File: `mobile-android/app/src/main/java/com/mutebi/stockinvestmentapp/data/repository/AuthRepository.kt`

> Replace if you want login/register/forgot-password in one consistent repo.

```kotlin
package com.mutebi.stockinvestmentapp.data.repository

import com.mutebi.stockinvestmentapp.core.utils.Resource
import com.mutebi.stockinvestmentapp.data.remote.api.AuthApi
import com.mutebi.stockinvestmentapp.data.remote.dto.ForgotPasswordRequest
import com.mutebi.stockinvestmentapp.data.remote.dto.LoginRequest
import com.mutebi.stockinvestmentapp.data.remote.dto.RegisterRequest
import com.mutebi.stockinvestmentapp.data.session.SessionManager

class AuthRepository(
    private val authApi: AuthApi,
    private val sessionManager: SessionManager
) {

    suspend fun register(
        email: String,
        password: String,
        fullName: String
    ): Resource<String> {
        return try {
            val response = authApi.register(
                RegisterRequest(
                    email = email,
                    password = password,
                    full_name = fullName
                )
            )

            if (response.isSuccessful) {
                Resource.Success(response.body()?.message ?: "Account created successfully.")
            } else {
                Resource.Error(response.body()?.message ?: "Registration failed.")
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Registration failed.")
        }
    }

    suspend fun login(
        email: String,
        password: String
    ): Resource<String> {
        return try {
            val response = authApi.login(
                LoginRequest(
                    email = email,
                    password = password
                )
            )

            val body = response.body()
            val token = body?.data?.access_token

            if (response.isSuccessful && !token.isNullOrBlank()) {
                sessionManager.saveAccessToken(token)
                Resource.Success(token)
            } else {
                Resource.Error(body?.message ?: "Login failed.")
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Login failed.")
        }
    }

    suspend fun forgotPassword(email: String): Resource<String> {
        return try {
            val response = authApi.forgotPassword(
                ForgotPasswordRequest(email = email)
            )

            if (response.isSuccessful) {
                Resource.Success(
                    response.body()?.message
                        ?: "If an account exists for that email, a reset link has been sent."
                )
            } else {
                Resource.Error(response.body()?.message ?: "Unable to process forgot password request.")
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Unable to process forgot password request.")
        }
    }

    fun isLoggedIn(): Boolean = sessionManager.isLoggedIn()

    fun logout() {
        sessionManager.clearSession()
    }
}
```

---

# 2) ANDROID PHASE 4 FILES

---

## File: `mobile-android/app/src/main/java/com/mutebi/stockinvestmentapp/features/auth/components/AuthHeader.kt`

```kotlin
package com.mutebi.stockinvestmentapp.features.auth.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun AuthHeader(
    title: String,
    subtitle: String
) {
    Column {
        Text(
            text = title,
            style = MaterialTheme.typography.headlineMedium
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = subtitle,
            style = MaterialTheme.typography.bodyMedium
        )
        Spacer(modifier = Modifier.height(24.dp))
    }
}
```

---

## File: `mobile-android/app/src/main/java/com/mutebi/stockinvestmentapp/features/auth/components/PasswordField.kt`

```kotlin
package com.mutebi.stockinvestmentapp.features.auth.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation

@Composable
fun PasswordField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String = "Password"
) {
    var visible by remember { mutableStateOf(false) }

    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier.fillMaxWidth(),
        label = { Text(label) },
        singleLine = true,
        visualTransformation = if (visible) VisualTransformation.None else PasswordVisualTransformation(),
        trailingIcon = {
            IconButton(onClick = { visible = !visible }) {
                Icon(
                    imageVector = if (visible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                    contentDescription = if (visible) "Hide password" else "Show password"
                )
            }
        }
    )
}
```

---

## File: `mobile-android/app/src/main/java/com/mutebi/stockinvestmentapp/features/auth/components/TermsCheckbox.kt`

```kotlin
package com.mutebi.stockinvestmentapp.features.auth.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun TermsCheckbox(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = checked,
            onCheckedChange = onCheckedChange
        )
        Text("I agree to the terms and conditions")
    }
}
```

---

## File: `mobile-android/app/src/main/java/com/mutebi/stockinvestmentapp/features/auth/login/LoginUiState.kt`

```kotlin
package com.mutebi.stockinvestmentapp.features.auth.login

data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val success: Boolean = false,
    val error: String? = null
)
```

---

## File: `mobile-android/app/src/main/java/com/mutebi/stockinvestmentapp/features/auth/login/LoginScreen.kt`

```kotlin
package com.mutebi.stockinvestmentapp.features.auth.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mutebi.stockinvestmentapp.features.auth.components.AuthHeader
import com.mutebi.stockinvestmentapp.features.auth.components.PasswordField

@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit,
    onNavigateToRegister: () -> Unit,
    onNavigateToForgotPassword: () -> Unit,
    vm: LoginViewModel = viewModel()
) {
    val state by vm.uiState.collectAsState()

    LaunchedEffect(state.success) {
        if (state.success) onLoginSuccess()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center
    ) {
        AuthHeader(
            title = "Welcome back",
            subtitle = "Login to continue investing"
        )

        OutlinedTextField(
            value = state.email,
            onValueChange = vm::onEmailChange,
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Email") },
            singleLine = true
        )

        Spacer(modifier = Modifier.height(12.dp))

        PasswordField(
            value = state.password,
            onValueChange = vm::onPasswordChange
        )

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
                Text("Login")
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        TextButton(
            onClick = onNavigateToForgotPassword,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Forgot password?")
        }

        TextButton(
            onClick = onNavigateToRegister,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Create account")
        }
    }
}
```

---

## File: `mobile-android/app/src/main/java/com/mutebi/stockinvestmentapp/features/auth/login/LoginViewModel.kt`

```kotlin
package com.mutebi.stockinvestmentapp.features.auth.login

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

class LoginViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = AuthRepository(
        authApi = RetrofitProvider.authApi(application),
        sessionManager = SessionManager(application)
    )

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState

    fun onEmailChange(value: String) {
        _uiState.value = _uiState.value.copy(email = value, error = null)
    }

    fun onPasswordChange(value: String) {
        _uiState.value = _uiState.value.copy(password = value, error = null)
    }

    fun submit() {
        val state = _uiState.value

        if (state.email.isBlank() || state.password.isBlank()) {
            _uiState.value = state.copy(error = "Email and password are required")
            return
        }

        viewModelScope.launch {
            _uiState.value = state.copy(isLoading = true, error = null)

            when (val result = repository.login(state.email.trim(), state.password)) {
                is Resource.Success -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        success = true
                    )
                }
                is Resource.Error -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = result.message ?: "Login failed"
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

## File: `mobile-android/app/src/main/java/com/mutebi/stockinvestmentapp/features/auth/register/RegisterUiState.kt`

```kotlin
package com.mutebi.stockinvestmentapp.features.auth.register

data class RegisterUiState(
    val fullName: String = "",
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val acceptTerms: Boolean = false,
    val isLoading: Boolean = false,
    val success: Boolean = false,
    val error: String? = null
)
```

---

## File: `mobile-android/app/src/main/java/com/mutebi/stockinvestmentapp/features/auth/register/RegisterViewModel.kt`

```kotlin
package com.mutebi.stockinvestmentapp.features.auth.register

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

class RegisterViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = AuthRepository(
        authApi = RetrofitProvider.authApi(application),
        sessionManager = SessionManager(application)
    )

    private val _uiState = MutableStateFlow(RegisterUiState())
    val uiState: StateFlow<RegisterUiState> = _uiState

    fun onFullNameChange(value: String) {
        _uiState.value = _uiState.value.copy(fullName = value, error = null)
    }

    fun onEmailChange(value: String) {
        _uiState.value = _uiState.value.copy(email = value, error = null)
    }

    fun onPasswordChange(value: String) {
        _uiState.value = _uiState.value.copy(password = value, error = null)
    }

    fun onConfirmPasswordChange(value: String) {
        _uiState.value = _uiState.value.copy(confirmPassword = value, error = null)
    }

    fun onAcceptTermsChange(value: Boolean) {
        _uiState.value = _uiState.value.copy(acceptTerms = value, error = null)
    }

    fun submit() {
        val state = _uiState.value

        when {
            state.fullName.isBlank() -> {
                _uiState.value = state.copy(error = "Full name is required")
                return
            }
            state.email.isBlank() -> {
                _uiState.value = state.copy(error = "Email is required")
                return
            }
            state.password.length < 8 -> {
                _uiState.value = state.copy(error = "Password must be at least 8 characters")
                return
            }
            state.password != state.confirmPassword -> {
                _uiState.value = state.copy(error = "Passwords do not match")
                return
            }
            !state.acceptTerms -> {
                _uiState.value = state.copy(error = "You must accept the terms")
                return
            }
        }

        viewModelScope.launch {
            _uiState.value = state.copy(isLoading = true, error = null)

            when (
                val result = repository.register(
                    email = state.email.trim(),
                    password = state.password,
                    fullName = state.fullName.trim()
                )
            ) {
                is Resource.Success -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        success = true
                    )
                }
                is Resource.Error -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = result.message ?: "Registration failed"
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

## File: `mobile-android/app/src/main/java/com/mutebi/stockinvestmentapp/features/auth/register/RegisterScreen.kt`

```kotlin
package com.mutebi.stockinvestmentapp.features.auth.register

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mutebi.stockinvestmentapp.features.auth.components.AuthHeader
import com.mutebi.stockinvestmentapp.features.auth.components.PasswordField
import com.mutebi.stockinvestmentapp.features.auth.components.TermsCheckbox

@Composable
fun RegisterScreen(
    onRegisterSuccess: () -> Unit,
    onNavigateToLogin: () -> Unit,
    vm: RegisterViewModel = viewModel()
) {
    val state by vm.uiState.collectAsState()

    LaunchedEffect(state.success) {
        if (state.success) onRegisterSuccess()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center
    ) {
        AuthHeader(
            title = "Create account",
            subtitle = "Start your responsible investment journey"
        )

        OutlinedTextField(
            value = state.fullName,
            onValueChange = vm::onFullNameChange,
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Full name") },
            singleLine = true
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = state.email,
            onValueChange = vm::onEmailChange,
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Email") },
            singleLine = true
        )

        Spacer(modifier = Modifier.height(12.dp))

        PasswordField(
            value = state.password,
            onValueChange = vm::onPasswordChange
        )

        Spacer(modifier = Modifier.height(12.dp))

        PasswordField(
            value = state.confirmPassword,
            onValueChange = vm::onConfirmPasswordChange,
            label = "Confirm password"
        )

        Spacer(modifier = Modifier.height(12.dp))

        TermsCheckbox(
            checked = state.acceptTerms,
            onCheckedChange = vm::onAcceptTermsChange
        )

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
                Text("Create account")
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        TextButton(
            onClick = onNavigateToLogin,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Already have an account? Login")
        }
    }
}
```

---

## File: `mobile-android/app/src/main/java/com/mutebi/stockinvestmentapp/features/auth/forgot_password/ForgotPasswordUiState.kt`

```kotlin
package com.mutebi.stockinvestmentapp.features.auth.forgot_password

data class ForgotPasswordUiState(
    val email: String = "",
    val isLoading: Boolean = false,
    val message: String? = null,
    val error: String? = null,
    val success: Boolean = false
)
```

---

## File: `mobile-android/app/src/main/java/com/mutebi/stockinvestmentapp/features/auth/forgot_password/ForgotPasswordViewModel.kt`

```kotlin
package com.mutebi.stockinvestmentapp.features.auth.forgot_password

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

class ForgotPasswordViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = AuthRepository(
        authApi = RetrofitProvider.authApi(application),
        sessionManager = SessionManager(application)
    )

    private val _uiState = MutableStateFlow(ForgotPasswordUiState())
    val uiState: StateFlow<ForgotPasswordUiState> = _uiState

    fun onEmailChange(value: String) {
        _uiState.value = _uiState.value.copy(
            email = value,
            error = null,
            message = null,
            success = false
        )
    }

    fun submit() {
        val state = _uiState.value

        if (state.email.isBlank()) {
            _uiState.value = state.copy(error = "Email is required")
            return
        }

        viewModelScope.launch {
            _uiState.value = state.copy(isLoading = true, error = null, message = null)

            when (val result = repository.forgotPassword(state.email.trim())) {
                is Resource.Success -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        success = true,
                        message = result.data ?: "If an account exists for that email, a reset link has been sent."
                    )
                }
                is Resource.Error -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = result.message ?: "Unable to process forgot password request."
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

## File: `mobile-android/app/src/main/java/com/mutebi/stockinvestmentapp/features/auth/forgot_password/ForgotPasswordScreen.kt`

```kotlin
package com.mutebi.stockinvestmentapp.features.auth.forgot_password

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mutebi.stockinvestmentapp.features.auth.components.AuthHeader

@Composable
fun ForgotPasswordScreen(
    onBackToLogin: () -> Unit,
    vm: ForgotPasswordViewModel = viewModel()
) {
    val state by vm.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center
    ) {
        AuthHeader(
            title = "Forgot password",
            subtitle = "Enter your email to request a reset link"
        )

        OutlinedTextField(
            value = state.email,
            onValueChange = vm::onEmailChange,
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Email") },
            singleLine = true
        )

        Spacer(modifier = Modifier.height(12.dp))

        state.error?.let {
            Text(text = it)
            Spacer(modifier = Modifier.height(8.dp))
        }

        state.message?.let {
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
                Text("Send reset link")
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        TextButton(
            onClick = onBackToLogin,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Back to login")
        }
    }
}
```

---

## File: `mobile-android/app/src/main/java/com/mutebi/stockinvestmentapp/domain/model/UserProfile.kt`

```kotlin
package com.mutebi.stockinvestmentapp.domain.model

data class UserProfile(
    val id: Int? = null,
    val email: String = "",
    val fullName: String = "",
    val phoneNumber: String = "",
    val country: String = "",
    val dateOfBirth: String = ""
)
```

---

## File: `mobile-android/app/src/main/java/com/mutebi/stockinvestmentapp/data/remote/dto/UserDtos.kt`

```kotlin
package com.mutebi.stockinvestmentapp.data.remote.dto

data class UserEnvelope<T>(
    val message: String? = null,
    val data: T? = null,
    val errors: Map<String, List<String>>? = null
)

data class UserProfileDto(
    val id: Int? = null,
    val email: String? = null,
    val full_name: String? = null,
    val phone_number: String? = null,
    val country: String? = null,
    val date_of_birth: String? = null
)

data class UpdateProfileRequest(
    val full_name: String,
    val phone_number: String,
    val country: String,
    val date_of_birth: String
)
```

---

## File: `mobile-android/app/src/main/java/com/mutebi/stockinvestmentapp/data/remote/api/UserApi.kt`

```kotlin
package com.mutebi.stockinvestmentapp.data.remote.api

import com.mutebi.stockinvestmentapp.data.remote.dto.UpdateProfileRequest
import com.mutebi.stockinvestmentapp.data.remote.dto.UserEnvelope
import com.mutebi.stockinvestmentapp.data.remote.dto.UserProfileDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH

interface UserApi {

    @GET("api/v1/users/me")
    suspend fun getMyProfile(): Response<UserEnvelope<UserProfileDto>>

    @PATCH("api/v1/users/me/profile")
    suspend fun updateMyProfile(
        @Body request: UpdateProfileRequest
    ): Response<UserEnvelope<UserProfileDto>>
}
```

---

## File: `mobile-android/app/src/main/java/com/mutebi/stockinvestmentapp/data/repository/UserRepository.kt`

```kotlin
package com.mutebi.stockinvestmentapp.data.repository

import android.app.Application
import com.mutebi.stockinvestmentapp.core.constants.ApiConstants
import com.mutebi.stockinvestmentapp.core.utils.Resource
import com.mutebi.stockinvestmentapp.data.remote.api.UserApi
import com.mutebi.stockinvestmentapp.data.remote.dto.UpdateProfileRequest
import com.mutebi.stockinvestmentapp.data.session.SessionManager
import com.mutebi.stockinvestmentapp.domain.model.UserProfile
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.mutebi.stockinvestmentapp.data.remote.network.AuthInterceptor

class UserRepository(application: Application) {

    private val userApi: UserApi by lazy {
        val client = OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor(SessionManager(application)))
            .build()

        Retrofit.Builder()
            .baseUrl(ApiConstants.BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(UserApi::class.java)
    }

    suspend fun getMyProfile(): Resource<UserProfile> {
        return try {
            val response = userApi.getMyProfile()
            val dto = response.body()?.data

            if (response.isSuccessful && dto != null) {
                Resource.Success(dto.toDomain())
            } else {
                Resource.Error(response.body()?.message ?: "Unable to load profile.")
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Unable to load profile.")
        }
    }

    suspend fun updateProfile(
        fullName: String,
        phoneNumber: String,
        country: String,
        dateOfBirth: String
    ): Resource<UserProfile> {
        return try {
            val response = userApi.updateMyProfile(
                UpdateProfileRequest(
                    full_name = fullName,
                    phone_number = phoneNumber,
                    country = country,
                    date_of_birth = dateOfBirth
                )
            )

            val dto = response.body()?.data

            if (response.isSuccessful && dto != null) {
                Resource.Success(dto.toDomain())
            } else {
                Resource.Error(response.body()?.message ?: "Unable to save profile.")
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Unable to save profile.")
        }
    }
}

private fun com.mutebi.stockinvestmentapp.data.remote.dto.UserProfileDto.toDomain(): UserProfile {
    return UserProfile(
        id = id,
        email = email.orEmpty(),
        fullName = full_name.orEmpty(),
        phoneNumber = phone_number.orEmpty(),
        country = country.orEmpty(),
        dateOfBirth = date_of_birth.orEmpty()
    )
}
```

> If your existing `AuthInterceptor` constructor accepts `Application` instead of `SessionManager`, change this line:

```kotlin
.addInterceptor(AuthInterceptor(SessionManager(application)))
```

> to:

```kotlin
.addInterceptor(AuthInterceptor(application))
```

---

## File: `mobile-android/app/src/main/java/com/mutebi/stockinvestmentapp/features/profile/setup/ProfileSetupUiState.kt`

```kotlin
package com.mutebi.stockinvestmentapp.features.profile.setup

data class ProfileSetupUiState(
    val fullName: String = "",
    val phoneNumber: String = "",
    val country: String = "",
    val dateOfBirth: String = "",
    val isLoading: Boolean = false,
    val success: Boolean = false,
    val error: String? = null
)
```

---

## File: `mobile-android/app/src/main/java/com/mutebi/stockinvestmentapp/features/profile/setup/ProfileSetupViewModel.kt`

```kotlin
package com.mutebi.stockinvestmentapp.features.profile.setup

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.mutebi.stockinvestmentapp.core.utils.Resource
import com.mutebi.stockinvestmentapp.data.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ProfileSetupViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = UserRepository(application)

    private val _uiState = MutableStateFlow(ProfileSetupUiState())
    val uiState: StateFlow<ProfileSetupUiState> = _uiState

    fun onFullNameChange(value: String) {
        _uiState.value = _uiState.value.copy(fullName = value, error = null)
    }

    fun onPhoneNumberChange(value: String) {
        _uiState.value = _uiState.value.copy(phoneNumber = value, error = null)
    }

    fun onCountryChange(value: String) {
        _uiState.value = _uiState.value.copy(country = value, error = null)
    }

    fun onDateOfBirthChange(value: String) {
        _uiState.value = _uiState.value.copy(dateOfBirth = value, error = null)
    }

    fun loadCurrentProfile() {
        viewModelScope.launch {
            when (val result = repository.getMyProfile()) {
                is Resource.Success -> {
                    val profile = result.data
                    _uiState.value = _uiState.value.copy(
                        fullName = profile?.fullName.orEmpty(),
                        phoneNumber = profile?.phoneNumber.orEmpty(),
                        country = profile?.country.orEmpty(),
                        dateOfBirth = profile?.dateOfBirth.orEmpty()
                    )
                }
                else -> Unit
            }
        }
    }

    fun submit() {
        val state = _uiState.value

        if (state.fullName.isBlank()) {
            _uiState.value = state.copy(error = "Full name is required")
            return
        }

        viewModelScope.launch {
            _uiState.value = state.copy(isLoading = true, error = null)

            when (
                val result = repository.updateProfile(
                    fullName = state.fullName.trim(),
                    phoneNumber = state.phoneNumber.trim(),
                    country = state.country.trim(),
                    dateOfBirth = state.dateOfBirth.trim()
                )
            ) {
                is Resource.Success -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        success = true
                    )
                }
                is Resource.Error -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = result.message ?: "Unable to save profile."
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

## File: `mobile-android/app/src/main/java/com/mutebi/stockinvestmentapp/features/profile/components/ProfileAvatarPicker.kt`

```kotlin
package com.mutebi.stockinvestmentapp.features.profile.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp

@Composable
fun ProfileAvatarPicker(
    initials: String
) {
    Box(
        modifier = Modifier
            .size(80.dp)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.primaryContainer),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = initials.ifBlank { "U" },
            style = MaterialTheme.typography.headlineSmall
        )
    }
}
```

---

## File: `mobile-android/app/src/main/java/com/mutebi/stockinvestmentapp/features/profile/components/ProfileForm.kt`

```kotlin
package com.mutebi.stockinvestmentapp.features.profile.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ProfileForm(
    fullName: String,
    phoneNumber: String,
    country: String,
    dateOfBirth: String,
    onFullNameChange: (String) -> Unit,
    onPhoneNumberChange: (String) -> Unit,
    onCountryChange: (String) -> Unit,
    onDateOfBirthChange: (String) -> Unit
) {
    Column {
        OutlinedTextField(
            value = fullName,
            onValueChange = onFullNameChange,
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Full name") },
            singleLine = true
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = phoneNumber,
            onValueChange = onPhoneNumberChange,
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Phone number") },
            singleLine = true
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = country,
            onValueChange = onCountryChange,
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Country") },
            singleLine = true
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = dateOfBirth,
            onValueChange = onDateOfBirthChange,
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Date of birth (YYYY-MM-DD)") },
            singleLine = true
        )
    }
}
```

---

## File: `mobile-android/app/src/main/java/com/mutebi/stockinvestmentapp/features/profile/setup/ProfileSetupScreen.kt`

```kotlin
package com.mutebi.stockinvestmentapp.features.profile.setup

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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mutebi.stockinvestmentapp.features.auth.components.AuthHeader
import com.mutebi.stockinvestmentapp.features.profile.components.ProfileAvatarPicker
import com.mutebi.stockinvestmentapp.features.profile.components.ProfileForm

@Composable
fun ProfileSetupScreen(
    onProfileSaved: () -> Unit,
    vm: ProfileSetupViewModel = viewModel()
) {
    val state by vm.uiState.collectAsState()

    LaunchedEffect(Unit) {
        vm.loadCurrentProfile()
    }

    LaunchedEffect(state.success) {
        if (state.success) onProfileSaved()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center
    ) {
        AuthHeader(
            title = "Complete your profile",
            subtitle = "This helps personalize your investment journey"
        )

        ProfileAvatarPicker(
            initials = state.fullName
                .trim()
                .split(" ")
                .mapNotNull { it.firstOrNull()?.toString() }
                .take(2)
                .joinToString("")
        )

        Spacer(modifier = Modifier.height(20.dp))

        ProfileForm(
            fullName = state.fullName,
            phoneNumber = state.phoneNumber,
            country = state.country,
            dateOfBirth = state.dateOfBirth,
            onFullNameChange = vm::onFullNameChange,
            onPhoneNumberChange = vm::onPhoneNumberChange,
            onCountryChange = vm::onCountryChange,
            onDateOfBirthChange = vm::onDateOfBirthChange
        )

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
                Text("Save and continue")
            }
        }
    }
}
```

---

## File: `mobile-android/app/src/main/java/com/mutebi/stockinvestmentapp/domain/model/KycProfile.kt`

```kotlin
package com.mutebi.stockinvestmentapp.domain.model

data class KycProfile(
    val firstName: String = "",
    val lastName: String = "",
    val nationalIdNumber: String = "",
    val documentType: String = "",
    val documentNumber: String = "",
    val status: String = "",
    val submittedAt: String = ""
)
```

---

## File: `mobile-android/app/src/main/java/com/mutebi/stockinvestmentapp/data/remote/dto/KycDtos.kt`

```kotlin
package com.mutebi.stockinvestmentapp.data.remote.dto

data class KycEnvelope<T>(
    val message: String? = null,
    val data: T? = null,
    val errors: Map<String, List<String>>? = null
)

data class KycSubmissionRequest(
    val first_name: String,
    val last_name: String,
    val national_id_number: String,
    val document_type: String,
    val document_number: String
)

data class KycProfileDto(
    val first_name: String? = null,
    val last_name: String? = null,
    val national_id_number: String? = null,
    val document_type: String? = null,
    val document_number: String? = null,
    val status: String? = null,
    val submitted_at: String? = null
)
```

---

## File: `mobile-android/app/src/main/java/com/mutebi/stockinvestmentapp/data/remote/api/KycApi.kt`

```kotlin
package com.mutebi.stockinvestmentapp.data.remote.api

import com.mutebi.stockinvestmentapp.data.remote.dto.KycEnvelope
import com.mutebi.stockinvestmentapp.data.remote.dto.KycProfileDto
import com.mutebi.stockinvestmentapp.data.remote.dto.KycSubmissionRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface KycApi {

    @POST("api/v1/kyc/submit")
    suspend fun submitKyc(
        @Body request: KycSubmissionRequest
    ): Response<KycEnvelope<KycProfileDto>>

    @GET("api/v1/kyc/me")
    suspend fun getMyKyc(): Response<KycEnvelope<KycProfileDto>>
}
```

---

## File: `mobile-android/app/src/main/java/com/mutebi/stockinvestmentapp/data/repository/KycRepository.kt`

```kotlin
package com.mutebi.stockinvestmentapp.data.repository

import android.app.Application
import com.mutebi.stockinvestmentapp.core.constants.ApiConstants
import com.mutebi.stockinvestmentapp.core.utils.Resource
import com.mutebi.stockinvestmentapp.data.remote.api.KycApi
import com.mutebi.stockinvestmentapp.data.remote.dto.KycProfileDto
import com.mutebi.stockinvestmentapp.data.remote.dto.KycSubmissionRequest
import com.mutebi.stockinvestmentapp.data.remote.network.AuthInterceptor
import com.mutebi.stockinvestmentapp.data.session.SessionManager
import com.mutebi.stockinvestmentapp.domain.model.KycProfile
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class KycRepository(application: Application) {

    private val kycApi: KycApi by lazy {
        val client = OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor(SessionManager(application)))
            .build()

        Retrofit.Builder()
            .baseUrl(ApiConstants.BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(KycApi::class.java)
    }

    suspend fun submitKyc(
        firstName: String,
        lastName: String,
        nationalIdNumber: String,
        documentType: String,
        documentNumber: String
    ): Resource<KycProfile> {
        return try {
            val response = kycApi.submitKyc(
                KycSubmissionRequest(
                    first_name = firstName,
                    last_name = lastName,
                    national_id_number = nationalIdNumber,
                    document_type = documentType,
                    document_number = documentNumber
                )
            )

            val dto = response.body()?.data

            if (response.isSuccessful && dto != null) {
                Resource.Success(dto.toDomain())
            } else {
                Resource.Error(response.body()?.message ?: "KYC submission failed.")
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "KYC submission failed.")
        }
    }

    suspend fun getMyKyc(): Resource<KycProfile> {
        return try {
            val response = kycApi.getMyKyc()
            val dto = response.body()?.data

            if (response.isSuccessful && dto != null) {
                Resource.Success(dto.toDomain())
            } else {
                Resource.Error(response.body()?.message ?: "Unable to load KYC profile.")
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Unable to load KYC profile.")
        }
    }
}

private fun KycProfileDto.toDomain(): KycProfile {
    return KycProfile(
        firstName = first_name.orEmpty(),
        lastName = last_name.orEmpty(),
        nationalIdNumber = national_id_number.orEmpty(),
        documentType = document_type.orEmpty(),
        documentNumber = document_number.orEmpty(),
        status = status.orEmpty(),
        submittedAt = submitted_at.orEmpty()
    )
}
```

> If your existing `AuthInterceptor` constructor accepts `Application` instead of `SessionManager`, change the same line here too.

---

## File: `mobile-android/app/src/main/java/com/mutebi/stockinvestmentapp/features/kyc/KycUiState.kt`

```kotlin
package com.mutebi.stockinvestmentapp.features.kyc

import com.mutebi.stockinvestmentapp.domain.model.KycProfile

data class KycUiState(
    val isLoading: Boolean = false,
    val success: Boolean = false,
    val message: String? = null,
    val error: String? = null,
    val currentProfile: KycProfile? = null
)
```

---

## File: `mobile-android/app/src/main/java/com/mutebi/stockinvestmentapp/features/kyc/KycViewModel.kt`

```kotlin
package com.mutebi.stockinvestmentapp.features.kyc

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.mutebi.stockinvestmentapp.core.utils.Resource
import com.mutebi.stockinvestmentapp.data.repository.KycRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class KycViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = KycRepository(application)

    private val _uiState = MutableStateFlow(KycUiState())
    val uiState: StateFlow<KycUiState> = _uiState

    fun submit(
        firstName: String,
        lastName: String,
        nationalIdNumber: String,
        documentType: String,
        documentNumber: String
    ) {
        when {
            firstName.isBlank() -> {
                _uiState.value = _uiState.value.copy(error = "First name is required")
                return
            }
            lastName.isBlank() -> {
                _uiState.value = _uiState.value.copy(error = "Last name is required")
                return
            }
            nationalIdNumber.isBlank() -> {
                _uiState.value = _uiState.value.copy(error = "National ID number is required")
                return
            }
            documentNumber.isBlank() -> {
                _uiState.value = _uiState.value.copy(error = "Document number is required")
                return
            }
        }

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null, message = null)

            when (
                val result = repository.submitKyc(
                    firstName = firstName.trim(),
                    lastName = lastName.trim(),
                    nationalIdNumber = nationalIdNumber.trim(),
                    documentType = documentType.trim(),
                    documentNumber = documentNumber.trim()
                )
            ) {
                is Resource.Success -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        success = true,
                        message = "KYC submitted successfully.",
                        currentProfile = result.data
                    )
                }
                is Resource.Error -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = result.message ?: "KYC submission failed."
                    )
                }
                else -> {
                    _uiState.value = _uiState.value.copy(isLoading = false)
                }
            }
        }
    }

    fun loadMyKyc() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)

            when (val result = repository.getMyKyc()) {
                is Resource.Success -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        currentProfile = result.data
                    )
                }
                is Resource.Error -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = result.message ?: "Unable to load KYC profile."
                    )
                }
                else -> {
                    _uiState.value = _uiState.value.copy(isLoading = false)
                }
            }
        }
    }

    fun clearTransientState() {
        _uiState.value = _uiState.value.copy(
            success = false,
            message = null,
            error = null
        )
    }
}
```

---

## File: `mobile-android/app/src/main/java/com/mutebi/stockinvestmentapp/features/kyc/KycIntroScreen.kt`

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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mutebi.stockinvestmentapp.features.auth.components.AuthHeader

@Composable
fun KycIntroScreen(
    onStartKyc: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center
    ) {
        AuthHeader(
            title = "Verify your identity",
            subtitle = "KYC helps keep your account secure and compliant"
        )

        Text("You will provide personal details and one identification document.")

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = onStartKyc,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Start KYC")
        }
    }
}
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mutebi.stockinvestmentapp.features.auth.components.AuthHeader

@Composable
fun KycPersonalInfoScreen(
    firstName: String,
    lastName: String,
    nationalIdNumber: String,
    onFirstNameChange: (String) -> Unit,
    onLastNameChange: (String) -> Unit,
    onNationalIdNumberChange: (String) -> Unit,
    onNext: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center
    ) {
        AuthHeader(
            title = "Personal information",
            subtitle = "Enter the details that match your identification document"
        )

        OutlinedTextField(
            value = firstName,
            onValueChange = onFirstNameChange,
            modifier = Modifier.fillMaxWidth(),
            label = { Text("First name") },
            singleLine = true
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = lastName,
            onValueChange = onLastNameChange,
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Last name") },
            singleLine = true
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = nationalIdNumber,
            onValueChange = onNationalIdNumberChange,
            modifier = Modifier.fillMaxWidth(),
            label = { Text("National ID number") },
            singleLine = true
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = onNext,
            modifier = Modifier.fillMaxWidth()
        ) {
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mutebi.stockinvestmentapp.features.auth.components.AuthHeader

@Composable
fun KycDocumentUploadScreen(
    documentType: String,
    documentNumber: String,
    onDocumentTypeChange: (String) -> Unit,
    onDocumentNumberChange: (String) -> Unit,
    onNext: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center
    ) {
        AuthHeader(
            title = "Document details",
            subtitle = "Enter your ID document type and number"
        )

        OutlinedTextField(
            value = documentType,
            onValueChange = onDocumentTypeChange,
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Document type") },
            singleLine = true
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = documentNumber,
            onValueChange = onDocumentNumberChange,
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Document number") },
            singleLine = true
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text("Suggested type: national_id")

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = onNext,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Review")
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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mutebi.stockinvestmentapp.features.auth.components.AuthHeader

@Composable
fun KycReviewScreen(
    firstName: String,
    lastName: String,
    nationalIdNumber: String,
    documentType: String,
    documentNumber: String,
    onBack: () -> Unit,
    onSuccess: () -> Unit,
    vm: KycViewModel = viewModel()
) {
    val state by vm.uiState.collectAsState()

    LaunchedEffect(state.success) {
        if (state.success) {
            vm.clearTransientState()
            onSuccess()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center
    ) {
        AuthHeader(
            title = "Review KYC details",
            subtitle = "Confirm your information before submission"
        )

        Text("First name: $firstName")
        Text("Last name: $lastName")
        Text("National ID number: $nationalIdNumber")
        Text("Document type: $documentType")
        Text("Document number: $documentNumber")

        Spacer(modifier = Modifier.height(12.dp))

        state.error?.let {
            Text(text = it)
            Spacer(modifier = Modifier.height(8.dp))
        }

        state.message?.let {
            Text(text = it)
            Spacer(modifier = Modifier.height(8.dp))
        }

        Button(
            onClick = {
                vm.submit(
                    firstName = firstName,
                    lastName = lastName,
                    nationalIdNumber = nationalIdNumber,
                    documentType = documentType,
                    documentNumber = documentNumber
                )
            },
            enabled = !state.isLoading,
            modifier = Modifier.fillMaxWidth()
        ) {
            if (state.isLoading) {
                CircularProgressIndicator()
            } else {
                Text("Submit KYC")
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = onBack,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Back")
        }
    }
}
```

---

## File: `mobile-android/app/src/main/java/com/mutebi/stockinvestmentapp/features/kyc/KycSuccessScreen.kt`

```kotlin
package com.mutebi.stockinvestmentapp.features.kyc

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mutebi.stockinvestmentapp.features.auth.components.AuthHeader

@Composable
fun KycSuccessScreen(
    onDone: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center
    ) {
        AuthHeader(
            title = "KYC submitted",
            subtitle = "Your information has been received successfully"
        )

        Button(
            onClick = onDone,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Continue")
        }
    }
}
```

---

# 3) OPTIONAL HILT / DI ADDITIONS

Only use this section if your current project is already wired around Hilt and your `NetworkModule.kt` / `RepositoryModule.kt` are active.

---

## File: `mobile-android/app/src/main/java/com/mutebi/stockinvestmentapp/di/NetworkModule.kt`

> Add these imports:

```kotlin
import com.mutebi.stockinvestmentapp.data.remote.api.KycApi
import com.mutebi.stockinvestmentapp.data.remote.api.UserApi
```

> Add these providers below your existing `provideAuthApi(...)`:

```kotlin
@Provides
fun provideUserApi(retrofit: Retrofit): UserApi =
    retrofit.create(UserApi::class.java)

@Provides
fun provideKycApi(retrofit: Retrofit): KycApi =
    retrofit.create(KycApi::class.java)
```

---

## File: `mobile-android/app/src/main/java/com/mutebi/stockinvestmentapp/di/RepositoryModule.kt`

> Add these imports:

```kotlin
import android.app.Application
import com.mutebi.stockinvestmentapp.data.repository.KycRepository
import com.mutebi.stockinvestmentapp.data.repository.UserRepository
```

> Add these providers:

```kotlin
@Provides
fun provideUserRepository(
    application: Application
): UserRepository = UserRepository(application)

@Provides
fun provideKycRepository(
    application: Application
): KycRepository = KycRepository(application)
```

---

# 4) BACKEND CLOSE-OUT EDITS

Use these to finish the Phase 4 auth/profile/KYC contract.

---

## File: `backend-api/app/__init__.py`

> Add this import near the top:

```python
from app.utils.response import error_response
```

> Then inside `register_extensions(app)` directly after `jwt.init_app(app)` add:

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

## File: `backend-api/app/routes/auth.py`

> Keep your existing register/login/me endpoints.  
> Insert this block **below `login()` and above `@auth_bp.get("/me")`**:

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

## File: `backend-api/app/routes/users.py`

> Use this if your current users route is still missing the authenticated profile endpoints.

```python
from flask import Blueprint, request
from flask_jwt_extended import get_jwt_identity, jwt_required

from app.extensions import db
from app.models.user import User
from app.utils.response import error_response, success_response

users_bp = Blueprint("users", __name__)

@users_bp.get("/me")
@jwt_required()
def get_me():
    user_id = get_jwt_identity()
    user = User.query.get(user_id)

    if not user:
        return error_response("User not found.", status_code=404)

    return success_response(
        "User profile retrieved successfully.",
        {
            "id": user.id,
            "email": user.email,
            "full_name": user.full_name,
            "phone_number": getattr(user, "phone_number", None),
            "country": getattr(user, "country", None),
            "date_of_birth": str(getattr(user, "date_of_birth", "") or ""),
        },
        200,
    )

@users_bp.patch("/me/profile")
@jwt_required()
def update_my_profile():
    user_id = get_jwt_identity()
    user = User.query.get(user_id)

    if not user:
        return error_response("User not found.", status_code=404)

    payload = request.get_json(silent=True) or {}

    full_name = (payload.get("full_name") or "").strip()
    phone_number = (payload.get("phone_number") or "").strip()
    country = (payload.get("country") or "").strip()
    date_of_birth = (payload.get("date_of_birth") or "").strip()

    if not full_name:
        return error_response("Full name is required.", status_code=400)

    user.full_name = full_name
    user.phone_number = phone_number or None
    user.country = country or None
    user.date_of_birth = date_of_birth or None

    db.session.commit()

    return success_response(
        "Profile updated successfully.",
        {
            "id": user.id,
            "email": user.email,
            "full_name": user.full_name,
            "phone_number": getattr(user, "phone_number", None),
            "country": getattr(user, "country", None),
            "date_of_birth": str(getattr(user, "date_of_birth", "") or ""),
        },
        200,
    )
```

> If your `date_of_birth` column is a real `date` type, convert the incoming string before saving.

---

## File: `backend-api/app/routes/kyc.py`

> Use this if your current KYC route is still missing `submit` and `me`.

```python
from datetime import datetime

from flask import Blueprint, request
from flask_jwt_extended import get_jwt_identity, jwt_required

from app.extensions import db
from app.models.kyc import KYCSubmission
from app.utils.response import error_response, success_response

kyc_bp = Blueprint("kyc", __name__)

@kyc_bp.post("/submit")
@jwt_required()
def submit_kyc():
    user_id = get_jwt_identity()
    payload = request.get_json(silent=True) or {}

    first_name = (payload.get("first_name") or "").strip()
    last_name = (payload.get("last_name") or "").strip()
    national_id_number = (payload.get("national_id_number") or "").strip()
    document_type = (payload.get("document_type") or "").strip()
    document_number = (payload.get("document_number") or "").strip()

    missing = []
    if not first_name:
        missing.append("first_name")
    if not last_name:
        missing.append("last_name")
    if not national_id_number:
        missing.append("national_id_number")
    if not document_type:
        missing.append("document_type")
    if not document_number:
        missing.append("document_number")

    if missing:
        return error_response(
            f"Missing required fields: {', '.join(missing)}",
            status_code=400
        )

    kyc = KYCSubmission.query.filter_by(user_id=user_id).first()

    if not kyc:
        kyc = KYCSubmission(user_id=user_id)
        db.session.add(kyc)

    kyc.first_name = first_name
    kyc.last_name = last_name
    kyc.national_id_number = national_id_number
    kyc.document_type = document_type
    kyc.document_number = document_number
    kyc.status = "submitted"
    kyc.submitted_at = datetime.utcnow()

    db.session.commit()

    return success_response(
        "KYC submitted successfully.",
        {
            "first_name": kyc.first_name,
            "last_name": kyc.last_name,
            "national_id_number": kyc.national_id_number,
            "document_type": kyc.document_type,
            "document_number": kyc.document_number,
            "status": kyc.status,
            "submitted_at": kyc.submitted_at.isoformat() if kyc.submitted_at else None,
        },
        200,
    )

@kyc_bp.get("/me")
@jwt_required()
def get_my_kyc():
    user_id = get_jwt_identity()
    kyc = KYCSubmission.query.filter_by(user_id=user_id).first()

    if not kyc:
        return error_response("KYC profile not found.", status_code=404)

    return success_response(
        "KYC profile retrieved successfully.",
        {
            "first_name": kyc.first_name,
            "last_name": kyc.last_name,
            "national_id_number": kyc.national_id_number,
            "document_type": kyc.document_type,
            "document_number": kyc.document_number,
            "status": kyc.status,
            "submitted_at": kyc.submitted_at.isoformat() if kyc.submitted_at else None,
        },
        200,
    )
```

> If your model class is named `KycSubmission` instead of `KYCSubmission`, rename the import and references accordingly.

---

## File: `backend-api/tests/test_users.py`

```python
import uuid

def _unique_email() -> str:
    return f"phase4_user_{uuid.uuid4().hex[:10]}@example.com"

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
    token = data.get("access_token")
    assert token, body

    return {"Authorization": f"Bearer {token}"}

def test_get_my_profile(client):
    headers = _auth_headers(client)

    response = client.get("/api/v1/users/me", headers=headers)

    assert response.status_code == 200, response.get_json()
    payload = response.get_json() or {}
    data = payload.get("data") or {}

    assert "email" in data
    assert "full_name" in data

def test_update_my_profile(client):
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

    assert response.status_code == 200, response.get_json()
    payload = response.get_json() or {}
    data = payload.get("data") or {}

    assert data["full_name"] == "Phase Four Updated"
    assert data["phone_number"] == "+256700000000"
    assert data["country"] == "Uganda"
    assert data["date_of_birth"] == "2000-01-01"
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
    token = data.get("access_token")
    assert token, body

    return {"Authorization": f"Bearer {token}"}

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

    assert response.status_code == 200, response.get_json()
    payload = response.get_json() or {}
    data = payload.get("data") or {}

    assert data["first_name"] == "Phase"
    assert data["last_name"] == "User"
    assert data["document_type"] == "national_id"
    assert data["status"] == "submitted"

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
    assert submit_response.status_code == 200, submit_response.get_json()

    response = client.get("/api/v1/kyc/me", headers=headers)

    assert response.status_code == 200, response.get_json()
    payload = response.get_json() or {}
    data = payload.get("data") or {}

    assert data["first_name"] == "Phase"
    assert data["last_name"] == "User"
    assert data["document_number"] == "ID-12345678"
```

---

# 5) TEST SEQUENCE

## Backend terminal

```bash
cd ~/Downloads/sem32/Mutebi/stock-investment-app/backend-api
source env/bin/activate
alembic upgrade head
python3 run.py
```

If your entrypoint is different:

```bash
python3 -m run
```

## Auth API checks

```bash
curl -s -X POST http://127.0.0.1:5000/api/v1/auth/register   -H "Content-Type: application/json"   -d '{
    "email": "phase4_test_user@example.com",
    "password": "Password123!",
    "full_name": "Phase Four User"
  }'

TOKEN=$(curl -s -X POST http://127.0.0.1:5000/api/v1/auth/login   -H "Content-Type: application/json"   -d '{
    "email": "phase4_test_user@example.com",
    "password": "Password123!"
  }' | python3 -c 'import sys, json; print((json.load(sys.stdin).get("data") or {}).get("access_token",""))')

curl -s http://127.0.0.1:5000/api/v1/auth/me   -H "Authorization: Bearer $TOKEN"

curl -s -X POST http://127.0.0.1:5000/api/v1/auth/forgot-password   -H "Content-Type: application/json"   -d '{"email":"phase4_test_user@example.com"}'
```

## Protected profile and KYC checks

```bash
curl -s http://127.0.0.1:5000/api/v1/users/me   -H "Authorization: Bearer $TOKEN"

curl -s -X PATCH http://127.0.0.1:5000/api/v1/users/me/profile   -H "Authorization: Bearer $TOKEN"   -H "Content-Type: application/json"   -d '{
    "full_name": "Phase Four Updated",
    "phone_number": "+256700000000",
    "country": "Uganda",
    "date_of_birth": "2000-01-01"
  }'

curl -s -X POST http://127.0.0.1:5000/api/v1/kyc/submit   -H "Authorization: Bearer $TOKEN"   -H "Content-Type: application/json"   -d '{
    "first_name": "Phase",
    "last_name": "User",
    "national_id_number": "CM1234567890AA",
    "document_type": "national_id",
    "document_number": "ID-12345678"
  }'

curl -s http://127.0.0.1:5000/api/v1/kyc/me   -H "Authorization: Bearer $TOKEN"
```

## Android device checks

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

# 6) WHAT SHOULD NOW HAPPEN

Expected user-visible behavior after applying this pack:

- register creates account
- login succeeds
- app does **not** go to a blank dashboard anymore
- post-login goes to **Profile Setup**
- profile setup saves
- KYC intro / personal info / document / review / success flow works
- after KYC success, dashboard route shows a **visible Phase 5 placeholder**, not a blank dark screen
- forgot-password returns a calm confirmation message
- protected backend routes return JSON instead of raw JWT framework failures

---

# 7) ONE IMPORTANT CAUSE-OF-ERROR NOTE

If Kotlin fails around `AuthInterceptor(...)` constructor mismatch, do not panic.

Use whichever one matches your existing project:

### Variant A
```kotlin
AuthInterceptor(SessionManager(application))
```

### Variant B
```kotlin
AuthInterceptor(application)
```

That is the most likely small adjustment you may need after pasting this pack.

---

# 8) AFTER PHASE 4 CLOSE-OUT

Once all the above passes, your next correct step is **Phase 5: Dashboard + Market Discovery**.

That is when you should replace the temporary dashboard placeholder with the real:

- `DashboardScreen.kt`
- `DashboardViewModel.kt`
- `DashboardUiState.kt`
- market list/detail
- watchlist screen

---
