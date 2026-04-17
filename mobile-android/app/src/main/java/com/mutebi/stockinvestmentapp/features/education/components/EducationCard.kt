package com.mutebi.stockinvestmentapp.features.education.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.mutebi.stockinvestmentapp.domain.model.EducationArticle

@Composable
fun EducationCard(
    article: EducationArticle,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(article.title)
            Text(article.summary)
            Text("Topic: ${article.topic}")
            if (article.isBeginnerFriendly) {
                Text("Beginner-friendly")
            }
        }
    }
}