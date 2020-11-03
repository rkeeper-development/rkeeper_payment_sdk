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
                    // TODO:
                }
                START_PRINT_XREPORT -> {
                    // TODO:
                }
                START_PRINT_ZREPORT -> {
                    // TODO:
                }
                else -> null
            }
        }

        return super.onStartCommand(intent, flags, startId)
    }

    suspend fun startPrintFiscalCheckInternal(intent: Intent){
        val operationId = intent.extras?.getString("OperationId")
        val order = intent.extras?.getString("Order")
        val headers = intent.extras?.getStringArray("Headers")
        val footers = intent.extras?.getStringArray("Footers")
        val printResult = startPrintFiscalCheck(order, headers, footers)
        val paymentResultIntent = when (printResult) {
            is PrintComplete ->
                Intent(PRINT_COMPLETE).apply {
                    putExtra("OperationId", operationId)
                }
            is PrintError ->
                Intent(PRINT_ERROR).apply {
                    putExtra("OperationId", operationId)
                    putExtra("ErrorCode", printResult.errorCode)
                    putExtra("ErrorMessage", printResult.errorMsg)

                }
        }
        sendBroadcast(paymentResultIntent)
    }

    suspend fun startPrintRefundCheckInternal(intent: Intent){
        val order = intent.extras?.getString("order")
        val operationId = intent.extras?.getString("operationId")
        val printResult = startPrintRefundCheck(order)
        val printResultIntent = when (printResult) {
            is PrintComplete ->
                Intent(PRINT_COMPLETE).apply {
                    putExtra("OperationId", operationId)
                }
            is PrintError ->
                Intent(PRINT_ERROR).apply {
                    putExtra("OperationId", operationId)
                    putExtra("ErrorCode", printResult.errorCode)
                    putExtra("ErrorMessage", printResult.errorMsg)
                }
        }
        sendBroadcast(printResultIntent)
    }

    suspend abstract fun startPrintFiscalCheck(order: String?, headers: Array<String>?, footers: Array<String>?): PrintResult

    suspend abstract fun startPrintRefundCheck(order: String?): PrintResult

    suspend abstract fun startPrintXReport(): PrintResult

    suspend abstract fun startPrintZReport(): PrintResult
}