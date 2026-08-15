package com.example.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.unit.sp
import com.example.ui.theme.AuraEmerald
import com.example.ui.theme.AuraGlassElevated
import com.example.ui.theme.AuraTextSecondary
import com.example.ui.theme.AuraTheme

@Composable
fun QuickPillsRow(
    onPercentClick: (Int) -> Unit,
    selectedPercent: Int? = null,
    modifier: Modifier = Modifier
) {
    val items = listOf(
        Pair("25%", 25),
        Pair("50%", 50),
        Pair("75%", 75),
        Pair("Max", 100)
    )

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items.forEach { (label, percent) ->
            val isSelected = selectedPercent == percent
            
            GlassSurface(
                modifier = Modifier
                    .weight(1f)
                    .height(44.dp)
                    .clip(RoundedCornerShape(AuraTheme.Radius.pill))
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = { onPercentClick(percent) }
                    )
                    .testTag("quick_pill_$label"),
                color = if (isSelected) AuraEmerald.copy(alpha = 0.15f) else AuraGlassElevated,
                shape = RoundedCornerShape(AuraTheme.Radius.pill),
                shadowElevation = if (isSelected) 0.dp else 2.dp,
                showBorder = isSelected
            ) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(
                        text = label,
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            fontSize = 14.sp
                        ),
                        color = if (isSelected) AuraEmerald else AuraTextSecondary
                    )
                }
            }
        }
    }
}
