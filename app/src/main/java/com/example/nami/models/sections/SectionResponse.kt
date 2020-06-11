package com.example.nami.models.sections

import io.realm.RealmList
import io.realm.RealmObject

open class SectionResponse(
    var orders: RealmList<OrdersList>? = null,
    var message: String? = null
) : RealmObject()


open class OrdersList(
    var id: Int? = null,
    var name: String? = null,
    var lastname: String? = null,
    var address: String? = null,
    var value: String? = null,
    var phoneClient: String? = null,
    var date: String? = null,
    var origin: String? = null,
    var idCodBranch: Int? = null,
    var hour: String? = null,
    var idState: Int? = null,
    var observations: String? = null,
    var methodPay: MethodPay? = null,
    var pickingOrder: PickingOrder? = null,
    var detailOrder: DetailOrder? = null,
    var behavior: Int? = null
) : RealmObject()

open class DetailOrder(
    var totalItems: Int? = null
) : RealmObject()

open class MethodPay(
    var id: Int? = null,
    var name: String? = null
) : RealmObject()

open class PickingOrder(
    var list: RealmList<Any?>? = null

) : RealmObject()
