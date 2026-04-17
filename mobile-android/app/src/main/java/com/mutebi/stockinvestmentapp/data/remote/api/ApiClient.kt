package com.mutebi.stockinvestmentapp.data.remote.api

import android.content.Context
import com.mutebi.stockinvestmentapp.data.session.SessionManager
import com.mutebi.stockinvestmentapp.core.constants.ApiConstants
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {

    fun createRetrofit(context: Context): Retrofit {
        val sessionManager = SessionManager(context)

        val authInterceptor = Interceptor { chain ->
            val original: Request = chain.request()
            val token = sessionManager.getToken()

            val requestBuilder = original.newBuilder()
                .header("Accept", "application/json")

            if (!token.isNullOrBlank()) {
                requestBuilder.header("Authorization", "Bearer $token")
            }

            chain.proceed(requestBuilder.build())
        }

        val client = OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .build()

        return Retrofit.Builder()
            .baseUrl(ApiConstants.BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    fun authApi(context: Context): AuthApi =
        createRetrofit(context).create(AuthApi::class.java)

    fun userApi(context: Context): UserApi =
        createRetrofit(context).create(UserApi::class.java)

    fun kycApi(context: Context): KycApi =
        createRetrofit(context).create(KycApi::class.java)
}