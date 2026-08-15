package com.example.ui.screens

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.model.SetupTask
import com.example.model.TaskStatus
import com.example.ui.components.AuraTopBar
import com.example.ui.components.GlassSurface
import com.example.ui.theme.AuraBackground
import com.example.ui.theme.AuraEmerald
import com.example.ui.theme.AuraSurfaceSecondary
import com.example.ui.theme.AuraTextMuted
import com.example.ui.theme.AuraTextPrimary
import com.example.ui.theme.AuraTextSecondary
import com.example.ui.theme.AuraTheme

@Composable
fun SetupJourneyScreen(
    tasks: List<SetupTask>,
    onTaskClick: (SetupTask) -> Unit,
    onBackClick: () -> Unit
) {
    val completedCount = tasks.count { it.status == TaskStatus.COMPLETED }
    val progress = if (tasks.isNotEmpty()) completedCount.toFloat() / tasks.size else 0f

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AuraBackground)
    ) {
        AuraTopBar(
            title = "Set up Aura",
            subtitle = "Follow the guide to get everything ready",
            onBackClick = onBackClick
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(16.dp))
                SetupProgressCard(completedCount, tasks.size, progress)
                Spacer(modifier = Modifier.height(24.dp))
                Text(
                    text = "Your journey",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = AuraTextPrimary
                )
            }

            items(tasks) { task ->
                SetupTaskRow(task = task, onClick = { onTaskClick(task) })
            }

            item {
                Spacer(modifier = Modifier.height(100.dp))
            }
        }
    }
}

@Composable
private fun SetupProgressCard(completed: Int, total: Int, progress: Float) {
    GlassSurface(
        shape = RoundedCornerShape(AuraTheme.Radius.xxl),
        color = Color.White,
        modifier = Modifier.fillMaxWidth(),
        shadowElevation = 4.dp
    ) {
        Column(modifier = Modifier.padding(24.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "$completed of $total complete",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                    color = AuraTextPrimary
                )
                Text(
                    text = "${(progress * 100).toInt()}%",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = AuraEmerald
                )
            }
            Spacer(modifier = Modifier.height(20.dp))
            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(CircleShape),
                color = AuraEmerald,
                trackColor = AuraSurfaceSecondary,
                strokeCap = StrokeCap.Round
            )
        }
    }
}

@Composable
private fun SetupTaskRow(task: SetupTask, onClick: () -> Unit) {
    val isCompleted = task.status == TaskStatus.COMPLETED

    Surface(
        shape = RoundedCornerShape(AuraTheme.Radius.l),
        color = if (isCompleted) Color.White.copy(alpha = 0.5f) else Color.White,
        border = if (!isCompleted) androidx.compose.foundation.BorderStroke(1.dp, com.example.ui.theme.AuraBorder) else null,
        modifier = Modifier
            .fillMaxWidth()
            .clickable(enabled = !isCompleted, onClick = onClick)
    ) {
        Row(
            modifier = Modifier.padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(if (isCompleted) AuraEmerald else AuraSurfaceSecondary),
                contentAlignment = Alignment.Center
            ) {
                if (isCompleted) {
                    Icon(imageVector = Icons.Default.CheckCircle, contentDescription = null, tint = Color.White, modifier = Modifier.size(20.dp))
                } else {
                    Text(text = "+${task.rewardAmount}", style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold), color = AuraEmerald)
                }
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = task.title,
                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold),
                    color = if (isCompleted) AuraTextMuted else AuraTextPrimary
                )
                Text(
                    text = task.description,
                    style = MaterialTheme.typography.bodySmall,
                    color = AuraTextMuted
                )
            }
            if (!isCompleted) {
                Icon(imageVector = Icons.Default.ChevronRight, contentDescription = null, tint = AuraTextMuted, modifier = Modifier.size(20.dp))
            }
        }
    }
}
