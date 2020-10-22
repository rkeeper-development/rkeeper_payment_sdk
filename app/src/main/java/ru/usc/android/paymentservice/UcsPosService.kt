package ru.usc.android.paymentservice

import android.app.Service
import android.content.Intent
import android.os.IBinder
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

abstract class UcsPosService : Service() {

    companion object {
        const val START_PAYMENT = "ru.usc.android.paymentservice.START_PAYMENT"
        const val START_REFUND = "ru.usc.android.paymentservice.START_REFUND"
        const val TRANSACTION_COMPLETE = "ru.usc.android.paymentservice.TRANSACTION_COMPLETE"
        const val TRANSACTION_ERROR = "ru.usc.android.paymentservice.TRANSACTION_ERROR"
    }

    override fun onBind(intent: Intent): IBinder {
        TODO("Return the communication channel to the service.")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent?.let {
            val action = it.action
            when (action) {
                START_PAYMENT -> {
                    GlobalScope.launch {
                        startPaymentInternal(it)
                        stopSelfResult(startId)
                    }
                }
                START_REFUND -> {
                    GlobalScope.launch {
                        startRefundInternal(it)
                        stopSelfResult(startId)
                    }
                }
                else -> null
            }
        }

        return super.onStartCommand(intent, flags, startId)
    }

    suspend fun startPaymentInternal(intent: Intent){
        val amount = intent.extras?.get("amount") as String?
        val paymentResult = startPayment(amount)
        val paymentResultIntent = when (paymentResult) {
            is TransactionComplete ->
                Intent(TRANSACTION_COMPLETE).apply {
                    putExtra("status", false)
                    putExtra("result", "ок")
                    putExtra("device", "Terminal 2")
                }
            is TransactionError ->
                Intent(TRANSACTION_ERROR).apply {
                    putExtra("status", false)
                    putExtra("result", "ок")
                    putExtra("device", "Terminal 2")
                }
        }
        sendBroadcast(paymentResultIntent)
    }

    suspend fun startRefundInternal(intent: Intent){
        val amount = intent.extras?.get("amount") as String?
        val paymentResult = startRefund(amount)
        val paymentResultIntent = when (paymentResult) {
            is TransactionComplete ->
                Intent(TRANSACTION_COMPLETE).apply {
                    putExtra("transactionId", paymentResult.transactionId)
                    putExtra("device", "Terminal 2")
                }
            is TransactionError ->
                Intent(TRANSACTION_ERROR).apply {
                    putExtra("error_code", paymentResult.errorCode)
                    putExtra("error_message", paymentResult.errorMsg)
                }
        }
        sendBroadcast(paymentResultIntent)
    }

    suspend abstract fun startPayment(amount: String?): PaymentResult

    suspend abstract fun startRefund(amount: String?): PaymentResult
}