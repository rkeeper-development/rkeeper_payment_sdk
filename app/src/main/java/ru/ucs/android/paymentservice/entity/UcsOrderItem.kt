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
     * Код
     */
    val code: String?,

    /**
     * Номер
     */
    val dignum: Int?,

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
    val type: UcsOrderItemType?,

    /**
     * Стоимость единицы предмета расчета
     */
    val cost: Double,

    /**
     * Количество предмета расчета
     */
    val quantity: Double,

    /**
     * Скидки
     */
    val discounts: List<UcsDiscount>?,

    /**
     * Единицы измерения
     */
    val units: String,


    /**
     * Стоимость позиции
     */
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
