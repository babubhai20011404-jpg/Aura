package com.example.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.AuraBorderSubtle
import com.example.ui.theme.AuraEmerald
import com.example.ui.theme.AuraEmeraldGlow
import com.example.ui.theme.AuraGlassElevated
import com.example.ui.theme.AuraSurfaceSecondary
import com.example.ui.theme.AuraTextMuted
import com.example.ui.theme.AuraTextPrimary
import com.example.ui.theme.AuraTextSecondary
import com.example.ui.theme.AuraTheme

@Composable
fun StatusTimeline(
    currentStepIndex: Int, // 1: Submitted, 2: Processing, 3: Settled
    modifier: Modifier = Modifier
) {
    val steps = listOf(
        Pair("Sale submitted", "USDT locked in escrow"),
        Pair("Settlement processing", "Banking rail payment initiated"),
        Pair("INR arrival", "Credited to HDFC Bank ···· 4821")
    )

    val infiniteTransition = rememberInfiniteTransition(label = "step_pulse")
    val pulseAlpha by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 0.7f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse"
    )

    GlassCard(
        modifier = modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            steps.forEachIndexed { index, (title, subtitle) ->
                val stepNumber = index + 1
                val isCompleted = currentStepIndex > stepNumber || (currentStepIndex == 3 && stepNumber == 3)
                val isCurrent = currentStepIndex == stepNumber && currentStepIndex != 3
                val isPending = currentStepIndex < stepNumber

                val nodeColor by animateColorAsState(
                    targetValue = if (isCompleted || isCurrent) AuraEmerald else AuraSurfaceSecondary,
                    animationSpec = spring(stiffness = Spring.StiffnessLow),
                    label = "node_color"
                )

                val textColor by animateColorAsState(
                    targetValue = when {
                        isCompleted -> AuraEmerald
                        isCurrent -> AuraTextPrimary
                        else -> AuraTextMuted
                    },
                    animationSpec = spring(stiffness = Spring.StiffnessLow),
                    label = "text_color"
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.Top
                ) {
                    // Step Indicator Node
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.width(36.dp)
                    ) {
                        Box(
                            modifier = Modifier.size(36.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            when {
                                isCompleted -> {
                                    Box(
                                        modifier = Modifier
                                            .size(28.dp)
                                            .clip(CircleShape)
                                            .background(AuraEmerald),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Check,
                                            contentDescription = "Completed",
                                            tint = Color.White,
                                            modifier = Modifier.size(16.dp)
                                        )
                                    }
                                }
                                isCurrent -> {
                                    Box(
                                        modifier = Modifier
                                            .size(32.dp)
                                            .clip(CircleShape)
                                            .background(AuraEmerald.copy(alpha = pulseAlpha))
                                    )
                                    Box(
                                        modifier = Modifier
                                            .size(16.dp)
                                            .clip(CircleShape)
                                            .background(AuraEmerald)
                                    )
                                }
                                else -> {
                                    Box(
                                        modifier = Modifier
                                            .size(20.dp)
                                            .clip(CircleShape)
                                            .background(AuraSurfaceSecondary)
                                    )
                                }
                            }
                        }

                        // Vertical connector line
                        if (index < steps.size - 1) {
                            Box(
                                modifier = Modifier
                                    .width(1.5.dp)
                                    .height(32.dp)
                                    .background(if (isCompleted) AuraEmerald else AuraBorderSubtle)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(20.dp))

                    // Text Description
                    Column {
                        Text(
                            text = title,
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = if (isCurrent || isCompleted) FontWeight.Bold else FontWeight.Medium
                            ),
                            color = textColor
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = subtitle,
                            style = MaterialTheme.typography.labelMedium,
                            color = if (isCurrent) AuraTextSecondary else AuraTextMuted
                        )
                    }
                }
            }
        }
    }
}
