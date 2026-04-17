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