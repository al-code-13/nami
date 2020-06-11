package com.example.nami.db.models

import com.example.nami.models.sections.SectionResponse
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class SectionDB(
    @PrimaryKey
    var id: Int? = null,
    var data: SectionResponse? = null
) : RealmObject()



