package ru.usc.android.paymentservice

import android.app.Service
import android.content.Intent
import android.os.IBinder
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

abstract class UcsPrintService : Service() {

    companion object {
        const val START_PRINT_PAYMENT_CHECK = "ru.usc.android.paymentservice.PRINT_CHECK"
        const val START_PRINT_REFUND_CHECK = "ru.usc.android.paymentservice.PRINT_CHECK_REFUND"
        const val START_PRINT_NONFISCAL = "ru.usc.android.paymentservice.PRINT_NONFISCAL"
        const val START_PRINT_XREPORT = "ru.usc.android.paymentservice.PRINT_XREPORT"
        const val START_PRINT_ZREPORT = "ru.usc.android.paymentservice.PRINT_ZREPORT"
        const val PRINT_COMPLETE = "ru.usc.android.paymentservice.PRINT_COMPLETE"
        const val PRINT_ERROR = "ru.usc.android.paymentservice.PRINT_ERROR"
        const val PARAM_OPERATION_ID = "OperationId"
        const val PARAM_ORDER = "Order"
        const val PARAM_HEADERS = "Headers"
        const val PARAM_FOOTERS = "Footers"
        const val PARAM_ERROR_CODE = "ErrorCode"
        const val PARAM_ERROR_MESSAGE = "ErrorMessage"
    }

    override fun onBind(intent: Intent): IBinder {
        TODO("Return the communication channel to the service.")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent?.let {
            val action = it.action
            when (action) {
                START_PRINT_PAYMENT_CHECK -> {
                    GlobalScope.launch {
                        startPrintFiscalCheckInternal(it)
                        stopSelfResult(startId)
                    }
                }
                START_PRINT_REFUND_CHECK -> {
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

    suspend fun startPrintFiscalCheckInternal(intent: Intent){
        val operationId = intent.extras?.getString(PARAM_OPERATION_ID)
        val order = intent.extras?.getString(PARAM_ORDER)
        val headers = intent.extras?.getStringArray(PARAM_HEADERS)
        val footers = intent.extras?.getStringArray(PARAM_FOOTERS)
        val printResult = startPrintFiscalCheck(order, headers, footers)
        val paymentResultIntent = when (printResult) {
            is PrintComplete ->
                Intent(PRINT_COMPLETE).apply {
                    putExtra(PARAM_OPERATION_ID, operationId)
                }
            is PrintError ->
                Intent(PRINT_ERROR).apply {
                    putExtra(PARAM_OPERATION_ID, operationId)
                    putExtra(PARAM_ERROR_CODE, printResult.errorCode)
                    putExtra(PARAM_ERROR_MESSAGE, printResult.errorMsg)

                }
        }
        sendBroadcast(paymentResultIntent)
    }

    suspend fun startPrintRefundCheckInternal(intent: Intent){
        val order = intent.extras?.getString(PARAM_ORDER)
        val operationId = intent.extras?.getString(PARAM_OPERATION_ID)
        val printResult = startPrintRefundCheck(order)
        val printResultIntent = when (printResult) {
            is PrintComplete ->
                Intent(PRINT_COMPLETE).apply {
                    putExtra(PARAM_OPERATION_ID, operationId)
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

    suspend fun startPrintNonFiscalDataInternal(intent: Intent){
        val operationId = intent.extras?.getString(PARAM_OPERATION_ID)

    }

    suspend fun startPrintXReportInternal(intent: Intent){
        val operationId = intent.extras?.getString(PARAM_OPERATION_ID)
        val printResult = startPrintXReport()
        val printResultIntent = when (printResult) {
            is PrintComplete ->
                Intent(PRINT_COMPLETE).apply {
                    putExtra(PARAM_OPERATION_ID, operationId)
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

    suspend fun startPrintZReportInternal(intent: Intent){
        val operationId = intent.extras?.getString(PARAM_OPERATION_ID)
        val printResult = startPrintZReport()
        val printResultIntent = when (printResult) {
            is PrintComplete ->
                Intent(PRINT_COMPLETE).apply {
                    putExtra(PARAM_OPERATION_ID, operationId)
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

    suspend abstract fun startPrintFiscalCheck(order: String?, headers: Array<String>?, footers: Array<String>?): PrintResult

    suspend abstract fun startPrintRefundCheck(order: String?): PrintResult

    suspend abstract fun startPrintXReport(): PrintResult

    suspend abstract fun startPrintZReport(): PrintResult
}