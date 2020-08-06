package com.chefmenu.nami.presenters

import android.util.Log
import com.chefmenu.nami.models.detailModels.DetailResponse
import com.chefmenu.nami.models.detailModels.ListDataPicker
import com.chefmenu.nami.models.sections.OrdersList
import com.chefmenu.nami.models.sections.SectionResponse
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

        if (observations.isNullOrEmpty() || observations.isNullOrBlank()) {
            interactor.putReleaseOrder(orderId, null, { data ->
                ui.showDetailFunctionReleased()
            }, { error ->
                ui.showError(error)
            })
        } else {
            interactor.putReleaseOrder(orderId, observations, { data ->
                ui.showDetailFunctionReleased()
            }, { error ->
                ui.showError(error)
            })
        }

    }

    fun actionPick(
        data: DetailResponse,
        adjustmentValue: Double,
        compareArticleList: List<String>,
        articleList: List<String>,
        observations: String?
    ) {

        val productsok = compareArticleList.equals(articleList)
        var listDataPicker: MutableList<ListDataPicker> = mutableListOf<ListDataPicker>()
        if (productsok != true) {
            for (i in data.order.detailOrder.list) {
                if (compareArticleList[data.order.detailOrder.list.indexOf(i)] != articleList[data.order.detailOrder.list.indexOf(
                        i
                    )]
                ) {
                    listDataPicker.add(
                        ListDataPicker(
                            i.id,
                            articleList[data.order.detailOrder.list.indexOf(i)].toString()
                        )
                    )
                }
            }
        }
        if (observations.isNullOrEmpty() || observations.isNullOrBlank()) {
            interactor.putPickingOrder(
                listDataPicker,
                orderId,
                productsok,
                adjustmentValue.toString(),
                null,
                { data ->
                    ui.showDetailFunctionPicked()
                },
                { error ->
                    ui.showError(error)
                })
        } else {
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
    }

    fun actionPutDeliverCourier(email:String?=null,phone:String?=null) {
        interactor.putDeliverCourier(orderId,email,phone, { data ->
            ui.showDetailFunctioDeliverCourier()
        }, { error ->
            ui.showError(error)
        })
    }

    fun actionPutDeliverCustomer(code:String,showDialog:()->Unit) {
        interactor.putConfirmDelivery(orderId,code, { data ->
            ui.showDetailFunctionDeliverCustomer()
        }, { error ->
            showDialog()
            ui.showError(error)
        })
    }

    fun actionPutSendConfirmation(email: String,phone: String){
        interactor.putSendConfirmation(orderId,email,phone, { data ->
            Log.i("todo salio correcto",data.toString())
            //ui.showDetailFunctionDeliverCustomer()
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
    fun cleanDB(){
        uiScope.launch {
            try {
                val realmResponse =
                    realm!!.where<SectionResponse>().equalTo("id", idSection).findFirst()!!
                realm!!.executeTransaction {
                    realmResponse.deleteFromRealm()
                }
            } catch (e: Exception) {
                ui.showError("Error al guardar")
            }
        }
    }
}