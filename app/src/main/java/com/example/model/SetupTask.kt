package com.example.model

data class SetupTask(
    val id: String,
    val title: String,
    val description: String,
    val status: TaskStatus = TaskStatus.INCOMPLETE,
    val rewardAmount: Int = 0,
    val actionLabel: String = "Continue",
    val deepLink: String? = null
)

enum class TaskStatus {
    INCOMPLETE,
    PROCESSING,
    COMPLETED,
    LOCKED
}
