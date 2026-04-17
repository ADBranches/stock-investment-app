package com.mutebi.stockinvestmentapp.features.education.article

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mutebi.stockinvestmentapp.features.education.components.RiskNoticeBanner

@Composable
fun ArticleDetailScreen(
    articleId: Int,
    onBack: () -> Unit,
    vm: ArticleDetailViewModel = viewModel()
) {
    val state by vm.uiState.collectAsState()

    LaunchedEffect(articleId) {
        vm.loadArticle(articleId)
    }

    if (state.isLoading) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            CircularProgressIndicator()
            Text("Loading article...")
        }
        return
    }

    val article = state.article
    if (article == null) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(state.error ?: "Article unavailable.")
            TextButton(onClick = onBack) {
                Text("Back")
            }
        }
        return
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        TextButton(onClick = onBack) {
            Text("Back")
        }

        Text(article.title)
        Text("Topic: ${article.topic}")

        if (article.isBeginnerFriendly) {
            Text("Beginner-friendly")
        }

        if (article.riskWarning.isNotBlank()) {
            RiskNoticeBanner(text = article.riskWarning)
        }

        Text(article.summary)
        Text(article.content)
    }
}