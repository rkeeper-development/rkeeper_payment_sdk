package ru.ucs.android.paymentservice.entity

import ru.ucs.android.paymentservice.value.Alignment
import ru.ucs.android.paymentservice.value.Wrap

data class UcsTextItem(

    /**
     * Текст
     */
    val text: String,

    /**
     * Выравнивание
     */
    val alignment: Alignment,

    /**
     * Перенос
     */
    val wrap: Wrap,

    /**
     * Шрифт
     */
    val font: Int,

    /**
     * Увеличение ширины символов вдвое
     */
    val doubleWidth: Boolean,

    /**
     * Увеличение высоты символов вдвое
     */
    val doubleHeight: Boolean
)
