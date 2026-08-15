package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
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
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.QrCode2
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.DepositAddress
import com.example.model.DepositAsset
import com.example.model.DepositNetwork
import com.example.ui.components.AuraSkeleton
import com.example.ui.components.AuraTopBar
import com.example.ui.components.GlassSurface
import com.example.ui.theme.AuraBackground
import com.example.ui.theme.AuraBorder
import com.example.ui.theme.AuraBorderSubtle
import com.example.ui.theme.AuraEmerald
import com.example.ui.theme.AuraGlassElevated
import com.example.ui.theme.AuraRose
import com.example.ui.theme.AuraRoseSurface
import com.example.ui.theme.AuraSurfaceSecondary
import com.example.ui.theme.AuraTextMuted
import com.example.ui.theme.AuraTextPrimary
import com.example.ui.theme.AuraTextSecondary
import com.example.ui.theme.AuraTheme
import com.example.ui.components.AuraButton
import com.example.R

@Composable
fun ReceiveScreen(
    selectedAsset: DepositAsset?,
    selectedNetwork: DepositNetwork?,
    depositAddress: DepositAddress?,
    isLoading: Boolean,
    onAssetSelect: (DepositAsset) -> Unit,
    onNetworkSelect: (DepositNetwork) -> Unit,
    onCopyAddress: (String) -> Unit,
    onShareAddress: (String) -> Unit,
    onBackClick: () -> Unit,
    networks: List<DepositNetwork>,
    showCoachMark: Boolean,
    onDismissCoachMark: () -> Unit
) {
    val scrollState = rememberScrollState()

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(AuraBackground)
        ) {
            AuraTopBar(
                title = "Receive crypto",
                subtitle = "Deposit USDT or USDC to your wallet",
                onBackClick = onBackClick
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
                    .padding(horizontal = 24.dp)
            ) {
                Spacer(modifier = Modifier.height(16.dp))

                // Asset Selection
                Text(text = "Select asset", style = MaterialTheme.typography.labelLarge, color = AuraTextMuted)
                Spacer(modifier = Modifier.height(12.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    AssetPill("USDT", selectedAsset?.symbol == "USDT", R.drawable.ic_usdt) { 
                        onAssetSelect(DepositAsset("USDT", "Tether", R.drawable.ic_usdt)) 
                    }
                    AssetPill("USDC", selectedAsset?.symbol == "USDC", R.drawable.ic_usdc) { 
                        onAssetSelect(DepositAsset("USDC", "USD Coin", R.drawable.ic_usdc)) 
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                // Network Selection
                Text(text = "Select network", style = MaterialTheme.typography.labelLarge, color = AuraTextMuted)
                Spacer(modifier = Modifier.height(12.dp))
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    networks.forEach { network ->
                        NetworkRow(
                            network = network,
                            isSelected = selectedNetwork?.id == network.id,
                            onClick = { onNetworkSelect(network) }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                // Deposit Area
                AnimatedVisibility(
                    visible = selectedNetwork != null,
                    enter = fadeIn(),
                    exit = fadeOut()
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        // Warning Pill
                        Surface(
                            shape = RoundedCornerShape(AuraTheme.Radius.m),
                            color = AuraRoseSurface,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(Icons.Default.Warning, contentDescription = null, tint = AuraRose, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "Send only ${selectedAsset?.symbol} via ${selectedNetwork?.name} network.",
                                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                    color = AuraRose
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(24.dp))

                        // QR & Address Glass Surface
                        GlassSurface(
                            shape = RoundedCornerShape(AuraTheme.Radius.xxl),
                            color = AuraGlassElevated,
                            modifier = Modifier.fillMaxWidth(),
                            shadowElevation = 8.dp
                        ) {
                            Column(
                                modifier = Modifier.padding(24.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                if (isLoading) {
                                    AuraSkeleton(modifier = Modifier.size(200.dp), cornerRadius = 16.dp)
                                    Spacer(modifier = Modifier.height(24.dp))
                                    AuraSkeleton(modifier = Modifier.fillMaxWidth().height(48.dp), cornerRadius = 12.dp)
                                } else if (depositAddress != null) {
                                    // QR Placeholder
                                    Box(
                                        modifier = Modifier
                                            .size(200.dp)
                                            .background(Color.White, RoundedCornerShape(16.dp))
                                            .padding(16.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.QrCode2,
                                            contentDescription = null,
                                            modifier = Modifier.fillMaxSize(),
                                            tint = Color.Black
                                        )
                                    }
                                    
                                    Spacer(modifier = Modifier.height(24.dp))
                                    
                                    Text(
                                        text = "Your ${selectedNetwork?.name} address",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = AuraTextMuted
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(
                                        text = depositAddress.address,
                                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                                        color = AuraTextPrimary,
                                        textAlign = TextAlign.Center
                                    )
                                    
                                    Spacer(modifier = Modifier.height(24.dp))
                                    
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .weight(1f)
                                                .height(48.dp)
                                                .clip(RoundedCornerShape(AuraTheme.Radius.m))
                                                .background(AuraEmerald.copy(alpha = 0.1f))
                                                .clickable { onCopyAddress(depositAddress.address) },
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Row(verticalAlignment = Alignment.CenterVertically) {
                                                Icon(Icons.Default.ContentCopy, contentDescription = null, tint = AuraEmerald, modifier = Modifier.size(16.dp))
                                                Spacer(modifier = Modifier.width(8.dp))
                                                Text(text = "Copy", color = AuraEmerald, fontWeight = FontWeight.Bold)
                                            }
                                        }
                                        
                                        Box(
                                            modifier = Modifier
                                                .size(48.dp)
                                                .clip(RoundedCornerShape(AuraTheme.Radius.m))
                                                .background(AuraSurfaceSecondary)
                                                .clickable { onShareAddress(depositAddress.address) },
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Icon(Icons.Default.Share, contentDescription = null, tint = AuraTextPrimary, modifier = Modifier.size(18.dp))
                                        }
                                    }
                                }
                            }
                        }
                        
                        if (selectedNetwork?.minDeposit != null) {
                            Spacer(modifier = Modifier.height(24.dp))
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text(text = "Minimum deposit", style = MaterialTheme.typography.bodySmall, color = AuraTextMuted)
                                Text(text = selectedNetwork.minDeposit, style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold), color = AuraTextSecondary)
                            }
                        }
                    }
                }
                
                Spacer(modifier = Modifier.height(80.dp))
            }
        }

        // Coach Mark Overlay
        AnimatedVisibility(
            visible = showCoachMark,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.7f))
                    .clickable(onClick = onDismissCoachMark),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(24.dp)
                ) {
                    CoachMarkItem("Choose asset and network", "Select the crypto and network you are receiving.")
                    CoachMarkItem("Copy or scan", "Use the address or QR code to deposit from another app.")
                    CoachMarkItem("Important", "Only send the selected asset using the selected network.")
                    
                    AuraButton(
                        text = "Got it",
                        onClick = onDismissCoachMark,
                        modifier = Modifier.width(160.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun CoachMarkItem(title: String, description: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = title, style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold), color = Color.White)
        Text(text = description, style = MaterialTheme.typography.bodyMedium, color = Color.White.copy(alpha = 0.8f), textAlign = TextAlign.Center)
    }
}

@Composable
private fun RowScope.AssetPill(label: String, isSelected: Boolean, iconRes: Int, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .weight(1f)
            .height(52.dp)
            .clip(RoundedCornerShape(AuraTheme.Radius.m))
            .background(if (isSelected) AuraEmerald.copy(alpha = 0.1f) else AuraSurfaceSecondary)
            .border(
                width = if (isSelected) 2.dp else 0.dp,
                color = if (isSelected) AuraEmerald else Color.Transparent,
                shape = RoundedCornerShape(AuraTheme.Radius.m)
            )
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            androidx.compose.foundation.Image(
                painter = androidx.compose.ui.res.painterResource(id = iconRes),
                contentDescription = label,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = label,
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = if (isSelected) AuraEmerald else AuraTextSecondary
            )
        }
    }
}

@Composable
private fun NetworkRow(network: DepositNetwork, isSelected: Boolean, onClick: () -> Unit) {
    Surface(
        shape = RoundedCornerShape(AuraTheme.Radius.l),
        color = if (isSelected) AuraEmerald.copy(alpha = 0.05f) else Color.Transparent,
        border = if (isSelected) androidx.compose.foundation.BorderStroke(1.dp, AuraEmerald) else androidx.compose.foundation.BorderStroke(1.dp, AuraBorderSubtle),
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(text = network.name, style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold), color = AuraTextPrimary)
                Text(text = network.protocol, style = MaterialTheme.typography.labelSmall, color = AuraTextMuted)
            }
            if (network.confirmationTime != null) {
                Text(text = network.confirmationTime, style = MaterialTheme.typography.labelSmall, color = AuraTextSecondary)
            }
        }
    }
}
