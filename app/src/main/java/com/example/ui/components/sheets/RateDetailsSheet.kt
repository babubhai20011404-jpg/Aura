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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.AuraButton
import com.example.ui.components.GlassCard
import com.example.ui.theme.AuraBackground
import com.example.ui.theme.AuraBorder
import com.example.ui.theme.AuraEmerald
import com.example.ui.theme.AuraSurfaceSecondary
import com.example.ui.theme.AuraTextMuted
import com.example.ui.theme.AuraTextPrimary
import com.example.ui.theme.AuraTextSecondary
import com.example.ui.theme.AuraTheme
import com.example.util.CurrencyFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RateDetailsSheet(
    rate: Double,
    secondsRemaining: Int = 30,
    onDismiss: () -> Unit,
    onManualRefresh: () -> Unit = {}
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = Color.White,
        scrimColor = Color.Black.copy(alpha = 0.3f),
        tonalElevation = 0.dp,
        shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp),
        dragHandle = {
            Box(
                modifier = Modifier
                    .padding(vertical = 12.dp)
                    .width(36.dp)
                    .height(5.dp)
                    .clip(CircleShape)
                    .background(AuraBorder)
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .padding(bottom = 48.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Rate transparency",
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                        color = AuraTextPrimary
                    )
                    Text(
                        text = "Real-time institutional liquidity",
                        style = MaterialTheme.typography.bodyMedium,
                        color = AuraTextMuted
                    )
                }
                IconButton(
                    onClick = onDismiss,
                    modifier = Modifier.clip(CircleShape).background(AuraSurfaceSecondary)
                ) {
                    Icon(imageVector = Icons.Default.Close, contentDescription = "Close", tint = AuraTextPrimary)
                }
            }

            // Current Rate Display (Glass Card)
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
                            text = "Execution rate",
                            style = MaterialTheme.typography.labelSmall,
                            color = AuraTextMuted
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "${CurrencyFormatter.formatRate(rate)} / USDT",
                            style = MaterialTheme.typography.headlineMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = AuraEmerald
                            )
                        )
                    }
                    Box(
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(AuraSurfaceSecondary)
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = "${secondsRemaining}s",
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                            color = AuraTextSecondary
                        )
                    }
                }
            }

            // Transparency Points
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                TransparencyItem(
                    icon = Icons.Default.Shield,
                    title = "Zero hidden spread",
                    desc = "The rate you see is the rate you get. No hidden markups."
                )
                TransparencyItem(
                    icon = Icons.AutoMirrored.Filled.TrendingUp,
                    title = "Aggregated spot liquidity",
                    desc = "Sourced directly from top institutional desks for optimal conversion."
                )
            }

            AuraButton(
                text = "Refresh Market Rate",
                onClick = onManualRefresh,
                modifier = Modifier.fillMaxWidth().height(56.dp)
            )
        }
    }
}

@Composable
private fun TransparencyItem(
    icon: ImageVector,
    title: String,
    desc: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top
    ) {
        Box(
            modifier = Modifier.size(40.dp).clip(CircleShape).background(AuraSurfaceSecondary),
            contentAlignment = Alignment.Center
        ) {
            Icon(imageVector = icon, contentDescription = null, tint = AuraEmerald, modifier = Modifier.size(20.dp))
        }
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(
                text = title,
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                color = AuraTextPrimary
            )
            Text(
                text = desc,
                style = MaterialTheme.typography.bodyMedium,
                color = AuraTextSecondary
            )
        }
    }
}
