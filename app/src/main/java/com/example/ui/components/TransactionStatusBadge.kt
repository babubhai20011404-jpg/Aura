package com.example.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.TransactionStatus
import com.example.ui.theme.AuraAmber
import com.example.ui.theme.AuraAmberSurface
import com.example.ui.theme.AuraEmerald
import com.example.ui.theme.AuraEmeraldMuted
import com.example.ui.theme.AuraRose
import com.example.ui.theme.AuraRoseSurface
import com.example.ui.theme.AuraSurfaceSecondary
import com.example.ui.theme.AuraTextMuted
import com.example.ui.theme.AuraTheme

@Composable
fun TransactionStatusBadge(
    status: TransactionStatus,
    modifier: Modifier = Modifier
) {
    val (bgColor, textColor, icon) = when (status) {
        TransactionStatus.COMPLETED -> Triple(
            AuraEmeraldMuted,
            AuraEmerald,
            Icons.Default.Check
        )
        TransactionStatus.PROCESSING, TransactionStatus.SUBMITTED -> Triple(
            AuraAmberSurface,
            AuraAmber,
            Icons.Default.Schedule
        )
        TransactionStatus.FAILED, TransactionStatus.CANCELLED -> Triple(
            AuraRoseSurface,
            AuraRose,
            Icons.Default.Close
        )
        else -> Triple(
            AuraSurfaceSecondary,
            AuraTextMuted,
            Icons.Default.Refresh
        )
    }

    GlassSurface(
        modifier = modifier,
        color = bgColor.copy(alpha = 0.8f),
        shape = CircleShape,
        shadowElevation = 0.dp,
        showBorder = true
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = textColor,
                modifier = Modifier.size(12.dp)
            )
            Text(
                text = status.displayName,
                style = MaterialTheme.typography.labelSmall.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 11.sp
                ),
                color = textColor
            )
        }
    }
}
