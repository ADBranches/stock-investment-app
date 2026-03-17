package com.mutebi.stockinvestmentapp.features.onboarding

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class OnboardingViewModel : ViewModel() {

    private val _pages = MutableStateFlow(
        listOf(
            OnboardingPage(
                title = "Learn Before You Invest",
                description = "Build confidence with guided investing journeys and beginner-friendly insights."
            ),
            OnboardingPage(
                title = "Track What Matters",
                description = "Monitor watchlists, portfolio movement, and opportunities through a clean modern dashboard."
            ),
            OnboardingPage(
                title = "Act With Clarity",
                description = "Place guided buy and sell actions through a secure, structured mobile experience."
            )
        )
    )

    val pages: StateFlow<List<OnboardingPage>> = _pages
}