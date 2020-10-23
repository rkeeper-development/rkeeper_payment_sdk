package ru.usc.android.paymentservice

data class UcsOrder(
    val orderGUID: String,
    val orderDetails: List<UcsOrderItem>,
    val cashier: UcsCashier
)