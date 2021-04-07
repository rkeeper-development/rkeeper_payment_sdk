package ru.ucs.android.paymentservice.value

/**
 * Система налогооблажения
 */
enum class TaxationType {

    /**
     * Общая
     */
    OSN,

    /**
     * Упрощенная - доход
     */
    USN_INCOME,

    /**
     * Упрощенная - доход минус расход
     */
    USN_INCOME_OUTCOME,

    /**
     * Единый сельскохозяйственный налог
     */
    ESN,

    /**
     * Патент
     */
    PATENT,

    /**
     * ЕНВД
     */
    ENVD
}