package com.mutebi.stockinvestmentapp.features.profile.setup

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.mutebi.stockinvestmentapp.core.utils.Resource
import com.mutebi.stockinvestmentapp.data.remote.network.RetrofitProvider
import com.mutebi.stockinvestmentapp.data.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ProfileSetupViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = UserRepository(
        userApi = RetrofitProvider.userApi(application)
    )

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

    fun submit() {
        val state = _uiState.value

        when {
            state.fullName.isBlank() -> {
                _uiState.value = state.copy(error = "Full name is required")
                return
            }

            state.phoneNumber.isBlank() -> {
                _uiState.value = state.copy(error = "Phone number is required")
                return
            }

            state.country.isBlank() -> {
                _uiState.value = state.copy(error = "Country is required")
                return
            }

            state.dateOfBirth.isBlank() -> {
                _uiState.value = state.copy(error = "Date of birth is required")
                return
            }
        }

        if (state.phoneNumber.trim().length < 7) {
            _uiState.value = state.copy(error = "Enter a valid phone number")
            return
        }

        val dob = state.dateOfBirth.trim()
        if (!Regex("""\d{4}-\d{2}-\d{2}""").matches(dob)) {
            _uiState.value = state.copy(error = "Use date format YYYY-MM-DD")
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
                        error = result.message ?: "Failed to update profile"
                    )
                }
            }
        }
    }

    fun loadProfile() {
        viewModelScope.launch {
            when (val result = repository.getProfile()) {
                is Resource.Success -> {
                    val profile = result.data
                    if (profile != null) {
                        _uiState.value = _uiState.value.copy(
                            fullName = profile.fullName,
                            phoneNumber = profile.phoneNumber,
                            country = profile.country,
                            dateOfBirth = profile.dateOfBirth,
                            error = null
                        )
                    }
                }

                is Resource.Error -> {
                    _uiState.value = _uiState.value.copy(
                        error = result.message
                    )
                }
            }
        }
    }
}