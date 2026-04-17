package com.mutebi.stockinvestmentapp.features.portfolio.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.mutebi.stockinvestmentapp.domain.model.Transaction

@Composable
fun TransactionRow(
    transaction: Transaction
) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text("${transaction.tradeType.uppercase()} • ${transaction.asset?.symbol ?: transaction.assetId}")
            Text("Quantity: ${"%.2f".format(transaction.quantity)}")
            Text("Price: $${"%.2f".format(transaction.price)}")
            Text("Status: ${transaction.status}")
            Text("Created: ${transaction.createdAt}")
        }
    }
}