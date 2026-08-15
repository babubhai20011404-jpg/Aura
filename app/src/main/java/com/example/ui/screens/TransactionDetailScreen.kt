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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.model.Transaction
import com.example.ui.components.AuraButton
import com.example.ui.components.AuraButtonVariant
import com.example.ui.components.AuraTopBar
import com.example.ui.components.TransactionStatusBadge
import com.example.ui.theme.AuraBackground
import com.example.ui.theme.AuraBorder
import com.example.ui.theme.AuraBorderSubtle
import com.example.ui.theme.AuraEmerald
import com.example.ui.theme.AuraSurfaceCard
import com.example.ui.theme.AuraSurfaceElevated
import com.example.ui.theme.AuraTextMuted
import com.example.ui.theme.AuraTextPrimary
import com.example.ui.theme.AuraTextSecondary
import com.example.ui.theme.AuraTheme
import com.example.util.CurrencyFormatter

@Composable
fun TransactionDetailScreen(
    transaction: Transaction?,
    onCopyText: (String) -> Unit,
    onNeedHelpClick: (String) -> Unit,
    onBackClick: () -> Unit
) {
    val scrollState = rememberScrollState()
    val txn = transaction ?: return

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AuraBackground)
    ) {
        AuraTopBar(
            title = "Transaction detail",
            onBackClick = onBackClick,
            actions = {
                IconButton(onClick = { onCopyText("Transaction ${txn.id}") }) {
                    Icon(imageVector = Icons.Default.Share, contentDescription = null, tint = AuraTextSecondary)
                }
            }
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(horizontal = AuraTheme.Spacing.xl),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(AuraTheme.Spacing.xxl))

            // Status Badge (Prominent)
            TransactionStatusBadge(status = txn.status)
            
            Spacer(modifier = Modifier.height(AuraTheme.Spacing.m))

            // Hero Net Amount
            Text(
                text = CurrencyFormatter.formatInr(txn.netInr, includeDecimalsIfAny = true),
                style = MaterialTheme.typography.displayLarge,
                color = AuraTextPrimary
            )
            Text(
                text = "${CurrencyFormatter.formatUsdt(txn.usdtAmount)} sold at ${CurrencyFormatter.formatRate(txn.ratePerUsdt)} / USDT",
                style = MaterialTheme.typography.bodyMedium,
                color = AuraTextMuted
            )

            Spacer(modifier = Modifier.height(AuraTheme.Spacing.xxxl))

            // Conversion Breakdown
            DetailCard(title = "Conversion") {
                DetailRow("Sold USDT", CurrencyFormatter.formatUsdt(txn.usdtAmount))
                DetailRow("Rate", "${CurrencyFormatter.formatRate(txn.ratePerUsdt)} / USDT")
                DetailRow("Gross INR", CurrencyFormatter.formatInr(txn.grossInr))
                DetailRow("Platform fee", "- ${CurrencyFormatter.formatInr(txn.feeInr, includeDecimalsIfAny = true)}")
                HorizontalDivider(color = AuraBorderSubtle, modifier = Modifier.padding(vertical = AuraTheme.Spacing.s))
                DetailRow("Net INR", CurrencyFormatter.formatInr(txn.netInr, includeDecimalsIfAny = true), isHighlighted = true)
            }

            Spacer(modifier = Modifier.height(AuraTheme.Spacing.xl))

            // Settlement Details
            DetailCard(title = "Settlement") {
                DetailRow("Bank", txn.settlementBankName)
                DetailRow("Account", txn.settlementAccountMasked)
                DetailRow("Created", CurrencyFormatter.formatDetailedDateTime(txn.createdAtMillis))
                if (txn.completedAtMillis != null) {
                    DetailRow("Completed", CurrencyFormatter.formatDetailedDateTime(txn.completedAtMillis))
                }
                
                if (!txn.utrNumber.isNullOrBlank()) {
                    Spacer(modifier = Modifier.height(AuraTheme.Spacing.m))
                    CopyableItem(label = "Bank Reference (UTR)", value = txn.utrNumber, onCopy = { onCopyText(txn.utrNumber) })
                }
                
                if (!txn.txHash.isNullOrBlank()) {
                    Spacer(modifier = Modifier.height(AuraTheme.Spacing.s))
                    CopyableItem(label = "Blockchain Reference", value = txn.txHash, onCopy = { onCopyText(txn.txHash) })
                }
            }

            Spacer(modifier = Modifier.height(AuraTheme.Spacing.xl))

            // Help & Support
            AuraButton(
                text = "Need help with this sale?",
                icon = Icons.Default.HelpOutline,
                variant = AuraButtonVariant.GHOST,
                onClick = { onNeedHelpClick(txn.id) },
                modifier = Modifier.fillMaxWidth()
            )
            
            Spacer(modifier = Modifier.height(AuraTheme.Spacing.huge))
        }
    }
}

@Composable
private fun DetailCard(title: String, content: @Composable androidx.compose.foundation.layout.ColumnScope.() -> Unit) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(text = title, style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold), color = AuraTextMuted, modifier = Modifier.padding(bottom = AuraTheme.Spacing.s))
        Surface(
            shape = RoundedCornerShape(AuraTheme.Radius.l),
            color = AuraSurfaceCard,
            border = androidx.compose.foundation.BorderStroke(1.dp, AuraBorder),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(AuraTheme.Spacing.l)) {
                content()
            }
        }
    }
}

@Composable
private fun DetailRow(label: String, value: String, isHighlighted: Boolean = false) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = AuraTheme.Spacing.s),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, style = MaterialTheme.typography.bodyMedium, color = AuraTextSecondary)
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = if (isHighlighted) FontWeight.Bold else FontWeight.Medium),
            color = if (isHighlighted) AuraEmerald else AuraTextPrimary
        )
    }
}

@Composable
private fun CopyableItem(label: String, value: String, onCopy: () -> Unit) {
    Surface(
        shape = RoundedCornerShape(AuraTheme.Radius.m),
        color = AuraSurfaceElevated,
        modifier = Modifier.fillMaxWidth().clickable(onClick = onCopy)
    ) {
        Row(
            modifier = Modifier.padding(AuraTheme.Spacing.m),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = label, style = MaterialTheme.typography.labelSmall, color = AuraTextMuted)
                Text(text = value, style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold), color = AuraTextSecondary)
            }
            Icon(imageVector = Icons.Default.ContentCopy, contentDescription = null, tint = AuraTextMuted, modifier = Modifier.size(14.dp))
        }
    }
}

