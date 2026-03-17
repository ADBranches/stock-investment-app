package com.mutebi.stockinvestmentapp.navigation

sealed class Routes(val route: String) {
    data object Splash : Routes("splash")
    data object Onboarding : Routes("onboarding")

    // Auth
    data object Login : Routes("login")
    data object Register : Routes("register")
    data object ForgotPassword : Routes("forgot_password")

    // Post-auth flow
    data object ProfileSetup : Routes("profile_setup")

    // KYC flow
    data object KycIntro : Routes("kyc_intro")
    data object KycPersonalInfo : Routes("kyc_personal_info")
    data object KycDocumentUpload : Routes("kyc_document_upload")
    data object KycReview : Routes("kyc_review")
    data object KycSuccess : Routes("kyc_success")

    // Main app
    data object Dashboard : Routes("dashboard")
    data object Market : Routes("market")
    data object Watchlist : Routes("watchlist")
    data object Portfolio : Routes("portfolio")
}