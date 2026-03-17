package com.mutebi.stockinvestmentapp.features.onboarding

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mutebi.stockinvestmentapp.core.ui.components.AppButton
import kotlinx.coroutines.launch

@Composable
fun OnboardingScreen(
    onGetStarted: () -> Unit
) {
    val pages = remember {
        listOf(
            OnboardingPage(
                "Learn Before You Invest",
                "Build confidence with guided investing journeys and beginner-friendly insights."
            ),
            OnboardingPage(
                "Track What Matters",
                "Monitor watchlists, portfolio movement, and opportunities through a clean premium dashboard."
            ),
            OnboardingPage(
                "Act With Clarity",
                "Use a secure and structured mobile workflow for responsible trading actions."
            )
        )
    }

    val pagerState = rememberPagerState(pageCount = { pages.size })
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        HorizontalPager(
            state = pagerState,
            contentPadding = PaddingValues(vertical = 48.dp)
        ) { page ->
            val item = pages[page]

            Column {
                Text(
                    text = item.title,
                    style = MaterialTheme.typography.headlineLarge
                )
                Text(
                    text = item.description,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(top = 16.dp)
                )
            }
        }

        AppButton(
            text = if (pagerState.currentPage == pages.lastIndex) "Get Started" else "Continue",
            onClick = {
                if (pagerState.currentPage == pages.lastIndex) {
                    onGetStarted()
                } else {
                    scope.launch {
                        pagerState.animateScrollToPage(pagerState.currentPage + 1)
                    }
                }
            }
        )
    }
}