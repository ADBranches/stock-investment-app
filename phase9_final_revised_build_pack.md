# Phase 9 — Testing, Debugging, and Stabilization

This pack is split exactly the way you asked:

- **keep as-is**
- **targeted edits only**
- **new files to create fully**

It is based on the audit you pasted, which showed:
- the Android test surface is still missing
- `PortfolioViewModel.kt` is missing
- backend core API tests mostly already exist
- `test_security.py` is still missing

---

## 1) Keep as-is

These files are already good enough for Phase 9 and do not need changes right now.

### Android production files
- `mobile-android/app/src/main/java/com/mutebi/stockinvestmentapp/data/repository/AuthRepository.kt`
- `mobile-android/app/src/main/java/com/mutebi/stockinvestmentapp/data/repository/AssetRepository.kt`
- `mobile-android/app/src/main/java/com/mutebi/stockinvestmentapp/data/repository/PortfolioRepository.kt`
- `mobile-android/app/src/main/java/com/mutebi/stockinvestmentapp/data/repository/TradeRepository.kt`
- `mobile-android/app/src/main/java/com/mutebi/stockinvestmentapp/features/home/dashboard/DashboardViewModel.kt`

### Backend test files
- `backend-api/tests/conftest.py`
- `backend-api/tests/test_users.py`
- `backend-api/tests/test_kyc.py`
- `backend-api/tests/test_market_assets.py`
- `backend-api/tests/test_watchlist.py`
- `backend-api/tests/test_portfolio.py`
- `backend-api/tests/test_trades.py`
- `backend-api/tests/test_notifications.py`

---

## 2) Targeted edits only

These files already exist, but they need stabilization or testability updates.

---

## File: `mobile-android/app/build.gradle.kts`

Add these dependencies inside `dependencies { ... }`:

```kotlin
testImplementation("io.mockk:mockk:1.13.12")
testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.8.1")
testImplementation("androidx.arch.core:core-testing:2.2.0")
testImplementation("androidx.test:core:1.6.1")

androidTestImplementation("androidx.test:core-ktx:1.6.1")
androidTestImplementation("androidx.test:rules:1.6.1")
```

Optional but recommended inside `android { ... }`:

```kotlin
testOptions {
    unitTests.isReturnDefaultValues = true
}
```

---

## File: `mobile-android/app/src/main/java/com/mutebi/stockinvestmentapp/core/utils/Validator.kt`

Replace with:

```kotlin
package com.mutebi.stockinvestmentapp.core.utils

import android.util.Patterns

object Validator {

    fun isRequired(value: String): Boolean {
        return value.trim().isNotEmpty()
    }

    fun isValidEmail(email: String): Boolean {
        return email.isNotBlank() && Patterns.EMAIL_ADDRESS.matcher(email.trim()).matches()
    }

    fun hasMinLength(value: String, minLength: Int): Boolean {
        return value.length >= minLength
    }

    fun passwordsMatch(password: String, confirmPassword: String): Boolean {
        return password == confirmPassword
    }

    fun isPositiveNumber(value: String): Boolean {
        val number = value.toDoubleOrNull() ?: return false
        return number > 0.0
    }
}
```

---

## File: `mobile-android/app/src/main/java/com/mutebi/stockinvestmentapp/features/auth/login/LoginViewModel.kt`

Replace with:

```kotlin
package com.mutebi.stockinvestmentapp.features.auth.login

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.mutebi.stockinvestmentapp.core.utils.Resource
import com.mutebi.stockinvestmentapp.core.utils.Validator
import com.mutebi.stockinvestmentapp.data.remote.network.RetrofitProvider
import com.mutebi.stockinvestmentapp.data.repository.AuthRepository
import com.mutebi.stockinvestmentapp.data.session.SessionManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class LoginViewModel(
    application: Application,
    private val repository: AuthRepository = AuthRepository(
        authApi = RetrofitProvider.authApi(application),
        sessionManager = SessionManager(application)
    )
) : AndroidViewModel(application) {

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

        when {
            !Validator.isRequired(state.email) -> {
                _uiState.value = state.copy(error = "Email is required")
                return
            }
            !Validator.isValidEmail(state.email) -> {
                _uiState.value = state.copy(error = "Enter a valid email address")
                return
            }
            !Validator.isRequired(state.password) -> {
                _uiState.value = state.copy(error = "Password is required")
                return
            }
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

## File: `mobile-android/app/src/main/java/com/mutebi/stockinvestmentapp/features/auth/register/RegisterViewModel.kt`

Replace with:

```kotlin
package com.mutebi.stockinvestmentapp.features.auth.register

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.mutebi.stockinvestmentapp.core.utils.Resource
import com.mutebi.stockinvestmentapp.core.utils.Validator
import com.mutebi.stockinvestmentapp.data.remote.network.RetrofitProvider
import com.mutebi.stockinvestmentapp.data.repository.AuthRepository
import com.mutebi.stockinvestmentapp.data.session.SessionManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class RegisterViewModel(
    application: Application,
    private val repository: AuthRepository = AuthRepository(
        authApi = RetrofitProvider.authApi(application),
        sessionManager = SessionManager(application)
    )
) : AndroidViewModel(application) {

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
            !Validator.isRequired(state.fullName) -> {
                _uiState.value = state.copy(error = "Full name is required")
                return
            }
            !Validator.isRequired(state.email) -> {
                _uiState.value = state.copy(error = "Email is required")
                return
            }
            !Validator.isValidEmail(state.email) -> {
                _uiState.value = state.copy(error = "Enter a valid email address")
                return
            }
            !Validator.isRequired(state.password) -> {
                _uiState.value = state.copy(error = "Password is required")
                return
            }
            !Validator.hasMinLength(state.password, 8) -> {
                _uiState.value = state.copy(error = "Password must be at least 8 characters")
                return
            }
            !Validator.isRequired(state.confirmPassword) -> {
                _uiState.value = state.copy(error = "Confirm password is required")
                return
            }
            !Validator.passwordsMatch(state.password, state.confirmPassword) -> {
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

## File: `mobile-android/app/src/main/java/com/mutebi/stockinvestmentapp/features/auth/login/LoginScreen.kt`

Replace with:

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

    LoginScreenContent(
        state = state,
        onEmailChange = vm::onEmailChange,
        onPasswordChange = vm::onPasswordChange,
        onSubmit = vm::submit,
        onNavigateToRegister = onNavigateToRegister,
        onNavigateToForgotPassword = onNavigateToForgotPassword
    )
}

@Composable
fun LoginScreenContent(
    state: LoginUiState,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onSubmit: () -> Unit,
    onNavigateToRegister: () -> Unit,
    onNavigateToForgotPassword: () -> Unit
) {
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
            onValueChange = onEmailChange,
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Email") },
            singleLine = true
        )

        Spacer(modifier = Modifier.height(12.dp))

        PasswordField(
            value = state.password,
            onValueChange = onPasswordChange
        )

        Spacer(modifier = Modifier.height(12.dp))

        state.error?.let {
            Text(text = it)
            Spacer(modifier = Modifier.height(8.dp))
        }

        Button(
            onClick = onSubmit,
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

## File: `mobile-android/app/src/main/java/com/mutebi/stockinvestmentapp/features/auth/register/RegisterScreen.kt`

Replace with:

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

    RegisterScreenContent(
        state = state,
        onFullNameChange = vm::onFullNameChange,
        onEmailChange = vm::onEmailChange,
        onPasswordChange = vm::onPasswordChange,
        onConfirmPasswordChange = vm::onConfirmPasswordChange,
        onAcceptTermsChange = vm::onAcceptTermsChange,
        onSubmit = vm::submit,
        onNavigateToLogin = onNavigateToLogin
    )
}

@Composable
fun RegisterScreenContent(
    state: RegisterUiState,
    onFullNameChange: (String) -> Unit,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onConfirmPasswordChange: (String) -> Unit,
    onAcceptTermsChange: (Boolean) -> Unit,
    onSubmit: () -> Unit,
    onNavigateToLogin: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center
    ) {
        AuthHeader(
            title = "Create account",
            subtitle = "Start your investment journey"
        )

        OutlinedTextField(
            value = state.fullName,
            onValueChange = onFullNameChange,
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Full name") },
            singleLine = true
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = state.email,
            onValueChange = onEmailChange,
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Email") },
            singleLine = true
        )

        Spacer(modifier = Modifier.height(12.dp))

        PasswordField(
            value = state.password,
            onValueChange = onPasswordChange,
            label = "Password"
        )

        Spacer(modifier = Modifier.height(12.dp))

        PasswordField(
            value = state.confirmPassword,
            onValueChange = onConfirmPasswordChange,
            label = "Confirm password"
        )

        Spacer(modifier = Modifier.height(12.dp))

        TermsCheckbox(
            checked = state.acceptTerms,
            onCheckedChange = onAcceptTermsChange
        )

        Spacer(modifier = Modifier.height(12.dp))

        state.error?.let {
            Text(text = it)
            Spacer(modifier = Modifier.height(8.dp))
        }

        Button(
            onClick = onSubmit,
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

## File: `mobile-android/app/src/main/java/com/mutebi/stockinvestmentapp/features/market/list/MarketListScreen.kt`

Replace with:

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
import androidx.hilt.navigation.compose.hiltViewModel
import com.mutebi.stockinvestmentapp.features.market.components.AssetCard
import com.mutebi.stockinvestmentapp.features.market.components.MarketSearchBar

@Composable
fun MarketListScreen(
    onAssetClick: (Int) -> Unit,
    onOpenWatchlist: () -> Unit,
    vm: MarketListViewModel = hiltViewModel()
) {
    val state by vm.uiState.collectAsState()

    MarketListContent(
        state = state,
        onSearchChange = vm::onSearchChange,
        onOpenWatchlist = onOpenWatchlist,
        onAssetClick = onAssetClick,
        onWatchlistClick = vm::addToWatchlist
    )
}

@Composable
fun MarketListContent(
    state: MarketListUiState,
    onSearchChange: (String) -> Unit,
    onOpenWatchlist: () -> Unit,
    onAssetClick: (Int) -> Unit,
    onWatchlistClick: (Int) -> Unit
) {
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
                onValueChange = onSearchChange
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
        } else if (state.filteredAssets.isEmpty()) {
            item {
                Text("No assets available.")
            }
        } else {
            items(
                items = state.filteredAssets,
                key = { asset -> asset.id }
            ) { asset ->
                AssetCard(
                    asset = asset,
                    isInWatchlist = false,
                    onClick = { onAssetClick(asset.id) },
                    onWatchlistClick = { onWatchlistClick(asset.id) }
                )
            }
        }

        state.message?.let { message ->
            item {
                Text(message)
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

## File: `mobile-android/app/src/main/java/com/mutebi/stockinvestmentapp/features/portfolio/overview/PortfolioScreen.kt`

Replace with:

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

    PortfolioContent(
        state = state,
        onOpenHistory = onOpenHistory,
        onTradeAsset = onTradeAsset,
        onOpenMarket = onOpenMarket
    )
}

@Composable
fun PortfolioContent(
    state: PortfolioUiState,
    onOpenHistory: () -> Unit,
    onTradeAsset: (Int) -> Unit,
    onOpenMarket: () -> Unit
) {
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

## File: `backend-api/tests/test_auth.py`

Replace with:

```python
import uuid


def _unique_email() -> str:
    return f"phase9_auth_{uuid.uuid4().hex[:10]}@example.com"


def test_register_and_login_success(client):
    email = _unique_email()

    register_response = client.post(
        "/api/v1/auth/register",
        json={
            "email": email,
            "password": "securePassword123",
            "full_name": "John Investor",
        },
    )
    register_data = register_response.get_json() or {}

    assert register_response.status_code == 201, register_data
    assert register_data.get("success") is True
    assert "access_token" in (register_data.get("data") or {})

    login_response = client.post(
        "/api/v1/auth/login",
        json={
            "email": email,
            "password": "securePassword123",
        },
    )
    login_data = login_response.get_json() or {}

    assert login_response.status_code == 200, login_data
    assert login_data.get("success") is True
    assert ((login_data.get("data") or {}).get("user") or {}).get("email") == email


def test_login_fails_with_wrong_password(client):
    email = _unique_email()

    register_response = client.post(
        "/api/v1/auth/register",
        json={
            "email": email,
            "password": "securePassword123",
            "full_name": "John Investor",
        },
    )
    assert register_response.status_code == 201, register_response.get_json()

    login_response = client.post(
        "/api/v1/auth/login",
        json={
            "email": email,
            "password": "wrongPassword123",
        },
    )
    login_data = login_response.get_json() or {}

    assert login_response.status_code in (400, 401), login_data
    assert login_data.get("success") is False
```

---

## 3) New files to create fully

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

class PortfolioViewModel(
    application: Application,
    private val repository: PortfolioRepository = PortfolioRepository(
        RetrofitProvider.portfolioApi(application)
    )
) : AndroidViewModel(application) {

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

## File: `mobile-android/app/src/test/java/com/mutebi/stockinvestmentapp/MainDispatcherRule.kt`

```kotlin
package com.mutebi.stockinvestmentapp

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.rules.TestWatcher
import org.junit.runner.Description

@OptIn(ExperimentalCoroutinesApi::class)
class MainDispatcherRule(
    val dispatcher: TestDispatcher = StandardTestDispatcher()
) : TestWatcher() {

    override fun starting(description: Description) {
        Dispatchers.setMain(dispatcher)
    }

    override fun finished(description: Description) {
        Dispatchers.resetMain()
    }
}
```

---

## File: `mobile-android/app/src/test/java/com/mutebi/stockinvestmentapp/repository/AuthRepositoryTest.kt`

```kotlin
package com.mutebi.stockinvestmentapp.repository

import com.mutebi.stockinvestmentapp.core.utils.Resource
import com.mutebi.stockinvestmentapp.data.remote.api.AuthApi
import com.mutebi.stockinvestmentapp.data.remote.dto.ApiEnvelopeDto
import com.mutebi.stockinvestmentapp.data.remote.dto.AuthPayloadDto
import com.mutebi.stockinvestmentapp.data.remote.dto.AuthUserDto
import com.mutebi.stockinvestmentapp.data.remote.dto.LoginRequestDto
import com.mutebi.stockinvestmentapp.data.repository.AuthRepository
import com.mutebi.stockinvestmentapp.data.session.SessionManager
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import retrofit2.Response

class AuthRepositoryTest {

    private val authApi = mockk<AuthApi>()
    private val sessionManager = mockk<SessionManager>(relaxed = true)

    @Test
    fun `login returns success and saves session`() = runBlocking {
        coEvery {
            authApi.login(LoginRequestDto("user@example.com", "Password123!"))
        } returns Response.success(
            ApiEnvelopeDto(
                success = true,
                message = "Login successful",
                data = AuthPayloadDto(
                    accessToken = "token-123",
                    user = AuthUserDto(
                        id = 1,
                        email = "user@example.com",
                        fullName = "User Example"
                    )
                )
            )
        )

        val repository = AuthRepository(authApi, sessionManager)
        val result = repository.login("user@example.com", "Password123!")

        assertTrue(result is Resource.Success)
        assertEquals("user@example.com", (result as Resource.Success).data?.email)

        verify {
            sessionManager.saveSession("token-123", 1, "user@example.com")
        }
    }

    @Test
    fun `login returns error when api fails`() = runBlocking {
        coEvery {
            authApi.login(any())
        } throws RuntimeException("Network error")

        val repository = AuthRepository(authApi, sessionManager)
        val result = repository.login("user@example.com", "Password123!")

        assertTrue(result is Resource.Error)
        assertEquals("Network error", (result as Resource.Error).message)
    }
}
```

---

## File: `mobile-android/app/src/test/java/com/mutebi/stockinvestmentapp/repository/AssetRepositoryTest.kt`

```kotlin
package com.mutebi.stockinvestmentapp.repository

import com.mutebi.stockinvestmentapp.core.utils.Resource
import com.mutebi.stockinvestmentapp.data.remote.api.AssetApi
import com.mutebi.stockinvestmentapp.data.remote.dto.ApiEnvelopeDto
import com.mutebi.stockinvestmentapp.data.remote.dto.AssetDto
import com.mutebi.stockinvestmentapp.data.remote.dto.AssetListPayloadDto
import com.mutebi.stockinvestmentapp.data.repository.AssetRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import retrofit2.Response

class AssetRepositoryTest {

    private val assetApi = mockk<AssetApi>()

    @Test
    fun `get assets returns mapped list`() = runBlocking {
        coEvery { assetApi.getAssets(query = null, active = true) } returns Response.success(
            ApiEnvelopeDto(
                success = true,
                data = AssetListPayloadDto(
                    assets = listOf(
                        AssetDto(
                            id = 1,
                            symbol = "AAPL",
                            name = "Apple",
                            price = 120.0,
                            changePercent = 1.5
                        )
                    )
                )
            )
        )

        val repository = AssetRepository(assetApi)
        val result = repository.getAssets()

        assertTrue(result is Resource.Success)
        assertEquals(1, (result as Resource.Success).data?.size)
        assertEquals("AAPL", result.data?.first()?.symbol)
    }

    @Test
    fun `get asset by id returns error when dto missing`() = runBlocking {
        coEvery { assetApi.getAssetById(99) } returns Response.success(
            ApiEnvelopeDto(
                success = false,
                message = "Not found"
            )
        )

        val repository = AssetRepository(assetApi)
        val result = repository.getAssetById(99)

        assertTrue(result is Resource.Error)
        assertEquals("Not found", (result as Resource.Error).message)
    }
}
```

---

## File: `mobile-android/app/src/test/java/com/mutebi/stockinvestmentapp/repository/PortfolioRepositoryTest.kt`

```kotlin
package com.mutebi.stockinvestmentapp.repository

import com.mutebi.stockinvestmentapp.core.utils.Resource
import com.mutebi.stockinvestmentapp.data.remote.api.PortfolioApi
import com.mutebi.stockinvestmentapp.data.remote.dto.ApiEnvelopeDto
import com.mutebi.stockinvestmentapp.data.remote.dto.AssetDto
import com.mutebi.stockinvestmentapp.data.remote.dto.HoldingDto
import com.mutebi.stockinvestmentapp.data.remote.dto.PortfolioCoreDto
import com.mutebi.stockinvestmentapp.data.remote.dto.PortfolioDataDto
import com.mutebi.stockinvestmentapp.data.repository.PortfolioRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import retrofit2.Response

class PortfolioRepositoryTest {

    private val portfolioApi = mockk<PortfolioApi>()

    @Test
    fun `get portfolio returns summary and holdings`() = runBlocking {
        coEvery { portfolioApi.getPortfolio() } returns Response.success(
            ApiEnvelopeDto(
                success = true,
                data = PortfolioDataDto(
                    portfolio = PortfolioCoreDto(
                        id = 1,
                        userId = 1,
                        name = "Main",
                        cashBalance = 1000.0
                    ),
                    totalValue = 1500.0,
                    holdingsCount = 1,
                    holdings = listOf(
                        HoldingDto(
                            id = 1,
                            portfolioId = 1,
                            quantity = 2.0,
                            averagePrice = 100.0,
                            marketValue = 240.0,
                            asset = AssetDto(
                                id = 10,
                                symbol = "AAPL",
                                name = "Apple",
                                price = 120.0,
                                changePercent = 1.0
                            )
                        )
                    )
                )
            )
        )

        val repository = PortfolioRepository(portfolioApi)
        val result = repository.getPortfolio()

        assertTrue(result is Resource.Success)
        assertEquals(1500.0, (result as Resource.Success).data?.summary?.totalValue)
        assertEquals(1, result.data?.holdings?.size)
    }
}
```

---

## File: `mobile-android/app/src/test/java/com/mutebi/stockinvestmentapp/repository/TradeRepositoryTest.kt`

```kotlin
package com.mutebi.stockinvestmentapp.repository

import com.mutebi.stockinvestmentapp.core.utils.Resource
import com.mutebi.stockinvestmentapp.data.remote.api.TradeApi
import com.mutebi.stockinvestmentapp.data.remote.dto.ApiEnvelopeDto
import com.mutebi.stockinvestmentapp.data.remote.dto.AssetDto
import com.mutebi.stockinvestmentapp.data.remote.dto.CreateTradeRequestDto
import com.mutebi.stockinvestmentapp.data.remote.dto.TradeResponseDto
import com.mutebi.stockinvestmentapp.data.repository.TradeRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import retrofit2.Response

class TradeRepositoryTest {

    private val tradeApi = mockk<TradeApi>()

    @Test
    fun `create trade returns transaction on success`() = runBlocking {
        coEvery {
            tradeApi.createTrade(
                CreateTradeRequestDto(
                    assetId = 1,
                    tradeType = "buy",
                    quantity = 2.0
                )
            )
        } returns Response.success(
            ApiEnvelopeDto(
                success = true,
                data = TradeResponseDto(
                    id = 11,
                    assetId = 1,
                    tradeType = "buy",
                    quantity = 2.0,
                    price = 100.0,
                    status = "completed",
                    createdAt = "2026-01-01T00:00:00",
                    asset = AssetDto(
                        id = 1,
                        symbol = "AAPL",
                        name = "Apple",
                        price = 100.0,
                        changePercent = 1.0
                    )
                )
            )
        )

        val repository = TradeRepository(tradeApi)
        val result = repository.createTrade(1, "buy", 2.0)

        assertTrue(result is Resource.Success)
        assertEquals("buy", (result as Resource.Success).data?.tradeType)
        assertEquals(2.0, result.data?.quantity)
    }
}
```

---

## File: `mobile-android/app/src/test/java/com/mutebi/stockinvestmentapp/viewmodel/LoginViewModelTest.kt`

```kotlin
package com.mutebi.stockinvestmentapp.viewmodel

import android.app.Application
import com.mutebi.stockinvestmentapp.MainDispatcherRule
import com.mutebi.stockinvestmentapp.core.utils.Resource
import com.mutebi.stockinvestmentapp.data.repository.AuthRepository
import com.mutebi.stockinvestmentapp.domain.model.UserProfile
import com.mutebi.stockinvestmentapp.features.auth.login.LoginViewModel
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class LoginViewModelTest {

    @get:Rule
    val dispatcherRule = MainDispatcherRule()

    private val application = mockk<Application>(relaxed = true)
    private val repository = mockk<AuthRepository>()

    @Test
    fun `submit sets success on valid login`() = runTest {
        coEvery {
            repository.login("user@example.com", "Password123!")
        } returns Resource.Success(
            UserProfile(
                id = 1,
                email = "user@example.com",
                fullName = "User"
            )
        )

        val vm = LoginViewModel(application, repository)
        vm.onEmailChange("user@example.com")
        vm.onPasswordChange("Password123!")

        vm.submit()
        advanceUntilIdle()

        assertTrue(vm.uiState.value.success)
        assertEquals(null, vm.uiState.value.error)
    }

    @Test
    fun `submit returns email validation error`() = runTest {
        val vm = LoginViewModel(application, repository)
        vm.onEmailChange("wrong-email")
        vm.onPasswordChange("Password123!")

        vm.submit()

        assertEquals("Enter a valid email address", vm.uiState.value.error)
    }
}
```

---

## File: `mobile-android/app/src/test/java/com/mutebi/stockinvestmentapp/viewmodel/RegisterViewModelTest.kt`

```kotlin
package com.mutebi.stockinvestmentapp.viewmodel

import android.app.Application
import com.mutebi.stockinvestmentapp.MainDispatcherRule
import com.mutebi.stockinvestmentapp.core.utils.Resource
import com.mutebi.stockinvestmentapp.data.repository.AuthRepository
import com.mutebi.stockinvestmentapp.domain.model.UserProfile
import com.mutebi.stockinvestmentapp.features.auth.register.RegisterViewModel
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class RegisterViewModelTest {

    @get:Rule
    val dispatcherRule = MainDispatcherRule()

    private val application = mockk<Application>(relaxed = true)
    private val repository = mockk<AuthRepository>()

    @Test
    fun `submit sets success on valid registration`() = runTest {
        coEvery {
            repository.register("user@example.com", "Password123!", "User Example")
        } returns Resource.Success(
            UserProfile(
                id = 1,
                email = "user@example.com",
                fullName = "User Example"
            )
        )

        val vm = RegisterViewModel(application, repository)
        vm.onFullNameChange("User Example")
        vm.onEmailChange("user@example.com")
        vm.onPasswordChange("Password123!")
        vm.onConfirmPasswordChange("Password123!")
        vm.onAcceptTermsChange(true)

        vm.submit()
        advanceUntilIdle()

        assertTrue(vm.uiState.value.success)
        assertEquals(null, vm.uiState.value.error)
    }

    @Test
    fun `submit fails when passwords do not match`() = runTest {
        val vm = RegisterViewModel(application, repository)
        vm.onFullNameChange("User Example")
        vm.onEmailChange("user@example.com")
        vm.onPasswordChange("Password123!")
        vm.onConfirmPasswordChange("Password1234!")
        vm.onAcceptTermsChange(true)

        vm.submit()

        assertEquals("Passwords do not match", vm.uiState.value.error)
    }
}
```

---

## File: `mobile-android/app/src/test/java/com/mutebi/stockinvestmentapp/viewmodel/DashboardViewModelTest.kt`

```kotlin
package com.mutebi.stockinvestmentapp.viewmodel

import com.mutebi.stockinvestmentapp.MainDispatcherRule
import com.mutebi.stockinvestmentapp.core.utils.Resource
import com.mutebi.stockinvestmentapp.data.repository.AssetRepository
import com.mutebi.stockinvestmentapp.data.repository.WatchlistRepository
import com.mutebi.stockinvestmentapp.domain.model.Asset
import com.mutebi.stockinvestmentapp.domain.model.WatchlistItem
import com.mutebi.stockinvestmentapp.features.home.dashboard.DashboardViewModel
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class DashboardViewModelTest {

    @get:Rule
    val dispatcherRule = MainDispatcherRule()

    private val assetRepository = mockk<AssetRepository>()
    private val watchlistRepository = mockk<WatchlistRepository>()

    @Test
    fun `dashboard loads assets and watchlist`() = runTest {
        coEvery { assetRepository.getAssets() } returns Resource.Success(
            listOf(
                Asset(id = 1, symbol = "AAPL", name = "Apple", price = 120.0, changePercent = 1.0)
            )
        )
        coEvery { watchlistRepository.getWatchlist() } returns Resource.Success(
            listOf(
                WatchlistItem(assetId = 1, symbol = "AAPL", name = "Apple", price = 120.0)
            )
        )

        val vm = DashboardViewModel(assetRepository, watchlistRepository)
        advanceUntilIdle()

        assertEquals(false, vm.uiState.value.isLoading)
        assertEquals(1, vm.uiState.value.topAssets.size)
        assertEquals(1, vm.uiState.value.watchlistItems.size)
    }
}
```

---

## File: `mobile-android/app/src/test/java/com/mutebi/stockinvestmentapp/viewmodel/PortfolioViewModelTest.kt`

```kotlin
package com.mutebi.stockinvestmentapp.viewmodel

import android.app.Application
import com.mutebi.stockinvestmentapp.MainDispatcherRule
import com.mutebi.stockinvestmentapp.core.utils.Resource
import com.mutebi.stockinvestmentapp.data.repository.PortfolioRepository
import com.mutebi.stockinvestmentapp.domain.model.Portfolio
import com.mutebi.stockinvestmentapp.domain.model.PortfolioSummary
import com.mutebi.stockinvestmentapp.features.portfolio.overview.PortfolioViewModel
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class PortfolioViewModelTest {

    @get:Rule
    val dispatcherRule = MainDispatcherRule()

    private val application = mockk<Application>(relaxed = true)
    private val repository = mockk<PortfolioRepository>()

    @Test
    fun `load portfolio exposes summary`() = runTest {
        coEvery { repository.getPortfolio() } returns Resource.Success(
            Portfolio(
                summary = PortfolioSummary(
                    totalValue = 2500.0,
                    cashBalance = 700.0,
                    holdingsCount = 2
                )
            )
        )

        val vm = PortfolioViewModel(application, repository)
        advanceUntilIdle()

        assertEquals(false, vm.uiState.value.isLoading)
        assertEquals(2500.0, vm.uiState.value.summary.totalValue)
        assertEquals(2, vm.uiState.value.summary.holdingsCount)
    }
}
```

---

## File: `mobile-android/app/src/test/java/com/mutebi/stockinvestmentapp/utils/ValidatorTest.kt`

```kotlin
package com.mutebi.stockinvestmentapp.utils

import com.mutebi.stockinvestmentapp.core.utils.Validator
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class ValidatorTest {

    @Test
    fun `isValidEmail returns true for correct email`() {
        assertTrue(Validator.isValidEmail("user@example.com"))
    }

    @Test
    fun `isValidEmail returns false for wrong email`() {
        assertFalse(Validator.isValidEmail("userexample.com"))
    }

    @Test
    fun `passwordsMatch returns true when equal`() {
        assertTrue(Validator.passwordsMatch("Password123!", "Password123!"))
    }

    @Test
    fun `isPositiveNumber returns false for zero`() {
        assertFalse(Validator.isPositiveNumber("0"))
    }
}
```

---

## File: `mobile-android/app/src/androidTest/java/com/mutebi/stockinvestmentapp/auth/LoginScreenTest.kt`

```kotlin
package com.mutebi.stockinvestmentapp.auth

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.mutebi.stockinvestmentapp.features.auth.login.LoginScreenContent
import com.mutebi.stockinvestmentapp.features.auth.login.LoginUiState
import org.junit.Rule
import org.junit.Test

class LoginScreenTest {

    @get:Rule
    val composeRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun loginScreen_displays_core_text() {
        composeRule.setContent {
            LoginScreenContent(
                state = LoginUiState(),
                onEmailChange = {},
                onPasswordChange = {},
                onSubmit = {},
                onNavigateToRegister = {},
                onNavigateToForgotPassword = {}
            )
        }

        composeRule.onNodeWithText("Welcome back").assertIsDisplayed()
        composeRule.onNodeWithText("Login").assertIsDisplayed()
        composeRule.onNodeWithText("Forgot password?").assertIsDisplayed()
    }
}
```

---

## File: `mobile-android/app/src/androidTest/java/com/mutebi/stockinvestmentapp/auth/RegisterScreenTest.kt`

```kotlin
package com.mutebi.stockinvestmentapp.auth

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.mutebi.stockinvestmentapp.features.auth.register.RegisterScreenContent
import com.mutebi.stockinvestmentapp.features.auth.register.RegisterUiState
import org.junit.Rule
import org.junit.Test

class RegisterScreenTest {

    @get:Rule
    val composeRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun registerScreen_displays_core_text() {
        composeRule.setContent {
            RegisterScreenContent(
                state = RegisterUiState(),
                onFullNameChange = {},
                onEmailChange = {},
                onPasswordChange = {},
                onConfirmPasswordChange = {},
                onAcceptTermsChange = {},
                onSubmit = {},
                onNavigateToLogin = {}
            )
        }

        composeRule.onNodeWithText("Create account").assertIsDisplayed()
        composeRule.onNodeWithText("Start your investment journey").assertIsDisplayed()
        composeRule.onNodeWithText("Already have an account? Login").assertIsDisplayed()
    }
}
```

---

## File: `mobile-android/app/src/androidTest/java/com/mutebi/stockinvestmentapp/market/MarketListScreenTest.kt`

```kotlin
package com.mutebi.stockinvestmentapp.market

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.mutebi.stockinvestmentapp.domain.model.Asset
import com.mutebi.stockinvestmentapp.features.market.list.MarketListContent
import com.mutebi.stockinvestmentapp.features.market.list.MarketListUiState
import org.junit.Rule
import org.junit.Test

class MarketListScreenTest {

    @get:Rule
    val composeRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun marketListScreen_displays_asset_item() {
        composeRule.setContent {
            MarketListContent(
                state = MarketListUiState(
                    assets = listOf(
                        Asset(
                            id = 1,
                            symbol = "AAPL",
                            name = "Apple",
                            price = 120.0,
                            changePercent = 1.2
                        )
                    ),
                    filteredAssets = listOf(
                        Asset(
                            id = 1,
                            symbol = "AAPL",
                            name = "Apple",
                            price = 120.0,
                            changePercent = 1.2
                        )
                    )
                ),
                onSearchChange = {},
                onOpenWatchlist = {},
                onAssetClick = {},
                onWatchlistClick = {}
            )
        }

        composeRule.onNodeWithText("Market").assertIsDisplayed()
        composeRule.onNodeWithText("AAPL • Apple").assertIsDisplayed()
    }
}
```

---

## File: `mobile-android/app/src/androidTest/java/com/mutebi/stockinvestmentapp/portfolio/PortfolioScreenTest.kt`

```kotlin
package com.mutebi.stockinvestmentapp.portfolio

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.mutebi.stockinvestmentapp.domain.model.PortfolioSummary
import com.mutebi.stockinvestmentapp.features.portfolio.overview.PortfolioContent
import com.mutebi.stockinvestmentapp.features.portfolio.overview.PortfolioUiState
import org.junit.Rule
import org.junit.Test

class PortfolioScreenTest {

    @get:Rule
    val composeRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun portfolioScreen_displays_summary() {
        composeRule.setContent {
            PortfolioContent(
                state = PortfolioUiState(
                    summary = PortfolioSummary(
                        totalValue = 2500.0,
                        cashBalance = 800.0,
                        holdingsCount = 2
                    )
                ),
                onOpenHistory = {},
                onTradeAsset = {},
                onOpenMarket = {}
            )
        }

        composeRule.onNodeWithText("Portfolio").assertIsDisplayed()
        composeRule.onNodeWithText("Total value: $2500.00").assertIsDisplayed()
        composeRule.onNodeWithText("Cash balance: $800.00").assertIsDisplayed()
    }
}
```

---

## File: `backend-api/tests/test_security.py`

```python
import uuid


def _unique_email() -> str:
    return f"phase9_security_{uuid.uuid4().hex[:10]}@example.com"


def _register_user(client, email: str, password: str = "Password123!"):
    return client.post(
        "/api/v1/auth/register",
        json={
            "email": email,
            "password": password,
            "full_name": "Phase Nine Security User",
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

## 4) New directories to create

### From `mobile-android/`
```bash
mkdir -p app/src/test/java/com/mutebi/stockinvestmentapp/repository
mkdir -p app/src/test/java/com/mutebi/stockinvestmentapp/viewmodel
mkdir -p app/src/test/java/com/mutebi/stockinvestmentapp/utils
mkdir -p app/src/androidTest/java/com/mutebi/stockinvestmentapp/auth
mkdir -p app/src/androidTest/java/com/mutebi/stockinvestmentapp/market
mkdir -p app/src/androidTest/java/com/mutebi/stockinvestmentapp/portfolio
mkdir -p app/src/main/java/com/mutebi/stockinvestmentapp/features/portfolio/overview
```

### From `backend-api/`
```bash
mkdir -p tests
```

---

## 5) Verification sequence

### Android unit tests
From `mobile-android/`:
```bash
./gradlew testDebugUnitTest
```

### Android instrumentation tests
Use a connected emulator or device, then run:
```bash
./gradlew connectedDebugAndroidTest
```

### Backend tests
From `backend-api/`:
```bash
source env/bin/activate
pytest tests -q
```

---

## 6) Expected Phase 9 outcome

After these updates:

- repository unit tests exist
- key viewmodel unit tests exist
- validator coverage exists
- basic screen instrumentation tests exist
- backend test surface is more complete
- `PortfolioViewModel.kt` is no longer missing
- UI content composables become easier to test
- stability confidence for final demo is much better
