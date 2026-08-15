package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.ui.components.AuraButton
import com.example.ui.components.GlassInput
import com.example.ui.theme.AuraBackground
import com.example.ui.theme.AuraEmerald
import com.example.ui.theme.AuraTextMuted
import com.example.ui.theme.AuraTextPrimary

@Composable
fun LoginScreen(
    onLogin: (String, String) -> Unit
) {
    var email by remember { mutableStateOf("aarav.sharma@example.com") }
    var password by remember { mutableStateOf("••••••••") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AuraBackground)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Welcome back",
            style = MaterialTheme.typography.displaySmall.copy(fontWeight = FontWeight.Bold),
            color = AuraTextPrimary
        )
        Text(
            text = "Login with your Anteprox account",
            style = MaterialTheme.typography.bodyLarge,
            color = AuraTextMuted
        )

        Spacer(modifier = Modifier.height(48.dp))

        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            GlassInput(
                label = "Email or Username",
                value = email,
                onValueChange = { email = it },
                placeholder = "Enter your email"
            )
            GlassInput(
                label = "Password",
                value = password,
                onValueChange = { password = it },
                placeholder = "Enter password"
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        AuraButton(
            text = "Login to Anteprox",
            onClick = { onLogin(email, password) },
            modifier = Modifier.fillMaxWidth().height(64.dp)
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Text(
            text = "Forgot password?",
            style = MaterialTheme.typography.labelLarge.copy(color = AuraEmerald, fontWeight = FontWeight.Bold),
            modifier = Modifier.padding(8.dp)
        )
    }
}
