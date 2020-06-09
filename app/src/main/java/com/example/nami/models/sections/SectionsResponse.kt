package com.example.nami.models.sections

data class SectionsResponse(
    val actions: List<Action>,
    val behaviors: List<Behavior>,
    val sections: List<Section>,
    val message: String?

)

data class Action(
    val id: Int,
    val name: String,
    val description: String? = null,
    val destructive: Boolean
)

data class Behavior(
    val id: Int,
    val name: String,
    val description: String? = null,
    val color: String?=null,
    val visible: Boolean,
    val actions: List<Int>? = null,
    val action: Int? = null
)


data class Section(
    val id: Int,
    val name: String,
    val color: String,
    val behaviors: List<Int>
)