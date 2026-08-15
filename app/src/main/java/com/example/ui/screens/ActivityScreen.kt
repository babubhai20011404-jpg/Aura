package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CurrencyRupee
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.model.Transaction
import com.example.ui.components.AuraButton
import com.example.ui.components.AuraTopBar
import com.example.ui.components.TransactionStatusBadge
import com.example.ui.theme.AuraBackground
import com.example.ui.theme.AuraBorder
import com.example.ui.theme.AuraBorderSubtle
import com.example.ui.theme.AuraEmerald
import com.example.ui.theme.AuraEmeraldGlow
import com.example.ui.theme.AuraSurfaceCard
import com.example.ui.theme.AuraSurfaceElevated
import com.example.ui.theme.AuraTextMuted
import com.example.ui.theme.AuraTextPrimary
import com.example.ui.theme.AuraTextSecondary
import com.example.ui.theme.AuraTheme
import com.example.util.CurrencyFormatter
import com.example.viewmodel.ActivityFilter

@Composable
fun ActivityScreen(
    transactions: List<Transaction>,
    currentFilter: ActivityFilter,
    searchQuery: String,
    onFilterChange: (ActivityFilter) -> Unit,
    onSearchChange: (String) -> Unit,
    onTransactionClick: (Transaction) -> Unit,
    onStartSellClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AuraBackground)
    ) {
        AuraTopBar(
            title = "Activity",
            subtitle = "Conversion & settlement history"
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = AuraTheme.Spacing.xl),
            verticalArrangement = Arrangement.spacedBy(AuraTheme.Spacing.l)
        ) {
            // Search Input: Minimal & Functional
            OutlinedTextField(
                value = searchQuery,
                onValueChange = onSearchChange,
                placeholder = { Text("Search transactions", style = MaterialTheme.typography.bodyMedium) },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = null,
                        tint = AuraTextMuted,
                        modifier = Modifier.size(20.dp)
                    )
                },
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .testTag("activity_search_input"),
                shape = RoundedCornerShape(AuraTheme.Radius.m),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = AuraEmerald,
                    unfocusedBorderColor = AuraBorder,
                    focusedTextColor = AuraTextPrimary,
                    unfocusedTextColor = AuraTextPrimary,
                    cursorColor = AuraEmerald,
                    focusedContainerColor = AuraSurfaceCard,
                    unfocusedContainerColor = AuraSurfaceCard
                )
            )

            // Compact Segmented Filter Pills
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(AuraTheme.Spacing.s),
                modifier = Modifier.fillMaxWidth()
            ) {
                val filters = listOf(
                    Pair(ActivityFilter.ALL, "All"),
                    Pair(ActivityFilter.COMPLETED, "Settled"),
                    Pair(ActivityFilter.PROCESSING, "Processing"),
                    Pair(ActivityFilter.FAILED, "Failed")
                )
                items(filters) { (filter, label) ->
                    val isSelected = currentFilter == filter
                    Surface(
                        shape = RoundedCornerShape(AuraTheme.Radius.m),
                        color = if (isSelected) AuraEmeraldGlow else AuraSurfaceElevated,
                        border = if (isSelected) androidx.compose.foundation.BorderStroke(1.dp, AuraEmerald.copy(alpha = 0.5f)) else null,
                        modifier = Modifier
                            .clip(RoundedCornerShape(AuraTheme.Radius.m))
                            .clickable { onFilterChange(filter) }
                    ) {
                        Text(
                            text = label,
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                            ),
                            color = if (isSelected) AuraEmerald else AuraTextSecondary,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                        )
                    }
                }
            }

            if (transactions.isEmpty()) {
                EmptyActivityState(onStartSellClick)
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(transactions) { txn ->
                        ActivityRow(
                            transaction = txn,
                            onClick = { onTransactionClick(txn) }
                        )
                        HorizontalDivider(color = AuraBorderSubtle, thickness = 0.5.dp)
                    }
                    item {
                        Spacer(modifier = Modifier.height(80.dp))
                    }
                }
            }
        }
    }
}

@Composable
private fun ActivityRow(
    transaction: Transaction,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = AuraTheme.Spacing.l),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.weight(1f)
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(AuraSurfaceElevated),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.CurrencyRupee,
                    contentDescription = null,
                    tint = AuraEmerald,
                    modifier = Modifier.size(16.dp)
                )
            }
            Spacer(modifier = Modifier.width(AuraTheme.Spacing.m))
            Column {
                Text(
                    text = "USDT Sale",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                    color = AuraTextPrimary
                )
                Text(
                    text = "${transaction.settlementBankName} · ${CurrencyFormatter.formatTransactionTime(transaction.createdAtMillis)}",
                    style = MaterialTheme.typography.labelSmall,
                    color = AuraTextMuted
                )
            }
        }

        Column(horizontalAlignment = Alignment.End) {
            Text(
                text = "+ ${CurrencyFormatter.formatInr(transaction.netInr, includeDecimalsIfAny = true)}",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = AuraEmerald
                )
            )
            TransactionStatusBadge(status = transaction.status)
        }
    }
}

@Composable
private fun EmptyActivityState(onStartSellClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 60.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(AuraTheme.Spacing.l),
            modifier = Modifier.padding(AuraTheme.Spacing.xxl)
        ) {
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .clip(CircleShape)
                    .background(AuraSurfaceElevated),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.History,
                    contentDescription = null,
                    tint = AuraTextMuted,
                    modifier = Modifier.size(32.dp)
                )
            }
            Text(
                text = "No activity found",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                color = AuraTextPrimary
            )
            Text(
                text = "Your USDT sale history and bank settlement status will appear here.",
                style = MaterialTheme.typography.bodyMedium,
                color = AuraTextMuted,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
            Spacer(modifier = Modifier.height(AuraTheme.Spacing.s))
            AuraButton(
                text = "Sell USDT Now",
                onClick = onStartSellClick,
                modifier = Modifier.width(200.dp)
            )
        }
    }
}
