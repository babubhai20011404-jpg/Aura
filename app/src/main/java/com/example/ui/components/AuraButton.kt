package com.example.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.ui.theme.AuraEmerald
import com.example.ui.theme.AuraEmeraldDark
import com.example.ui.theme.AuraLiquidGlass
import com.example.ui.theme.AuraRose
import com.example.ui.theme.AuraRoseSurface
import com.example.ui.theme.AuraShadow
import com.example.ui.theme.AuraTextDisabled
import com.example.ui.theme.AuraTextPrimary
import com.example.ui.theme.AuraTheme
import com.example.ui.theme.AuraWhite

enum class AuraButtonVariant {
    PRIMARY,
    SECONDARY,
    OUTLINED,
    GHOST,
    DANGER
}

@Composable
fun AuraButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    variant: AuraButtonVariant = AuraButtonVariant.PRIMARY,
    icon: ImageVector? = null,
    trailingIcon: ImageVector? = null,
    isLoading: Boolean = false,
    enabled: Boolean = true,
    height: Dp = 56.dp,
    testTag: String = "aura_button"
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val haptic = LocalHapticFeedback.current

    val scale by animateFloatAsState(
        targetValue = if (isPressed && enabled && !isLoading) 0.96f else 1.0f,
        animationSpec = spring(dampingRatio = 0.7f, stiffness = 500f),
        label = "button_scale"
    )

    val buttonBrush = when (variant) {
        AuraButtonVariant.PRIMARY -> {
            if (enabled) Brush.linearGradient(listOf(AuraEmerald, AuraEmeraldDark))
            else Brush.linearGradient(listOf(AuraLiquidGlass, AuraLiquidGlass))
        }
        AuraButtonVariant.SECONDARY -> Brush.linearGradient(listOf(Color.White.copy(alpha = 0.9f), AuraLiquidGlass))
        else -> Brush.linearGradient(listOf(Color.Transparent, Color.Transparent))
    }

    val contentColor = when (variant) {
        AuraButtonVariant.PRIMARY -> if (enabled) AuraWhite else AuraTextDisabled
        AuraButtonVariant.SECONDARY -> if (enabled) AuraTextPrimary else AuraTextDisabled
        AuraButtonVariant.OUTLINED -> if (enabled) AuraTextPrimary else AuraTextDisabled
        AuraButtonVariant.GHOST -> if (enabled) AuraEmerald else AuraTextDisabled
        AuraButtonVariant.DANGER -> if (enabled) AuraWhite else AuraTextDisabled
    }

    val shape = RoundedCornerShape(AuraTheme.Radius.xxl)

    Surface(
        modifier = modifier
            .scale(scale)
            .height(height)
            .clip(shape)
            .shadow(
                elevation = if (variant == AuraButtonVariant.PRIMARY && enabled) 6.dp else 0.dp,
                shape = shape,
                ambientColor = AuraShadow,
                spotColor = AuraShadow
            )
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                enabled = enabled && !isLoading,
                onClick = {
                    haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                    onClick()
                }
            )
            .testTag(testTag),
        shape = shape,
        color = Color.Transparent
    ) {
        Box(
            modifier = Modifier
                .background(buttonBrush)
                .padding(horizontal = AuraTheme.Spacing.xxl),
            contentAlignment = Alignment.Center
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    strokeWidth = 3.dp,
                    color = contentColor
                )
            } else {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (icon != null) {
                        Icon(
                            imageVector = icon,
                            contentDescription = null,
                            tint = contentColor,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(AuraTheme.Spacing.m))
                    }
                    Text(
                        text = text,
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = contentColor
                    )
                    if (trailingIcon != null) {
                        Spacer(modifier = Modifier.width(AuraTheme.Spacing.m))
                        Icon(
                            imageVector = trailingIcon,
                            contentDescription = null,
                            tint = contentColor,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
        }
    }
}
