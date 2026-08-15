package com.example.model

data class Transaction(
    val id: String,
    val usdtAmount: Double,
    val ratePerUsdt: Double,
    val feeInr: Double,
    val grossInr: Double,
    val netInr: Double,
    val settlementBankName: String,
    val settlementAccountMasked: String,
    val ifsc: String,
    val status: TransactionStatus,
    val createdAtMillis: Long,
    val completedAtMillis: Long? = null,
    val utrNumber: String? = null,
    val txHash: String? = null,
    val network: String = "TRON (TRC-20)",
    val failureReason: String? = null,
    val currentStepIndex: Int = 1 // 1: submitted, 2: processing, 3: settled
)
