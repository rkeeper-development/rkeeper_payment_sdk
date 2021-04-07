package ru.ucs.android.paymentservice.value

enum class PaymentType {

    /**
     * Наличными
     */
    CASH,

    /**
     * Безналичными
     */
    ELECTRONICALLY,

    /**
     * Предварительная оплата
     */
    PREPAID,

    /**
     * Последующая оплата
     */
    CREDIT,

    /**
     * Иная форма оплаты
     */
    OTHER
}