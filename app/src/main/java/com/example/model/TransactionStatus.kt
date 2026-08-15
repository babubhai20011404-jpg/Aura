package com.example.model

enum class TransactionStatus(
    val displayName: String,
    val description: String
) {
    IDLE("Ready", "Enter amount to get quote"),
    QUOTE_LOADING("Fetching Quote", "Calculating optimal market rate"),
    QUOTE_AVAILABLE("Quote Active", "Guaranteed rate reserved"),
    QUOTE_EXPIRED("Rate Expired", "Market rate has refreshed"),
    REVIEW("Review", "Verify conversion and payout account"),
    CONFIRMING("Confirming", "Authorizing sale transaction"),
    SUBMITTED("Sale Submitted", "USDT escrow locked"),
    PROCESSING("Settling INR", "Direct bank transfer in progress"),
    COMPLETED("Settled", "INR successfully credited to bank"),
    FAILED("Transfer Failed", "We couldn't complete this sale"),
    CANCELLED("Cancelled", "Order was cancelled by user"),
    ACTION_REQUIRED("Action Required", "Verification needed by bank")
}
