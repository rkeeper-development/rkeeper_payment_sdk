package ru.ucs.android.paymentservice.entity

data class UcsTax(
    val type: String,
    val name: String,
    val rate: Double,
    val included: Boolean,
    val amount: Double
)