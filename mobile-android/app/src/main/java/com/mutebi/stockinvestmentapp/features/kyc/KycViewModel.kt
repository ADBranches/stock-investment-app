package com.mutebi.stockinvestmentapp.features.kyc

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.mutebi.stockinvestmentapp.core.utils.Resource
import com.mutebi.stockinvestmentapp.data.remote.network.RetrofitProvider
import com.mutebi.stockinvestmentapp.data.repository.KycRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class KycViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = KycRepository(
        kycApi = RetrofitProvider.kycApi(application)
    )

    private val _uiState = MutableStateFlow(KycUiState())
    val uiState: StateFlow<KycUiState> = _uiState

    fun onFirstNameChange(value: String) {
        _uiState.value = _uiState.value.copy(firstName = value, error = null)
    }

    fun onLastNameChange(value: String) {
        _uiState.value = _uiState.value.copy(lastName = value, error = null)
    }

    fun onNationalIdNumberChange(value: String) {
        _uiState.value = _uiState.value.copy(nationalIdNumber = value, error = null)
    }

    fun onDocumentTypeChange(value: String) {
        _uiState.value = _uiState.value.copy(documentType = value, error = null)
    }

    fun onDocumentNumberChange(value: String) {
        _uiState.value = _uiState.value.copy(documentNumber = value, error = null)
    }

    fun submit() {
        val state = _uiState.value

        when {
            state.firstName.isBlank() -> {
                _uiState.value = state.copy(error = "First name is required")
                return
            }
            state.lastName.isBlank() -> {
                _uiState.value = state.copy(error = "Last name is required")
                return
            }
            state.nationalIdNumber.isBlank() -> {
                _uiState.value = state.copy(error = "National ID number is required")
                return
            }
            state.documentType.isBlank() -> {
                _uiState.value = state.copy(error = "Document type is required")
                return
            }
            state.documentNumber.isBlank() -> {
                _uiState.value = state.copy(error = "Document number is required")
                return
            }
        }

        viewModelScope.launch {
            _uiState.value = state.copy(isLoading = true, error = null)

            when (
                val result = if (
                    state.status.equals("not_started", ignoreCase = true) ||
                    state.status.isBlank()
                ) {
                    repository.submitKyc(
                        firstName = state.firstName.trim(),
                        lastName = state.lastName.trim(),
                        nationalId = state.nationalIdNumber.trim(),
                        documentType = state.documentType.trim(),
                        documentNumber = state.documentNumber.trim()
                    )
                } else {
                    repository.updateKyc(
                        firstName = state.firstName.trim(),
                        lastName = state.lastName.trim(),
                        nationalId = state.nationalIdNumber.trim(),
                        documentType = state.documentType.trim(),
                        documentNumber = state.documentNumber.trim()
                    )
                }
            ) {
                is Resource.Success -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        success = true,
                        status = result.data?.status.orEmpty()
                    )
                }
                is Resource.Error -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = result.message ?: "Failed to submit KYC"
                    )
                }
                else -> {
                    _uiState.value = _uiState.value.copy(isLoading = false)
                }
            }
        }
    }

    fun loadKyc() {
        viewModelScope.launch {
            when (val result = repository.getMyKyc()) {
                is Resource.Success -> {
                    val kyc = result.data
                    if (kyc != null) {
                        _uiState.value = _uiState.value.copy(
                            firstName = kyc.firstName,
                            lastName = kyc.lastName,
                            nationalIdNumber = kyc.nationalIdNumber,
                            documentType = kyc.documentType,
                            documentNumber = kyc.documentNumber,
                            status = kyc.status,
                            error = null
                        )
                    }
                }
                is Resource.Error -> {
                    // Keeping this silent for now unless we want inline error on first load.
                }
                else -> Unit
            }
        }
    }
}