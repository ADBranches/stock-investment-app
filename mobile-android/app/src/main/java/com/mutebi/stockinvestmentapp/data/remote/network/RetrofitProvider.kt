package com.mutebi.stockinvestmentapp.data.remote.network

import android.app.Application
import com.google.gson.GsonBuilder
import com.mutebi.stockinvestmentapp.data.remote.api.AuthApi
import com.mutebi.stockinvestmentapp.data.remote.api.KycApi
import com.mutebi.stockinvestmentapp.data.remote.api.UserApi
import com.mutebi.stockinvestmentapp.data.session.SessionManager
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.mutebi.stockinvestmentapp.core.constants.ApiConstants
import java.util.concurrent.TimeUnit

object RetrofitProvider {

//    private const val BASE_URL = "http://192.168.100.172:5000/"

    fun provideRetrofit(authInterceptor: AuthInterceptor): Retrofit {
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        val okHttpClient = OkHttpClient.Builder()
            .connectTimeout(ApiConstants.TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .readTimeout(ApiConstants.TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .writeTimeout(ApiConstants.TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .addInterceptor(authInterceptor)
            .addInterceptor(logging)
            .build()

        return Retrofit.Builder()
            .baseUrl(ApiConstants.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(
                GsonConverterFactory.create(GsonBuilder().create())
            )
            .build()
    }

    fun authApi(application: Application): AuthApi {
        val retrofit = provideRetrofit(
            AuthInterceptor(SessionManager(application))
        )
        return retrofit.create(AuthApi::class.java)
    }

    fun userApi(application: Application): UserApi {
        val retrofit = provideRetrofit(
            AuthInterceptor(SessionManager(application))
        )
        return retrofit.create(UserApi::class.java)
    }

    fun kycApi(application: Application): KycApi {
        val retrofit = provideRetrofit(
            AuthInterceptor(SessionManager(application))
        )
        return retrofit.create(KycApi::class.java)
    }
}