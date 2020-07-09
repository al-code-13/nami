package com.example.nami.presenters

import android.content.Context
import android.util.Log
import com.example.nami.models.sections.SectionResponse
import io.realm.kotlin.where
import kotlinx.coroutines.*


interface SectionUI {
    fun showData(data: SectionResponse)
    fun showError(error: String)
    fun actionSuccess(message: String)
}

class SectionPresenter(private val ui: SectionUI, val context: Context) : BasePresenter() {

    private var viewModelJob: Job = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    fun actionSection(idSection: Int,initialDate:String?="null",finalDate:String?="null") {
        uiScope.launch {
            try {
                val realmResponse = realm!!.where<SectionResponse>().equalTo("id", idSection).findFirst()
                if (realmResponse == null) {
                    interactor.getSection(
                        idSection,
                        initialDate,
                        finalDate,
                        { data ->
                            data.id=idSection
                            addDataToDB(data)
                            ui.showData(data)
                        },
                        { error ->
                            ui.showError(error)
                        })
                } else {
                    ui.showData(realmResponse!!)
                }
            } catch (e: Exception) {
            }
        }
    }

    fun actionRefreshSection(idSection: Int,initialDate:String?="null",finalDate:String?="null") {
        Log.i("refresh section",idSection.toString())
        interactor.getSection(
            idSection,
            initialDate,
            finalDate,
            { data ->
                data.id=idSection
                addDataToDB(data)
                ui.showData(data)
            },
            { error ->
                ui.showError(error)
            })

    }

    private fun addDataToDB(data: SectionResponse) = runBlocking {
        launch(Dispatchers.Main) {
            try {
                realm!!.executeTransaction {
                    it.copyToRealmOrUpdate(data)
                }
            } catch (e: Exception) {
            }

        }


    }

    fun actionTake(orderId: Int) {
        interactor.putTakeOrder(orderId, "2020-05-20", { data ->
            ui.actionSuccess(data.message)
        }, { error ->
            ui.showError(error)
        })
    }

    fun actionRelease(orderId: Int, observations: String?) {
        if(observations.isNullOrEmpty()||observations.isNullOrBlank() ){

            interactor.putReleaseOrder(orderId, null, { data ->
                ui.actionSuccess(data.message)
            }, { error ->
                ui.showError(error)
            })
        }
        else{
            Log.i("observationsxddd", observations.toString())
            interactor.putReleaseOrder(orderId, observations, { data ->
                ui.actionSuccess(data.message)
            }, { error ->
                ui.showError(error)
            })
        }
    }

    fun actionPutDeliverCourier(orderId: Int) {
        interactor.putDeliverCourier(orderId, { data ->
            ui.actionSuccess(data.message)
        }, { error ->
            ui.showError(error)
        })
    }

    fun actionPutDeliverCustomer(orderId: Int) {
        interactor.putDeliverCustomer(orderId, { data ->
            ui.actionSuccess(data.message)
        }, { error ->
            ui.showError(error)
        })
    }

    fun actionPutFreeze(orderId: Int, idReason: Int) {
        interactor.putFreeze(orderId, idReason, { data ->
            ui.actionSuccess(data.message)
        }, { error ->
            ui.showError(error)
        })
    }
}