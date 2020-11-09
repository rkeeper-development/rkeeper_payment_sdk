package ru.ucs.android.paymentservice

sealed class PrintResult

data class PrintComplete(val fiscalDocument: String, val transactionId: String, val shiftNumber: String): PrintResult()

data class PrintError(val errorCode: Int, val errorMsg: String): PrintResult()