package com.example.nami.presenters

import android.util.Log
import com.example.nami.models.detailModels.DetailResponse
import com.example.nami.models.detailModels.ListDataPicker
import com.example.nami.models.sections.OrdersList
import com.example.nami.models.sections.SectionResponse
import io.realm.kotlin.where
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch


interface DetailUI {
    fun showDetailInfo(data: DetailResponse, order: OrdersList)
    fun showError(error: String)
    fun showDetailFunctionTaked()
    fun showDetailFunctionReleased()
    fun showDetailFunctionPicked()
    fun showDetailFunctioDeliverCourier()
    fun showDetailFunctionDeliverCustomer()
    fun showDetailFunctionFreeze()
}

class DetailPresenter(
    private val orderId: Int,
    private val ui: DetailUI,
    private val idSection: Int
) : BasePresenter() {

    private var viewModelJob: Job = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    fun actionDetail() {
        interactor.getDetail(orderId, { data ->
            uiScope.launch {
                try {
                    val realmResponse =
                        realm!!.where<SectionResponse>().equalTo("id", idSection).findFirst()!!
                    if (realmResponse != null) {
                        val order = realmResponse.orders!!.first { it.id == orderId }
                        ui.showDetailInfo(data, order)
                    }
                } catch (e: Exception) {
                    Log.i("Errorbuscandobd", e.message)
                }
            }
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
        adjustmentValue: Double,
        articleList: List<String>,
        observations: String?
    ) {

        val productsok = data.order.detailOrder.list == articleList
        var listDataPicker: MutableList<ListDataPicker> = mutableListOf<ListDataPicker>()
        for (i in data.order.detailOrder.list) {
            listDataPicker.add(
                ListDataPicker(
                    i.id,
                    articleList[data.order.detailOrder.list.indexOf(i)]
                )
            )
        }
        Log.i("adjustemenscms", adjustmentValue.toString())
        interactor.putPickingOrder(
            listDataPicker,
            orderId,
            productsok,
            adjustmentValue.toString(),
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