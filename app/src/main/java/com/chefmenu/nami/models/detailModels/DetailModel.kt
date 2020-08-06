package com.example.nami.models.detailModels

data class DetailResponse (
    val order: Order,
    val message:String?
)

data class Order (
    val id: Int,
    val comments: String,
    val email: String,
    val turns: String,
    val deliveryValue: String,
    val service: String,
    val reasonNotDelivery: Any? = null,
    val detailOrder: DetailOrder
)

data class DetailOrder (
    val list: List<ListElement>
)

data class ListElement (
    val id: Int,
    val description: String? = null,
    var quantityArticle: String,
    val valueTotalArticle: String,
    val codOptionalsExternals: Any? = null,
    val codTamano: Any? = null,
    val observations: String? = null,
    val picking: Any? = null,
    val article: Article
)


data class Article (
    val id: Long,
    val name: String,
    val description: String,
    val value: String,
    val image: String,
    val upc: String,
    val sku: String
)