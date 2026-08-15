package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.model.BankAccount
import com.example.model.Quote
import com.example.ui.theme.AuraBorderSubtle
import com.example.ui.theme.AuraEmerald
import com.example.ui.theme.AuraGlassElevated
import com.example.ui.theme.AuraSurfaceSecondary
import com.example.ui.theme.AuraTextMuted
import com.example.ui.theme.AuraTextPrimary
import com.example.ui.theme.AuraTextSecondary
import com.example.ui.theme.AuraTheme
import com.example.util.CurrencyFormatter

@Composable
fun FeeBreakdownCard(
    quote: Quote,
    bankAccount: BankAccount,
    onFeeInfoClick: () -> Unit,
    onBankChangeClick: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    GlassCard(
        modifier = modifier
            .fillMaxWidth()
            .testTag("fee_breakdown_card")
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Conversion Info
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                BreakdownRow(label = "Selling", value = CurrencyFormatter.formatUsdt(quote.usdtAmount))
                BreakdownRow(label = "Conversion rate", value = "${CurrencyFormatter.formatRate(quote.ratePerUsdt)} / USDT")
                
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null,
                            onClick = onFeeInfoClick
                        ),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(text = "Platform fee (0.15%)", style = MaterialTheme.typography.bodyMedium, color = AuraTextSecondary)
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(imageVector = Icons.Default.Info, contentDescription = null, tint = AuraTextMuted, modifier = Modifier.size(14.dp))
                    }
                    Text(text = "- ${CurrencyFormatter.formatInr(quote.feeInr, includeDecimalsIfAny = true)}", style = MaterialTheme.typography.bodyMedium, color = AuraTextSecondary)
                }
            }

            HorizontalDivider(color = AuraBorderSubtle, thickness = 1.dp)

            // Primary "You Receive"
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(text = "You receive", style = MaterialTheme.typography.titleMedium, color = AuraTextPrimary)
                    Text(text = "Net payout", style = MaterialTheme.typography.labelSmall, color = AuraTextMuted)
                }
                Text(
                    text = CurrencyFormatter.formatInr(quote.netInr, includeDecimalsIfAny = true),
                    style = MaterialTheme.typography.headlineMedium,
                    color = AuraEmerald
                )
            }

            HorizontalDivider(color = AuraBorderSubtle, thickness = 1.dp)

            // Settlement Bank (Internal Card)
            GlassSurface(
                shape = RoundedCornerShape(AuraTheme.Radius.l),
                color = AuraSurfaceSecondary.copy(alpha = 0.5f),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(enabled = onBankChangeClick != null, onClick = { onBankChangeClick?.invoke() }),
                showBorder = false,
                shadowElevation = 0.dp
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                        Box(
                            modifier = Modifier.size(36.dp).clip(CircleShape).background(Color.White),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(imageVector = Icons.Default.AccountBalance, contentDescription = null, tint = AuraEmerald, modifier = Modifier.size(18.dp))
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                        Column {
                            Text(text = "${bankAccount.bankName} ···· ${bankAccount.accountNumberMasked.takeLast(4)}", style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold), color = AuraTextPrimary)
                            Text(text = "Direct INR deposit", style = MaterialTheme.typography.labelSmall, color = AuraTextMuted)
                        }
                    }
                    if (onBankChangeClick != null) {
                        Icon(imageVector = Icons.Default.ChevronRight, contentDescription = null, tint = AuraTextMuted, modifier = Modifier.size(18.dp))
                    }
                }
            }

            // Arrival Timing
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(imageVector = Icons.Default.Schedule, contentDescription = null, tint = AuraTextMuted, modifier = Modifier.size(14.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "Estimated arrival: 10–30 minutes", style = MaterialTheme.typography.labelSmall, color = AuraTextMuted)
            }
        }
    }
}

@Composable
private fun BreakdownRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = label, style = MaterialTheme.typography.bodyMedium, color = AuraTextSecondary)
        Text(text = value, style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold), color = AuraTextPrimary)
    }
}
