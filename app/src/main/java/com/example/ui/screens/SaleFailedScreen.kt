package com.example.ui.screens

import androidx.compose.foundation.background
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
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Shield
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
import androidx.compose.ui.unit.sp
import com.example.model.Transaction
import com.example.ui.components.AuraButton
import com.example.ui.components.AuraButtonVariant
import com.example.ui.components.AuraTopBar
import com.example.ui.components.GlassCard
import com.example.ui.components.GlassSurface
import com.example.ui.theme.AuraBackground
import com.example.ui.theme.AuraEmerald
import com.example.ui.theme.AuraEmeraldMuted
import com.example.ui.theme.AuraGlassElevated
import com.example.ui.theme.AuraRose
import com.example.ui.theme.AuraRoseSurface
import com.example.ui.theme.AuraSurfaceSecondary
import com.example.ui.theme.AuraTextMuted
import com.example.ui.theme.AuraTextPrimary
import com.example.ui.theme.AuraTextSecondary
import com.example.ui.theme.AuraTheme
import com.example.util.CurrencyFormatter

@Composable
fun SaleFailedScreen(
    transaction: Transaction?,
    onTryAgainClick: () -> Unit,
    onNeedHelpClick: () -> Unit,
    onReturnHomeClick: () -> Unit
) {
    val scrollState = rememberScrollState()
    val txn = transaction ?: return

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AuraBackground)
    ) {
        AuraTopBar(
            title = "Sale unsuccessful",
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

            // Calm Error Indicator
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
                    .background(AuraRoseSurface),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Failed",
                    tint = AuraRose,
                    modifier = Modifier.size(40.dp)
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Title & Reason
            Text(
                text = "We couldn't complete this sale",
                style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                color = AuraTextPrimary,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = txn.failureReason ?: "Bank node timeout during processing.",
                style = MaterialTheme.typography.bodyLarge,
                color = AuraTextSecondary,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(40.dp))

            // Reassurance Card
            GlassSurface(
                shape = RoundedCornerShape(AuraTheme.Radius.xxl),
                color = AuraEmeraldMuted.copy(alpha = 0.5f),
                modifier = Modifier.fillMaxWidth(),
                shadowElevation = 2.dp
            ) {
                Row(
                    modifier = Modifier.padding(20.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Icon(
                        imageVector = Icons.Default.Shield,
                        contentDescription = null,
                        tint = AuraEmerald,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Column {
                        Text(
                            text = "Your USDT is safe",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = AuraTextPrimary
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "No funds were debited. ${CurrencyFormatter.formatUsdt(txn.usdtAmount)} is available in your balance.",
                            style = MaterialTheme.typography.labelMedium,
                            color = AuraTextSecondary
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Summary Info
            GlassCard(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Transaction ID", style = MaterialTheme.typography.labelMedium, color = AuraTextMuted)
                        Text(txn.id, style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold), color = AuraTextSecondary)
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Destination Bank", style = MaterialTheme.typography.labelMedium, color = AuraTextMuted)
                        Text(txn.settlementBankName, style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold), color = AuraTextSecondary)
                    }
                }
            }

            Spacer(modifier = Modifier.height(48.dp))

            // Actions
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 48.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                AuraButton(
                    text = "Try Selling Again",
                    icon = Icons.Default.Refresh,
                    onClick = onTryAgainClick,
                    modifier = Modifier.fillMaxWidth().height(64.dp)
                )

                AuraButton(
                    text = "Contact Support",
                    icon = Icons.Default.HelpOutline,
                    variant = AuraButtonVariant.SECONDARY,
                    onClick = onNeedHelpClick,
                    modifier = Modifier.fillMaxWidth().height(56.dp)
                )

                AuraButton(
                    text = "Back to Home",
                    variant = AuraButtonVariant.GHOST,
                    onClick = onReturnHomeClick,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}
