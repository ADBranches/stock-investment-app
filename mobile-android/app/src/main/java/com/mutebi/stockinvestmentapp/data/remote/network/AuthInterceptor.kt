package com.mutebi.stockinvestmentapp.data.remote.network

import com.mutebi.stockinvestmentapp.core.constants.ApiConstants
import com.mutebi.stockinvestmentapp.data.session.SessionManager
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AuthInterceptor @Inject constructor(
    private val sessionManager: SessionManager
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val token = sessionManager.getToken()

        val request = chain.request().newBuilder().apply {
            if (!token.isNullOrBlank()) {
                addHeader(
                    ApiConstants.AUTH_HEADER,
                    "${ApiConstants.BEARER_PREFIX}$token"
                )
            }
        }.build()

        return chain.proceed(request)
    }
}