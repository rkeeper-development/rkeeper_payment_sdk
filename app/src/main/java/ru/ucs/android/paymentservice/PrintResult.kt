package ru.ucs.android.paymentservice

sealed class PrintResult

object PrintComplete: PrintResult()

data class PrintError(val errorCode: Int, val errorMsg: String): PrintResult()