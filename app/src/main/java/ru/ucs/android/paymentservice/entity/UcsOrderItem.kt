package ru.ucs.android.paymentservice.entity

/**
 * Товарные позиции в заказе
 */
data class UcsOrderItem(
    /**
     * Уникальный идентификатор позиции заказа
     */
    val orderItemGUID: String,

    /**
     * Наименование предмета расчета(товара)
     */
    val name: String,

    /**
     * Стоимость единицы предмета расчета
     */
    val cost: Double,

    /**
     * Количество предмета расчета
     */
    val quantity: Double,

    val discounts: List<UcsDiscount>?,

    val units: String,

    val amount: Double,

    /**
     * Сущность с налогом на данную позицию
     */
    val tax:UcsTax
)
