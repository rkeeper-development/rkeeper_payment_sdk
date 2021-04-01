package ru.ucs.android.paymentservice.value

enum class PaymentType(paymentType: Int) {
    CASH(0),
    ELECTRONICALLY(1),
    PREPAID(2),
    CREDIT(3),
    OTHER(4)
}