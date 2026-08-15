package com.example.ui.screens

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material.icons.filled.HeadsetMic
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.model.UserAccount
import com.example.ui.components.AuraTopBar
import com.example.ui.components.GlassSurface
import com.example.ui.theme.AuraBackground
import com.example.ui.theme.AuraBorderSubtle
import com.example.ui.theme.AuraEmerald
import com.example.ui.theme.AuraEmeraldMuted
import com.example.ui.theme.AuraSurfaceSecondary
import com.example.ui.theme.AuraTextMuted
import com.example.ui.theme.AuraTextPrimary
import com.example.ui.theme.AuraTextSecondary
import com.example.ui.theme.AuraTheme
import com.example.util.CurrencyFormatter

@Composable
fun ProfileScreen(
    userAccount: UserAccount,
    isOffline: Boolean,
    onToggleOffline: () -> Unit,
    onOpenRewards: () -> Unit,
    onOpenSetupJourney: () -> Unit,
    onOpenSupport: () -> Unit,
    onStartTour: () -> Unit
) {
    var biometricEnabled by remember { mutableStateOf(userAccount.biometricEnabled) }
    var twoFactorEnabled by remember { mutableStateOf(userAccount.twoFactorEnabled) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AuraBackground)
    ) {
        AuraTopBar(
            title = "Security & account",
            subtitle = "Manage your digital financial identity"
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(16.dp))

                // Profile Identity Header
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 24.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(72.dp)
                            .clip(CircleShape)
                            .background(AuraSurfaceSecondary),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = userAccount.name.take(1),
                            style = MaterialTheme.typography.displaySmall,
                            color = AuraEmerald
                        )
                    }
                    Spacer(modifier = Modifier.width(20.dp))
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = userAccount.name,
                                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                                color = AuraTextPrimary
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Icon(imageVector = Icons.Default.Verified, contentDescription = null, tint = AuraEmerald, modifier = Modifier.size(18.dp))
                        }
                        Text(text = userAccount.email, style = MaterialTheme.typography.bodyMedium, color = AuraTextSecondary)
                    }
                }
            }

            // Settlement Limit Progress Card
            item {
                GlassSurface(
                    shape = RoundedCornerShape(AuraTheme.Radius.xxl),
                    color = Color.White,
                    modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp),
                    shadowElevation = 2.dp
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text(text = "Settlement limit", style = MaterialTheme.typography.labelLarge, color = AuraTextSecondary)
                            Text(text = CurrencyFormatter.formatInr(userAccount.dailyLimitInr), style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold), color = AuraEmerald)
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        val fraction = (userAccount.dailyUsedInr / userAccount.dailyLimitInr).toFloat().coerceIn(0f, 1f)
                        Box(modifier = Modifier.fillMaxWidth().height(6.dp).clip(CircleShape).background(AuraSurfaceSecondary)) {
                            Box(modifier = Modifier.fillMaxWidth(fraction).height(6.dp).clip(CircleShape).background(AuraEmerald))
                        }
                        
                        Spacer(modifier = Modifier.height(12.dp))
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text(text = "Used: ${CurrencyFormatter.formatInr(userAccount.dailyUsedInr)}", style = MaterialTheme.typography.labelSmall, color = AuraTextMuted)
                            Text(text = "Remaining: ${CurrencyFormatter.formatInr(userAccount.dailyLimitInr - userAccount.dailyUsedInr)}", style = MaterialTheme.typography.labelSmall, color = AuraTextSecondary)
                        }
                    }
                }
            }

            // Sections
            item { ProfileSectionHeader("Account") }
            item { ProfileRow("Aura Rewards", "View your benefits", Icons.Default.AutoAwesome, onClick = onOpenRewards) }
            item { ProfileRow("Setup guide", "Complete your account", Icons.Default.History, onClick = onOpenSetupJourney) }
            item { ProfileRow("Identity verification", userAccount.kycLevel, Icons.Default.Shield) }
            item { ProfileRow("Phone number", userAccount.phoneMasked, Icons.Default.Person) }
            
            item { ProfileSectionHeader("Security") }
            item { 
                ProfileSwitchRow(
                    label = "Biometric authorization", 
                    subtitle = "Face ID / Fingerprint for sales", 
                    icon = Icons.Default.Fingerprint, 
                    checked = biometricEnabled, 
                    onCheckedChange = { 
                        biometricEnabled = it
                        // State persistence would happen via ViewModel
                    }
                ) 
            }
            item { 
                ProfileSwitchRow(
                    label = "2-Factor Authentication", 
                    subtitle = "Enhanced account protection", 
                    icon = Icons.Default.Lock, 
                    checked = twoFactorEnabled, 
                    onCheckedChange = { 
                        twoFactorEnabled = it
                    }
                ) 
            }
            
            item { ProfileSectionHeader("Support & Guidance") }
            item { ProfileRow("Priority support chat", "24/7 specialist access", Icons.Default.HeadsetMic, onClick = onOpenSupport) }
            item { ProfileRow("Take app tour", "Explore important parts", Icons.Default.PlayArrow, onClick = onStartTour) }
            item { ProfileRow("Terms & privacy", "Legal disclosures", Icons.Default.Description, onClick = {}) }
            
            item { 
                ProfileRow(
                    label = "App Mode", 
                    value = if (isOffline) "Simulation Mode" else "Live Mode", 
                    icon = Icons.Default.Security, 
                    onClick = onToggleOffline 
                ) 
            }
        }
    }
}

@Composable
private fun ProfileSectionHeader(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
        color = AuraTextMuted,
        modifier = Modifier.padding(top = 32.dp, bottom = 8.dp)
    )
}

@Composable
private fun ProfileRow(
    label: String, 
    value: String, 
    icon: ImageVector,
    onClick: (() -> Unit)? = null
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(enabled = onClick != null) { onClick?.invoke() }
            .padding(vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier.size(40.dp).clip(CircleShape).background(AuraSurfaceSecondary),
                contentAlignment = Alignment.Center
            ) {
                Icon(imageVector = icon, contentDescription = null, tint = AuraTextSecondary, modifier = Modifier.size(20.dp))
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(text = label, style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Medium), color = AuraTextPrimary)
                Text(text = value, style = MaterialTheme.typography.labelMedium, color = AuraTextMuted)
            }
        }
        Icon(imageVector = Icons.Default.ChevronRight, contentDescription = null, tint = AuraTextMuted, modifier = Modifier.size(20.dp))
    }
    HorizontalDivider(color = AuraBorderSubtle, thickness = 0.5.dp)
}

@Composable
private fun ProfileSwitchRow(
    label: String,
    subtitle: String,
    icon: ImageVector,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier.size(40.dp).clip(CircleShape).background(AuraSurfaceSecondary),
                contentAlignment = Alignment.Center
            ) {
                Icon(imageVector = icon, contentDescription = null, tint = AuraTextSecondary, modifier = Modifier.size(20.dp))
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(text = label, style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Medium), color = AuraTextPrimary)
                Text(text = subtitle, style = MaterialTheme.typography.labelMedium, color = AuraTextMuted)
            }
        }
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                checkedTrackColor = AuraEmerald,
                uncheckedThumbColor = Color.White,
                uncheckedTrackColor = AuraSurfaceSecondary,
                uncheckedBorderColor = Color.Transparent
            )
        )
    }
    HorizontalDivider(color = AuraBorderSubtle, thickness = 0.5.dp)
}
