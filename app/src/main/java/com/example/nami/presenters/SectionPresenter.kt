package com.example.nami.presenters

import android.content.Context
import android.util.Log
import com.example.nami.db.models.SectionDB
import com.example.nami.models.sections.SectionResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking


interface SectionUI {
    fun showData(data: SectionResponse)
    fun showError(error: String)
    fun actionSuccess(message: String)
}

class SectionPresenter(private val ui: SectionUI,val context: Context): BasePresenter() {

    fun actionSection(idSection: Int) {
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


    private fun addDataToDB(data: SectionDB)= runBlocking {
        launch(Dispatchers.Main) {
            try {
                Log.i("HILOO","I'm working in thread ${Thread.currentThread().name}")

                realm!!.executeTransaction {

                    it.copyToRealmOrUpdate(data)
                }

            } catch (e: Exception) {

                Log.i("SE TOTIO", "AL GUARDAR")
                Log.i("ErrorSErio", e.message)
            }

        }

        /** launch(newSingleThreadContext(tag)){
            try {
                realm!!.beginTransaction()
                realm.executeTransaction {  }
                Log.i("Si guardo","GUARDADITO")
                realm!!.copyToRealmOrUpdate(data)
                realm!!.commitTransaction()
               // Log.i("Si guardo","GUARDADITO")

            } catch (e: Exception) {

                Log.i("SE TOTIO", "AL GUARDAR")
                Log.i("ErrorSErio", e.message)
            }
        }**/

       /** launch(newSingleThreadContext(tag)){
            try {
                realm!!.beginTransaction()
                realm.executeTransaction {  }
                Log.i("Si guardo","GUARDADITO")
                realm!!.copyToRealmOrUpdate(data)
                realm!!.commitTransaction()
               // Log.i("Si guardo","GUARDADITO")

            } catch (e: Exception) {

                Log.i("SE TOTIO", "AL GUARDAR")
                Log.i("ErrorSErio", e.message)
            }
        }**/

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