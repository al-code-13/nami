package com.example.nami.controllers.services

import android.util.Log
import com.example.nami.models.auth.LoginRequest
import com.example.nami.models.auth.LoginResponse
import com.example.nami.models.detailModels.*
import com.example.nami.models.sections.ReasonsResponse
import com.example.nami.models.sections.SectionResponse
import com.example.nami.models.sections.SectionsResponse
import com.example.nami.models.user.UserResponse
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import kotlinx.coroutines.*
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import java.io.IOException


class ServiceInteractor : ServiceFactory() {

    private var viewModelJob: Job = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)
    fun postLogin(
        user: String,
        password: String,
        then: (LoginResponse) -> Unit,
        error: (String) -> Unit
    ) {
        uiScope.launch {
            postLoginCoroutine(user, password, then, error)
        }
    }

    private suspend fun postLoginCoroutine(
        user: String,
        password: String,
        then: (LoginResponse) -> Unit,
        error: (String) -> Unit
    ) {
        val url = serverUrl + routeBase + routeAuth + routeLogin
        val request = LoginRequest(user, password)
        val json = Gson().toJson(request)
        withContext(Dispatchers.IO) {
            post(url, json).enqueue(object : Callback {

                override fun onResponse(call: Call, response: Response) {
                    val body = response.body?.string()

                    val gson = GsonBuilder().create()
                    val res = gson.fromJson(body, LoginResponse::class.java)
                    if (response.isSuccessful) {
                        token = res.token
                        then(res)
                    } else {
                        error(res.message.toString())
                    }
                }

                override fun onFailure(call: Call, e: IOException) {
                    error("Error en el servicio")
                }
            })
        }

    }

    fun getSections(
        then: (SectionsResponse) -> Unit,
        error: (String) -> Unit
    ) {
        uiScope.launch {
            getSectionsCorrutine(then, error)
        }
    }

    fun getSectionsCorrutine(
        then: (SectionsResponse) -> Unit,
        error: (String) -> Unit
    ) {

        val url = serverUrl + routeBase + routeSections
        get(url, token!!).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {

                val body = response.body?.string()
                val gson = GsonBuilder().create()
                val res = gson.fromJson(body, SectionsResponse::class.java)
                if (response.isSuccessful) {
                    data = res
                    then(res)
                } else {
                    error(res.message.toString())
                }
            }

            override fun onFailure(call: Call, e: IOException) {
                error("Error en el servicio")
            }
        })
    }

    fun getReasons(
        then: (ReasonsResponse) -> Unit,
        error: (String) -> Unit
    ) {
        uiScope.launch {
            getReasonsCorrutine(then, error)
        }
    }

    private fun getReasonsCorrutine(
        then: (ReasonsResponse) -> Unit,
        error: (String) -> Unit
    ) {

        val url = serverUrl + routeBase + routeReasons
        get(url, token!!).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                val body = response.body?.string()
                val gson = GsonBuilder().create()
                val res = gson.fromJson(body, ReasonsResponse::class.java)
                if (response.isSuccessful) {
                    reasons = res
                    then(res)
                } else {
                    error(res.message.toString())

                }
            }

            override fun onFailure(call: Call, e: IOException) {
                error("Error en el servicio")
            }
        })
    }

    fun getSection(
        section: Int,
        initialDate:String?=null,
        finalDate:String?=null,
        then: (SectionResponse) -> Unit,
        error: (String) -> Unit
    ) {
        uiScope.launch {
            getSectionCorutine(section,initialDate,finalDate, then, error)
        }
    }

    private suspend fun getSectionCorutine(
        section: Int,
        initialDate:String?="null",
        finalDate:String?="null",
        then: (SectionResponse) -> Unit,
        error: (String) -> Unit
    ) {
        val url = "$serverUrl$routeBase$routeSections/$section/$initialDate/$finalDate"
        Log.i("urlSection",url)
        withContext(Dispatchers.IO) {
            get(url, token!!).enqueue(object : Callback {
                override fun onResponse(call: Call, response: Response) {
                    Log.i("ME LLAMARON", "POS AQUI ESTOY")

                    val body = response.body?.string()
                    Log.i("bodySection",body)
                    val gson = GsonBuilder().create()
                    val res = gson.fromJson(body, SectionResponse::class.java)
                    if (response.isSuccessful) {
                        then(res)
                    } else {
                        error(res.message.toString())
                    }
                }

                override fun onFailure(call: Call, e: IOException) {
                    Log.i("timeout?",e.message)
                    error("Error en el servicio")
                }
            })
        }

    }

    fun getDetail(
        order: Int,
        then: (DetailResponse) -> Unit,
        error: (String) -> Unit
    ) {
        uiScope.launch {
            getDetailCorutine(order, then, error)
        }
    }

    private suspend fun getDetailCorutine(
        order: Int,
        then: (DetailResponse) -> Unit,
        error: (String) -> Unit
    ) {
        Log.i("token peticion detail", token)
        val url = "$serverUrl$routeBase$routeOrders/$order"
        withContext(Dispatchers.IO) {
            get(url, token!!).enqueue(object : Callback {
                override fun onResponse(call: Call, response: Response) {
                    val body = response.body?.string()
                    val gson = GsonBuilder().create()
                    val res = gson.fromJson(body, DetailResponse::class.java)
                    if (response.isSuccessful) {
                        then(res)
                    } else {
                        error(res.message.toString())
                    }
                }

                override fun onFailure(call: Call, e: IOException) {
                    error("Error en el servicio")
                }
            })
        }

    }


    fun putTakeOrder(
        idOrder: Int,
        dataTake: String,
        then: (TakeOrderResponse) -> Unit,
        error: (String) -> Unit
    ) {
        uiScope.launch {
            putTakeOrdercorutine(idOrder, dataTake, then, error)
        }
    }

    private suspend fun putTakeOrdercorutine(
        idOrder: Int,
        dataTake: String,
        then: (TakeOrderResponse) -> Unit,
        error: (String) -> Unit
    ) {
        val url = serverUrl + routeBase + routeOrders + idOrder + routeTake
        val request = TakeOrderRequest(dataTake)
        val json = Gson().toJson(request)
        withContext(Dispatchers.IO) {
            put(url, token!!, json).enqueue(object : Callback {

                override fun onResponse(call: Call, response: Response) {
                    val body = response.body?.string()
                    val gson = GsonBuilder().create()
                    val res = gson.fromJson(body, TakeOrderResponse::class.java)
                    if (response.isSuccessful) {
                        then(res)
                    } else {
                        error(res.message.toString())
                        //Log.i("respuesta",response.message)
                    }
                }

                override fun onFailure(call: Call, e: IOException) {
                    error("Error en el servicio")
                }
            })
        }

    }

    fun putReleaseOrder(
        idOrder: Int,
        observations: String?,
        then: (ReleaseOrderResponse) -> Unit,
        error: (String) -> Unit
    ) {
        uiScope.launch {
            putReleaseOrderCorutine(idOrder, observations, then, error)
        }
    }

    private suspend fun putReleaseOrderCorutine(
        idOrder: Int,
        observations: String?,
        then: (ReleaseOrderResponse) -> Unit,
        error: (String) -> Unit
    ) {
        Log.i("elobservaarfva",observations.toString())
        val url = "$serverUrl$routeBase$routeOrders/$idOrder$routeRelease"
        val request = ReleaseOrderRequest(observations)
        val json = Gson().toJson(request)
        withContext(Dispatchers.IO) {
            put(url, token!!, json).enqueue(object : Callback {

                override fun onResponse(call: Call, response: Response) {
                    val body = response.body?.string()
                    val gson = GsonBuilder().create()
                    val res = gson.fromJson(body, ReleaseOrderResponse::class.java)
                    if (response.isSuccessful) {
                        then(res)
                    } else {
                        error(res.message.toString())
                    }
                }

                override fun onFailure(call: Call, e: IOException) {
                    error("Error en el servicio")
                }
            })
        }

    }

    fun putPickingOrder(
        listDataPicker: List<ListDataPicker>,
        idOrder: Int,
        productosok: Boolean,
        totalPicker: String,
        observations: String?,
        then: (PickingOrderResponse) -> Unit,
        error: (String) -> Unit
    ) {
        uiScope.launch {
            putPickingOrderCorutine(
                listDataPicker,
                idOrder,
                productosok,
                totalPicker,
                observations,
                then,
                error
            )
        }
    }

    private suspend fun putPickingOrderCorutine(
        listDataPicker: List<ListDataPicker>,
        idOrder: Int,
        productosok: Boolean,
        totalPicker: String,
        observations: String?,
        then: (PickingOrderResponse) -> Unit,
        error: (String) -> Unit
    ) {
        val url = "$serverUrl$routeBase$routeOrders$idOrder$routePicking"
        val request = PickingOrderRequest(
            idOrder,
            listDataPicker,
            productosok,
            totalPicker,
            observations
        )
        Log.i("ARTICLzzz",listDataPicker.toString())
        val json = Gson().toJson(request)
        withContext(Dispatchers.IO) {
            put(url, token!!, json).enqueue(object : Callback {
                override fun onResponse(call: Call, response: Response) {
                    val body = response.body?.string()

                    val gson = GsonBuilder().create()
                    val res = gson.fromJson(body, PickingOrderResponse::class.java)
                    if (response.isSuccessful) {
                        then(res)
                    } else {
                        error(res.message)

                    }
                }

                override fun onFailure(call: Call, e: IOException) {
                    error("Error en el servicio")
                }
            })
        }

    }

    fun putDeliverCourier(
        idOrder: Int,
        then: (DeliverCourierResponse) -> Unit,
        error: (String) -> Unit
    ) {
        uiScope.launch {
            putDeliverCourierCorutine(idOrder, then, error)
        }
    }


    private suspend fun putDeliverCourierCorutine(
        idOrder: Int,
        then: (DeliverCourierResponse) -> Unit,
        error: (String) -> Unit
    ) {
        val url = "$serverUrl$routeBase$routeOrders$idOrder$routeDeliverCourier"
        val request = DeliverCourierRequest(idOrder)
        val json = Gson().toJson(request)
        withContext(Dispatchers.IO) {
            put(url, token!!, json).enqueue(object : Callback {
                override fun onResponse(call: Call, response: Response) {
                    val body = response.body?.string()
                    val gson = GsonBuilder().create()
                    val res = gson.fromJson(body, DeliverCourierResponse::class.java)
                    if (response.isSuccessful) {
                        then(res)
                    } else {
                        error(res.message.toString())
                    }
                }

                override fun onFailure(call: Call, e: IOException) {
                    error("Error en el servicio")
                }
            })
        }

    }

    fun putDeliverCustomer(
        idOrder: Int,
        then: (DeliverConsumerResponse) -> Unit,
        error: (String) -> Unit
    ) {
        uiScope.launch {
            putDeliverCustomerCorutine(idOrder, then, error)
        }
    }

    private suspend fun putDeliverCustomerCorutine(
        idOrder: Int,
        then: (DeliverConsumerResponse) -> Unit,
        error: (String) -> Unit
    ) {
        val url = "$serverUrl$routeBase$routeOrders/$idOrder$routeDeliverConsumer"
        val request = DeliverConsumerRequest(idOrder)
        val json = Gson().toJson(request)
        withContext(Dispatchers.IO) {
            put(url, token!!, json).enqueue(object : Callback {
                override fun onResponse(call: Call, response: Response) {
                    val body = response.body?.string()
                    val gson = GsonBuilder().create()
                    val res = gson.fromJson(body, DeliverConsumerResponse::class.java)
                    if (response.isSuccessful) {
                        then(res)
                    } else {
                        error(res.message.toString())
                    }
                }

                override fun onFailure(call: Call, e: IOException) {
                    error("Error en el servicio")
                }
            })
        }

    }

    fun putFreeze(
        idOrder: Int,
        idReason: Int,
        then: (FreezeResponse) -> Unit,
        error: (String) -> Unit
    ) {
        uiScope.launch {
            putFreezeCorutine(idOrder, idReason, then, error)
        }
    }

    private suspend fun putFreezeCorutine(
        idOrder: Int,
        idReason: Int,
        then: (FreezeResponse) -> Unit,
        error: (String) -> Unit
    ) {
        val url = "$serverUrl$routeBase$routeOrders$idOrder$routeFreeze"
        val request = FreezeRequest(idReason)
        val json = Gson().toJson(request)
        withContext(Dispatchers.IO) {
            put(token!!, url, json).enqueue(object : Callback {
                override fun onResponse(call: Call, response: Response) {
                    val body = response.body?.string()
                    val gson = GsonBuilder().create()
                    val res = gson.fromJson(body, FreezeResponse::class.java)
                    if (response.isSuccessful) {
                        then(res)
                    } else {
                        error(res.message.toString())
                    }
                }

                override fun onFailure(call: Call, e: IOException) {
                    error("Error en el servicio")
                }
            })
        }

    }

    fun getMe(
        then: (UserResponse) -> Unit,
        error: (String) -> Unit
    ) {
        uiScope.launch {
            getMeCorutine(then, error)
        }
    }

    private suspend fun getMeCorutine(
        then: (UserResponse) -> Unit,
        error: (String) -> Unit
    ) {

        val url = "$serverUrl$routeBase$routePicker$routeMe"
        withContext(Dispatchers.IO) {
            get(url, token!!).enqueue(object : Callback {
                override fun onResponse(call: Call, response: Response) {
                    val body = response.body?.string()
                    val gson = GsonBuilder().create()
                    val res = gson.fromJson(body, UserResponse::class.java)
                    if (response.isSuccessful) {
                        then(res)
                    } else {
                        error(res.message.toString())
                    }
                }

                override fun onFailure(call: Call, e: IOException) {
                    error("Error en el servicio")
                }
            })
        }

    }

}