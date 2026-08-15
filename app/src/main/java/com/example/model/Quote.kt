package com.example.model

data class Quote(
    val usdtAmount: Double,
    val ratePerUsdt: Double,
    val grossInr: Double,
    val feePercentage: Double = 0.15, // 0.15% transparent fee
    val feeInr: Double,
    val tdsPercentage: Double = 1.0, // 1% statutory TDS for Indian crypto compliance (optional toggle)
    val tdsInr: Double = 0.0,
    val netInr: Double,
    val validForSeconds: Int = 30,
    val secondsRemaining: Int = 30,
    val isExpired: Boolean = false,
    val previousRate: Double? = null,
    val estimatedArrivalMinutesMin: Int = 10,
    val estimatedArrivalMinutesMax: Int = 30,
    val settlementMethod: String = "IMPS / RTGS Direct Bank Settlement"
) {
    companion object {
        fun calculate(amount: Double, currentRate: Double, oldRate: Double? = null): Quote {
            val gross = amount * currentRate
            val fee = (gross * 0.0015).coerceAtLeast(10.0) // 0.15% minimum ₹10
            val net = (gross - fee).coerceAtLeast(0.0)
            return Quote(
                usdtAmount = amount,
                ratePerUsdt = currentRate,
                grossInr = gross,
                feeInr = fee,
                netInr = net,
                previousRate = oldRate
            )
        }
    }
}
