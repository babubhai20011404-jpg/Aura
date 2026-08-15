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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.automirrored.filled.HelpOutline
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.MailOutline
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.unit.sp
import com.example.ui.components.AuraButton
import com.example.ui.theme.AuraBorder
import com.example.ui.theme.AuraBorderSubtle
import com.example.ui.theme.AuraEmerald
import com.example.ui.theme.AuraGraphiteDark
import com.example.ui.theme.AuraSurfaceCard
import com.example.ui.theme.AuraSurfaceElevated
import com.example.ui.theme.AuraTextMuted
import com.example.ui.theme.AuraTextPrimary
import com.example.ui.theme.AuraTextSecondary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SupportSheet(
    transactionId: String? = null,
    onDismiss: () -> Unit,
    onStartChat: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

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
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Settlement Support",
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                        color = AuraTextPrimary
                    )
                    Text(
                        text = "Priority 24/7 banking assistance",
                        style = MaterialTheme.typography.bodyMedium,
                        color = AuraTextMuted
                    )
                }
                IconButton(onClick = onDismiss) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close",
                        tint = AuraTextSecondary
                    )
                }
            }

            if (transactionId != null) {
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = AuraSurfaceElevated,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Reference: ",
                            style = MaterialTheme.typography.labelMedium,
                            color = AuraTextMuted
                        )
                        Text(
                            text = transactionId,
                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                            color = AuraEmerald
                        )
                    }
                }
            }

            // Quick FAQs
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                FaqItem(
                    q = "How long does INR bank settlement take?",
                    a = "98% of settlements arrive via IMPS within 10 to 30 minutes. High-volume transfers over ₹2,00,000 are routed via RTGS during banking hours."
                )
                FaqItem(
                    q = "What if my bank account rejects the transfer?",
                    a = "If the receiving bank declines the deposit, the full USDT amount is automatically released back to your available wallet balance."
                )
                FaqItem(
                    q = "Where can I download my TDS certificate?",
                    a = "Every completed sale generates a Form 26AS compliant TDS receipt, downloadable under the Activity tab."
                )
            }

            AuraButton(
                text = "Chat with Settlement Specialist",
                icon = Icons.AutoMirrored.Filled.Chat,
                onClick = onStartChat,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
private fun FaqItem(q: String, a: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(AuraSurfaceCard)
            .padding(14.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.HelpOutline,
                contentDescription = null,
                tint = AuraEmerald,
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = q,
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.SemiBold, fontSize = 14.sp),
                color = AuraTextPrimary
            )
        }
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = a,
            style = MaterialTheme.typography.bodyMedium.copy(fontSize = 13.sp),
            color = AuraTextSecondary
        )
    }
}
