package ru.ucs.android.paymentservice.entity

import ru.ucs.android.paymentservice.value.PaymentType

data class UcsPayment(
    /**
     * Способ расчета
     */
    val type: PaymentType,

    /**
     * Сумма
     */
    val amount: Double
)
