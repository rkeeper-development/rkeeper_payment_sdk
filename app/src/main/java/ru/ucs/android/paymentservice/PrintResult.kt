package ru.ucs.android.paymentservice

sealed class PrintResult

data class GetStatusComplete(
        val factoryNumber: String,   // заводской номер / серийный номер
        val shiftStatus: String,       // статус смены (открыта/закрыта)
        val shiftNumber: String,       // номер смены
        val lastDocNumber: String,   // номер последнего фискального документа
        val ofdInfo: String          // сводная информация о ККТ
): PrintResult()

object PrintProcess: PrintResult()
data class PrintComplete(val fiscalDocument: String, val transactionId: String, val shiftNumber: String): PrintResult()
data class PrintError(val errorCode: Int, val errorMsg: String): PrintResult()
object InitSettingsComplete: PrintResult()
data class InitSettingsError(val errorCode: Int, val errorMsg: String): PrintResult()
