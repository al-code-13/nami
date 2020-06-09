package com.example.nami.model


class ModelOrders {

    var name: String? = null
    var idOrder: Int? = 0
    var amount: String? = null
    var date: String? = null
    var cell: String? = null
    var total: Int? = 0
    var state: String? = null

    constructor(
        name: String?,
        idOrder: Int?,
        amount: String?,
        date: String?,
        cell: String?,
        total: Int?,
        state: String?
    ) {
        this.name = name
        this.idOrder = idOrder
        this.amount = amount
        this.date = date
        this.cell = cell
        this.total = total
        this.state = state
    }
}
class ProductData(
    val id: Int,
    val name: String,
    val price: Int,
    val cant: Int
)

data class ItemIndicators(
    val name: String ?= null,
    val drawable: String? = null
)