package com.chefmenu.nami.controllers.services

import com.chefmenu.nami.models.sections.ReasonsResponse
import com.chefmenu.nami.models.sections.SectionsResponse
import okhttp3.Call
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException
import java.util.concurrent.TimeUnit

open class ServiceFactory {

    val JSON = "application/json; charset=utf-8".toMediaType()
    val routeBase: String = "/api/v2"
    val routeAuth: String = "/auth"
    val routePicker: String = "/pickers"
    val routeBranchs: String = "/branchs"
    val routeOrders: String = "/orders/"
    val routeLogin: String = "/login"
    val routeSection:String="/section"
    val routeSections: String = "/sections"
    val routeReasons: String = "/reasons"
    val routeTake: String = "/take"
    val routeRelease: String = "/release"
    val routePicking = "/checked"
    val routeDeliverCourier: String = "/deliver-courier"
    val routeconfirmDelivery: String = "/confirm-delivery"
    val routeSendConfirmation:String="/send-confirmation"
    val routeFreeze: String = "freeze"
    val routeMe: String = "/me"
    val routeProfits:String="/profits"

    var timeOut = 40L

    private val client: OkHttpClient =
        OkHttpClient().newBuilder().connectTimeout(timeOut, TimeUnit.SECONDS)
            .readTimeout(timeOut, TimeUnit.SECONDS).build()


    @Throws(IOException::class)
    fun get(url: String, token: String): Call {
        val request: Request = Request.Builder()
            .url(url)
            .addHeader("x-access-token-nami", token)
            .build()
        //client!!.newCall(request).execute().use { response -> return response.body!!.string() }

        return client.newCall(request)
    }

    @Throws(IOException::class)
    fun post(url: String, json: String): Call {
        val body = json.toRequestBody(JSON)
        val request: Request = Request.Builder()
            .url(url)
            .post(body)
            .build()
        //client!!.newCall(request).execute().use { response -> return response.body!!.string() }

        return client.newCall(request)
    }

    @Throws(IOException::class)
    fun put(url: String, token: String, json: String): Call {
        val body = json.toRequestBody(JSON)
        val request: Request = Request.Builder()
            .url(url)
            .addHeader("x-access-token-nami", token)
            .put(body)
            .build()
        //client!!.newCall(request).execute().use { response -> return response.body!!.string() }

        return client.newCall(request)
    }

    companion object {
        //Desarrollo
        val serverUrl: String = "https://d1-dev-test.chefmenu.com.co:6443"
        //stage
        // val serverUrl: String = "https://d1-picking-test.chefmenu.com.co"
        lateinit var data: SectionsResponse
        var token: String? = null
        lateinit var reasons: ReasonsResponse
      //  var development:Boolean = this.serverUrl=="https://d1-dev-test.chefmenu.com.co:6443"
    }


}