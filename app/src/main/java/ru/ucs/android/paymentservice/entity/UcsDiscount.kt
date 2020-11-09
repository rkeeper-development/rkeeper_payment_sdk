package ru.ucs.android.paymentservice.entity

/**
 * Скидка
 */
data class UcsDiscount(
    /**
     * Тип скидки
     */
    val type: String,

    /**
     * Наименование скидки
     */
    val name: String,

    /**
     * Величина скидки по позиции
     */
    val rate: Double

)