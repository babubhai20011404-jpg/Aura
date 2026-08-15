package com.example.model

data class BankAccount(
    val id: String,
    val bankName: String,
    val accountNumberMasked: String,
    val rawAccountNumber: String,
    val ifsc: String,
    val accountHolder: String,
    val isPrimary: Boolean = false,
    val isPennyDropVerified: Boolean = true,
    val bankLogoType: BankType = BankType.HDFC
) {
    enum class BankType {
        HDFC, ICICI, SBI, KOTAK, AXIS, OTHER
    }
}
