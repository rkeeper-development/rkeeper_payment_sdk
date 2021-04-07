package ru.ucs.android.paymentservice.value

enum class TaxRate(val rate: String) {
    VAT_0("0"),
    VAT_10("1000"),
    VAT_18("1800"),
    VAT_20("2000"),
    VAT_110("11000"),
    VAT_120("12000"),
    VAT_NONE("none")
}