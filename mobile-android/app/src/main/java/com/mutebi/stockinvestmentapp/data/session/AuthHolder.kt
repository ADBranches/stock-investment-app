package com.mutebi.stockinvestmentapp.data.session

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

data class AuthState(
    val token: String? = null,
    val userId: Int? = null,
    val email: String? = null,
    val isLoggedIn: Boolean = false
)

object AuthHolder {
    private val _state = MutableStateFlow(AuthState())
    val state: StateFlow<AuthState> = _state

    val currentToken: String?
        get() = state.value.token

    val isAuthenticated: Boolean
        get() = state.value.isLoggedIn && !state.value.token.isNullOrBlank()

    fun setSession(token: String, userId: Int?, email: String?) {
        _state.update {
            it.copy(
                token = token,
                userId = userId,
                email = email,
                isLoggedIn = true
            )
        }
    }

    fun clear() {
        _state.value = AuthState()
    }
}