package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.Brush
import com.example.ui.theme.AuraGlassBorder
import com.example.ui.theme.AuraLiquidGlass
import com.example.ui.theme.AuraShadow
import com.example.ui.theme.AuraSpecularHighlight
import com.example.ui.theme.AuraTheme

@Composable
fun GlassSurface(
    modifier: Modifier = Modifier,
    color: Color = AuraLiquidGlass,
    shape: Shape = RoundedCornerShape(AuraTheme.Radius.xxl),
    blur: Dp = 0.dp,
    showBorder: Boolean = true,
    shadowElevation: Dp = 4.dp,
    content: @Composable BoxScope.() -> Unit
) {
    val liquidBrush = Brush.linearGradient(
        colors = listOf(
            Color.White.copy(alpha = 0.4f),
            color,
            color.copy(alpha = 0.9f)
        )
    )

    Box(
        modifier = modifier
            .shadow(
                elevation = shadowElevation,
                shape = shape,
                clip = false,
                ambientColor = AuraShadow,
                spotColor = AuraShadow
            )
            .clip(shape)
            .background(liquidBrush)
            .then(
                if (showBorder) {
                    Modifier.border(0.5.dp, AuraGlassBorder, shape)
                } else Modifier
            )
    ) {
        // Subtle Specular Reflection Overlay
        Box(
            modifier = Modifier
                .matchParentSize()
                .border(
                    width = 1.dp,
                    brush = Brush.verticalGradient(
                        0.0f to AuraSpecularHighlight,
                        0.3f to Color.Transparent,
                        1.0f to Color.Transparent
                    ),
                    shape = shape
                )
        )
        content()
    }
}

@Composable
fun GlassCard(
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit
) {
    GlassSurface(
        modifier = modifier,
        color = AuraLiquidGlass.copy(alpha = 0.4f), // More transparent for inner cards
        shape = RoundedCornerShape(AuraTheme.Radius.xxl),
        shadowElevation = 1.dp,
        content = content
    )
}

@Composable
fun GlassInput(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String = "",
    modifier: Modifier = Modifier,
    singleLine: Boolean = true
) {
    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
            color = com.example.ui.theme.AuraTextSecondary,
            modifier = Modifier.padding(horizontal = 4.dp)
        )
        androidx.compose.material3.OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = { Text(text = placeholder, style = MaterialTheme.typography.bodyLarge, color = com.example.ui.theme.AuraTextMuted) },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(AuraTheme.Radius.l),
            singleLine = singleLine,
            colors = androidx.compose.material3.OutlinedTextFieldDefaults.colors(
                focusedBorderColor = com.example.ui.theme.AuraEmerald,
                unfocusedBorderColor = com.example.ui.theme.AuraBorder,
                focusedContainerColor = Color.White,
                unfocusedContainerColor = com.example.ui.theme.AuraSurfaceSecondary.copy(alpha = 0.5f),
                cursorColor = com.example.ui.theme.AuraEmerald,
                focusedTextColor = com.example.ui.theme.AuraTextPrimary,
                unfocusedTextColor = com.example.ui.theme.AuraTextPrimary,
                unfocusedLabelColor = com.example.ui.theme.AuraTextMuted,
                focusedLabelColor = com.example.ui.theme.AuraEmerald
            )
        )
    }
}
