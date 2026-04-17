package com.mutebi.stockinvestmentapp.navigation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.mutebi.stockinvestmentapp.features.auth.forgot_password.ForgotPasswordScreen
import com.mutebi.stockinvestmentapp.features.auth.login.LoginScreen
import com.mutebi.stockinvestmentapp.features.auth.register.RegisterScreen
import com.mutebi.stockinvestmentapp.features.education.article.ArticleDetailScreen
import com.mutebi.stockinvestmentapp.features.education.hub.EducationHubScreen
import com.mutebi.stockinvestmentapp.features.home.dashboard.DashboardScreen
import com.mutebi.stockinvestmentapp.features.kyc.KycDocumentUploadScreen
import com.mutebi.stockinvestmentapp.features.kyc.KycIntroScreen
import com.mutebi.stockinvestmentapp.features.kyc.KycPersonalInfoScreen
import com.mutebi.stockinvestmentapp.features.kyc.KycReviewScreen
import com.mutebi.stockinvestmentapp.features.kyc.KycSuccessScreen
import com.mutebi.stockinvestmentapp.features.kyc.KycViewModel
import com.mutebi.stockinvestmentapp.features.market.details.AssetDetailScreen
import com.mutebi.stockinvestmentapp.features.market.list.MarketListScreen
import com.mutebi.stockinvestmentapp.features.notifications.NotificationCenterScreen
import com.mutebi.stockinvestmentapp.features.onboarding.OnboardingScreen
import com.mutebi.stockinvestmentapp.features.portfolio.history.TransactionHistoryScreen
import com.mutebi.stockinvestmentapp.features.portfolio.overview.PortfolioScreen
import com.mutebi.stockinvestmentapp.features.profile.setup.ProfileSetupScreen
import com.mutebi.stockinvestmentapp.features.splash.SplashScreen
import com.mutebi.stockinvestmentapp.features.trade.confirm.TradeConfirmScreen
import com.mutebi.stockinvestmentapp.features.trade.confirm.TradeResultScreen
import com.mutebi.stockinvestmentapp.features.trade.order.TradeOrderScreen
import com.mutebi.stockinvestmentapp.features.trade.order.TradeOrderViewModel
import com.mutebi.stockinvestmentapp.features.watchlist.WatchlistScreen

@Composable
fun AppNavGraph(
    navController: NavHostController,
    isLoggedIn: Boolean,
    modifier: Modifier = Modifier
) {
    val kycVm: KycViewModel = viewModel()
    val tradeVm: TradeOrderViewModel = viewModel()

    NavHost(
        navController = navController,
        startDestination = Routes.Splash.route,
        modifier = modifier
    ) {
        composable(Routes.Splash.route) {
            SplashScreen(
                isLoggedIn = isLoggedIn,
                onNavigateToOnboarding = {
                    navController.navigate(Routes.Onboarding.route) {
                        popUpTo(Routes.Splash.route) { inclusive = true }
                    }
                },
                onNavigateToDashboard = {
                    navController.navigate(Routes.Dashboard.route) {
                        popUpTo(Routes.Splash.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Routes.Onboarding.route) {
            OnboardingScreen(
                onGetStarted = {
                    navController.navigate(Routes.Login.route) {
                        popUpTo(Routes.Onboarding.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Routes.Login.route) {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate(Routes.Dashboard.route) {
                        popUpTo(Routes.Login.route) { inclusive = true }
                    }
                },
                onNavigateToRegister = {
                    navController.navigate(Routes.Register.route)
                },
                onNavigateToForgotPassword = {
                    navController.navigate(Routes.ForgotPassword.route)
                }
            )
        }

        composable(Routes.Register.route) {
            RegisterScreen(
                onRegisterSuccess = {
                    navController.navigate(Routes.ProfileSetup.route) {
                        popUpTo(Routes.Register.route) { inclusive = true }
                    }
                },
                onNavigateToLogin = {
                    navController.popBackStack()
                }
            )
        }

        composable(Routes.ForgotPassword.route) {
            ForgotPasswordScreen(
                onBackToLogin = {
                    navController.popBackStack()
                }
            )
        }

        composable(Routes.ProfileSetup.route) {
            ProfileSetupScreen(
                onProfileSaved = {
                    navController.navigate(Routes.KycIntro.route) {
                        popUpTo(Routes.ProfileSetup.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Routes.KycIntro.route) {
            KycIntroScreen(
                onStartKyc = {
                    navController.navigate(Routes.KycPersonalInfo.route)
                }
            )
        }

        composable(Routes.KycPersonalInfo.route) {
            KycPersonalInfoScreen(
                vm = kycVm,
                onNext = {
                    navController.navigate(Routes.KycDocumentUpload.route)
                }
            )
        }

        composable(Routes.KycDocumentUpload.route) {
            KycDocumentUploadScreen(
                vm = kycVm,
                onNext = {
                    navController.navigate(Routes.KycReview.route)
                }
            )
        }

        composable(Routes.KycReview.route) {
            KycReviewScreen(
                vm = kycVm,
                onSuccess = {
                    navController.navigate(Routes.KycSuccess.route) {
                        popUpTo(Routes.KycReview.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Routes.KycSuccess.route) {
            KycSuccessScreen(
                onDone = {
                    navController.navigate(Routes.Dashboard.route) {
                        popUpTo(Routes.KycIntro.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Routes.Dashboard.route) {
            DashboardScreen(
                onOpenMarket = {
                    navController.navigate(Routes.Market.route)
                },
                onOpenWatchlist = {
                    navController.navigate(Routes.Watchlist.route)
                },
                onOpenEducation = {
                    navController.navigate(Routes.EducationHub.route)
                },
                onOpenNotifications = {
                    navController.navigate(Routes.NotificationCenter.route)
                }
            )
        }

        composable(Routes.Market.route) {
            MarketListScreen(
                onAssetClick = { assetId ->
                    navController.navigate(Routes.AssetDetail.createRoute(assetId))
                },
                onOpenWatchlist = {
                    navController.navigate(Routes.Watchlist.route)
                }
            )
        }

        composable(Routes.Watchlist.route) {
            WatchlistScreen(
                onOpenMarket = {
                    navController.navigate(Routes.Market.route)
                },
                onBack = {
                    navController.popBackStack()
                }
            )
        }

        composable(Routes.AssetDetail.route) { backStackEntry ->
            val assetId = backStackEntry.arguments?.getString("assetId")?.toIntOrNull()

            if (assetId == null) {
                Phase7PlaceholderScreen(
                    title = "Asset detail unavailable",
                    message = "The selected asset ID was not found."
                )
            } else {
                AssetDetailScreen(
                    assetId = assetId,
                    onBack = { navController.popBackStack() },
                    onOpenWatchlist = {
                        navController.navigate(Routes.Watchlist.route)
                    },
                    onTradeAsset = {
                        navController.navigate(Routes.TradeOrder.createRoute(assetId))
                    }
                )
            }
        }

        composable(Routes.Portfolio.route) {
            PortfolioScreen(
                onOpenHistory = {
                    navController.navigate(Routes.TransactionHistory.route)
                },
                onTradeAsset = { assetId ->
                    navController.navigate(Routes.TradeOrder.createRoute(assetId))
                },
                onOpenMarket = {
                    navController.navigate(Routes.Market.route)
                }
            )
        }

        composable(Routes.TransactionHistory.route) {
            TransactionHistoryScreen(
                onBack = {
                    navController.popBackStack()
                }
            )
        }

        composable(Routes.TradeOrder.route) { backStackEntry ->
            val assetId = backStackEntry.arguments?.getString("assetId")?.toIntOrNull()

            if (assetId == null) {
                Phase7PlaceholderScreen(
                    title = "Trade setup unavailable",
                    message = "The selected asset ID was not found."
                )
            } else {
                TradeOrderScreen(
                    assetId = assetId,
                    vm = tradeVm,
                    onBack = { navController.popBackStack() },
                    onContinue = {
                        navController.navigate(Routes.TradeConfirm.route)
                    }
                )
            }
        }

        composable(Routes.TradeConfirm.route) {
            TradeConfirmScreen(
                vm = tradeVm,
                onBack = { navController.popBackStack() },
                onTradeCompleted = {
                    navController.navigate(Routes.TradeResult.route)
                }
            )
        }

        composable(Routes.TradeResult.route) {
            TradeResultScreen(
                vm = tradeVm,
                onDone = {
                    navController.navigate(Routes.Portfolio.route) {
                        popUpTo(Routes.TradeOrder.route) { inclusive = false }
                    }
                }
            )
        }

        composable(Routes.EducationHub.route) {
            EducationHubScreen(
                onBack = { navController.popBackStack() },
                onOpenNotifications = {
                    navController.navigate(Routes.NotificationCenter.route)
                },
                onOpenArticle = { articleId ->
                    navController.navigate(Routes.ArticleDetail.createRoute(articleId))
                }
            )
        }

        composable(Routes.ArticleDetail.route) { backStackEntry ->
            val articleId = backStackEntry.arguments?.getString("articleId")?.toIntOrNull()

            if (articleId == null) {
                Phase7PlaceholderScreen(
                    title = "Article unavailable",
                    message = "The selected article ID was not found."
                )
            } else {
                ArticleDetailScreen(
                    articleId = articleId,
                    onBack = { navController.popBackStack() }
                )
            }
        }

        composable(Routes.NotificationCenter.route) {
            NotificationCenterScreen(
                onBack = { navController.popBackStack() }
            )
        }
    }
}

@Composable
private fun Phase7PlaceholderScreen(
    title: String,
    message: String
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(text = title)
            Text(text = message)
        }
    }
}