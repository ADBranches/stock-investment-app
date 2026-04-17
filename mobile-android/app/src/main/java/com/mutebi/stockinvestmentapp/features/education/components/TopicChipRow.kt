package com.mutebi.stockinvestmentapp.features.education.components

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun TopicChipRow(
    topics: List<String>,
    selectedTopic: String,
    onTopicSelected: (String) -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.horizontalScroll(rememberScrollState())
    ) {
        topics.forEach { topic ->
            FilterChip(
                selected = selectedTopic == topic,
                onClick = { onTopicSelected(topic) },
                label = { Text(topic) }
            )
        }
    }
}