package ru.ucs.android.paymentservice.entity

data class UcsText(

    /**
     * Тип нефискального документа
     */
    val type: String,

    /**
     * Строки
     */
    val items: List<UcsTextItem>
)
