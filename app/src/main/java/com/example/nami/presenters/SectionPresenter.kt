package com.example.nami.presenters

import android.util.Log
import com.example.nami.controllers.services.ServiceInteractor
import com.example.nami.db.models.SectionDB
import com.example.nami.models.sections.SectionResponse
import io.realm.Realm


interface SectionUI {
    fun showData(data: SectionResponse)
    fun showError(error: String)
    fun actionSuccess(message: String)
}

class SectionPresenter(private val ui: SectionUI) {
    private lateinit var realm: Realm
    private val interactor = ServiceInteractor()
    fun actionSection(idSection: Int) {
        realm = Realm.getDefaultInstance()

        interactor.getSection(
            idSection,
            { data ->
                val newData = SectionDB()
                newData.id = idSection
                newData.data = data
                addDataToDB(newData)
                ui.showData(data)
            },
            { error ->
                ui.showError(error)
            })
    }

    private fun addDataToDB(data: SectionDB) {
        try {
            realm.beginTransaction()
            realm.copyToRealmOrUpdate(data)
            realm.commitTransaction()
            Log.i("Si guardo","GUARDADITO")

        } catch (e: Exception) {
            Log.i("SE TOTIO", "AL GUARDAR")
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
        interactor.putReleaseOrder(orderId, observations, { data ->
            ui.actionSuccess(data.message)
        }, { error ->
            ui.showError(error)
        })
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