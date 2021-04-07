package ru.ucs.android.paymentservice.value

enum class ReceiptType {

    /**
     * Чек прихода
     */
    SELL,

    /**
     * Чек расхода
     */
    BUY,

    /**
     * Чек возврата прихода
     */
    SELL_RETURN,

    /**
     * Чек возврата расхода
     */
    BUY_RETURN;
}