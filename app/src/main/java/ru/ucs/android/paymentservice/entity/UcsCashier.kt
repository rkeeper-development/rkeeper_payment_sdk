package ru.ucs.android.paymentservice.entity

data class UcsCashier(

    /**
     * Уникальный идентификатор кассира
     */
    val userGUID: String,

    /**
     * Имя кассира (опция)
     */
    val name: String?,

    /**
     * ИНН кассира
     */
    val tin: String,

    /**
     * Код кассира (опция)
     */
    val code: String?,

    /**
     * Пароль (или пин-код) кассира (опция)
     */
    val password: String?

)