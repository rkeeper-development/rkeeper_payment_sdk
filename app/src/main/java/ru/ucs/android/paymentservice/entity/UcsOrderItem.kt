package ru.ucs.android.paymentservice.entity

data class UcsOrderItem(
    val orderDetailGUID: String,
    val name: String,
    val cost: Double,
    val quantity: Double,
    val amount: Double,
    val tax: UcsTax,
    val discounts: List<UcsDiscount>?
)
