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
     * Тип позиции:
     *    DISH       = 1;
     *    COMBO      = 2;
     *    COMPONENT  = 3;
     *    COMBOMODI  = 4;
     *    MODI       = 5;
     */
    val type: Int?,

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
     * Комментарий
     */
    val comment: String?,

    /**
     * Сущность с налогом на данную позицию
     */
    val tax:UcsTax
)
