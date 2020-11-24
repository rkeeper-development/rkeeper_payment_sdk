package ru.ucs.android.paymentservice

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.util.Log
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

abstract class UcsPrintService : Service() {

    companion object {
        const val START_PRINT_PAYMENT_CHECK = "ru.ucs.android.paymentservice.PRINT_CHECK"
        const val START_PRINT_REFUND_CHECK = "ru.ucs.android.paymentservice.PRINT_CHECK_REFUND"
        const val START_REPRINT_LAST_CHECK = "ru.ucs.android.paymentservice.REPRINT_LAST_CHECK"
        const val START_PRINT_NONFISCAL = "ru.ucs.android.paymentservice.PRINT_NONFISCAL"
        const val START_PRINT_XREPORT = "ru.ucs.android.paymentservice.PRINT_XREPORT"
        const val START_PRINT_ZREPORT = "ru.ucs.android.paymentservice.PRINT_ZREPORT"
        const val START_GET_STATUS = "ru.ucs.android.paymentservice.GET_STATUS"
        const val START_EXTERNAL_ACTIVITY = "ru.ucs.android.paymentservice.START_EXTERNAL_ACTIVITY"
        const val GET_STATUS_COMPLETE = "ru.ucs.android.paymentservice.GET_STATUS_COMPLETE"
        const val PRINT_PROCESS = "ru.ucs.android.paymentservice.PRINT_PROCESS"
        const val PRINT_COMPLETE = "ru.ucs.android.paymentservice.PRINT_COMPLETE"
        const val PRINT_ERROR = "ru.ucs.android.paymentservice.PRINT_ERROR"
        const val PARAM_OPERATION_ID = "OperationId"
        const val PARAM_REG_NUMBER = "RegistrationNumber"
        const val PARAM_SHIFT_STATUS = "ShiftStatus"
        const val PARAM_LAST_FISCAL_DOCUMENT = "LastFiscalDocument"
        const val PARAM_FACTORY_NUMBER = "FactoryNumber"
        const val PARAM_OFD_INFO = "OfdInfo"
        const val PARAM_FISCAL_DOCUMENT = "FiscalDocument"
        const val PARAM_TRANSACTION_ID = "TransactionId"
        const val PARAM_SHIFT_NUMBER = "ShiftNumber"
        const val PARAM_ORDER = "Order"
        const val PARAM_CASHIER = "Cashier"
        const val PARAM_NON_FISCAL_DATA = "Text"
        const val PARAM_HEADERS = "Headers"
        const val PARAM_FOOTERS = "Footers"
        const val PARAM_ERROR_CODE = "ErrorCode"
        const val PARAM_ERROR_MESSAGE = "ErrorMessage"
        const val PARAM_EXTERNAL_ACTIVITY_INTENT = "ExternalActivityIntent"
    }

    inner class UcsPrintServiceBinder : Binder() {
        fun getService() = this@UcsPrintService
    }

    override fun onBind(intent: Intent): IBinder {
        return UcsPrintServiceBinder()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent?.let {
            val action = it.action
            when (action) {
                START_PRINT_PAYMENT_CHECK ->
                    GlobalScope.launch {
                        startPrintFiscalCheckInternal(it)
                        stopSelfResult(startId)
                    }
                START_GET_STATUS ->
                    GlobalScope.launch {
                        startGetStatusInternal(it)
                        stopSelfResult(startId)
                    }
                START_PRINT_REFUND_CHECK ->
                    GlobalScope.launch {
                        startPrintRefundCheckInternal(it)
                        stopSelfResult(startId)
                    }
                START_PRINT_NONFISCAL -> {
                    GlobalScope.launch {
                        startPrintNonFiscalDataInternal(it)
                        stopSelfResult(startId)
                    }
                }
                START_REPRINT_LAST_CHECK ->
                    GlobalScope.launch {
                        startReprintLastCheckInternal(it)
                        stopSelfResult(startId)
                    }
                START_PRINT_XREPORT -> {
                    GlobalScope.launch {
                        startPrintXReportInternal(it)
                        stopSelfResult(startId)
                    }
                }
                START_PRINT_ZREPORT -> {
                    GlobalScope.launch {
                        startPrintZReportInternal(it)
                        stopSelfResult(startId)
                    }
                }
                else -> null
            }
        }

        return super.onStartCommand(intent, flags, startId)
    }

    suspend fun onStartActivity(activityIntent: Intent){
        val intent = Intent(START_EXTERNAL_ACTIVITY).apply {
            putExtra(PARAM_EXTERNAL_ACTIVITY_INTENT, activityIntent)
        }
        sendBroadcast(intent)
    }

    suspend fun startGetStatusInternal(intent: Intent){
        val operationId = intent.extras?.getString(PARAM_OPERATION_ID)
        val printResult = getStatus()
        postProcess(operationId, printResult)
    }

    suspend fun startPrintFiscalCheckInternal(intent: Intent){
        val operationId = intent.extras?.getString(PARAM_OPERATION_ID)
        val order = intent.extras?.getString(PARAM_ORDER)
        val headers = intent.extras?.getStringArray(PARAM_HEADERS)
        val footers = intent.extras?.getStringArray(PARAM_FOOTERS)
        val printResult = startPrintFiscalCheck(order, headers, footers)
        postProcess(operationId, printResult)
    }

    suspend fun startPrintRefundCheckInternal(intent: Intent){
        val fiscalDocument = intent.extras?.getString(PARAM_FISCAL_DOCUMENT)
        val cashier = intent.extras?.getString(PARAM_CASHIER)
        val operationId = intent.extras?.getString(PARAM_OPERATION_ID)
        val printResult = startPrintRefundCheck(fiscalDocument, cashier)
        postProcess(operationId, printResult)
    }

    suspend fun startPrintNonFiscalDataInternal(intent: Intent){
        val operationId = intent.extras?.getString(PARAM_OPERATION_ID)
        val text = intent.extras?.getString(PARAM_NON_FISCAL_DATA)
        val cashier = intent.extras?.getString(PARAM_CASHIER)
        val printResult = startPrintNonFiscalData(text, cashier)
        postProcess(operationId, printResult)
    }

    suspend fun startReprintLastCheckInternal(intent: Intent){
        val operationId = intent.extras?.getString(PARAM_OPERATION_ID)
        val cashier = intent.extras?.getString(PARAM_CASHIER)
        val printResult = startReprintLastCheck(cashier)
        postProcess(operationId, printResult)
    }

    suspend fun startPrintXReportInternal(intent: Intent){
        val operationId = intent.extras?.getString(PARAM_OPERATION_ID)
        val cashier = intent.extras?.getString(PARAM_CASHIER)
        val printResult = startPrintXReport(cashier)
        postProcess(operationId, printResult)
    }

    suspend fun startPrintZReportInternal(intent: Intent){
        val operationId = intent.extras?.getString(PARAM_OPERATION_ID)
        val cashier = intent.extras?.getString(PARAM_CASHIER)
        val printResult = startPrintZReport(cashier)
        postProcess(operationId, printResult)
    }

    suspend abstract fun getStatus(): PrintResult

    suspend abstract fun startPrintFiscalCheck(order: String?, headers: Array<String>?, footers: Array<String>?): PrintResult

    suspend abstract fun startPrintRefundCheck(fiscalDocument: String?, cashier: String?): PrintResult

    suspend abstract fun startPrintNonFiscalData(text: String?, cashier: String?): PrintResult

    suspend abstract fun startReprintLastCheck(cashier: String?): PrintResult

    suspend abstract fun startPrintXReport(cashier: String?): PrintResult

    suspend abstract fun startPrintZReport(cashier: String?): PrintResult

    private fun postProcess(operationId: String?, printResult: PrintResult){
        val printResultIntent = when (printResult) {
            is PrintProcess ->
                Intent(PRINT_PROCESS).apply {
                    putExtra(PARAM_OPERATION_ID, operationId)
                }
            is GetStatusComplete ->
                Intent(GET_STATUS_COMPLETE).apply {
                    putExtra(PARAM_OPERATION_ID, operationId)
                    putExtra(PARAM_FACTORY_NUMBER, printResult.factoryNumber)
                    putExtra(PARAM_LAST_FISCAL_DOCUMENT, printResult.lastDocNumber)
                    putExtra(PARAM_SHIFT_STATUS, printResult.shiftStatus)
                    putExtra(PARAM_SHIFT_NUMBER, printResult.shiftNumber)
                    putExtra(PARAM_OFD_INFO, printResult.ofdInfo)
                }
            is PrintComplete ->
                Intent(PRINT_COMPLETE).apply {
                    putExtra(PARAM_OPERATION_ID, operationId)
                    putExtra(PARAM_FISCAL_DOCUMENT, printResult.fiscalDocument)
                    putExtra(PARAM_TRANSACTION_ID, printResult.transactionId)
                    putExtra(PARAM_SHIFT_NUMBER, printResult.shiftNumber)
                }
            is PrintError ->
                Intent(PRINT_ERROR).apply {
                    putExtra(PARAM_OPERATION_ID, operationId)
                    putExtra(PARAM_ERROR_CODE, printResult.errorCode)
                    putExtra(PARAM_ERROR_MESSAGE, printResult.errorMsg)
                }
        }
        sendBroadcast(printResultIntent)
    }
}