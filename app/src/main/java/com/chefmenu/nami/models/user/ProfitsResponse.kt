package com.chefmenu.nami.models.user

data class ProfitsResponse(
    val total: Long? = null,
    val history: List<History>? = null,
    val message: String? = null
)

data class History(
    val cycle: Long? = null,
    val dateStart: String? = null,
    val dateEnd: String? = null,
    val total: Long? = null,
    val datePay: String? = null,
    val profits: List<Profit>? = null
)

data class Profit(
    val date: String? = null,
    val quantity: Long? = null,
    val profit: Long? = null
)
