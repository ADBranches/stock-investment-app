package com.mutebi.stockinvestmentapp.features.trade.order

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.mutebi.stockinvestmentapp.core.utils.Resource
import com.mutebi.stockinvestmentapp.data.remote.network.RetrofitProvider
import com.mutebi.stockinvestmentapp.data.repository.AssetRepository
import com.mutebi.stockinvestmentapp.data.repository.TradeRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class TradeOrderViewModel(application: Application) : AndroidViewModel(application) {

    private val assetRepository = AssetRepository(
        RetrofitProvider.assetApi(application)
    )
    private val tradeRepository = TradeRepository(
        RetrofitProvider.tradeApi(application)
    )

    private val _uiState = MutableStateFlow(TradeOrderUiState())
    val uiState: StateFlow<TradeOrderUiState> = _uiState

    fun loadAsset(assetId: Int) {
        if (_uiState.value.assetId == assetId && _uiState.value.asset != null) return

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true,
                error = null,
                assetId = assetId,
                submittedTransaction = null,
                successMessage = null
            )

            when (val result = assetRepository.getAssetById(assetId)) {
                is Resource.Success -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        asset = result.data
                    )
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

    fun onTradeTypeChange(value: String) {
        _uiState.value = _uiState.value.copy(
            tradeType = value,
            error = null
        )
    }

    fun onQuantityChange(value: String) {
        _uiState.value = _uiState.value.copy(
            quantityText = value,
            error = null
        )
    }

    fun clearResult() {
        _uiState.value = _uiState.value.copy(
            submittedTransaction = null,
            successMessage = null,
            error = null
        )
    }

    fun canProceedToConfirm(): Boolean {
        return validateQuantity() != null && _uiState.value.asset != null
    }

    fun submitTrade() {
        val current = _uiState.value
        val assetId = current.assetId ?: return
        val quantity = validateQuantity()

        if (quantity == null) {
            _uiState.value = current.copy(error = "Enter a valid quantity greater than zero")
            return
        }

        viewModelScope.launch {
            _uiState.value = current.copy(
                isSubmitting = true,
                error = null,
                submittedTransaction = null,
                successMessage = null
            )

            when (
                val result = tradeRepository.createTrade(
                    assetId = assetId,
                    tradeType = current.tradeType,
                    quantity = quantity
                )
            ) {
                is Resource.Success -> {
                    _uiState.value = _uiState.value.copy(
                        isSubmitting = false,
                        submittedTransaction = result.data,
                        successMessage = "Trade submitted successfully."
                    )
                }
                is Resource.Error -> {
                    _uiState.value = _uiState.value.copy(
                        isSubmitting = false,
                        error = result.message
                    )
                }
                else -> {
                    _uiState.value = _uiState.value.copy(isSubmitting = false)
                }
            }
        }
    }

    private fun validateQuantity(): Double? {
        val quantity = _uiState.value.quantityText.trim().toDoubleOrNull()
        return quantity?.takeIf { it > 0.0 }
    }
}