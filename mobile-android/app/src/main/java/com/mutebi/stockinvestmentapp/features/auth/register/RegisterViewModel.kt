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
            !android.util.Patterns.EMAIL_ADDRESS.matcher(state.email.trim()).matches() -> {
                _uiState.value = state.copy(error = "Enter a valid email address")
                return
            }
            state.password.isBlank() -> {
                _uiState.value = state.copy(error = "Password is required")
                return
            }
            state.confirmPassword.isBlank() -> {
                _uiState.value = state.copy(error = "Confirm password is required")
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