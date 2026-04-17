# Phase 4 Final Android Audit Pack (Based on Your Real Current Files)

## First: yes, these are the files I needed

Yes — the latest files you pasted are the **real current contents** I needed in order to stop guessing and make a precise Phase 4 decision. My earlier mistake was treating a bad path audit as if it proved files were missing. Your corrected paste fixed that. This pack is therefore based on the **actual files now shown**, not on assumptions. fileciteturn18file10 fileciteturn18file18

---

## Phase 4 decision summary

### Keep as-is
These are already populated enough for Phase 4 and do **not** need wholesale replacement:

- `features/auth/login/LoginUiState.kt`
- `features/auth/register/RegisterUiState.kt`
- `features/auth/forgot_password/ForgotPasswordUiState.kt`
- `features/profile/setup/ProfileSetupUiState.kt`
- `features/profile/setup/components/ProfileAvatarPicker.kt`
- `features/profile/setup/components/ProfileForm.kt`
- `features/kyc/KycIntroScreen.kt`
- `features/kyc/KycPersonalInfoScreen.kt`
- `features/kyc/KycDocumentUploadScreen.kt`
- `features/kyc/KycReviewScreen.kt`
- `features/kyc/KycSuccessScreen.kt`
- `data/remote/dto/UserDtos.kt`
- `data/remote/dto/KycDtos.kt`
- `domain/model/UserProfile.kt`
- `domain/model/KycProfile.kt`
- `data/remote/api/AuthApi.kt`
- `data/repository/AuthRepository.kt`
- `features/auth/forgot_password/ForgotPasswordScreen.kt`
- `features/auth/forgot_password/ForgotPasswordViewModel.kt`
- `features/auth/register/RegisterScreen.kt`
- `features/auth/register/RegisterViewModel.kt`
- `features/profile/setup/ProfileSetupScreen.kt`
- `features/profile/setup/ProfileSetupViewModel.kt`
- `data/remote/api/UserApi.kt`
- `data/remote/api/KycApi.kt`
- `data/repository/UserRepository.kt`
- `data/repository/KycRepository.kt`
- `di/NetworkModule.kt`
- `di/RepositoryModule.kt` fileciteturn18file10 fileciteturn18file17 fileciteturn18file18

### Needs patch for Phase 4 to behave correctly
These are the real Android-side blockers or near-blockers:

1. `navigation/AppNavGraph.kt`  
   Login and splash currently route to `Dashboard`, but your dashboard destination is still a TODO placeholder, so post-login lands on a blank screen. Dashboard belongs to Phase 5, not Phase 4. fileciteturn18file15 fileciteturn18file16

2. `navigation/AppNavGraph.kt` + KYC screens  
   Each KYC screen currently uses `viewModel()` directly, which usually creates separate screen-scoped instances. That risks losing KYC form state as you move from personal info to document info to review. Your KYC flow should share one `KycViewModel` across the KYC destinations for Phase 4. fileciteturn18file18

3. `features/kyc/KycUiState.kt`  
   `status` currently defaults to `""`. That still works with your current `KycViewModel`, but using `"not_started"` is cleaner and matches the flow logic already in the view model. fileciteturn18file18

---

## Exact patches to apply

---

## File: `app/src/main/java/com/mutebi/stockinvestmentapp/features/kyc/KycUiState.kt`

### Replace with:
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

## File: `app/src/main/java/com/mutebi/stockinvestmentapp/features/kyc/KycPersonalInfoScreen.kt`

### Replace with:
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

## File: `app/src/main/java/com/mutebi/stockinvestmentapp/features/kyc/KycDocumentUploadScreen.kt`

### Replace with:
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

## File: `app/src/main/java/com/mutebi/stockinvestmentapp/features/kyc/KycReviewScreen.kt`

### Replace with:
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

## File: `app/src/main/java/com/mutebi/stockinvestmentapp/navigation/AppNavGraph.kt`

### Replace with this Phase 4-safe version:
```kotlin
package com.mutebi.stockinvestmentapp.navigation

import androidx.compose.runtime.Composable
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
            androidx.compose.foundation.layout.Box(
                modifier = androidx.compose.ui.Modifier.fillMaxSize(),
                contentAlignment = androidx.compose.ui.Alignment.Center
            ) {
                androidx.compose.material3.Text("Dashboard comes in Phase 5")
            }
        }

        composable(Routes.Market.route) {
            androidx.compose.foundation.layout.Box(
                modifier = androidx.compose.ui.Modifier.fillMaxSize(),
                contentAlignment = androidx.compose.ui.Alignment.Center
            ) {
                androidx.compose.material3.Text("Market comes in Phase 5")
            }
        }

        composable(Routes.Watchlist.route) {
            androidx.compose.foundation.layout.Box(
                modifier = androidx.compose.ui.Modifier.fillMaxSize(),
                contentAlignment = androidx.compose.ui.Alignment.Center
            ) {
                androidx.compose.material3.Text("Watchlist comes in Phase 5")
            }
        }

        composable(Routes.Portfolio.route) {
            androidx.compose.foundation.layout.Box(
                modifier = androidx.compose.ui.Modifier.fillMaxSize(),
                contentAlignment = androidx.compose.ui.Alignment.Center
            ) {
                androidx.compose.material3.Text("Portfolio comes in Phase 6")
            }
        }
    }
}
```

---

## Why these are the only Android changes I recommend now

Your latest current files show that the Android auth/profile/KYC surface is **already mostly populated**. The only real functional gaps on the Android side are:

- routing into a blank Phase 5 dashboard too early
- not guaranteeing shared KYC state across the multi-screen KYC flow
- a small `KycUiState` default cleanup

Everything else you showed is acceptable for Phase 4 close-out and does not need replacement. fileciteturn18file10 fileciteturn18file17 fileciteturn18file18

---

## Backend status

I still do **not** have the actual contents of these backend Phase 4 files yet:

- `backend-api/app/routes/users.py`
- `backend-api/app/routes/kyc.py`
- `backend-api/app/services/user_service.py`
- `backend-api/app/services/kyc_service.py`
- `backend-api/tests/test_users.py`
- `backend-api/tests/test_kyc.py`

So I am not calling them complete yet. Your timeline says they are part of Phase 4, but their real contents were not included in the corrected latest paste. fileciteturn18file15 fileciteturn18file16

---

## Test sequence after pasting the Android patches

From `mobile-android/`:

```bash
./gradlew clean
./gradlew installDebug
adb reverse tcp:5000 tcp:5000
adb shell am start -n com.mutebi.stockinvestmentapp/.MainActivity
adb logcat
```

### Expected result
- login works
- no blank dark screen immediately after login
- profile setup opens after login
- KYC personal info -> document info -> review preserves entered values
- KYC submit works or at least reaches backend cleanly
- dashboard no longer appears blank; it shows a temporary Phase 5 placeholder instead

---

## Next exact backend audit command

Run this from inside `mobile-android/`:

```bash
for f in \
../backend-api/app/routes/users.py \
../backend-api/app/routes/kyc.py \
../backend-api/app/services/user_service.py \
../backend-api/app/services/kyc_service.py \
../backend-api/tests/test_users.py \
../backend-api/tests/test_kyc.py
do
  echo
  echo "==================== $f ===================="
  cat "$f"
done
```

Once you paste that output, I can finish the backend Phase 4 close-out without guessing.
