package ru.ucs.android.paymentservice.entity

/**
 * Заказ
 */
data class UcsOrder(
    /**
     * Уникальный идентификатор заказа
     */
    val orderGUID: String,

    /**
     * Сущность с данными кассира
     */
    val cashier: UcsCashier?,

    /**
     * Сущность с данными клиента
     */
    val customer: UcsCustomer?,

    /**
     * Позиции в заказе
     */
    val items: List<UcsOrderItem>,

    /**
     * Скидки на заказ
     */
    val discounts: List<UcsDiscount>?,

    /**
     * Общая стоимость заказа
     */
    val amount: Double

)