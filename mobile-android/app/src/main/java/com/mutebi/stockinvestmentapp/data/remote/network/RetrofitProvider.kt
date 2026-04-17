package com.mutebi.stockinvestmentapp.data.remote.network

import android.app.Application
import com.google.gson.GsonBuilder
import com.mutebi.stockinvestmentapp.core.constants.ApiConstants
import com.mutebi.stockinvestmentapp.data.remote.api.AssetApi
import com.mutebi.stockinvestmentapp.data.remote.api.AuthApi
import com.mutebi.stockinvestmentapp.data.remote.api.KycApi
import com.mutebi.stockinvestmentapp.data.remote.api.PortfolioApi
import com.mutebi.stockinvestmentapp.data.remote.api.TradeApi
import com.mutebi.stockinvestmentapp.data.remote.api.TransactionApi
import com.mutebi.stockinvestmentapp.data.remote.api.UserApi
import com.mutebi.stockinvestmentapp.data.remote.api.WatchlistApi
import com.mutebi.stockinvestmentapp.data.session.SessionManager
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitProvider {

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

    private fun retrofit(application: Application): Retrofit {
        return provideRetrofit(
            AuthInterceptor(SessionManager(application))
        )
    }

    fun authApi(application: Application): AuthApi =
        retrofit(application).create(AuthApi::class.java)

    fun userApi(application: Application): UserApi =
        retrofit(application).create(UserApi::class.java)

    fun kycApi(application: Application): KycApi =
        retrofit(application).create(KycApi::class.java)

    fun assetApi(application: Application): AssetApi =
        retrofit(application).create(AssetApi::class.java)

    fun watchlistApi(application: Application): WatchlistApi =
        retrofit(application).create(WatchlistApi::class.java)

    fun portfolioApi(application: Application): PortfolioApi =
        retrofit(application).create(PortfolioApi::class.java)

    fun tradeApi(application: Application): TradeApi =
        retrofit(application).create(TradeApi::class.java)

    fun transactionApi(application: Application): TransactionApi =
        retrofit(application).create(TransactionApi::class.java)
}