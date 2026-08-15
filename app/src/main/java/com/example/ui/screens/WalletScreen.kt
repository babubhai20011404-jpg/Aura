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
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.model.BankAccount
import com.example.model.UserAccount
import com.example.ui.components.AuraButton
import com.example.ui.components.AuraTopBar
import com.example.ui.components.GlassCard
import com.example.ui.components.GlassSurface
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
import com.example.R

@Composable
fun WalletScreen(
    userAccount: UserAccount,
    bankAccounts: List<BankAccount>,
    currentRate: Double,
    onSellUsdtClick: () -> Unit,
    onReceiveClick: (String?) -> Unit,
    onManageBanksClick: () -> Unit,
    onCopyAddress: (String) -> Unit
) {
    var selectedNetwork by remember { mutableStateOf("Tron") }
    val tronAddress = "TX8a9Jk2LpNmQ4vW1Rz9PxY6tB3cD7eFgH"
    val solanaAddress = "7xKX3z12J...SolSample"

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AuraBackground)
    ) {
        AuraTopBar(
            title = "Wallet",
            subtitle = "Holdings & settlement accounts"
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(8.dp))

                // USDT Holding Card (Liquid Glass)
                GlassCard(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        verticalArrangement = Arrangement.spacedBy(20.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.Top
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(52.dp)
                                        .clip(CircleShape)
                                        .background(Color.Transparent),
                                    contentAlignment = Alignment.Center
                                ) {
                                    androidx.compose.foundation.Image(
                                        painter = androidx.compose.ui.res.painterResource(id = R.drawable.ic_usdt),
                                        contentDescription = "USDT",
                                        modifier = Modifier.fillMaxSize()
                                    )
                                }
                                Spacer(modifier = Modifier.width(16.dp))
                                Column {
                                    Text(text = "Tether", style = MaterialTheme.typography.titleMedium, color = AuraTextPrimary)
                                    Text(text = "USDT", style = MaterialTheme.typography.labelMedium, color = AuraTextMuted)
                                }
                            }
                            Column(horizontalAlignment = Alignment.End) {
                                Text(text = CurrencyFormatter.formatUsdt(userAccount.usdtBalance), style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold), color = AuraTextPrimary)
                                Text(text = "≈ ${CurrencyFormatter.formatInr(userAccount.usdtBalance * currentRate)}", style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold), color = AuraEmerald)
                            }
                        }

                        // Premium Network Selector
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Text(text = "Deposit Network", style = MaterialTheme.typography.labelSmall, color = AuraTextMuted)
                            GlassSurface(
                                modifier = Modifier.fillMaxWidth().height(48.dp),
                                color = AuraSurfaceSecondary,
                                shape = RoundedCornerShape(AuraTheme.Radius.l),
                                showBorder = false,
                                shadowElevation = 0.dp
                            ) {
                                Row(modifier = Modifier.padding(4.dp)) {
                                    NetworkSegment("Tron", selectedNetwork == "Tron", Modifier.weight(1f)) { selectedNetwork = "Tron" }
                                    NetworkSegment("Solana", selectedNetwork == "Solana", Modifier.weight(1f)) { selectedNetwork = "Solana" }
                                }
                            }
                        }

                        // Deposit Address
                        val activeAddress = if (selectedNetwork == "Tron") tronAddress else solanaAddress
                        GlassSurface(
                            shape = RoundedCornerShape(AuraTheme.Radius.l),
                            color = AuraGlassElevated,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onCopyAddress(activeAddress) },
                            shadowElevation = 2.dp
                        ) {
                            Row(
                                modifier = Modifier.padding(16.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(text = "$selectedNetwork Address", style = MaterialTheme.typography.labelSmall, color = AuraTextMuted)
                                    Text(text = activeAddress, style = MaterialTheme.typography.labelSmall, color = AuraTextSecondary)
                                }
                                Icon(imageVector = Icons.Default.ContentCopy, contentDescription = "Copy", tint = AuraEmerald, modifier = Modifier.size(16.dp))
                            }
                        }

                        AuraButton(
                            text = "Sell USDT",
                            onClick = onSellUsdtClick,
                            modifier = Modifier.weight(1f).height(56.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        AuraButton(
                            text = "Receive",
                            variant = com.example.ui.components.AuraButtonVariant.SECONDARY,
                            onClick = { onReceiveClick(null) },
                            modifier = Modifier.weight(1f).height(56.dp)
                        )
                    }
                }
            }

            // Settlement Accounts Section
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "Settlement accounts", style = MaterialTheme.typography.titleMedium, color = AuraTextPrimary)
                    Text(
                        text = "Manage",
                        style = MaterialTheme.typography.labelLarge.copy(color = AuraEmerald, fontWeight = FontWeight.Bold),
                        modifier = Modifier.clickable(onClick = onManageBanksClick)
                    )
                }
            }

            items(bankAccounts) { bank ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .clip(RoundedCornerShape(AuraTheme.Radius.m))
                            .background(AuraSurfaceSecondary),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(imageVector = Icons.Default.AccountBalance, contentDescription = null, tint = AuraTextSecondary, modifier = Modifier.size(20.dp))
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(text = bank.bankName, style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold), color = AuraTextPrimary)
                            if (bank.isPrimary) {
                                Spacer(modifier = Modifier.width(8.dp))
                                Icon(imageVector = Icons.Default.Verified, contentDescription = "Verified", tint = AuraEmerald, modifier = Modifier.size(14.dp))
                            }
                        }
                        Text(text = bank.accountNumberMasked, style = MaterialTheme.typography.labelMedium, color = AuraTextMuted)
                    }
                    if (bank.isPrimary) {
                        Text(text = "Primary", style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold), color = AuraEmerald)
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(100.dp))
            }
        }
    }
}

@Composable
private fun NetworkSegment(label: String, isSelected: Boolean, modifier: Modifier = Modifier, onClick: () -> Unit) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .clip(RoundedCornerShape(AuraTheme.Radius.m))
            .background(if (isSelected) Color.White else Color.Transparent)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium.copy(fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium),
            color = if (isSelected) AuraTextPrimary else AuraTextSecondary
        )
    }
}
