package com.chefmenu.nami.models.user

class UpdateProfileRequest (
    var name: String? = null,
    var lastname: String? = null,
    var phone: String? = null,
    var email: String? = null
)

class UpdateProfileResponse (
    val message:String
)