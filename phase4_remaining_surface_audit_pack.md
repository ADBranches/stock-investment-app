# Phase 4 Remaining-Surface Audit Pack (Path-Corrected + Safe Drop-Ins)

## Important finding first

Your latest audit **did not actually prove those files are missing**.

You ran the command **from inside**:

```bash
~/.../stock-investment-app/mobile-android
```

but then tried to `cat` paths that also started with `mobile-android/...`, so Android paths were effectively doubled. The uploaded terminal output shows the repeated `No such file or directory` results for those doubled paths, which means the audit path was wrong, not necessarily the repo structure. The same happened for backend paths: from inside `mobile-android`, backend files should be addressed as `../backend-api/...`, not `backend-api/...`. fileciteturn16file4 fileciteturn16file5

Also, some of those files are almost certainly present already, because your current code compiles and imports them. For example, your reviewed ViewModels reference `LoginUiState`, `RegisterUiState`, `ForgotPasswordUiState`, `ProfileSetupUiState`, `UserProfile`, and `KycProfile`, so those files cannot be assumed absent from the project just because the latest `cat` command used the wrong paths. fileciteturn16file16 fileciteturn16file18

---

## Phase 4 scope reminder

Per your timeline, Phase 4 is for:

- login
- register
- forgot password
- profile setup
- KYC submission screens
- User/KYC API + repository wiring
- protected backend profile/KYC flow

Dashboard belongs to Phase 5, not Phase 4. fileciteturn16file9 fileciteturn16file15

---

## Files already reviewed and safe to keep as-is

These were already inspected and are sufficiently populated for Phase 4, so do **not** replace them wholesale:

- `app/src/main/java/com/mutebi/stockinvestmentapp/data/remote/api/AuthApi.kt`
- `app/src/main/java/com/mutebi/stockinvestmentapp/data/repository/AuthRepository.kt`
- `app/src/main/java/com/mutebi/stockinvestmentapp/features/auth/forgot_password/ForgotPasswordScreen.kt`
- `app/src/main/java/com/mutebi/stockinvestmentapp/features/auth/forgot_password/ForgotPasswordViewModel.kt`
- `app/src/main/java/com/mutebi/stockinvestmentapp/features/auth/register/RegisterScreen.kt`
- `app/src/main/java/com/mutebi/stockinvestmentapp/features/auth/register/RegisterViewModel.kt`
- `app/src/main/java/com/mutebi/stockinvestmentapp/features/profile/setup/ProfileSetupScreen.kt`
- `app/src/main/java/com/mutebi/stockinvestmentapp/features/profile/setup/ProfileSetupViewModel.kt`
- `app/src/main/java/com/mutebi/stockinvestmentapp/features/kyc/KycViewModel.kt`
- `app/src/main/java/com/mutebi/stockinvestmentapp/data/remote/api/UserApi.kt`
- `app/src/main/java/com/mutebi/stockinvestmentapp/data/remote/api/KycApi.kt`
- `app/src/main/java/com/mutebi/stockinvestmentapp/data/repository/UserRepository.kt`
- `app/src/main/java/com/mutebi/stockinvestmentapp/data/repository/KycRepository.kt`
- `app/src/main/java/com/mutebi/stockinvestmentapp/di/NetworkModule.kt`
- `app/src/main/java/com/mutebi/stockinvestmentapp/di/RepositoryModule.kt` fileciteturn16file13 fileciteturn16file14 fileciteturn16file16

---

## Corrected audit commands before pasting anything

### Android files
Run this **from inside** `mobile-android/`:

```bash
for f in \
app/src/main/java/com/mutebi/stockinvestmentapp/features/auth/login/LoginUiState.kt \
app/src/main/java/com/mutebi/stockinvestmentapp/features/auth/register/RegisterUiState.kt \
app/src/main/java/com/mutebi/stockinvestmentapp/features/auth/forgot_password/ForgotPasswordUiState.kt \
app/src/main/java/com/mutebi/stockinvestmentapp/features/profile/setup/ProfileSetupUiState.kt \
app/src/main/java/com/mutebi/stockinvestmentapp/features/profile/setup/components/ProfileAvatarPicker.kt \
app/src/main/java/com/mutebi/stockinvestmentapp/features/profile/setup/components/ProfileForm.kt \
app/src/main/java/com/mutebi/stockinvestmentapp/features/kyc/KycIntroScreen.kt \
app/src/main/java/com/mutebi/stockinvestmentapp/features/kyc/KycPersonalInfoScreen.kt \
app/src/main/java/com/mutebi/stockinvestmentapp/features/kyc/KycDocumentUploadScreen.kt \
app/src/main/java/com/mutebi/stockinvestmentapp/features/kyc/KycReviewScreen.kt \
app/src/main/java/com/mutebi/stockinvestmentapp/features/kyc/KycSuccessScreen.kt \
app/src/main/java/com/mutebi/stockinvestmentapp/features/kyc/KycUiState.kt \
app/src/main/java/com/mutebi/stockinvestmentapp/data/remote/dto/UserDtos.kt \
app/src/main/java/com/mutebi/stockinvestmentapp/data/remote/dto/KycDtos.kt \
app/src/main/java/com/mutebi/stockinvestmentapp/domain/model/UserProfile.kt \
app/src/main/java/com/mutebi/stockinvestmentapp/domain/model/KycProfile.kt
do
  echo
  echo "==================== $f ===================="
  cat "$f"
done
```

### Backend files
Run this **from inside** `mobile-android/`:

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

---

## Safe drop-ins for the not-yet-reviewed Android surface area

Use these **only if the corrected audit shows the file is missing, empty, or clearly broken**.

---

## File: `app/src/main/java/com/mutebi/stockinvestmentapp/features/auth/login/LoginUiState.kt`

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

## File: `app/src/main/java/com/mutebi/stockinvestmentapp/features/auth/register/RegisterUiState.kt`

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

## File: `app/src/main/java/com/mutebi/stockinvestmentapp/features/auth/forgot_password/ForgotPasswordUiState.kt`

```kotlin
package com.mutebi.stockinvestmentapp.features.auth.forgot_password

data class ForgotPasswordUiState(
    val email: String = "",
    val isLoading: Boolean = false,
    val successMessage: String? = null,
    val error: String? = null
)
```

---

## File: `app/src/main/java/com/mutebi/stockinvestmentapp/features/profile/setup/ProfileSetupUiState.kt`

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

## File: `app/src/main/java/com/mutebi/stockinvestmentapp/features/profile/setup/components/ProfileAvatarPicker.kt`

```kotlin
package com.mutebi.stockinvestmentapp.features.profile.setup.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp

@Composable
fun ProfileAvatarPicker() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Surface(
            modifier = Modifier
                .size(84.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.surfaceVariant),
            shape = CircleShape,
            tonalElevation = 2.dp
        ) {}
        Text(
            text = "Avatar upload will be added later",
            style = MaterialTheme.typography.bodySmall
        )
    }
}
```

---

## File: `app/src/main/java/com/mutebi/stockinvestmentapp/features/profile/setup/components/ProfileForm.kt`

```kotlin
package com.mutebi.stockinvestmentapp.features.profile.setup.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.Spacer
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
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        OutlinedTextField(
            value = fullName,
            onValueChange = onFullNameChange,
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Full name") },
            singleLine = true
        )

        OutlinedTextField(
            value = phoneNumber,
            onValueChange = onPhoneNumberChange,
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Phone number") },
            singleLine = true
        )

        OutlinedTextField(
            value = country,
            onValueChange = onCountryChange,
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Country") },
            singleLine = true
        )

        OutlinedTextField(
            value = dateOfBirth,
            onValueChange = onDateOfBirthChange,
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Date of birth (YYYY-MM-DD)") },
            singleLine = true
        )

        Spacer(modifier = Modifier.height(4.dp))
    }
}
```

---

## File: `app/src/main/java/com/mutebi/stockinvestmentapp/features/kyc/KycUiState.kt`

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
    val success: Boolean = false,
    val error: String? = null
)
```

---

## File: `app/src/main/java/com/mutebi/stockinvestmentapp/features/kyc/KycIntroScreen.kt`

```kotlin
package com.mutebi.stockinvestmentapp.features.kyc

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun KycIntroScreen(
    onStartKyc: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Verify your identity",
            style = MaterialTheme.typography.headlineSmall
        )

        Text(
            text = "KYC helps keep your account secure and prepares you for the later investment flow."
        )

        Text(
            text = "You will provide your personal details and identity document information."
        )

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

## File: `app/src/main/java/com/mutebi/stockinvestmentapp/features/kyc/KycPersonalInfoScreen.kt`

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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun KycPersonalInfoScreen(
    onNext: () -> Unit,
    vm: KycViewModel = viewModel()
) {
    val state by vm.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "Personal information",
            style = MaterialTheme.typography.headlineSmall
        )

        OutlinedTextField(
            value = state.firstName,
            onValueChange = vm::onFirstNameChange,
            modifier = Modifier.fillMaxWidth(),
            label = { Text("First name") },
            singleLine = true
        )

        OutlinedTextField(
            value = state.lastName,
            onValueChange = vm::onLastNameChange,
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Last name") },
            singleLine = true
        )

        OutlinedTextField(
            value = state.nationalIdNumber,
            onValueChange = vm::onNationalIdNumberChange,
            modifier = Modifier.fillMaxWidth(),
            label = { Text("National ID number") },
            singleLine = true
        )

        Spacer(modifier = Modifier.height(4.dp))

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

## File: `app/src/main/java/com/mutebi/stockinvestmentapp/features/kyc/KycDocumentUploadScreen.kt`

```kotlin
package com.mutebi.stockinvestmentapp.features.kyc

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun KycDocumentUploadScreen(
    onNext: () -> Unit,
    vm: KycViewModel = viewModel()
) {
    val state by vm.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "Document information",
            style = MaterialTheme.typography.headlineSmall
        )

        Text(
            text = "For Phase 4, capture the document type and number. Real file upload can come later."
        )

        OutlinedTextField(
            value = state.documentType,
            onValueChange = vm::onDocumentTypeChange,
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Document type") },
            singleLine = true
        )

        OutlinedTextField(
            value = state.documentNumber,
            onValueChange = vm::onDocumentNumberChange,
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Document number") },
            singleLine = true
        )

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

## File: `app/src/main/java/com/mutebi/stockinvestmentapp/features/kyc/KycReviewScreen.kt`

```kotlin
package com.mutebi.stockinvestmentapp.features.kyc

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun KycReviewScreen(
    onSuccess: () -> Unit,
    vm: KycViewModel = viewModel()
) {
    val state by vm.uiState.collectAsState()

    LaunchedEffect(state.success) {
        if (state.success) onSuccess()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "Review KYC details",
            style = MaterialTheme.typography.headlineSmall
        )

        Text("First name: ${state.firstName}")
        Text("Last name: ${state.lastName}")
        Text("National ID: ${state.nationalIdNumber}")
        Text("Document type: ${state.documentType}")
        Text("Document number: ${state.documentNumber}")

        state.error?.let {
            Text(text = it)
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

## File: `app/src/main/java/com/mutebi/stockinvestmentapp/features/kyc/KycSuccessScreen.kt`

```kotlin
package com.mutebi.stockinvestmentapp.features.kyc

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun KycSuccessScreen(
    onDone: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "KYC submitted",
            style = MaterialTheme.typography.headlineSmall
        )

        Text(
            text = "Your KYC information has been recorded for this Phase 4 flow."
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

## File: `app/src/main/java/com/mutebi/stockinvestmentapp/data/remote/dto/UserDtos.kt`

```kotlin
package com.mutebi.stockinvestmentapp.data.remote.dto

import com.google.gson.annotations.SerializedName

data class UpdateProfileRequestDto(
    @SerializedName("full_name")
    val fullName: String,
    @SerializedName("phone_number")
    val phoneNumber: String,
    @SerializedName("country")
    val country: String,
    @SerializedName("date_of_birth")
    val dateOfBirth: String
)

data class UserProfileDto(
    @SerializedName("id")
    val id: Int = 0,
    @SerializedName("email")
    val email: String = "",
    @SerializedName("full_name")
    val fullName: String? = null,
    @SerializedName("phone_number")
    val phoneNumber: String? = null,
    @SerializedName("country")
    val country: String? = null,
    @SerializedName("date_of_birth")
    val dateOfBirth: String? = null,
    @SerializedName("profile_completed")
    val profileCompleted: Boolean = false,
    @SerializedName("kyc_status")
    val kycStatus: String? = null
)
```

---

## File: `app/src/main/java/com/mutebi/stockinvestmentapp/data/remote/dto/KycDtos.kt`

```kotlin
package com.mutebi.stockinvestmentapp.data.remote.dto

import com.google.gson.annotations.SerializedName
import com.mutebi.stockinvestmentapp.domain.model.KycProfile

data class SubmitKycRequestDto(
    @SerializedName("first_name")
    val firstName: String,
    @SerializedName("last_name")
    val lastName: String,
    @SerializedName("national_id_number")
    val nationalIdNumber: String,
    @SerializedName("document_type")
    val documentType: String,
    @SerializedName("document_number")
    val documentNumber: String
)

data class KycProfileDto(
    @SerializedName("first_name")
    val firstName: String? = null,
    @SerializedName("last_name")
    val lastName: String? = null,
    @SerializedName("national_id_number")
    val nationalIdNumber: String? = null,
    @SerializedName("document_type")
    val documentType: String? = null,
    @SerializedName("document_number")
    val documentNumber: String? = null,
    @SerializedName("status")
    val status: String? = null
) {
    fun toDomain(): KycProfile {
        return KycProfile(
            firstName = firstName.orEmpty(),
            lastName = lastName.orEmpty(),
            nationalIdNumber = nationalIdNumber.orEmpty(),
            documentType = documentType.orEmpty(),
            documentNumber = documentNumber.orEmpty(),
            status = status.orEmpty()
        )
    }
}
```

---

## File: `app/src/main/java/com/mutebi/stockinvestmentapp/domain/model/UserProfile.kt`

```kotlin
package com.mutebi.stockinvestmentapp.domain.model

data class UserProfile(
    val id: Int = 0,
    val email: String = "",
    val fullName: String = "",
    val phoneNumber: String = "",
    val country: String = "",
    val dateOfBirth: String = "",
    val profileCompleted: Boolean = false,
    val kycStatus: String = ""
)
```

---

## File: `app/src/main/java/com/mutebi/stockinvestmentapp/domain/model/KycProfile.kt`

```kotlin
package com.mutebi.stockinvestmentapp.domain.model

data class KycProfile(
    val firstName: String = "",
    val lastName: String = "",
    val nationalIdNumber: String = "",
    val documentType: String = "",
    val documentNumber: String = "",
    val status: String = "not_started"
)
```

---

## Required Phase 4 nav patch

Your auth flow currently reaches a blank screen because login and splash navigate into `Dashboard`, while `Dashboard` still has only a TODO placeholder. Since dashboard belongs to Phase 5, temporarily route Phase 4 success into `ProfileSetup` instead. This is consistent with the Phase 4 scope and avoids the blank dark screen. fileciteturn16file9 fileciteturn16file15

### File: `app/src/main/java/com/mutebi/stockinvestmentapp/navigation/AppNavGraph.kt`

### Replace this:
```kotlin
onNavigateToDashboard = {
    navController.navigate(Routes.Dashboard.route) {
        popUpTo(Routes.Splash.route) { inclusive = true }
    }
}

onLoginSuccess = {
    navController.navigate(Routes.Dashboard.route) {
        popUpTo(Routes.Login.route) { inclusive = true }
    }
}
```

### With this:
```kotlin
onNavigateToDashboard = {
    navController.navigate(Routes.ProfileSetup.route) {
        popUpTo(Routes.Splash.route) { inclusive = true }
    }
}

onLoginSuccess = {
    navController.navigate(Routes.ProfileSetup.route) {
        popUpTo(Routes.Login.route) { inclusive = true }
    }
}
```

---

## KYC state-sharing note

The KYC screens above assume the same `KycViewModel` is reused across the KYC flow. If your project currently creates a fresh view model on every KYC destination, then the simplest Phase 4-safe fix is to create one shared `KycViewModel` in `AppNavGraph` and pass it into the KYC composables.

### Example pattern inside `AppNavGraph.kt`
```kotlin
val kycViewModel: KycViewModel = viewModel()

composable(Routes.KycPersonalInfo.route) {
    KycPersonalInfoScreen(
        vm = kycViewModel,
        onNext = { navController.navigate(Routes.KycDocumentUpload.route) }
    )
}

composable(Routes.KycDocumentUpload.route) {
    KycDocumentUploadScreen(
        vm = kycViewModel,
        onNext = { navController.navigate(Routes.KycReview.route) }
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
```

---

## Test sequence after pasting

From `backend-api/`:
```bash
source env/bin/activate
python3 -m run
```

From `mobile-android/`:
```bash
adb devices
adb reverse tcp:5000 tcp:5000
./gradlew clean
./gradlew installDebug
adb shell am start -n com.mutebi.stockinvestmentapp/.MainActivity
adb logcat
```

### Expected Phase 4 result
- register works
- login works
- forgot password returns a calm message
- profile setup saves
- KYC screens render and submit
- app no longer drops into a blank screen immediately after login
- dashboard is still deferred to Phase 5, which matches your timeline. fileciteturn16file12 fileciteturn16file15
