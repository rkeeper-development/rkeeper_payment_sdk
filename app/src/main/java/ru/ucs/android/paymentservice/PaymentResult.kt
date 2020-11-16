package ru.ucs.android.paymentservice

sealed class PaymentResult

data class TransactionComplete(val transactionId: String): PaymentResult()

data class TransactionError(val errorCode: Int, val errorMsg: String): PaymentResult()

data class PrintCheck(val check: String, val copies: Int): PaymentResult()