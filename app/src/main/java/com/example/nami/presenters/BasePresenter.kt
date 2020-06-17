package com.example.nami.presenters

import android.util.Log
import com.example.nami.controllers.services.ServiceInteractor
import io.realm.Realm

open class BasePresenter {


    protected var realm: Realm ?= null

    protected val interactor = ServiceInteractor()

    init {

        Log.i("HILOO22222","I'm working in thread ${Thread.currentThread().name}")
            realm = Realm.getDefaultInstance()

    }

}