package com.mutebi.stockinvestmentapp.di

import com.mutebi.stockinvestmentapp.data.session.SessionManager
import com.mutebi.stockinvestmentapp.data.remote.api.AssetApi
import com.mutebi.stockinvestmentapp.data.remote.api.AuthApi
import com.mutebi.stockinvestmentapp.data.remote.api.PortfolioApi
import com.mutebi.stockinvestmentapp.data.remote.api.WatchlistApi
import com.mutebi.stockinvestmentapp.data.repository.AssetRepository
import com.mutebi.stockinvestmentapp.data.repository.AuthRepository
import com.mutebi.stockinvestmentapp.data.repository.PortfolioRepository
import com.mutebi.stockinvestmentapp.data.repository.WatchlistRepository
import com.mutebi.stockinvestmentapp.data.remote.api.KycApi
import com.mutebi.stockinvestmentapp.data.remote.api.UserApi
import com.mutebi.stockinvestmentapp.data.repository.KycRepository
import com.mutebi.stockinvestmentapp.data.repository.UserRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    fun provideAuthRepository(
        authApi: AuthApi,
        sessionManager: SessionManager
    ): AuthRepository = AuthRepository(authApi, sessionManager)

    @Provides
    fun provideAssetRepository(
        assetApi: AssetApi
    ): AssetRepository = AssetRepository(assetApi)

    @Provides
    fun providePortfolioRepository(
        portfolioApi: PortfolioApi
    ): PortfolioRepository = PortfolioRepository(portfolioApi)

    @Provides
    fun provideWatchlistRepository(
        watchlistApi: WatchlistApi
    ): WatchlistRepository = WatchlistRepository(watchlistApi)

    @Provides
    fun provideUserRepository(
        userApi: UserApi
    ): UserRepository = UserRepository(userApi)

    @Provides
    fun provideKycRepository(
        kycApi: KycApi
    ): KycRepository = KycRepository(kycApi)
}