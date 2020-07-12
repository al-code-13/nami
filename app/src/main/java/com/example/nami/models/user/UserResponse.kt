package com.example.nami.models.user

import io.realm.RealmList
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class UserResponse (
    @PrimaryKey
    var id: String = "userId",
    var user: User? = null,
    var message:String ?=null
): RealmObject()

open class User (
    var alias: String? = null,
    var name: String? = null,
    var lastname: String? = null,
    var phone: String? = null,
    var role: Role? = null,
    var branchs: RealmList<Branch>? = null
): RealmObject()


open class Branch (
    var id: Int? = null,
    var name: String? = null,
    var establishment: Establishment? = null
): RealmObject()

open class ListElement (
    var branch: Int? = null,
    var establishment: Int? = null
): RealmObject()

open class Establishment (
    var id: Int? = null,
    var name: String? = null,
    var logo: String? = null
):RealmObject()

open class Role (
    var id: Int? = null,
    var name: String? = null
): RealmObject()