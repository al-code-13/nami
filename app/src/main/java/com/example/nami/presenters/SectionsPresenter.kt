package com.example.nami.presenters

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.example.nami.controllers.services.ServiceFactory
import com.example.nami.models.sections.SectionResponse
import com.example.nami.models.sections.SectionsResponse
import com.example.nami.models.user.BranchsResponse
import com.example.nami.models.user.UserResponse
import io.realm.Realm
import io.realm.RealmResults
import io.realm.kotlin.where
import kotlinx.coroutines.*


interface SectionsUI {
    fun showBranchs(data: BranchsResponse, userData: UserResponse)
    fun showSection(data: SectionsResponse)
    fun showError(error: String)
    fun exit()
}

class SectionsPresenter(private val ui: SectionsUI, val context: Context) : BasePresenter() {
    private var viewModelJob: Job = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    fun actionBranchs() {

        uiScope.launch {
            try {
                var userResponse = realm!!.where<UserResponse>().equalTo("id", "userId").findFirst()
                Log.i("userResponse", userResponse.toString())
                interactor.getBranchs(
                    { data ->
                        Log.i("branchResponse", data.toString())

                        ui.showBranchs(data, userResponse!!)
                    },
                    { error ->
                        ui.showError(error)
                    })

            } catch (e: Exception) {
            }
        }
    }

    fun actionSections(branchId: Int) {
        uiScope.launch {
            try {
                val realmResponse =
                    realm!!.where<SectionsResponse>().equalTo("id", "sections").findFirst()

                val sharedPreference =
                    context.getSharedPreferences("localStorage", Context.MODE_PRIVATE)
                var editor = sharedPreference.edit()
                editor.putString("branchId", branchId.toString())
                editor.commit()
                if (realmResponse == null) {
                    actionRefreshSections(branchId)
                } else {
                    ServiceFactory.data = realmResponse
                    ui.showSection(realmResponse)
                }
            } catch (e: Exception) {
            }
        }
    }

     fun actionRefreshSections(branchId: Int) {
        uiScope.launch {
            try {
                val sharedPreference =
                    context.getSharedPreferences("localStorage", Context.MODE_PRIVATE)
                var editor = sharedPreference.edit()
                editor.putString("branchId", branchId.toString())
                editor.commit()
                interactor.getSections(branchId,
                    { data ->
                        interactor.getReasons({
                            removeSectionsDataToDB()
                            addDataToDB(data)
                            ui.showSection(data)
                        }, { error ->
                            ui.showError(error)
                        })
                    },
                    { error ->
                        ui.showError(error)
                    })

            } catch (e: Exception) {
            }
        }
    }

    private fun removeSectionsDataToDB() = runBlocking {
        launch(Dispatchers.Main) {
            try {
                realm!!.executeTransaction(object : Realm.Transaction {
                    override fun execute(realm: Realm) {
                        val sectionsResult: RealmResults<SectionsResponse> =
                            realm.where(SectionsResponse::class.java)
                                .findAll()
                        sectionsResult.deleteAllFromRealm()
                        val sectionResult: RealmResults<SectionResponse> =
                            realm.where(SectionResponse::class.java)
                                .findAll()
                        sectionResult.deleteAllFromRealm()
                    }
                })
            } catch (e: Exception) {
                Log.i("que paso?", "amiguito")
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
                ui.showError("Error al cerrar sesión")
            }
        }
    }

}
