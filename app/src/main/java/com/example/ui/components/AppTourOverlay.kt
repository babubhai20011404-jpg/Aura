package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.ui.theme.AuraEmerald
import com.example.ui.theme.AuraTextMuted
import com.example.ui.theme.AuraTextPrimary
import com.example.ui.theme.AuraTheme
import com.example.ui.components.AuraButton
import com.example.ui.components.AuraButtonVariant

@Composable
fun AppTourOverlay(
    step: Int,
    onNext: () -> Unit,
    onSkip: () -> Unit,
    visible: Boolean
) {
    val tourData = listOf(
        TourStepData("Home", "This is your Aura home. View your balance and start conversions here."),
        TourStepData("Wallet", "Your digital vault. Manage assets and view deposit addresses."),
        TourStepData("Conversion", "Convert USDT to INR instantly with transparent rates."),
        TourStepData("Activity", "Track every transaction, status, and reference in one place."),
        TourStepData("Intelligence", "Discover A+ market setups and setup explanations.")
    )

    val currentData = if (step < tourData.size) tourData[step] else tourData.last()

    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.6f))
        ) {
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(24.dp)
                    .padding(bottom = 100.dp) // Avoid overlap with bottom nav
            ) {
                GlassSurface(
                    shape = RoundedCornerShape(AuraTheme.Radius.xxl),
                    color = Color.White,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(24.dp)) {
                        Text(
                            text = "Step ${step + 1} of 5",
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                            color = AuraEmerald
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = currentData.title,
                            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                            color = AuraTextPrimary
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = currentData.description,
                            style = MaterialTheme.typography.bodyMedium,
                            color = AuraTextMuted
                        )
                        Spacer(modifier = Modifier.height(24.dp))
                        Row {
                            AuraButton(
                                text = "Skip",
                                variant = AuraButtonVariant.GHOST,
                                onClick = onSkip,
                                modifier = Modifier.weight(1f)
                            )
                            Spacer(modifier = Modifier.padding(8.dp))
                            AuraButton(
                                text = if (step < 4) "Next" else "Done",
                                onClick = onNext,
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }
            }
        }
    }
}

private data class TourStepData(val title: String, val description: String)
