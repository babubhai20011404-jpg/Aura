package com.example.ui.components.sheets

import androidx.compose.foundation.background
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.model.BankAccount
import com.example.ui.components.AuraButton
import com.example.ui.components.GlassInput
import com.example.ui.theme.AuraBorder
import com.example.ui.theme.AuraEmerald
import com.example.ui.theme.AuraSurfaceSecondary
import com.example.ui.theme.AuraTextMuted
import com.example.ui.theme.AuraTextPrimary
import com.example.ui.theme.AuraTextSecondary
import com.example.ui.theme.AuraTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddBankSheet(
    onAddBank: (bankName: String, accountNumber: String, ifsc: String, holderName: String, type: BankAccount.BankType) -> Unit,
    onDismiss: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scrollState = rememberScrollState()

    var bankName by remember { mutableStateOf("") }
    var accountNumber by remember { mutableStateOf("") }
    var confirmAccountNumber by remember { mutableStateOf("") }
    var ifsc by remember { mutableStateOf("") }
    var holderName by remember { mutableStateOf("AARAV SHARMA") }
    var isVerifying by remember { mutableStateOf(false) }

    val isFormValid = accountNumber.isNotBlank() &&
            accountNumber == confirmAccountNumber &&
            ifsc.length >= 11 &&
            holderName.isNotBlank()

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = Color.White,
        scrimColor = Color.Black.copy(alpha = 0.3f),
        tonalElevation = 0.dp,
        shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp),
        dragHandle = {
            Box(
                modifier = Modifier
                    .padding(vertical = 12.dp)
                    .width(36.dp)
                    .height(5.dp)
                    .clip(CircleShape)
                    .background(AuraBorder)
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(scrollState)
                .padding(horizontal = 24.dp)
                .padding(bottom = 48.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Add bank account",
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                        color = AuraTextPrimary
                    )
                    Text(
                        text = "Instant penny-drop verification",
                        style = MaterialTheme.typography.bodyMedium,
                        color = AuraTextMuted
                    )
                }
                IconButton(
                    onClick = onDismiss,
                    modifier = Modifier.clip(CircleShape).background(AuraSurfaceSecondary)
                ) {
                    Icon(imageVector = Icons.Default.Close, contentDescription = "Close", tint = AuraTextPrimary)
                }
            }

            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                GlassInput(
                    label = "Bank Name",
                    value = bankName,
                    onValueChange = { bankName = it },
                    placeholder = "e.g. HDFC Bank"
                )
                GlassInput(
                    label = "Account Number",
                    value = accountNumber,
                    onValueChange = { accountNumber = it },
                    placeholder = "Enter account number"
                )
                GlassInput(
                    label = "Confirm Account Number",
                    value = confirmAccountNumber,
                    onValueChange = { confirmAccountNumber = it },
                    placeholder = "Re-enter account number"
                )
                GlassInput(
                    label = "IFSC Code",
                    value = ifsc,
                    onValueChange = { ifsc = it.uppercase() },
                    placeholder = "e.g. HDFC0001234"
                )
                GlassInput(
                    label = "Account Holder",
                    value = holderName,
                    onValueChange = { holderName = it.uppercase() },
                    placeholder = "Full name as per bank"
                )
            }

            // Verification Note
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(AuraTheme.Radius.l))
                    .background(AuraSurfaceSecondary.copy(alpha = 0.5f))
                    .padding(16.dp),
                verticalAlignment = Alignment.Top
            ) {
                Icon(
                    imageVector = Icons.Default.Verified,
                    contentDescription = null,
                    tint = AuraEmerald,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = "A ₹1 penny-drop will instantly verify that the bank account name matches your record.",
                    style = MaterialTheme.typography.labelMedium,
                    color = AuraTextSecondary
                )
            }

            AuraButton(
                text = "Verify & Link Bank",
                enabled = isFormValid,
                isLoading = isVerifying,
                onClick = {
                    onAddBank(bankName, accountNumber, ifsc, holderName, BankAccount.BankType.OTHER)
                },
                modifier = Modifier.fillMaxWidth().height(64.dp)
            )
        }
    }
}
