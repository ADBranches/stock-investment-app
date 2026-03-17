package com.mutebi.stockinvestmentapp.data.repository

import com.mutebi.stockinvestmentapp.data.remote.api.WatchlistApi
import javax.inject.Inject

class WatchlistRepository @Inject constructor(
    private val api: WatchlistApi
)
