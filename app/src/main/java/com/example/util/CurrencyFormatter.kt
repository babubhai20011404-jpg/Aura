package com.example.util

import java.text.DecimalFormat
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object CurrencyFormatter {

    /**
     * Formats an amount using standard Indian Numbering grouping (e.g. ₹1,25,000, ₹8,47,200)
     */
    fun formatInr(amount: Double, includeDecimalsIfAny: Boolean = false): String {
        val longVal = amount.toLong()
        val decimalVal = (amount - longVal)
        val formattedInteger = formatIndianGrouping(longVal)
        
        return if (includeDecimalsIfAny && decimalVal > 0.001) {
            val decStr = String.format(Locale.US, "%.2f", decimalVal).substring(1) // e.g. ".80"
            "₹$formattedInteger$decStr"
        } else {
            "₹$formattedInteger"
        }
    }

    /**
     * Formats Indian number grouping (1,23,45,678)
     */
    fun formatIndianGrouping(value: Long): String {
        if (value < 0) return "-" + formatIndianGrouping(-value)
        if (value < 1000) return value.toString()

        val str = value.toString()
        val len = str.length
        val lastThree = str.substring(len - 3)
        val remaining = str.substring(0, len - 3)

        val result = StringBuilder()
        var count = 0
        for (i in remaining.length - 1 downTo 0) {
            result.insert(0, remaining[i])
            count++
            if (count % 2 == 0 && i != 0) {
                result.insert(0, ',')
            }
        }
        result.append(',').append(lastThree)
        return result.toString()
    }

    /**
     * Formats USDT amount with commas and precision
     */
    fun formatUsdt(amount: Double, alwaysShowDecimals: Boolean = false): String {
        val formatter = if (alwaysShowDecimals || amount % 1.0 != 0.0) {
            DecimalFormat("#,##0.00")
        } else {
            DecimalFormat("#,##0")
        }
        return "${formatter.format(amount)} USDT"
    }

    /**
     * Formats rate: e.g. ₹84.72
     */
    fun formatRate(rate: Double): String {
        return "₹" + String.format(Locale.US, "%.2f", rate)
    }

    /**
     * Formats relative or absolute time for transactions
     */
    fun formatTransactionTime(millis: Long): String {
        val now = System.currentTimeMillis()
        val diff = now - millis
        val timeFormat = SimpleDateFormat("hh:mm a", Locale.US)
        val timeStr = timeFormat.format(Date(millis))

        return when {
            diff < 24 * 60 * 60 * 1000L -> "Today, $timeStr"
            diff < 48 * 60 * 60 * 1000L -> "Yesterday, $timeStr"
            else -> {
                val dateFormat = SimpleDateFormat("dd MMM, hh:mm a", Locale.US)
                dateFormat.format(Date(millis))
            }
        }
    }

    fun formatDetailedDateTime(millis: Long): String {
        val dateFormat = SimpleDateFormat("dd MMM yyyy 'at' hh:mm:ss a", Locale.US)
        return dateFormat.format(Date(millis))
    }
}
