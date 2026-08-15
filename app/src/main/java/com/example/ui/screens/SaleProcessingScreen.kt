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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.model.Transaction
import com.example.ui.components.AuraButton
import com.example.ui.components.AuraTopBar
import com.example.ui.components.GlassCard
import com.example.ui.components.GlassSurface
import com.example.ui.components.StatusTimeline
import com.example.ui.theme.AuraBackground
import com.example.ui.theme.AuraEmerald
import com.example.ui.theme.AuraEmeraldMuted
import com.example.ui.theme.AuraGlassElevated
import com.example.ui.theme.AuraSurfaceSecondary
import com.example.ui.theme.AuraTextMuted
import com.example.ui.theme.AuraTextPrimary
import com.example.ui.theme.AuraTextSecondary
import com.example.ui.theme.AuraTheme
import com.example.util.CurrencyFormatter

@Composable
fun SaleProcessingScreen(
    transaction: Transaction?,
    onReturnHomeClick: () -> Unit,
    onSimulateFailure: () -> Unit = {}
) {
    val scrollState = rememberScrollState()
    val txn = transaction ?: return

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AuraBackground)
    ) {
        AuraTopBar(
            title = "Processing sale",
            onBackClick = onReturnHomeClick
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(32.dp))

            // Premium Processing Indicator
            Box(
                modifier = Modifier.size(100.dp),
                contentAlignment = Alignment.Center
            ) {
                GlassSurface(
                    modifier = Modifier.size(100.dp),
                    color = AuraGlassElevated,
                    shape = CircleShape,
                    shadowElevation = 8.dp
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(60.dp).align(Alignment.Center),
                        strokeWidth = 4.dp,
                        color = AuraEmerald,
                        trackColor = AuraSurfaceSecondary
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Title & Status
            Text(
                text = "Your sale is processing",
                style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                color = AuraTextPrimary,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "USDT has been locked for settlement.",
                style = MaterialTheme.typography.bodyLarge,
                color = AuraTextSecondary,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(40.dp))

            // Amount Summary Card
            GlassCard(
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(20.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Expected INR",
                            style = MaterialTheme.typography.labelMedium,
                            color = AuraTextMuted
                        )
                        Text(
                            text = CurrencyFormatter.formatInr(txn.netInr, includeDecimalsIfAny = true),
                            style = MaterialTheme.typography.titleLarge.copy(color = AuraEmerald, fontWeight = FontWeight.Bold)
                        )
                    }
                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            text = "Selling",
                            style = MaterialTheme.typography.labelMedium,
                            color = AuraTextMuted
                        )
                        Text(
                            text = CurrencyFormatter.formatUsdt(txn.usdtAmount),
                            style = MaterialTheme.typography.titleMedium,
                            color = AuraTextPrimary
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Timeline
            StatusTimeline(currentStepIndex = txn.currentStepIndex)

            Spacer(modifier = Modifier.height(24.dp))

            // Details Panel
            GlassSurface(
                shape = RoundedCornerShape(AuraTheme.Radius.l),
                color = Color.White.copy(alpha = 0.5f),
                modifier = Modifier.fillMaxWidth(),
                shadowElevation = 0.dp,
                showBorder = true
            ) {
                Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    DetailRow("Destination Bank", txn.settlementBankName)
                    DetailRow("Transaction ID", txn.id)
                    DetailRow("Est. Arrival", "10–30 min")
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Help Link
            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(AuraTheme.Radius.m))
                    .clickable { /* Support */ }
                    .padding(vertical = 12.dp, horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.HelpOutline,
                    contentDescription = null,
                    tint = AuraTextSecondary,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Need help with this transaction?",
                    style = MaterialTheme.typography.labelMedium,
                    color = AuraTextSecondary
                )
            }

            Spacer(modifier = Modifier.height(48.dp))

            // Action
            AuraButton(
                text = "Return to Home",
                onClick = onReturnHomeClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp)
                    .testTag("processing_return_home_button")
            )
            
            Spacer(modifier = Modifier.height(48.dp))
        }
    }
}

@Composable
private fun DetailRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, style = MaterialTheme.typography.labelMedium, color = AuraTextMuted)
        Text(text = value, style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold), color = AuraTextSecondary)
    }
}
