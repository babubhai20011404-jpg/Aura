package com.example.model

data class SecurityState(
    val isAuthenticated: Boolean = false,
    val isAppLocked: Boolean = true,
    val hasPinSet: Boolean = false,
    val isBiometricAvailable: Boolean = true,
    val failedPinAttempts: Int = 0,
    val lockTimestamp: Long = 0L
)
