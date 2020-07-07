package com.example.nami

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.nami.adapter.ItemsDetailAdapter
import com.example.nami.controllers.services.ServiceFactory
import com.example.nami.models.detailModels.DetailResponse
import com.example.nami.presenters.DetailPresenter
import com.example.nami.presenters.DetailUI
import com.example.nami.utils.ButtonDialogActions
import kotlinx.android.synthetic.main.activity_detail.*
import java.text.NumberFormat
import java.util.*

class Detail : AppCompatActivity(), DetailUI {
    private val numberFormat = NumberFormat.getCurrencyInstance(Locale("es", "CO"))
    private var presenter: DetailPresenter? = null
    var recyclerItemsDetail: RecyclerView? = null
    var userInfo = arrayOf<String>()
    var behavior = -1
    lateinit var data: DetailResponse
    var articleList: MutableList<String> = mutableListOf<String>()
    var compareArticleList: MutableList<String> = mutableListOf<String>()
    private lateinit var observationsView: EditText

    companion object {
        var adjustvalue: Double = 0.0
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val intent: Intent = intent
        val orderId = intent.getIntExtra("orderId", -1)

        val actionbar = supportActionBar
        actionbar!!.title = "Orden #$orderId"
        actionbar.setBackgroundDrawable(ColorDrawable(Color.RED))
        behavior = intent.getIntExtra("behavior", -1)
        userInfo = intent.getStringArrayExtra("userInfo")
        presenter = DetailPresenter(orderId, this)
        name.text = userInfo[1] + " " + userInfo[2]
        idProduct.text = userInfo[0]
        phoneNumber.text = userInfo[5]
        method.text = userInfo[12]
        adress.text = userInfo[3]
        date.text = userInfo[6]
        time.text = userInfo[9].substring(0, userInfo[9].length - 13)
        observationsView = findViewById(R.id.editObservations)
        recyclerItemsDetail = findViewById(R.id.layoutArticles)
        presenter!!.actionDetail()
        checkBox.setOnClickListener { checkAll(checkBox.isChecked) }

    }

    private fun createArticleView(newFunction: Int) {
        recyclerItemsDetail!!.setHasFixedSize(true)
        recyclerItemsDetail!!.layoutManager = LinearLayoutManager(this)
        recyclerItemsDetail?.adapter =
            ItemsDetailAdapter(
                this,
                data.order.detailOrder.list,
                newFunction,
                this.articleList,
                adjustTotal,
                checkBox,
                compareArticleList
            )
        //checkBox.isChecked = compareArticleList.equals(articleList)
    }

    override fun showDetailInfo(data: DetailResponse) {
        runOnUiThread {
            if (data.order.service == "D") {
                type.text = "Domicilio"
            }
            if (userInfo[12] != "Datafono") {
                pay.text = numberFormat.format(data.order.turns.toDouble()).toString()
                change.text =
                    numberFormat.format(data.order.turns.toDouble() - adjustvalue.toDouble())
                        .toString()
            } else {
                pay.text = "No Aplica"
                change.text = "No Aplica"
            }
            orderValue.text =
                numberFormat.format(userInfo[4].toDouble() - data.order.deliveryValue.toDouble())
                    .toString()
            delivered.text = numberFormat.format(data.order.deliveryValue.toDouble()).toString()
            adjustvalue = data.order.deliveryValue.toDouble()
            totalValue.text = numberFormat.format(userInfo[4].toDouble()).toString()
            comments.text = data.order.comments
            this.data = data
            for (i in data.order.detailOrder.list) {
                articleList.add(
                    data.order.detailOrder.list.indexOf(i),
                    "0"
                )
                compareArticleList.add(
                    data.order.detailOrder.list.indexOf(i),
                    i.quantityArticle
                )
            }
            createArticleView(behavior)
            createButtons(behavior)
        }
    }

    fun checkAll(deliveryOk: Boolean) {
        if (deliveryOk) {
            for (i in data.order.detailOrder.list) {
                articleList[data.order.detailOrder.list.indexOf(i)] =
                    compareArticleList[data.order.detailOrder.list.indexOf(i)]
            }
            adjustvalue = userInfo[4].toDouble()
            checkBox.isChecked = true
        } else {
            for (i in data.order.detailOrder.list) {
                articleList[data.order.detailOrder.list.indexOf(i)] = "0"
            }
            checkBox.isChecked = false

            adjustvalue = data.order.deliveryValue.toDouble()
        }
        createArticleView(behavior)
    }

    private fun createButtons(newFunction: Int) {
        runOnUiThread {
            if (newFunction != 2) {
                checkBox.visibility = View.GONE
            }
            else{
                checkBox.visibility = View.VISIBLE
            }
            buttonsLinearLayout.removeAllViews()

            val actionsList =
                ServiceFactory.data.behaviors!!.firstOrNull { it.id == newFunction }?.actions

            if (actionsList!!.contains(5) || actionsList.contains(4)) {
                observationsView.visibility = View.VISIBLE
            } else {
                observationsView.visibility = View.GONE
            }

            for (i in actionsList) {

                if (i != 2 && i != 8) {
                    val action = ServiceFactory.data.actions!!.firstOrNull { it.id == i }
                    val layoutNewButton = layoutInflater.inflate(R.layout.save_button, null)
                    val button = layoutNewButton.findViewById<Button>(R.id.pickButton)
                    if (action?.destructive!!) {
                        button.setBackgroundResource(R.drawable.button_red_background)
                    }
                    val param: ViewGroup.MarginLayoutParams =
                        button.layoutParams as ViewGroup.MarginLayoutParams
                    param.setMargins(6, 6, 6, 6)
                    button.setPadding(6, 6, 6, 6)
                    button.layoutParams = param
                    button.text = "${action.name}"
                    button.setOnClickListener {
                        ButtonDialogActions().actionsDetail(
                            this,
                            presenter!!,
                            action.id!!,
                            data,
                            articleList,
                            observationsView
                        )
                    }
                    buttonsLinearLayout.addView(layoutNewButton)
                }
            }
        }
    }

    override fun showError(error: String) {
        runOnUiThread {
            Toast.makeText(this, error, Toast.LENGTH_LONG).show()
        }
    }

    override fun showDetailFunctionReleased() {
        runOnUiThread {
            createArticleView(3)
            createButtons(3)
        }
    }

    override fun showDetailFunctionPicked() {
        runOnUiThread {
            createArticleView(7)
            createButtons(7)
        }
    }

    override fun showDetailFunctionTaked() {
        runOnUiThread {
            createArticleView(2)
            createButtons(2)
        }
    }

    override fun showDetailFunctioDeliverCourier() {
        runOnUiThread {
            createArticleView(8)
            createButtons(8)
        }
    }

    override fun showDetailFunctionDeliverCustomer() {
        runOnUiThread {
            createArticleView(9)
            createButtons(9)
        }
    }

    override fun showDetailFunctionFreeze() {
        runOnUiThread {
            createArticleView(behavior)
            createButtons(behavior)
        }
    }

}