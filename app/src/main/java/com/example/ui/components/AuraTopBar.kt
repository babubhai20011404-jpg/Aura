package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.WifiOff
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.ui.theme.AuraAmber
import com.example.ui.theme.AuraAmberSurface
import com.example.ui.theme.AuraBackground
import com.example.ui.theme.AuraBorder
import com.example.ui.theme.AuraLiquidGlass
import com.example.ui.theme.AuraTextMuted
import com.example.ui.theme.AuraTextPrimary
import com.example.ui.theme.AuraTheme

@Composable
fun AuraTopBar(
    title: String,
    subtitle: String? = null,
    onBackClick: (() -> Unit)? = null,
    showSecurityBadge: Boolean = false,
    isOffline: Boolean = false,
    actions: @Composable (() -> Unit)? = null
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(AuraBackground)
            .statusBarsPadding()
    ) {
        if (isOffline) {
            Surface(
                color = AuraAmberSurface,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = AuraTheme.Spacing.xl, vertical = AuraTheme.Spacing.xs)
                    .clip(RoundedCornerShape(AuraTheme.Radius.m))
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(imageVector = Icons.Default.WifiOff, contentDescription = null, tint = AuraAmber, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(AuraTheme.Spacing.m))
                    Text(text = "Connection unstable · Live rates paused", style = MaterialTheme.typography.labelMedium, color = AuraAmber)
                }
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = AuraTheme.Spacing.xl, vertical = AuraTheme.Spacing.l),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                if (onBackClick != null) {
                    GlassSurface(
                        modifier = Modifier
                            .size(44.dp)
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null,
                                onClick = onBackClick
                            )
                            .testTag("top_bar_back_button"),
                        color = AuraLiquidGlass,
                        shape = RoundedCornerShape(14.dp), // Soft droplet back button
                        shadowElevation = 1.dp
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack, 
                            contentDescription = "Back", 
                            tint = AuraTextPrimary, 
                            modifier = Modifier.size(20.dp).align(Alignment.Center)
                        )
                    }
                    Spacer(modifier = Modifier.width(AuraTheme.Spacing.xl))
                }

                Column {
                    Text(
                        text = title, 
                        style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold), 
                        color = AuraTextPrimary
                    )
                    if (!subtitle.isNullOrBlank()) {
                        Text(
                            text = subtitle, 
                            style = MaterialTheme.typography.labelMedium, 
                            color = AuraTextMuted
                        )
                    }
                }
            }

            if (actions != null) {
                actions()
            }
        }
        
        // Subtle environment definition line
        androidx.compose.material3.HorizontalDivider(
            modifier = Modifier.fillMaxWidth(),
            thickness = 0.5.dp,
            color = AuraBorder.copy(alpha = 0.5f)
        )
    }
}
