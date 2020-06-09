package com.example.nami.presenters

import com.example.nami.controllers.services.ServiceInteractor
import com.example.nami.models.detailModels.CompareListElement
import com.example.nami.models.detailModels.DetailResponse
import com.example.nami.models.detailModels.ListDataPicker
import com.example.nami.models.detailModels.ListElement

interface DetailUI {
    fun showDetailInfo(data: DetailResponse)
    fun showError(error: String)
    fun showDetailFunctionTaked()
    fun showDetailFunctionReleased()
    fun showDetailFunctionPicked()
    fun showDetailFunctioDeliverCourier()
    fun showDetailFunctionDeliverCustomer()
    fun showDetailFunctionFreeze()
}

class DetailPresenter(private val orderId: Int, private val ui: DetailUI) {
    private val interactor = ServiceInteractor()
    fun actionDetail() {
        interactor.getDetail(orderId, { data ->
            ui.showDetailInfo(data)
        }, { error ->
            ui.showError(error)
        })
    }

    fun actionTake() {
        interactor.putTakeOrder(orderId, "2020-05-20", { data ->
            ui.showDetailFunctionTaked()
        }, { error ->
            ui.showError(error)
        })
    }

    fun actionRelease(observations: String?) {
        interactor.putReleaseOrder(orderId, observations, { data ->
            ui.showDetailFunctionReleased()
        }, { error ->
            ui.showError(error)
        })
    }

    fun actionPick(
        data: DetailResponse,
        articleList: List<String>,
        observations: String?
    ) {
        val productsok=data.order.detailOrder.list==articleList
        var totalPicker=0.0
        var listDataPicker:MutableList<ListDataPicker> = mutableListOf<ListDataPicker>()
        for(i in data.order.detailOrder.list){
            totalPicker += i.valueTotalArticle.toDouble()
            listDataPicker.add(ListDataPicker(i.id,i.quantityArticle))
        }
        interactor.putPickingOrder(
            listDataPicker,
            orderId,
            productsok,
            totalPicker.toString(),
            observations,
            { data ->
                ui.showDetailFunctionPicked()
            },
            { error ->
                ui.showError(error)
            })
    }

    fun actionPutDeliverCourier() {
        interactor.putDeliverCourier(orderId, { data ->
            ui.showDetailFunctioDeliverCourier()
        }, { error ->
            ui.showError(error)
        })
    }

    fun actionPutDeliverCustomer() {
        interactor.putDeliverCustomer(orderId, { data ->
            ui.showDetailFunctionDeliverCustomer()
        }, { error ->
            ui.showError(error)
        })
    }

    fun actionPutFreeze(idReason: Int) {
        interactor.putFreeze(orderId, idReason, { data ->
            ui.showDetailFunctionFreeze()
        }, { error ->
            ui.showError(error)
        })
    }
}