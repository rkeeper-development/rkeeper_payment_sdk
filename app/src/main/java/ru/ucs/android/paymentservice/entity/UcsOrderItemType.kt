package ru.ucs.android.paymentservice.entity

enum class UcsOrderItemType(val type: Int){
    DISH(1),
    COMBO(2),
    COMPONENT(3),
    COMBOMODI(4),
    MODI(5)
}