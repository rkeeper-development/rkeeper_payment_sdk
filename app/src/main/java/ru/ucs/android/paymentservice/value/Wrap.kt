package ru.ucs.android.paymentservice.value

enum class Wrap {

    /**
     * Без переноса
     */
    WRAP_NONE,

    /**
     * Перенос по словам
     */
    WRAP_WORDS,

    /**
     * Посимвольный перенос
     */
    WRAP_CHARS;
}