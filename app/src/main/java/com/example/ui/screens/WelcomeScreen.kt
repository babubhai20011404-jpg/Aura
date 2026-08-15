package com.example.ui.screens

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Wallet
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.ui.components.AuraButton
import com.example.ui.components.AuraButtonVariant
import com.example.ui.theme.AuraBackground
import com.example.ui.theme.AuraEmerald
import com.example.ui.theme.AuraTextMuted
import com.example.ui.theme.AuraTextPrimary
import com.example.ui.theme.AuraTextSecondary

@Composable
fun WelcomeScreen(
    onGetStarted: () -> Unit,
    onSkip: () -> Unit
) {
    var step by remember { mutableStateOf(1) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AuraBackground)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Spacer(modifier = Modifier.height(64.dp))

        AnimatedContent(
            targetState = step,
            transitionSpec = {
                fadeIn(animationSpec = tween(400)) togetherWith fadeOut(animationSpec = tween(400))
            },
            label = "welcome_content"
        ) { currentStep ->
            when (currentStep) {
                1 -> WelcomeStep1()
                2 -> WelcomeStep2()
                3 -> WelcomeStep3()
            }
        }

        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            AuraButton(
                text = if (step < 3) "Continue" else "Get started",
                onClick = {
                    if (step < 3) step++ else onGetStarted()
                },
                modifier = Modifier.fillMaxWidth().height(64.dp)
            )
            AuraButton(
                text = "Skip",
                variant = AuraButtonVariant.GHOST,
                onClick = onSkip,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
private fun WelcomeStep1() {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(
            imageVector = Icons.Default.AutoAwesome,
            contentDescription = null,
            tint = AuraEmerald,
            modifier = Modifier.size(80.dp)
        )
        Spacer(modifier = Modifier.height(32.dp))
        Text(
            text = "Welcome to Aura",
            style = MaterialTheme.typography.displaySmall.copy(fontWeight = FontWeight.Bold),
            color = AuraTextPrimary,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Your crypto, simplified.",
            style = MaterialTheme.typography.bodyLarge,
            color = AuraTextMuted,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun WelcomeStep2() {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = "Everything important,\nin one place.",
            style = MaterialTheme.typography.headlineLarge.copy(fontWeight = FontWeight.Bold),
            color = AuraTextPrimary,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(48.dp))
        
        WelcomeFeatureRow(Icons.Default.Wallet, "USDT → INR", "Convert with transparent rates and fees.")
        Spacer(modifier = Modifier.height(24.dp))
        WelcomeFeatureRow(Icons.Default.AutoAwesome, "A+ Setups", "Discover high-quality market intelligence.")
        Spacer(modifier = Modifier.height(24.dp))
        WelcomeFeatureRow(Icons.Default.Security, "Secure Wallet", "Receive and manage your assets safely.")
    }
}

@Composable
private fun WelcomeStep3() {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = "You stay in control.",
            style = MaterialTheme.typography.displaySmall.copy(fontWeight = FontWeight.Bold),
            color = AuraTextPrimary,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(32.dp))
        
        Column(verticalArrangement = Arrangement.spacedBy(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Text("• Transparent fees", style = MaterialTheme.typography.bodyLarge, color = AuraTextSecondary)
            Text("• Clear transaction status", style = MaterialTheme.typography.bodyLarge, color = AuraTextSecondary)
            Text("• Security controls", style = MaterialTheme.typography.bodyLarge, color = AuraTextSecondary)
            Text("• Activity history", style = MaterialTheme.typography.bodyLarge, color = AuraTextSecondary)
        }
    }
}

@Composable
private fun WelcomeFeatureRow(icon: ImageVector, title: String, sub: String) {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
        Icon(imageVector = icon, contentDescription = null, tint = AuraEmerald, modifier = Modifier.size(32.dp))
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(text = title, style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold), color = AuraTextPrimary)
            Text(text = sub, style = MaterialTheme.typography.bodyMedium, color = AuraTextSecondary)
        }
    }
}
