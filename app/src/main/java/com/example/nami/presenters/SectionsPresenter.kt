package com.example.nami.presenters

import android.util.Log
import com.example.nami.db.models.SectionDB
import com.example.nami.models.sections.SectionsResponse
import io.realm.kotlin.where
import kotlinx.coroutines.*

interface SectionsUI {
    fun showSection(data: SectionsResponse)
    fun showError(error: String)
}

class SectionsPresenter(private val ui: SectionsUI) : BasePresenter() {
    private var viewModelJob: Job = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)
    fun actionSections() {
        uiScope.launch {
            try {
                val realmResponse = realm!!.where<SectionsResponse>().findFirst()
                if (realmResponse == null) {
                    Log.i("no trae sections", "EL NULL")
                    interactor.getSections(
                        { data ->
                            interactor.getReasons({

                                addDataToDB(data)
                                ui.showSection(data)
                            }, { error ->
                                ui.showError(error)
                            })
                        },
                        { error ->
                            ui.showError(error)
                        })
                } else {
                    Log.i("si trae sections ", "ALGO")
                    ui.showSection(realmResponse)
                }
            } catch (e: Exception) {
                Log.i("ERROCOOO", e.message)
            }
        }
    }

    private fun addDataToDB(data: SectionsResponse) = runBlocking {
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
