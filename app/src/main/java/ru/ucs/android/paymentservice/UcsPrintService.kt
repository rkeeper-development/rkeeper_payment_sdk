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
        const val START_PRINT_NONFISCAL = "ru.ucs.android.paymentservice.PRINT_NONFISCAL"
        const val START_PRINT_XREPORT = "ru.ucs.android.paymentservice.PRINT_XREPORT"
        const val START_PRINT_ZREPORT = "ru.ucs.android.paymentservice.PRINT_ZREPORT"
        const val START_EXTERNAL_ACTIVITY = "ru.ucs.android.paymentservice.START_EXTERNAL_ACTIVITY"
        const val PRINT_COMPLETE = "ru.ucs.android.paymentservice.PRINT_COMPLETE"
        const val PRINT_ERROR = "ru.ucs.android.paymentservice.PRINT_ERROR"
        const val PARAM_OPERATION_ID = "OperationId"
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
            Log.d("paymentmodule", action.toString())
            when (action) {
                START_PRINT_PAYMENT_CHECK -> {
                    Log.d("paymentmodule", "payment check")
                    GlobalScope.launch {
                        startPrintFiscalCheckInternal(it)
                        stopSelfResult(startId)
                    }
                }
                START_PRINT_REFUND_CHECK -> {
                    Log.d("paymentmodule", "refund check")
                    GlobalScope.launch {
                        startPrintRefundCheckInternal(it)
                        stopSelfResult(startId)
                    }
                }
                START_PRINT_NONFISCAL -> {
                    GlobalScope.launch {
                        startPrintNonFiscalDataInternal(it)
                        stopSelfResult(startId)
                    }
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

    suspend fun startPrintFiscalCheckInternal(intent: Intent){
        Log.d("paymentmodule", "startPrintFiscalCheckInternal()")
        val operationId = intent.extras?.getString(PARAM_OPERATION_ID)
        val order = intent.extras?.getString(PARAM_ORDER)
        val headers = intent.extras?.getStringArray(PARAM_HEADERS)
        val footers = intent.extras?.getStringArray(PARAM_FOOTERS)
        val printResult = startPrintFiscalCheck(order, headers, footers)
        postProcess(operationId, printResult)
    }

    suspend fun startPrintRefundCheckInternal(intent: Intent){
        Log.d("paymentmodule", "startPrintRefundCheckInternal()")
        val order = intent.extras?.getString(PARAM_ORDER)
        val headers = intent.extras?.getStringArray(PARAM_HEADERS)
        val footers = intent.extras?.getStringArray(PARAM_FOOTERS)
        val operationId = intent.extras?.getString(PARAM_OPERATION_ID)
        val printResult = startPrintRefundCheck(order, headers, footers)
        postProcess(operationId, printResult)
    }

    suspend fun startPrintNonFiscalDataInternal(intent: Intent){
        Log.d("printService", "startPrintNonFiscalDataInternal()")
        val operationId = intent.extras?.getString(PARAM_OPERATION_ID)
        val text = intent.extras?.getString(PARAM_NON_FISCAL_DATA)
        val cashier = intent.extras?.getString(PARAM_CASHIER)
        val printResult = startPrintNonFiscalData(text, cashier)
        postProcess(operationId, printResult)
    }

    suspend fun startPrintXReportInternal(intent: Intent){
        val operationId = intent.extras?.getString(PARAM_OPERATION_ID)
        val printResult = startPrintXReport()
        postProcess(operationId, printResult)
    }

    suspend fun startPrintZReportInternal(intent: Intent){
        val operationId = intent.extras?.getString(PARAM_OPERATION_ID)
        val printResult = startPrintZReport()
        postProcess(operationId, printResult)
    }

    suspend abstract fun startPrintFiscalCheck(order: String?, headers: Array<String>?, footers: Array<String>?): PrintResult

    suspend abstract fun startPrintRefundCheck(order: String?, headers: Array<String>?, footers: Array<String>?): PrintResult

    suspend abstract fun startPrintNonFiscalData(text: String?, cashier: String?): PrintResult

    suspend abstract fun startPrintXReport(): PrintResult

    suspend abstract fun startPrintZReport(): PrintResult


    private fun postProcess(operationId: String?, printResult: PrintResult){
        val printResultIntent = when (printResult) {
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