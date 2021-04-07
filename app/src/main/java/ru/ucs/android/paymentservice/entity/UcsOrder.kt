package ru.ucs.android.paymentservice.entity

import ru.ucs.android.paymentservice.value.ReceiptType
import ru.ucs.android.paymentservice.value.TaxationType

/**
 * Заказ
 */
data class UcsOrder(
    /**
     * Уникальный идентификатор заказа
     */
    val orderGUID: String,

    /**
     * Тип чека
     */
    val type: ReceiptType,

    /**
     * Система налогооблажения
     */
    val taxationType: TaxationType,

    /**
     * Сущность с данными кассира
     */
    val cashier: UcsCashier,

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
     * Платежи
     */
    val payments: List<UcsPayment>

)