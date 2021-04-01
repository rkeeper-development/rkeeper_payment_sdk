package ru.ucs.android.paymentservice.value

/**
 * Тип задания
 */
enum class CorrectionTaskType {

    /**
     * Коррекция прихода
     */
    SELL_CORRECTION,

    /**
     * Коррекция расхода
     */
    BUY_CORRECTION,

    /**
     * Коррекция возврата прихода
     */
    SELL_RETURN_CORRECTION,

    /**
     * Коррекция возврата расхода
     */
    BUY_RETURN_CORRECTION
}