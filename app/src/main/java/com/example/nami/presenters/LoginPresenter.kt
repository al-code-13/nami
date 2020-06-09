package com.example.nami.presenters

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.example.nami.controllers.services.ServiceFactory
import com.example.nami.controllers.services.ServiceInteractor

interface LoginUI{
    fun showHome()
    fun showError(error:String)
    fun showLoad()
}

class LoginPresenter (val context: Context,private val ui: LoginUI){
    private val interactor = ServiceInteractor()

    fun actionAutoLogin(){
        val sharedPreference = this.context.getSharedPreferences("localStorage", Context.MODE_PRIVATE)
        val token = sharedPreference.getString("token","null").toString()
        if(token=="null"){
            ui.showError("Vuelve a iniciar sesion")
        }
        else{
            ServiceFactory.token=token
            ui.showHome()
        }
    }

    fun actionLogin(user: String, password: String) {
        interactor.postLogin(user, password, { data ->
            val sharedPreference = this.context.getSharedPreferences("localStorage", Context.MODE_PRIVATE)
            var editor = sharedPreference.edit()
            editor.putString("token", data.token)
            editor.commit()
            ui.showLoad()
            ui.showHome()
        }, { error ->
            ui.showError(error)
        })
    }
}