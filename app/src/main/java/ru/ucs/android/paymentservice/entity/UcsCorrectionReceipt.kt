package ru.ucs.android.paymentservice.entity

import ru.ucs.android.paymentservice.value.CorrectionTaskType
import ru.ucs.android.paymentservice.value.CorrectionType
import ru.ucs.android.paymentservice.value.TaxationType

data class UcsCorrectionReceipt(

    /**
     * Тип задания
     */
    val type: CorrectionTaskType,

    /**
     * Тип коррекции
     */
    val correctionType: CorrectionType,

    /**
     * Система налогооблажения
     */
    val taxationType: TaxationType,

    /**
     * Сущность с данными кассира
     */
    val cashier: UcsCashier,

    /**
     * Дата совершения корректируемого расчета
     */
    val correctionBaseDate: String,

    /**
     * Номер предписания налогового органа
     */
    val correctionBaseNumber: String,

    /**
     * Документ оснований
     */
    val correctionBaseName: String,

    /**
     * Платежи
     */
    val payments: List<UcsPayment>,

    /**
     * Сущность с налогом на данную позицию
     */
    val taxes: List<UcsTax>


)
