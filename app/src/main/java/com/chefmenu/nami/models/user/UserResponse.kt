package com.chefmenu.nami.models.user

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class UserResponse (
    @PrimaryKey
    var id: String = "userId",
    var user: User? = null,
    var message:String ?=null
):RealmObject()

open class User (
    var alias: String? = null,
    var name: String? = null,
    var lastname: String? = null,
    var phone: String? = null,
    var email: String? = null,
    var typeIdentification: String? = null,
    var identification: String? = null,
    var role: Role? = null
):RealmObject()

open class Role (
    var name: String? = null
):RealmObject()
