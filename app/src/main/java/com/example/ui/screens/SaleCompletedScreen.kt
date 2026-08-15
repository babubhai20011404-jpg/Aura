package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
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
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.model.Transaction
import com.example.ui.components.AuraButton
import com.example.ui.components.AuraButtonVariant
import com.example.ui.components.AuraTopBar
import com.example.ui.components.GlassCard
import com.example.ui.components.GlassSurface
import com.example.ui.theme.AuraBackground
import com.example.ui.theme.AuraBorderSubtle
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
fun SaleCompletedScreen(
    transaction: Transaction?,
    onViewDetailClick: (Transaction) -> Unit,
    onDoneClick: () -> Unit,
    onCopyUtr: (String) -> Unit
) {
    val scrollState = rememberScrollState()
    val txn = transaction ?: return

    var animateTrigger by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        kotlinx.coroutines.delay(100)
        animateTrigger = true
    }

    val checkScale by animateFloatAsState(
        targetValue = if (animateTrigger) 1.0f else 0.8f,
        animationSpec = spring(dampingRatio = 0.5f, stiffness = 300f),
        label = "check_scale"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AuraBackground)
    ) {
        AuraTopBar(
            title = "Sale completed",
            onBackClick = onDoneClick
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(48.dp))

            // Premium Success Indicator
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .scale(checkScale)
                    .clip(CircleShape)
                    .background(AuraEmeraldMuted),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .clip(CircleShape)
                        .background(AuraEmerald),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Success",
                        tint = Color.White,
                        modifier = Modifier.size(32.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Hero Amount Display
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = CurrencyFormatter.formatInr(txn.netInr, includeDecimalsIfAny = true),
                    style = MaterialTheme.typography.displayLarge,
                    color = AuraEmerald,
                    textAlign = TextAlign.Center
                )
                Text(
                    text = "INR settlement complete",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                    color = AuraTextPrimary,
                    textAlign = TextAlign.Center
                )
            }

            Spacer(modifier = Modifier.height(48.dp))

            // Details Section
            AnimatedVisibility(
                visible = animateTrigger,
                enter = fadeIn(animationSpec = tween(600)) + slideInVertically(
                    initialOffsetY = { 40 },
                    animationSpec = tween(600)
                )
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(32.dp)
                ) {
                    // Itemized Receipt Glass Card
                    GlassCard(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier.padding(24.dp),
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            ReceiptRow("Sold USDT", CurrencyFormatter.formatUsdt(txn.usdtAmount))
                            ReceiptRow("Rate", "${CurrencyFormatter.formatRate(txn.ratePerUsdt)} / USDT")
                            ReceiptRow("Settled to", "${txn.settlementBankName} ···· ${txn.settlementAccountMasked.takeLast(4)}")
                            
                            if (!txn.utrNumber.isNullOrBlank()) {
                                HorizontalDivider(color = AuraBorderSubtle)
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(AuraTheme.Radius.l))
                                        .background(AuraSurfaceSecondary)
                                        .clickable { onCopyUtr(txn.utrNumber) }
                                        .padding(16.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column {
                                        Text(text = "Reference (UTR)", style = MaterialTheme.typography.labelSmall, color = AuraTextMuted)
                                        Text(text = txn.utrNumber, style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold), color = AuraTextSecondary)
                                    }
                                    Icon(imageVector = Icons.Default.ContentCopy, contentDescription = "Copy", tint = AuraTextMuted, modifier = Modifier.size(16.dp))
                                }
                            }
                        }
                    }

                    // Done CTA
                    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                        AuraButton(
                            text = "Done",
                            onClick = onDoneClick,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(64.dp)
                                .testTag("completed_done_button")
                        )
                        
                        AuraButton(
                            text = "View details",
                            variant = AuraButtonVariant.GHOST,
                            onClick = { onViewDetailClick(txn) },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(48.dp))
                }
            }
        }
    }
}

@Composable
private fun ReceiptRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, style = MaterialTheme.typography.bodyLarge, color = AuraTextSecondary)
        Text(text = value, style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold), color = AuraTextPrimary)
    }
}
