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
        val order = intent.extras?.getSerializable("order") as String?
        val headers = intent.extras?.getSerializable("headers") as List<String>?
        val footers = intent.extras?.getSerializable("footers") as List<String>?
        val printResult = startPrintFiscalCheck(order, headers, footers)
        val paymentResultIntent = when (printResult) {
            is PrintComplete ->
                Intent(PRINT_COMPLETE).apply {
                    putExtra("device", "Print Terminal 2")
                }
            is PrintError ->
                Intent(PRINT_ERROR).apply {
                    putExtra("error_code", printResult.errorCode)
                    putExtra("error_message", printResult.errorMsg)
                }
        }
        sendBroadcast(paymentResultIntent)
    }

    suspend fun startPrintRefundCheckInternal(intent: Intent){
        val order = intent.extras?.get("order") as String?
        val printResult = startPrintRefundCheck(order)
        val printResultIntent = when (printResult) {
            is PrintComplete ->
                Intent(PRINT_COMPLETE).apply {
                    putExtra("device", "Print Terminal 2")
                }
            is PrintError ->
                Intent(PRINT_ERROR).apply {
                    putExtra("error_code", printResult.errorCode)
                    putExtra("error_message", printResult.errorMsg)
                }
        }
        sendBroadcast(printResultIntent)
    }

    suspend abstract fun startPrintFiscalCheck(order: String?, headers: List<String>?, footers: List<String>?): PrintResult

    suspend abstract fun startPrintRefundCheck(order: String?): PrintResult

    suspend abstract fun startPrintXReport(): PrintResult

    suspend abstract fun startPrintZReport(): PrintResult
}