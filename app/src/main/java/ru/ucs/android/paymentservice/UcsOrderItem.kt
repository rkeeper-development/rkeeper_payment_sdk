package ru.ucs.android.paymentservice

data class UcsOrderItem(
    val orderDetailGUID: String,
    val name: String,
    val cost: Double,
    val quantity: Double,
    val amount: Double,
    val tax: UcsTax,
    val discounts: List<UcsDiscount>?
)

data class UcsTax(
   val type: String,
   val name: String,
   val rate: Double,
   val included: Boolean,
   val amount: Double
)

data class UcsDiscount(
    val type: String,
    val rate: Double,
    val name: String
)