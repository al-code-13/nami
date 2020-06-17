package com.example.nami.controllers.services

import com.example.nami.models.sections.ReasonsResponse
import com.example.nami.models.sections.SectionsResponse
import okhttp3.Call
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException

open class ServiceFactory {

    val JSON = "application/json; charset=utf-8".toMediaType()

    val serverUrl: String = "https://d1-dev-test.chefmenu.com.co:6443"
    val routeBase: String = "/api/v2"
    val routeAuth: String = "/auth"
    val routePicker: String = "/pickers"
    val routeOrders: String = "/orders/"
    val routeLogin: String = "/login"
    val routeSections: String = "/sections"
    val routeReasons:String="/reasons"
    val routeTake: String = "/take"
    val routeRelease: String = "/release"
    val routePicking = "/checked"
    val routeDeliverCourier: String = "/deliver-courier"
    val routeDeliverConsumer: String = "/deliver-customer"
    val routeFreeze: String = "freeze"
    val routeMe: String = "/me"



    private val client: OkHttpClient = OkHttpClient().newBuilder().build()


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
        lateinit var data: SectionsResponse
        var token: String?=null
        lateinit var reasons: ReasonsResponse
    }


}