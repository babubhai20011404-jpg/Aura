package com.example.model

data class DepositAsset(
    val symbol: String,
    val name: String,
    val iconRes: Int? = null
)

data class DepositNetwork(
    val id: String,
    val name: String,
    val protocol: String, // e.g. TRC-20
    val confirmationTime: String? = null,
    val feeInfo: String? = null,
    val minDeposit: String? = null
)

data class DepositAddress(
    val address: String,
    val qrPayload: String,
    val memo: String? = null,
    val tag: String? = null,
    val expiresAt: Long? = null
)
