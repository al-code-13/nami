package com.example.nami.presenters

import com.example.nami.controllers.services.ServiceInteractor
import io.realm.Realm

open class BasePresenter {


    protected var realm: Realm ?= null

    protected val interactor = ServiceInteractor()

    init {
            realm = Realm.getDefaultInstance()
    }

}