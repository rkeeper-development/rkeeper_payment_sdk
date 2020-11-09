package ru.ucs.android.paymentservice.entity

data class UcsTax(
    val type: String,
    val name: String,
    val rate: String,
    val included: Boolean,
    val amount: Double
)