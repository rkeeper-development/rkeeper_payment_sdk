package ru.ucs.android.paymentservice.entity

import java.io.Serializable

/**
 * Клиент
 */
data class UcsCustomer(

    /**
     * Уникальный идентификатор клиента
     */
    val userGUID: String,

    /**
     * Имя клиента (опция)
     */
    val name: String?,

    /**
     * E-mail клиента (опция)
     */
    val email: String?,

    /**
     * Телефон клиента (опция)
     */
    val phone: String?

): Serializable