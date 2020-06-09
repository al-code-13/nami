package com.example.nami.presenters

import SectionResponse
import android.content.Context
import android.util.Log
import com.example.nami.controllers.services.ServiceInteractor
import com.example.nami.models.detailModels.DetailResponse
import com.example.nami.models.detailModels.ListDataPicker
import com.example.nami.models.detailModels.ListElement


interface SectionUI {
    fun showData(data: SectionResponse)
    fun showError(error: String)
    fun actionSuccess(message:String)
}

class SectionPresenter(val ui: SectionUI) {

    val interactor = ServiceInteractor()
    fun actionSection(idSection:Int) {
        interactor.getSection(

            idSection,
            { data ->
                ui.showData(data)
            },
            { error ->
                ui.showError(error)
            })
    }

    fun actionTake(orderId:Int) {
        interactor.putTakeOrder(orderId, "2020-05-20", { data ->
            ui.actionSuccess(data.message)
        }, { error ->
            ui.showError(error)
        })
    }

    fun actionRelease(orderId:Int,observations: String?) {
        interactor.putReleaseOrder(orderId, observations, { data ->
            ui.actionSuccess(data.message)
        }, { error ->
            ui.showError(error)
        })
    }

    fun actionPutDeliverCourier(orderId:Int) {
        interactor.putDeliverCourier(orderId, { data ->
            ui.actionSuccess(data.message)
        }, { error ->
            ui.showError(error)
        })
    }

    fun actionPutDeliverCustomer(orderId:Int) {
        interactor.putDeliverCustomer(orderId, { data ->
            ui.actionSuccess(data.message)
        }, { error ->
            ui.showError(error)
        })
    }

    fun actionPutFreeze(orderId:Int,idReason: Int) {
        interactor.putFreeze(orderId, idReason, { data ->
            ui.actionSuccess(data.message)
        }, { error ->
            ui.showError(error)
        })
    }
}