package com.mutebi.stockinvestmentapp.features.education.hub

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mutebi.stockinvestmentapp.features.education.components.EducationCard
import com.mutebi.stockinvestmentapp.features.education.components.RiskNoticeBanner
import com.mutebi.stockinvestmentapp.features.education.components.TopicChipRow

@Composable
fun EducationHubScreen(
    onBack: () -> Unit,
    onOpenNotifications: () -> Unit,
    onOpenArticle: (Int) -> Unit,
    vm: EducationHubViewModel = viewModel()
) {
    val state by vm.uiState.collectAsState()

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            TextButton(onClick = onBack) {
                Text("Back")
            }
        }

        item {
            Text("Education hub")
        }

        item {
            RiskNoticeBanner(
                text = "Responsible investing reminder: education should guide decisions, not replace careful judgment or personal affordability checks."
            )
        }

        item {
            OutlinedTextField(
                value = state.query,
                onValueChange = vm::onQueryChange,
                label = { Text("Search lessons and tips") },
                singleLine = true
            )
        }

        item {
            TopicChipRow(
                topics = state.topics,
                selectedTopic = state.selectedTopic,
                onTopicSelected = vm::onTopicChange
            )
        }

        item {
            TextButton(onClick = onOpenNotifications) {
                Text("Open notification center")
            }
        }

        if (state.isLoading) {
            item {
                CircularProgressIndicator()
            }
        } else if (state.visibleArticles.isEmpty()) {
            item {
                Text("No education articles available yet.")
            }
        } else {
            items(state.visibleArticles, key = { it.id }) { article ->
                EducationCard(
                    article = article,
                    onClick = { onOpenArticle(article.id) }
                )
            }
        }

        state.error?.let { errorMessage ->
            item {
                Text(errorMessage)
            }
        }
    }
}