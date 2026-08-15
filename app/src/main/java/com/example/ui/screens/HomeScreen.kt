package com.example.ui.screens

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Security
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.BankAccount
import com.example.model.SetupTask
import com.example.model.TaskStatus
import com.example.model.Transaction
import com.example.model.UserAccount
import androidx.compose.material.icons.filled.AutoAwesome
import com.example.ui.components.AuraButton
import com.example.ui.components.GlassCard
import com.example.ui.components.GlassSurface
import com.example.ui.components.QuotePill
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
fun HomeScreen(
    userAccount: UserAccount,
    currentRate: Double,
    quoteTimerSeconds: Int,
    isRateUpdating: Boolean,
    recentTransactions: List<Transaction>,
    selectedBank: BankAccount,
    isOffline: Boolean,
    nextTask: SetupTask?,
    onSellUsdtClick: () -> Unit,
    onReceiveClick: () -> Unit,
    onRateDetailsClick: () -> Unit,
    onTransactionClick: (Transaction) -> Unit,
    onViewAllActivityClick: () -> Unit,
    onManageBanksClick: () -> Unit,
    onSetupJourneyClick: () -> Unit,
    onRewardsClick: () -> Unit,
    onSecurityClick: () -> Unit = {}
) {
    val estimatedTotalInr = userAccount.usdtBalance * currentRate

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(AuraBackground)
            .padding(horizontal = 24.dp),
        verticalArrangement = Arrangement.spacedBy(AuraTheme.Spacing.xxl)
    ) {
        item {
            Spacer(modifier = Modifier.height(32.dp))
            // Top Header: Spacious & Calm
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column {
                    Text(
                        text = "Good morning,",
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontWeight = FontWeight.Normal,
                            color = AuraTextSecondary
                        )
                    )
                    Text(
                        text = userAccount.name.substringBefore(" "),
                        style = MaterialTheme.typography.displaySmall.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = AuraTextPrimary
                    )
                }

                QuotePill(
                    rate = currentRate,
                    secondsRemaining = quoteTimerSeconds,
                    isUpdating = isRateUpdating,
                    onClick = onRateDetailsClick
                )
            }
        }

        // Hero Balance Area
        item {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = "Available balance",
                    style = MaterialTheme.typography.labelLarge,
                    color = AuraTextMuted
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.Bottom) {
                    Text(
                        text = CurrencyFormatter.formatUsdt(userAccount.usdtBalance).substringBefore(" "),
                        style = MaterialTheme.typography.displayLarge,
                        color = AuraTextPrimary
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "USDT",
                        style = MaterialTheme.typography.headlineMedium,
                        color = AuraTextSecondary,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                }
                Text(
                    text = "≈ ${CurrencyFormatter.formatInr(estimatedTotalInr)}",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.SemiBold,
                        color = AuraEmerald
                    )
                )

                Spacer(modifier = Modifier.height(32.dp))

                // Primary Hero Action: SELL USDT (Liquid Glass Style)
                AuraButton(
                    text = "Sell USDT",
                    onClick = onSellUsdtClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(64.dp)
                        .testTag("home_sell_usdt_cta"),
                    trailingIcon = Icons.AutoMirrored.Filled.ArrowForward
                )
            }
        }

        // Quick Actions: Compact Rounded Liquid Glass Controls
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                QuickActionItem(
                    icon = Icons.Default.Add, 
                    label = "Receive", 
                    onClick = { onReceiveClick() } 
                )
                QuickActionItem(
                    icon = Icons.Default.ArrowUpward, 
                    label = "Send", 
                    onClick = { onSellUsdtClick() }
                )
                QuickActionItem(
                    icon = Icons.Default.AccountBalance, 
                    label = "Banks", 
                    onClick = onManageBanksClick
                )
                QuickActionItem(
                    icon = Icons.Default.History, 
                    label = "Activity", 
                    onClick = onViewAllActivityClick
                )
            }
        }

        // Setup Journey Contextual Card
        item {
            if (nextTask != null) {
                SetupJourneyHomeCard(
                    task = nextTask,
                    onClick = onSetupJourneyClick
                )
            } else {
                RewardsHomeCard(onClick = onRewardsClick)
            }
        }

        // Recent Settlements Section
        item {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Recent settlements",
                        style = MaterialTheme.typography.titleMedium,
                        color = AuraTextPrimary
                    )
                    Text(
                        text = "See all",
                        style = MaterialTheme.typography.labelLarge.copy(
                            fontWeight = FontWeight.SemiBold,
                            color = AuraEmerald
                        ),
                        modifier = Modifier.clickable(onClick = onViewAllActivityClick)
                    )
                }
                
                Spacer(modifier = Modifier.height(AuraTheme.Spacing.l))

                if (recentTransactions.isEmpty()) {
                    EmptySettlements()
                }
            }
        }

        items(recentTransactions.take(3)) { txn ->
            TransactionRow(
                transaction = txn,
                onClick = { onTransactionClick(txn) }
            )
        }

        item {
            Spacer(modifier = Modifier.height(100.dp)) // Safe area for floating nav
        }
    }
}

@Composable
private fun QuickActionItem(
    icon: ImageVector,
    label: String,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier
            .clip(RoundedCornerShape(AuraTheme.Radius.l))
            .clickable(onClick = onClick)
            .padding(8.dp)
    ) {
        GlassSurface(
            modifier = Modifier.size(56.dp),
            color = AuraGlassElevated,
            shape = CircleShape,
            shadowElevation = 2.dp
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = AuraTextPrimary,
                modifier = Modifier.size(24.dp).align(Alignment.Center)
            )
        }
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Medium),
            color = AuraTextSecondary
        )
    }
}

@Composable
private fun TransactionRow(
    transaction: Transaction,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.weight(1f)
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(AuraTheme.Radius.m))
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
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(
                    text = "USDT Sale",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                    color = AuraTextPrimary
                )
                Text(
                    text = "${transaction.settlementBankName} · ${CurrencyFormatter.formatTransactionTime(transaction.createdAtMillis)}",
                    style = MaterialTheme.typography.labelMedium,
                    color = AuraTextMuted
                )
            }
        }

        Column(horizontalAlignment = Alignment.End) {
            Text(
                text = "+ ${CurrencyFormatter.formatInr(transaction.netInr, includeDecimalsIfAny = true)}",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = AuraEmerald
                )
            )
            Text(
                text = CurrencyFormatter.formatUsdt(transaction.usdtAmount),
                style = MaterialTheme.typography.labelSmall,
                color = AuraTextMuted
            )
        }
    }
}

@Composable
private fun SetupJourneyHomeCard(task: SetupTask, onClick: () -> Unit) {
    GlassSurface(
        shape = RoundedCornerShape(AuraTheme.Radius.xxl),
        color = Color.White,
        modifier = Modifier.fillMaxWidth().clickable(onClick = onClick),
        shadowElevation = 2.dp
    ) {
        Row(
            modifier = Modifier.padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier.size(48.dp).clip(CircleShape).background(AuraEmerald.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(imageVector = Icons.Default.AutoAwesome, contentDescription = null, tint = AuraEmerald, modifier = Modifier.size(24.dp))
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(text = "Finish setting up Aura", style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold), color = AuraTextPrimary)
                Text(text = task.title, style = MaterialTheme.typography.bodySmall, color = AuraTextSecondary)
            }
            Icon(imageVector = Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null, tint = AuraEmerald, modifier = Modifier.size(20.dp))
        }
    }
}

@Composable
private fun RewardsHomeCard(onClick: () -> Unit) {
    GlassSurface(
        shape = RoundedCornerShape(AuraTheme.Radius.xxl),
        color = Color.White,
        modifier = Modifier.fillMaxWidth().clickable(onClick = onClick),
        shadowElevation = 2.dp
    ) {
        Row(
            modifier = Modifier.padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier.size(48.dp).clip(CircleShape).background(AuraEmerald.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(imageVector = Icons.Default.AutoAwesome, contentDescription = null, tint = AuraEmerald, modifier = Modifier.size(24.dp))
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(text = "Aura Rewards", style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold), color = AuraTextPrimary)
                Text(text = "View your achievements", style = MaterialTheme.typography.bodySmall, color = AuraTextSecondary)
            }
            Icon(imageVector = Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null, tint = AuraEmerald, modifier = Modifier.size(20.dp))
        }
    }
}

@Composable
private fun EmptySettlements() {
    GlassCard(
        modifier = Modifier.fillMaxWidth().height(120.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "No settlements yet",
                style = MaterialTheme.typography.titleSmall,
                color = AuraTextMuted
            )
            Text(
                text = "Your USDT → INR sales will appear here.",
                style = MaterialTheme.typography.labelSmall,
                color = AuraTextMuted
            )
        }
    }
}
