package com.example.ui.screens

import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.ui.components.AuraKeypad
import com.example.ui.theme.AuraBackground
import com.example.ui.theme.AuraEmerald
import com.example.ui.theme.AuraSurfaceSecondary
import com.example.ui.theme.AuraTextMuted
import com.example.ui.theme.AuraTextPrimary
import com.example.ui.theme.AuraTheme

@Composable
fun PinScreen(
    title: String,
    subtitle: String,
    pinBuffer: String,
    isBiometricEnabled: Boolean = false,
    onKeyPressed: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AuraBackground)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(64.dp))
        
        Box(
            modifier = Modifier.size(64.dp).clip(CircleShape).background(AuraSurfaceSecondary),
            contentAlignment = Alignment.Center
        ) {
            Icon(imageVector = Icons.Default.Lock, contentDescription = null, tint = AuraEmerald, modifier = Modifier.size(32.dp))
        }
        
        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = title,
            style = MaterialTheme.typography.headlineLarge.copy(fontWeight = FontWeight.Bold),
            color = AuraTextPrimary
        )
        Text(
            text = subtitle,
            style = MaterialTheme.typography.bodyLarge,
            color = AuraTextMuted
        )

        Spacer(modifier = Modifier.height(48.dp))

        // PIN Indicators
        Row(
            horizontalArrangement = Arrangement.spacedBy(20.dp),
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(modifier = Modifier.weight(1f))
            repeat(4) { index ->
                val isFilled = index < pinBuffer.length
                Box(
                    modifier = Modifier
                        .size(16.dp)
                        .clip(CircleShape)
                        .background(if (isFilled) AuraEmerald else AuraSurfaceSecondary)
                )
            }
            Spacer(modifier = Modifier.weight(1f))
        }

        Spacer(modifier = Modifier.height(64.dp))
        
        if (isBiometricEnabled) {
            Icon(
                imageVector = Icons.Default.Fingerprint,
                contentDescription = null,
                tint = AuraTextMuted,
                modifier = Modifier.size(48.dp)
            )
            Spacer(modifier = Modifier.height(32.dp))
        }

        Spacer(modifier = Modifier.weight(1f))
        
        AuraKeypad(onKeyPressed = onKeyPressed)
        
        Spacer(modifier = Modifier.height(48.dp))
    }
}
