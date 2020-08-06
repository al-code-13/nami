package com.example.nami.models.detailModels


data class SendConfirmationRequest (
    val email:String?=null,
    val phone:String?=null
)
data class SendConfirmationResponse (
    val message: String
)