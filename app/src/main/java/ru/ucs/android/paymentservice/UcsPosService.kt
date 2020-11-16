package ru.ucs.android.paymentservice

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.util.Log
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.microedition.khronos.opengles.GL

abstract class UcsPosService : Service() {

    companion object {
        const val START_PAYMENT = "ru.ucs.android.paymentservice.START_PAYMENT"
        const val START_REFUND = "ru.ucs.android.paymentservice.START_REFUND"
        const val START_RECONCILIATION = "ru.ucs.android.paymentservice.START_RECONCILIATION"
        const val PRINT_COMPLETE = "ru.ucs.android.paymentservice.PRINT_COMPLETE"
        const val START_EXTERNAL_ACTIVITY = "ru.ucs.android.paymentservice.START_EXTERNAL_ACTIVITY"
        const val TRANSACTION_COMPLETE = "ru.ucs.android.paymentservice.TRANSACTION_COMPLETE"
        const val TRANSACTION_ERROR = "ru.ucs.android.paymentservice.TRANSACTION_ERROR"
        const val PRINT_CHECK = "ru.ucs.android.paymentservice.START_PRINT_CHECK"
        const val PARAM_AMOUNT = "Amount"
        const val PARAM_PRINT_IS_FINISHED = "PrintIsFinished"
        const val PARAM_CHECK_DATA = "CheckData"
        const val PARAM_CHECK_COPIES = "CheckCopies"
        const val PARAM_CURRENCY_CODE = "CurrencyCode"
        const val PARAM_OPERATION_ID = "OperationId"
        const val PARAM_TRANSACTION_ID = "TransactionId"
        const val PARAM_ERROR_CODE = "ErrorCode"
        const val PARAM_ERROR_MESSAGE = "ErrorMessage"
        const val PARAM_EXTERNAL_ACTIVITY_INTENT = "ExternalActivityIntent"
    }

    inner class UcsPosServiceBinder : Binder() {
        fun getService() = this@UcsPosService
    }

    override fun onBind(intent: Intent): IBinder {
        return UcsPosServiceBinder()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent?.let {
            val action = it.action
            when (action) {
                START_PAYMENT ->
                    GlobalScope.launch {
                        startPaymentInternal(it)
                        stopSelfResult(startId)
                    }
                START_REFUND ->
                    GlobalScope.launch {
                        startRefundInternal(it)
                        stopSelfResult(startId)
                    }
                START_RECONCILIATION ->
                    GlobalScope.launch {
                        startReconciliationInternal(intent)
                        stopSelfResult(startId)
                    }
                PRINT_COMPLETE ->
                    GlobalScope.launch {
                        printCompleteInternal(intent)
                        stopSelfResult(startId)
                    }
                else -> null
            }
        }

        return super.onStartCommand(intent, flags, startId)
    }

    fun onStartActivity(activityIntent: Intent){
        val intent = Intent(START_EXTERNAL_ACTIVITY).apply {
            putExtra(PARAM_EXTERNAL_ACTIVITY_INTENT, activityIntent)
        }
        sendBroadcast(intent)
    }

    suspend fun startPaymentInternal(intent: Intent){
        val amount = intent.extras?.get(PARAM_AMOUNT) as String?
        val currencyCode = intent.extras?.getString(PARAM_CURRENCY_CODE)
        val operationId = intent.extras?.getString(PARAM_OPERATION_ID)
        val paymentResult = startPayment(amount, currencyCode)
        postProcess(operationId, paymentResult)
    }

    suspend fun startRefundInternal(intent: Intent){
        val amount = intent.extras?.getString(PARAM_AMOUNT)
        val currencyCode = intent.extras?.getString(PARAM_CURRENCY_CODE)
        val operationId = intent.extras?.getString(PARAM_OPERATION_ID)
        val transactionId = intent.extras?.getString(PARAM_TRANSACTION_ID)
        val paymentResult = startRefund(amount, currencyCode, transactionId)
        postProcess(operationId, paymentResult)
    }

    suspend fun startReconciliationInternal(intent: Intent){
        val operationId = intent.extras?.getString(PARAM_OPERATION_ID)
        val paymentResult = startReconciliation()
        postProcess(operationId, paymentResult)
    }

    suspend fun printCompleteInternal(intent: Intent){
        val operationId = intent.extras?.getString(PARAM_OPERATION_ID)
        val printIsFinished = intent.extras?.getBoolean(PARAM_PRINT_IS_FINISHED)
        val paymentResult = printFinished(printIsFinished)
        postProcess(operationId, paymentResult)
    }

    suspend abstract fun startPayment(amount: String?, currencyCode: String?): PaymentResult

    suspend abstract fun startRefund(amount: String?, currencyCode: String?, transactionId: String?): PaymentResult

    suspend abstract fun startReconciliation(): PaymentResult

    suspend abstract fun printFinished(isFinished: Boolean?): PaymentResult

    private fun postProcess(operationId: String?, paymentResult: PaymentResult){
        val paymentResultIntent = when (paymentResult) {
            is PrintCheck ->
                Intent(PRINT_CHECK).apply {
                    putExtra(PARAM_CHECK_DATA, paymentResult.check)
                    putExtra(PARAM_CHECK_COPIES, paymentResult.copies)
                }
            is TransactionComplete ->
                Intent(TRANSACTION_COMPLETE).apply {
                    putExtra(PARAM_OPERATION_ID, operationId)
                    putExtra(PARAM_TRANSACTION_ID, paymentResult.transactionId)
                }
            is TransactionError ->
                Intent(TRANSACTION_ERROR).apply {
                    putExtra(PARAM_OPERATION_ID, operationId)
                    putExtra(PARAM_ERROR_CODE, paymentResult.errorCode)
                    putExtra(PARAM_ERROR_MESSAGE, paymentResult.errorMsg)
                }
        }
        sendBroadcast(paymentResultIntent)
    }

}