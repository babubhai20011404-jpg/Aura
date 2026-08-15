package com.example.model

data class TourState(
    val version: Int = 1,
    val isCompleted: Boolean = false,
    val isSkipped: Boolean = false,
    val currentStep: Int = 0
)
