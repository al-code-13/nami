package com.example.nami.presenters

import android.content.Context
import android.util.Log
import com.example.nami.controllers.services.ServiceFactory
import com.example.nami.db.models.SectionDB
import com.example.nami.models.user.UserResponse
import kotlinx.coroutines.*

interface LoginUI {
    fun showHome()
    fun showError(error: String)
    fun showLoad()
}

class LoginPresenter(val context: Context, private val ui: LoginUI) : BasePresenter() {
    private var viewModelJob: Job = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)


    fun actionAutoLogin() {
        val sharedPreference =
            this.context.getSharedPreferences("localStorage", Context.MODE_PRIVATE)
        val token = sharedPreference.getString("token", "null").toString()
        if (token == "null") {
            ui.showError("Vuelve a iniciar sesion")
        } else {
            ServiceFactory.token = token

            ui.showHome()
        }
    }

    fun actionLogin(user: String, password: String) {
        val sharedPreference =
            this.context.getSharedPreferences("localStorage", Context.MODE_PRIVATE)
        uiScope.launch {
            try {
                interactor.postLogin(user, password, { data ->
                    var editor = sharedPreference.edit()
                    editor.putString("token", data.token)
                    editor.commit()
                    ui.showLoad()
                    interactor.getMe({ data ->

                        addDataToDB(data)
                        Log.i("si pude", "INFO PENDEJO")
                        Log.i("DATAAAA PENDEJO", data.user.toString())
                    }, { error ->
                        ui.showError(error)
                    })
                    ui.showHome()
                }, { error ->
                    ui.showError(error)
                })
            } catch (e: Exception) {
                Log.i("ERROCOOO", e.message)
            }

        }
    }
    private fun addDataToDB(data: UserResponse) = runBlocking {
        launch(Dispatchers.Main) {
            try {
                Log.i("HILOO", "I'm working in thread ${Thread.currentThread().name}")
                realm!!.executeTransaction {
                    it.copyToRealmOrUpdate(data)
                }
            } catch (e: Exception) {
                Log.i("SE TOTIO", "AL GUARDAR")
                Log.i("ErrorSErio", e.message)
            }

        }


    }

}