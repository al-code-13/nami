package com.chefmenu.nami.presenters

import android.content.Context
import android.content.SharedPreferences
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
    fun exit();
}

class DetailPresenter(
    private val context:Context,
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
                    //Log.i("Errorbuscandobd", e.message)
                }
            }
        }, { error ->
            ui.showError(error)
        })
    }

    fun actionTake() {
        interactor.putTakeOrder(orderId, "2020-05-20", { _ ->
            ui.showDetailFunctionTaked()
        }, { error ->
            ui.showError(error)
        })
    }

    fun actionRelease(observations: String?) {

        if (observations.isNullOrEmpty() || observations.isNullOrBlank()) {
            interactor.putReleaseOrder(orderId, null, { _ ->
                ui.showDetailFunctionReleased()
            }, { error ->
                ui.showError(error)
            })
        } else {
            interactor.putReleaseOrder(orderId, observations, { _ ->
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

        val productsOk = compareArticleList == articleList
        var listDataPicker: MutableList<ListDataPicker> = mutableListOf<ListDataPicker>()
        if (!productsOk) {
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
                productsOk,
                adjustmentValue.toString(),
                null,
                { _ ->
                    ui.showDetailFunctionPicked()
                },
                { error ->
                    ui.showError(error)
                })
        } else {
            interactor.putPickingOrder(
                listDataPicker,
                orderId,
                productsOk,
                adjustmentValue.toString(),
                observations,
                { _ ->
                    ui.showDetailFunctionPicked()
                },
                { error ->
                    ui.showError(error)
                })
        }
    }

    fun actionPutDeliverCourier(email:String?=null,phone:String?=null) {
        interactor.putDeliverCourier(orderId,email,phone, { _ ->
            ui.showDetailFunctioDeliverCourier()
        }, { error ->
            ui.showError(error)
        })
    }

    fun actionPutDeliverCustomer(code:String,showDialog:()->Unit) {
        interactor.putConfirmDelivery(orderId,code, { _ ->
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
        interactor.putFreeze(orderId, idReason, { _ ->
            ui.showDetailFunctionFreeze()
        }, { error ->
            ui.showError(error)
        })
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