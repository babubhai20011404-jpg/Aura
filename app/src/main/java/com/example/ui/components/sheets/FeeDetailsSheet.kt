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
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.model.Quote
import com.example.ui.components.AuraButton
import com.example.ui.components.GlassCard
import com.example.ui.theme.AuraTheme
import com.example.ui.theme.AuraBorder
import com.example.ui.theme.AuraBorderSubtle
import com.example.ui.theme.AuraEmerald
import com.example.ui.theme.AuraSurfaceSecondary
import com.example.ui.theme.AuraTextMuted
import com.example.ui.theme.AuraTextPrimary
import com.example.ui.theme.AuraTextSecondary
import com.example.util.CurrencyFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FeeDetailsSheet(
    quote: Quote,
    onDismiss: () -> Unit
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
                        text = "Fee breakdown",
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                        color = AuraTextPrimary
                    )
                    Text(
                        text = "Transparent pricing · No surprises",
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

            // Breakdown card
            GlassCard(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    FeeItemRow("Gross value", CurrencyFormatter.formatInr(quote.grossInr))
                    FeeItemRow(
                        "Platform fee (0.15%)",
                        "- ${CurrencyFormatter.formatInr(quote.feeInr, includeDecimalsIfAny = true)}"
                    )
                    FeeItemRow("Settlement fee", "Free")
                    
                    HorizontalDivider(color = AuraBorderSubtle, thickness = 1.dp)

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Net credit",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = AuraTextPrimary
                        )
                        Text(
                            text = CurrencyFormatter.formatInr(quote.netInr, includeDecimalsIfAny = true),
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Bold,
                                color = AuraEmerald
                            )
                        )
                    }
                }
            }

            // Compliance Note
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(AuraTheme.Radius.l))
                    .background(AuraSurfaceSecondary.copy(alpha = 0.5f))
                    .padding(16.dp),
                verticalAlignment = Alignment.Top
            ) {
                Icon(
                    imageVector = Icons.Default.VerifiedUser,
                    contentDescription = null,
                    tint = AuraEmerald,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = "Compliant settlement",
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                        color = AuraTextPrimary
                    )
                    Text(
                        text = "Settled via RBI-approved partners. TDS certificates available in activity.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = AuraTextSecondary
                    )
                }
            }

            AuraButton(
                text = "Got it",
                onClick = onDismiss,
                modifier = Modifier.fillMaxWidth().height(56.dp)
            )
        }
    }
}

@Composable
private fun FeeItemRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyLarge,
            color = AuraTextSecondary
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
            color = AuraTextPrimary
        )
    }
}
