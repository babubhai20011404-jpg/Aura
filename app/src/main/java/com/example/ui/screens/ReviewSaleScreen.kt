package com.example.ui.screens

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
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
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import com.example.model.BankAccount
import com.example.model.Quote
import com.example.ui.components.AuraButton
import com.example.ui.components.AuraButtonVariant
import com.example.ui.components.AuraTopBar
import com.example.ui.components.FeeBreakdownCard
import com.example.ui.components.GlassCard
import com.example.ui.components.GlassSurface
import com.example.ui.theme.AuraBackground
import com.example.ui.theme.AuraEmerald
import com.example.ui.theme.AuraGlassElevated
import com.example.ui.theme.AuraSurfaceSecondary
import com.example.ui.theme.AuraTextMuted
import com.example.ui.theme.AuraTextPrimary
import com.example.ui.theme.AuraTextSecondary
import com.example.ui.theme.AuraTheme
import com.example.util.CurrencyFormatter

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun ReviewSaleScreen(
    sharedTransitionScope: SharedTransitionScope,
    animatedContentScope: AnimatedContentScope,
    quote: Quote,
    selectedBank: BankAccount,
    isConfirming: Boolean,
    isOffline: Boolean,
    onConfirmClick: () -> Unit,
    onEditClick: () -> Unit,
    onBankChangeClick: () -> Unit,
    onFeeInfoClick: () -> Unit,
    onBackClick: () -> Unit
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AuraBackground)
    ) {
        AuraTopBar(
            title = "Review sale",
            subtitle = "Verify all details before authorization",
            onBackClick = onBackClick,
            isOffline = isOffline
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(horizontal = 24.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                Spacer(modifier = Modifier.height(8.dp))

                // Hero Conversion Flow Card (Liquid Glass)
                GlassCard(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // USDT Source
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "Selling",
                                style = MaterialTheme.typography.labelLarge,
                                color = AuraTextMuted
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            with(sharedTransitionScope) {
                                Text(
                                    text = CurrencyFormatter.formatUsdt(quote.usdtAmount),
                                    style = MaterialTheme.typography.displayMedium,
                                    color = AuraTextPrimary,
                                    modifier = Modifier.sharedElement(
                                        rememberSharedContentState(key = "usdt_amount"),
                                        animatedVisibilityScope = animatedContentScope
                                    )
                                )
                            }
                        }

                        // Arrow separator
                        Box(
                            modifier = Modifier
                                .padding(vertical = 16.dp)
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(AuraSurfaceSecondary),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.ArrowDownward,
                                contentDescription = null,
                                tint = AuraEmerald,
                                modifier = Modifier.size(20.dp)
                            )
                        }

                        // INR Destination
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "You will receive in bank",
                                style = MaterialTheme.typography.labelLarge,
                                color = AuraTextMuted
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            with(sharedTransitionScope) {
                                Text(
                                    text = CurrencyFormatter.formatInr(quote.netInr, includeDecimalsIfAny = true),
                                    style = MaterialTheme.typography.displaySmall.copy(color = AuraEmerald),
                                    modifier = Modifier.sharedElement(
                                        rememberSharedContentState(key = "inr_amount"),
                                        animatedVisibilityScope = animatedContentScope
                                    )
                                )
                            }
                        }
                    }
                }

                // Itemized Details (Conversion & Settlement)
                FeeBreakdownCard(
                    quote = quote,
                    bankAccount = selectedBank,
                    onFeeInfoClick = onFeeInfoClick,
                    onBankChangeClick = onBankChangeClick
                )

                // Security Reinforcement
                GlassSurface(
                    shape = RoundedCornerShape(AuraTheme.Radius.l),
                    color = AuraGlassElevated,
                    modifier = Modifier.fillMaxWidth(),
                    shadowElevation = 2.dp
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Shield,
                            contentDescription = null,
                            tint = AuraEmerald,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = "Guaranteed rate reserved. Payout deposited into ${selectedBank.bankName}.",
                            style = MaterialTheme.typography.labelMedium,
                            color = AuraTextSecondary
                        )
                    }
                }
            }

            // Bottom Actions
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 48.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                AuraButton(
                    text = "Confirm Sale",
                    icon = Icons.Default.Fingerprint,
                    isLoading = isConfirming,
                    onClick = onConfirmClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(64.dp)
                        .testTag("review_confirm_sale_button")
                )

                AuraButton(
                    text = "Edit Amount",
                    icon = Icons.Default.Edit,
                    variant = AuraButtonVariant.GHOST,
                    onClick = onEditClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("review_edit_amount_button")
                )
            }
        }
    }
}
