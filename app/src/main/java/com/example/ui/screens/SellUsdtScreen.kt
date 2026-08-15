package com.example.ui.screens

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.BankAccount
import com.example.model.Quote
import com.example.model.UserAccount
import com.example.ui.components.AuraButton
import com.example.ui.components.AuraKeypad
import com.example.ui.components.AuraTopBar
import com.example.ui.components.GlassSurface
import com.example.ui.components.QuickPillsRow
import com.example.ui.components.QuotePill
import com.example.ui.theme.AuraBackground
import com.example.ui.theme.AuraEmerald
import com.example.ui.theme.AuraGlassElevated
import com.example.ui.theme.AuraRose
import com.example.ui.theme.AuraRoseSurface
import com.example.ui.theme.AuraSurfaceSecondary
import com.example.ui.theme.AuraTextMuted
import com.example.ui.theme.AuraTextPrimary
import com.example.ui.theme.AuraTextSecondary
import com.example.ui.theme.AuraTheme
import com.example.util.CurrencyFormatter

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SellUsdtScreen(
    sharedTransitionScope: SharedTransitionScope,
    animatedContentScope: AnimatedContentScope,
    userAccount: UserAccount,
    quote: Quote,
    inputAmountString: String,
    selectedBank: BankAccount,
    validationError: String?,
    isOffline: Boolean,
    onKeyPressed: (String) -> Unit,
    onPercentClick: (Int) -> Unit,
    onContinueClick: () -> Unit,
    onRateDetailsClick: () -> Unit,
    onFeeDetailsClick: () -> Unit,
    onBackClick: () -> Unit
) {
    val scrollState = rememberScrollState()
    val isContinueEnabled = validationError == null && (inputAmountString.toDoubleOrNull() ?: 0.0) >= 10.0

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AuraBackground)
    ) {
        AuraTopBar(
            title = "Sell USDT",
            subtitle = "Convert your USDT to INR",
            onBackClick = onBackClick,
            isOffline = isOffline
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(AuraTheme.Spacing.l))

            // Available Balance Indicator
            Row(
                modifier = Modifier
                    .clip(CircleShape)
                    .background(AuraSurfaceSecondary)
                    .clickable { onPercentClick(100) }
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Available: ",
                    style = MaterialTheme.typography.labelMedium,
                    color = AuraTextMuted
                )
                Text(
                    text = CurrencyFormatter.formatUsdt(userAccount.usdtBalance),
                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                    color = AuraTextPrimary
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "MAX",
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.Black,
                        fontSize = 10.sp
                    ),
                    color = AuraEmerald
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Hero Amount Input Surface
            GlassSurface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp),
                color = AuraGlassElevated,
                shape = RoundedCornerShape(AuraTheme.Radius.xxl),
                shadowElevation = 8.dp
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 40.dp, horizontal = 24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        verticalAlignment = Alignment.Bottom,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        AnimatedContent(
                            targetState = inputAmountString,
                            transitionSpec = {
                                fadeIn() togetherWith fadeOut()
                            },
                            label = "amount_display"
                        ) { amountText ->
                            with(sharedTransitionScope) {
                                Text(
                                    text = if (amountText.isBlank()) "0" else amountText,
                                    style = MaterialTheme.typography.displayLarge,
                                    color = AuraTextPrimary,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier
                                        .sharedElement(
                                            rememberSharedContentState(key = "usdt_amount"),
                                            animatedVisibilityScope = animatedContentScope
                                        )
                                        .testTag("sell_usdt_amount_input_text")
                                )
                            }
                        }
                        Spacer(modifier = Modifier.width(AuraTheme.Spacing.s))
                        Text(
                            text = "USDT",
                            style = MaterialTheme.typography.headlineMedium,
                            color = AuraTextSecondary,
                            modifier = Modifier.padding(bottom = 12.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // You receive: INR Display
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "You receive ",
                            style = MaterialTheme.typography.bodyLarge,
                            color = AuraTextMuted
                        )
                        
                        val animatedInr by animateFloatAsState(
                            targetValue = quote.netInr.toFloat(),
                            animationSpec = tween(durationMillis = 400),
                            label = "inr_counter"
                        )

                        with(sharedTransitionScope) {
                            Text(
                                text = CurrencyFormatter.formatInr(animatedInr.toDouble(), includeDecimalsIfAny = true),
                                style = MaterialTheme.typography.displaySmall.copy(color = AuraEmerald),
                                modifier = Modifier
                                    .sharedElement(
                                        rememberSharedContentState(key = "inr_amount"),
                                        animatedVisibilityScope = animatedContentScope
                                    )
                                    .testTag("sell_usdt_inr_receive_text")
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Live Rate Component
            QuotePill(
                rate = quote.ratePerUsdt,
                secondsRemaining = quote.secondsRemaining,
                onClick = onRateDetailsClick
            )

            // Validation Warning (Calm)
            AnimatedVisibility(
                visible = validationError != null,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                if (validationError != null) {
                    Surface(
                        shape = RoundedCornerShape(AuraTheme.Radius.m),
                        color = AuraRoseSurface,
                        modifier = Modifier.padding(vertical = 16.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Warning,
                                contentDescription = null,
                                tint = AuraRose,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = validationError,
                                style = MaterialTheme.typography.labelMedium,
                                color = AuraRose
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Floating Glass Pills for Percentages
            QuickPillsRow(onPercentClick = onPercentClick)

            Spacer(modifier = Modifier.height(32.dp))

            // Premium Keypad
            AuraKeypad(onKeyPressed = onKeyPressed)

            Spacer(modifier = Modifier.height(32.dp))

            // Fee Transparency Link
            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(AuraTheme.Radius.m))
                    .clickable { onFeeDetailsClick() }
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Info,
                    contentDescription = null,
                    tint = AuraTextSecondary,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "View transparent fee breakdown",
                    style = MaterialTheme.typography.labelMedium,
                    color = AuraTextSecondary
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Primary CTA: Continue
            AuraButton(
                text = "Continue",
                enabled = isContinueEnabled,
                onClick = onContinueClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp)
                    .testTag("sell_usdt_continue_button")
            )
            
            Spacer(modifier = Modifier.height(48.dp))
        }
    }
}
