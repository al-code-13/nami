package com.example.nami

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.nami.adapter.ItemsDetailAdapter
import com.example.nami.controllers.services.ServiceFactory
import com.example.nami.models.detailModels.CompareListElement
import com.example.nami.models.detailModels.DetailResponse
import com.example.nami.presenters.DetailPresenter
import com.example.nami.presenters.DetailUI
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.android.synthetic.main.action_item.view.*
import kotlinx.android.synthetic.main.activity_detail.*

class Detail : AppCompatActivity(), DetailUI {

    private var presenter: DetailPresenter? = null
    var recyclerItemsDetail: RecyclerView? = null
    var userInfo = arrayOf<String>()
    var behavior = -1
    private var observations: String? = null
    lateinit var data: DetailResponse
    var articleList: MutableList<String> = mutableListOf<String>()
    private lateinit var observationsView: EditText


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
        Log.i("Id de la orden", userInfo.toString())
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
    }

    private fun createArticleView(newFunction: Int) {
        recyclerItemsDetail!!.setHasFixedSize(true)
        recyclerItemsDetail!!.layoutManager = LinearLayoutManager(this)
        recyclerItemsDetail?.adapter =
            ItemsDetailAdapter(this, data.order.detailOrder.list, newFunction, this.articleList)
    }

    override fun showDetailInfo(data: DetailResponse) {
        runOnUiThread {
            if (data.order.service == "D") {
                type.text = "Domicilio"
            }
            orderValue.text = userInfo[4]
            delivered.text = data.order.deliveryValue
            totalValue.text = userInfo[4]
            adjustTotal.text = userInfo[4]
            comments.text = data.order.comments
            // change
            // pay.text =
            this.data = data
            for (i in data.order.detailOrder.list) {
                articleList.add(
                    data.order.detailOrder.list.indexOf(i),
                    i.quantityArticle
                )
            }
            createArticleView(behavior)
            createButtons(behavior)
        }
    }

    private fun createButtons(newFunction: Int) {
        runOnUiThread {
            buttonsLinearLayout.removeAllViews()

            val actionsList =
                ServiceFactory.data.behaviors.firstOrNull { it.id == newFunction }?.actions

            if (actionsList!!.contains(5) || actionsList.contains(4)) {
                observationsView.visibility = View.VISIBLE
            } else {
                observationsView.visibility = View.GONE
            }

            for (i in actionsList) {

                if (i != 2) {
                    val action = ServiceFactory.data.actions.firstOrNull { it.id == i }
                    val layoutNewButton = layoutInflater.inflate(R.layout.save_button, null)
                    val button = layoutNewButton.findViewById<Button>(R.id.pickButton)
                    Log.i("accion", action!!.name)
                    if (action.destructive) {
                        button.setBackgroundColor(Color.parseColor("#ff0000"))
                    }
                    val param: ViewGroup.MarginLayoutParams =
                        button.layoutParams as ViewGroup.MarginLayoutParams
                    param.setMargins(0, 6, 6, 6)
                    button.layoutParams = param
                    button.text = "${action.name}"
                    button.setOnClickListener {
                        when (action.id) {
                            1 -> {

                            }
                            3 -> {
                                presenter!!.actionTake()
                            }
                            4 -> {

                                val dialog = BottomSheetDialog(this)
                                val dialogView =
                                    LayoutInflater.from(this).inflate(R.layout.activity_popup, null)
                                val title = dialogView.findViewById<TextView>(R.id.titleOrderId)
                                title.text = "¿Esta seguro de guardar esta orden?"

                                val layoutActions =
                                    dialogView.findViewById<LinearLayout>(R.id.listActions)
                                val v: View =
                                    LayoutInflater.from(this).inflate(R.layout.action_item, null)
                                v.setOnClickListener {
                                    observations = observationsView.text.toString()
                                    observationsView.text = null
                                    presenter!!.actionPick(data, articleList, observations)
                                    dialog.dismiss()
                                }
                                v.action.text = "Aceptar"
                                v.action.setCompoundDrawablesWithIntrinsicBounds(
                                    R.drawable.yes_action,
                                    0,
                                    0,
                                    0
                                )
                                layoutActions.addView(v)

                                val cancel: View =
                                    LayoutInflater.from(this).inflate(R.layout.action_item, null)
                                cancel.setOnClickListener {
                                    dialog.dismiss()
                                }
                                cancel.action.text = "Cancelar"
                                cancel.action.setCompoundDrawablesWithIntrinsicBounds(
                                        R.drawable.cancel,
                                0,
                                0,
                                0
                                )
                                layoutActions.addView(cancel)

                                dialog.setContentView(dialogView)
                                dialog.show()
                            }
                            5 -> {
                                val dialog = BottomSheetDialog(this)
                                val dialogView =
                                    LayoutInflater.from(this).inflate(R.layout.activity_popup, null)
                                val title = dialogView.findViewById<TextView>(R.id.titleOrderId)
                                title.text = "¿Esta seguro de liberar esta orden?"

                                val layoutActions =
                                    dialogView.findViewById<LinearLayout>(R.id.listActions)
                                val v: View =
                                    LayoutInflater.from(this).inflate(R.layout.action_item, null)
                                v.setOnClickListener {
                                    observations = observationsView.text.toString()
                                    observationsView.text = null
                                    presenter!!.actionRelease(observations)
                                    dialog.dismiss()
                                }
                                v.action.text = "Aceptar"
                                v.action.setCompoundDrawablesWithIntrinsicBounds(
                                    R.drawable.yes_action,
                                    0,
                                    0,
                                    0
                                )
                                layoutActions.addView(v)

                                val cancel: View =
                                    LayoutInflater.from(this).inflate(R.layout.action_item, null)
                                cancel.setOnClickListener {
                                    dialog.dismiss()
                                }
                                cancel.action.text = "Cancelar"
                                cancel.action.setCompoundDrawablesWithIntrinsicBounds(
                                        R.drawable.cancel,
                                0,
                                0,
                                0
                                )
                                layoutActions.addView(cancel)

                                dialog.setContentView(dialogView)
                                dialog.show()
                            }
                            6 -> {
                                presenter!!.actionPutDeliverCourier()
                            }
                            7 -> {
                                presenter!!.actionPutDeliverCustomer()
                            }
                            8 -> {
                                val freezeActions = ServiceFactory.reasons.reasons.list
                                val dialog = BottomSheetDialog(this)
                                val dialogView =
                                    LayoutInflater.from(this).inflate(R.layout.activity_popup, null)
                                val title = dialogView.findViewById<TextView>(R.id.titleOrderId)
                                title.text = "¿Esta seguro de congelar esta orden?"
                                val layoutActions =
                                    dialogView.findViewById<LinearLayout>(R.id.listActions)
                                for (i in freezeActions) {
                                    val v: View =
                                        LayoutInflater.from(this)
                                            .inflate(R.layout.action_item, null)
                                    v.setOnClickListener {
                                        presenter!!.actionPutFreeze(i.id)
                                        dialog.dismiss()
                                    }
                                    v.action.text = i.description
                                    v.action.setCompoundDrawablesWithIntrinsicBounds(
                                        R.drawable.freeze_icon,
                                        0,
                                        0,
                                        0
                                    )
                                    layoutActions.addView(v)
                                }
                                val cancel: View =
                                    LayoutInflater.from(this).inflate(R.layout.action_item, null)
                                cancel.setOnClickListener {
                                    dialog.dismiss()
                                }
                                cancel.action.text = "Cancelar"
                                cancel.action.setCompoundDrawablesWithIntrinsicBounds(
                                    R.drawable.cancel,
                                    0,
                                    0,
                                    0
                                )
                                layoutActions.addView(cancel)
                                dialog.setContentView(dialogView)
                                dialog.show()
                            }
                        }
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