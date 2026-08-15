package com.example.ui.components.sheets

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.TrendingDown
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.model.Quote
import com.example.ui.components.AuraButton
import com.example.ui.components.AuraButtonVariant
import com.example.ui.theme.AuraAmber
import com.example.ui.theme.AuraAmberSurface
import com.example.ui.theme.AuraBorder
import com.example.ui.theme.AuraEmerald
import com.example.ui.theme.AuraGraphiteDark
import com.example.ui.theme.AuraSurfaceCard
import com.example.ui.theme.AuraSurfaceElevated
import com.example.ui.theme.AuraTextMuted
import com.example.ui.theme.AuraTextPrimary
import com.example.ui.theme.AuraTextSecondary
import com.example.util.CurrencyFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuoteExpiredSheet(
    oldRate: Double,
    newRate: Double,
    quote: Quote,
    onAcceptNewRate: () -> Unit,
    onDismiss: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val isHigher = newRate >= oldRate

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = AuraGraphiteDark,
        tonalElevation = 0.dp,
        dragHandle = {
            Box(
                modifier = Modifier
                    .padding(vertical = 12.dp)
                    .width(36.dp)
                    .height(4.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(AuraBorder)
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .padding(bottom = 32.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(AuraAmberSurface),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = if (isHigher) Icons.AutoMirrored.Filled.TrendingUp else Icons.AutoMirrored.Filled.TrendingDown,
                        contentDescription = null,
                        tint = if (isHigher) AuraEmerald else AuraAmber,
                        modifier = Modifier.size(20.dp)
                    )
                }
                Column {
                    Text(
                        text = "Market Rate Updated",
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                        color = AuraTextPrimary
                    )
                    Text(
                        text = "The 30s locked quote has refreshed",
                        style = MaterialTheme.typography.bodyMedium,
                        color = AuraTextMuted
                    )
                }
            }

            // Rate Comparison Card
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = AuraSurfaceCard,
                border = androidx.compose.foundation.BorderStroke(1.dp, AuraBorder),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(20.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Previous rate",
                            style = MaterialTheme.typography.labelSmall,
                            color = AuraTextMuted
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = CurrencyFormatter.formatRate(oldRate),
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Medium,
                                color = AuraTextSecondary
                            )
                        )
                    }

                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = null,
                        tint = AuraTextMuted,
                        modifier = Modifier.size(18.dp)
                    )

                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            text = "New rate",
                            style = MaterialTheme.typography.labelSmall,
                            color = AuraTextMuted
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "${CurrencyFormatter.formatRate(newRate)} / USDT",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Bold,
                                color = if (isHigher) AuraEmerald else AuraAmber
                            )
                        )
                    }
                }
            }

            // Updated Net Payout Preview
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(AuraSurfaceElevated)
                    .padding(14.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Updated Payout (${CurrencyFormatter.formatUsdt(quote.usdtAmount)})",
                    style = MaterialTheme.typography.bodyMedium,
                    color = AuraTextSecondary
                )
                Text(
                    text = CurrencyFormatter.formatInr(quote.netInr, includeDecimalsIfAny = true),
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = AuraEmerald
                )
            }

            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                AuraButton(
                    text = "Review New Rate",
                    onClick = onAcceptNewRate,
                    modifier = Modifier.fillMaxWidth()
                )
                AuraButton(
                    text = "Keep Previous Screen",
                    variant = AuraButtonVariant.GHOST,
                    onClick = onDismiss,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}
