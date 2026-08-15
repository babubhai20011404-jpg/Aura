package com.example.model

data class RewardEvent(
    val id: String,
    val taskId: String,
    val title: String,
    val amount: Int,
    val timestamp: Long = System.currentTimeMillis()
)
