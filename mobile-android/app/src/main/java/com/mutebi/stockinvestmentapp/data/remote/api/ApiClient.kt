package com.stockapp.data.remote.api

import android.content.Context
import com.stockapp.data.session.AuthHolder
import com.stockapp.data.session.SessionManager
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {
    private const val BASE_URL = "http://10.0.2.2:5000/"

    fun createRetrofit(context: Context): Retrofit {
        val sessionManager = SessionManager(context)
        AuthHolder.token = sessionManager.getAuthToken()

        val authInterceptor = Interceptor { chain ->
            val original: Request = chain.request()
            val token = AuthHolder.token

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
            .baseUrl(BASE_URL)
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