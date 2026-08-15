package com.example.ui.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.CircularProgressIndicator
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
import com.example.ui.theme.AuraEmerald
import com.example.ui.theme.AuraLiquidGlass
import com.example.ui.theme.AuraSurfaceSecondary
import com.example.ui.theme.AuraTextMuted
import com.example.ui.theme.AuraTextPrimary
import com.example.ui.theme.AuraTheme
import com.example.util.CurrencyFormatter

@Composable
fun QuotePill(
    rate: Double,
    secondsRemaining: Int,
    isUpdating: Boolean = false,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    GlassSurface(
        modifier = modifier
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick
            )
            .testTag("quote_pill_card"),
        color = AuraLiquidGlass,
        shape = RoundedCornerShape(AuraTheme.Radius.pill),
        shadowElevation = 1.dp
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // Tiny Live Indicator with Glow
            Box(
                modifier = Modifier
                    .size(6.dp)
                    .clip(CircleShape)
                    .background(AuraEmerald)
            )

            Text(
                text = "Live rate",
                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Medium),
                color = AuraTextMuted
            )

            AnimatedContent(
                targetState = rate,
                transitionSpec = {
                    fadeIn() togetherWith fadeOut()
                },
                label = "rate_text"
            ) { targetRate ->
                Text(
                    text = "${CurrencyFormatter.formatRate(targetRate)} / USDT",
                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                    color = AuraTextPrimary
                )
            }

            Box(modifier = Modifier.size(width = 0.5.dp, height = 12.dp).background(AuraTextMuted.copy(alpha = 0.3f)))

            if (isUpdating) {
                CircularProgressIndicator(
                    modifier = Modifier.size(10.dp),
                    strokeWidth = 1.5.dp,
                    color = AuraEmerald
                )
            } else {
                Text(
                    text = "${30 - secondsRemaining}s",
                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                    color = AuraEmerald
                )
            }

            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = null,
                tint = AuraTextMuted,
                modifier = Modifier.size(14.dp)
            )
        }
    }
}
