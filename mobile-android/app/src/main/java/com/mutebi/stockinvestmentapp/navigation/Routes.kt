package com.mutebi.stockinvestmentapp.navigation

sealed class Routes(val route: String) {
    data object Splash : Routes("splash")
    data object Onboarding : Routes("onboarding")

    data object Login : Routes("login")
    data object Register : Routes("register")
    data object ForgotPassword : Routes("forgot_password")

    data object ProfileSetup : Routes("profile_setup")

    data object KycIntro : Routes("kyc_intro")
    data object KycPersonalInfo : Routes("kyc_personal_info")
    data object KycDocumentUpload : Routes("kyc_document_upload")
    data object KycReview : Routes("kyc_review")
    data object KycSuccess : Routes("kyc_success")

    data object Dashboard : Routes("dashboard")
    data object Market : Routes("market")
    data object Watchlist : Routes("watchlist")
    data object Portfolio : Routes("portfolio")
    data object TransactionHistory : Routes("portfolio_history")

    data object AssetDetail : Routes("asset_detail/{assetId}") {
        fun createRoute(assetId: Int): String = "asset_detail/$assetId"
    }

    data object TradeOrder : Routes("trade_order/{assetId}") {
        fun createRoute(assetId: Int): String = "trade_order/$assetId"
    }

    data object TradeConfirm : Routes("trade_confirm")
    data object TradeResult : Routes("trade_result")
}