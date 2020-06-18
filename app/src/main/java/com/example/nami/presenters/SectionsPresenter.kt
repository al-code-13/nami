package com.example.nami.presenters

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.util.Log
import androidx.core.content.ContextCompat.startActivity
import com.example.nami.Login
import com.example.nami.controllers.services.ServiceFactory
import com.example.nami.models.sections.SectionsResponse
import com.example.nami.models.user.UserResponse
import io.realm.kotlin.where
import kotlinx.coroutines.*

interface SectionsUI {
    fun showSection(data: SectionsResponse, userData: UserResponse)
    fun showError(error: String)
    fun exit()
}

class SectionsPresenter(private val ui: SectionsUI,val context: Context) : BasePresenter() {
    private var viewModelJob: Job = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    fun actionSections() {
        uiScope.launch {
            try {
                val realmResponse =
                    realm!!.where<SectionsResponse>().equalTo("id", "sections").findFirst()
                var userResponse = realm!!.where<UserResponse>().equalTo("id", "userId").findFirst()
                if (realmResponse == null) {
                    interactor.getSections(
                        { data ->
                            interactor.getReasons({

                                addDataToDB(data)
                                ui.showSection(data, userResponse!!)
                            }, { error ->
                                ui.showError(error)
                            })
                        },
                        { error ->
                            ui.showError(error)
                        })
                } else {
                    ServiceFactory.data = realmResponse
                    ui.showSection(realmResponse, userResponse!!)
                }
            } catch (e: Exception) {
            }
        }
    }

    private fun addDataToDB(data: SectionsResponse) = runBlocking {
        launch(Dispatchers.Main) {
            try {
                realm!!.executeTransaction {
                    it.copyToRealmOrUpdate(data)
                }

            } catch (e: Exception) {
            }

        }


    }


    fun actionLogOut() {
        val sharedPreference: SharedPreferences =
            this.context.getSharedPreferences("localStorage", Context.MODE_PRIVATE)

        uiScope.launch {
            try {
                realm!!.executeTransaction {
                    it.deleteAll()
                }
                sharedPreference.edit()
                    .clear()
                    .apply()
                ui.exit()

            } catch (e: Exception) {
            }

        }
    }

}
