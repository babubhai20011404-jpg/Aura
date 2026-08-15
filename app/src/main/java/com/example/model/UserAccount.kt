package com.example.model

data class UserAccount(
    val id: String = "usr_99812903",
    val name: String = "Aarav Sharma",
    val email: String = "aarav.sharma@example.com",
    val phoneMasked: String = "+91 98••• ••210",
    val usdtBalance: Double = 12450.00,
    val inrBalance: Double = 34500.00,
    val kycLevel: String = "Level 3 (Verified)",
    val fiuRegistered: Boolean = true,
    val dailyLimitInr: Double = 5000000.0, // 50 Lakhs / day
    val dailyUsedInr: Double = 847200.0,
    val biometricEnabled: Boolean = true,
    val twoFactorEnabled: Boolean = true
)
