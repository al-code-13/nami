package com.example.nami

import android.app.Dialog
import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
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
import kotlinx.android.synthetic.main.code_scanner_popup.view.*
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
        recyclerItemsDetail = findViewById(R.id.layoutArticles)
        presenter!!.actionDetail()
        checkBox.setOnClickListener { checkAll(checkBox.isChecked) }
        edit_codecito.inputType = InputType.TYPE_NULL
        edit_codecito.setImeActionLabel("OKis", KeyEvent.KEYCODE_ENTER)
        edit_codecito.setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP) {
                scannerFunction()
                return@OnKeyListener true
            }
            false
        })

    }

    private fun scannerFunction() {

        var productScanned =
            data.order.detailOrder.list.firstOrNull { it.article.upc == edit_codecito.text.toString() }
        if (productScanned != null) {
            var actualCantValue = articleList[data.order.detailOrder.list.indexOf(productScanned)]
            if (actualCantValue < productScanned.quantityArticle) {
                if (productScanned.quantityArticle.toInt() > 1) {
                    var scannerCant = actualCantValue.toInt()
                    val scannerDialog = Dialog(this)
                    val contentScannerDialog =
                        layoutInflater.inflate(R.layout.code_scanner_popup, null)
                    contentScannerDialog.findViewById<TextView>(R.id.sku).text =
                        productScanned.article.upc
                    contentScannerDialog.findViewById<TextView>(R.id.productName).text =
                        productScanned.article.name
                    val cantDialog = contentScannerDialog.findViewById<TextView>(R.id.cant)
                    cantDialog.text = scannerCant.toString()
                    contentScannerDialog.findViewById<Button>(R.id.aceptButton).setOnClickListener {
                        articleList[data.order.detailOrder.list.indexOf(productScanned)] =
                            scannerCant.toString()
                        createArticleView(behavior)
                        scannerDialog.dismiss()
                    }
                    contentScannerDialog.findViewById<Button>(R.id.declineButton)
                        .setOnClickListener {
                            scannerDialog.dismiss()
                        }

                    val minusButton = contentScannerDialog.findViewById<ImageView>(R.id.minusButton)
                    val moreButton = contentScannerDialog.findViewById<ImageView>(R.id.moreButton)

                    minusButton?.setOnClickListener {
                        if (scannerCant > 0) {
                            scannerCant -= 1
                            contentScannerDialog.findViewById<TextView>(R.id.cant).text =
                                scannerCant.toString()
                        } else {

                        }
                        if (scannerCant == 0) {
                            minusButton.visibility = View.INVISIBLE
                        }

                        if (scannerCant < productScanned.quantityArticle.toInt()) {
                            moreButton.visibility = View.VISIBLE
                        }
                    }

                    minusButton?.setOnLongClickListener {
                        if (scannerCant > 0) {
                            scannerCant = 1
                            contentScannerDialog.findViewById<TextView>(R.id.cant).text =
                                scannerCant.toString()
                        } else {

                        }
                        if (scannerCant == 0) {
                            minusButton.visibility = View.INVISIBLE
                        }

                        if (scannerCant < productScanned.quantityArticle.toInt()) {
                            moreButton.visibility = View.VISIBLE
                        }
                        it.isActivated
                    }


                    val oldvalue = productScanned.quantityArticle.toInt()

                    moreButton?.setOnClickListener {
                        if (scannerCant < oldvalue) {
                            contentScannerDialog.cant.setTypeface(
                                Typeface.create(
                                    contentScannerDialog.cant.typeface,
                                    Typeface.NORMAL
                                ), Typeface.NORMAL
                            )

                            scannerCant = scannerCant + 1
                            contentScannerDialog.findViewById<TextView>(R.id.cant).text =
                                scannerCant.toString()
                            Log.i("elnuebvo", scannerCant.toString())
                        } else {
                            contentScannerDialog.cant.setTypeface(
                                contentScannerDialog.cant.typeface,
                                Typeface.BOLD
                            )
                        }
                        if (scannerCant == oldvalue) {
                            moreButton.visibility = View.INVISIBLE
                        }
                        if (scannerCant > 0) {
                            minusButton.visibility = View.VISIBLE
                        }
                    }

                    moreButton?.setOnLongClickListener {
                        if (scannerCant < oldvalue) {
                            contentScannerDialog.cant.setTypeface(
                                Typeface.create(
                                    contentScannerDialog.cant.typeface,
                                    Typeface.NORMAL
                                ), Typeface.NORMAL
                            )

                            scannerCant = oldvalue - 1
                            contentScannerDialog.findViewById<TextView>(R.id.cant).text =
                                scannerCant.toString()
                            Log.i("elnuebvo", scannerCant.toString())
                        } else {
                            contentScannerDialog.cant.setTypeface(
                                contentScannerDialog.cant.typeface,
                                Typeface.BOLD
                            )
                        }
                        if (scannerCant == oldvalue) {
                            moreButton.visibility = View.INVISIBLE
                        }
                        if (scannerCant > 0) {
                            minusButton.visibility = View.VISIBLE
                        }
                        it.isActivated
                    }
                    scannerDialog.setContentView(contentScannerDialog)
                    scannerDialog.show()
                } else {
                    articleList[data.order.detailOrder.list.indexOf(productScanned)] = "1"
                    createArticleView(behavior)
                    Toast.makeText(
                        this,
                        "Producto agregado correctamente",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } else {
                Toast.makeText(
                    this,
                    "El producto ya alcanzo el limite permitido",
                    Toast.LENGTH_SHORT
                ).show()
            }
        } else {
            Toast.makeText(this, "El producto no se encuentra en esta orden", Toast.LENGTH_SHORT)
                .show()
        }
        edit_codecito.text.clear()
    }

    private fun createArticleView(newFunction: Int) {
        behavior = newFunction
        recyclerItemsDetail!!.setHasFixedSize(true)
        recyclerItemsDetail!!.layoutManager = LinearLayoutManager(this)
        recyclerItemsDetail?.adapter =
            ItemsDetailAdapter(
                this,
                data.order.detailOrder.list,
                newFunction,
                this.articleList,
                {calculateAdjustTotal()},
                checkBox,
                compareArticleList
            )

        calculateAdjustTotal()
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
            } else {
                checkBox.visibility = View.VISIBLE
            }
            buttonsLinearLayout.removeAllViews()

            val actionsList =
                ServiceFactory.data.behaviors!!.firstOrNull { it.id == newFunction }?.actions

            for (i in actionsList!!) {

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
                            articleList
                        )
                    }
                    buttonsLinearLayout.addView(layoutNewButton)
                }
            }
        }
    }

    private fun calculateAdjustTotal() {
        adjustvalue = data.order.deliveryValue.toDouble()
        for (i in data.order.detailOrder.list) {
            var unitValue: Double = i.valueTotalArticle.toDouble() / i.quantityArticle.toDouble()
            adjustvalue += unitValue * articleList[data.order.detailOrder.list.indexOf(i)].toDouble()
            Log.i("valor ajustao", adjustvalue.toString())
        }
        adjustTotal.text= NumberFormat.getCurrencyInstance(Locale("es","CO")).format(Detail.adjustvalue).toString()
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