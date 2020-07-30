package com.example.nami.utils

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import com.example.nami.BuildConfig
import com.example.nami.Detail
import com.example.nami.R
import com.example.nami.controllers.services.ServiceFactory
import com.example.nami.models.detailModels.DetailResponse
import com.example.nami.models.sections.OrdersList
import com.example.nami.presenters.DetailPresenter
import com.example.nami.presenters.SectionPresenter
import com.google.android.material.bottomsheet.BottomSheetDialog
import io.realm.RealmList
import kotlinx.android.synthetic.main.action_item.view.*

class ButtonDialogActions {
    fun actionsSection(
        mContext: Context,
        presenter: SectionPresenter,
        layoutActions: LinearLayout,
        dialog: BottomSheetDialog,
        items: OrdersList,
        actions: RealmList<Int>?,
        verDetalle: (OrdersList) -> Unit
    ) {
        for (id in actions!!) {
            val v: View =
                LayoutInflater.from(mContext).inflate(R.layout.action_item, null)
            v.setOnClickListener {
                if (id == 2 || id == 4) {//Ver detalle
                    verDetalle(items)
                } else {
                    when (id) {
                        3 -> {
                            presenter.actionTake(items.id!!)
                        }
                        5 -> {
                            val dialog = BottomSheetDialog(mContext)
                            val dialogView =
                                LayoutInflater.from(mContext)
                                    .inflate(R.layout.activity_popup, null)
                            val title =
                                dialogView.findViewById<TextView>(R.id.titleOrderId)
                            title.text = "¿Esta seguro de liberar esta orden?"

                            val observationsView =
                                dialogView.findViewById<EditText>(R.id.editObservations)
                            observationsView.visibility = View.VISIBLE
                            val layoutActions =
                                dialogView.findViewById<LinearLayout>(R.id.listActions)
                            val v: View =
                                LayoutInflater.from(mContext)
                                    .inflate(R.layout.action_item, null)
                            v.setOnClickListener {
                                var observations = observationsView.text.toString()
                                presenter.actionRelease(items.id!!, observations)
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
                                LayoutInflater.from(mContext)
                                    .inflate(R.layout.action_item, null)
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

                            val dialog = BottomSheetDialog(mContext)
                            val dialogView =
                                LayoutInflater.from(mContext)
                                    .inflate(R.layout.activity_popup, null)
                            val title =
                                dialogView.findViewById<TextView>(R.id.titleOrderId)
                            title.text =
                                "¿Esta seguro de entregar esta orden a un domiciliario?"
                            val emailEdit = dialogView.findViewById<EditText>(R.id.editEmail)
                            val phoneEdit = dialogView.findViewById<EditText>(R.id.editPhone)

                            if (BuildConfig.DEBUG) {
                                emailEdit!!.visibility = View.VISIBLE
                                phoneEdit!!.visibility = View.VISIBLE
                            }

                            val layoutActions =
                                dialogView.findViewById<LinearLayout>(R.id.listActions)
                            val v: View =
                                LayoutInflater.from(mContext)
                                    .inflate(R.layout.action_item, null)
                            v.setOnClickListener {
                                if (BuildConfig.DEBUG) {
                                    presenter.actionPutDeliverCourier(
                                        items.id!!,
                                        emailEdit.text.toString(),
                                        phoneEdit.text.toString()
                                    )
                                } else {
                                    presenter.actionPutDeliverCourier(
                                        items.id!!
                                    )
                                }
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
                                LayoutInflater.from(mContext)
                                    .inflate(R.layout.action_item, null)
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
                        7 -> {

                            val dialog = BottomSheetDialog(mContext)
                            val dialogView =
                                LayoutInflater.from(mContext)
                                    .inflate(R.layout.activity_popup, null)
                            val title =
                                dialogView.findViewById<TextView>(R.id.titleOrderId)
                            title.text = "¿Esta seguro de entregar esta orden al cliente?"
                            val emailEdit = dialogView.findViewById<EditText>(R.id.editEmail)
                            val phoneEdit = dialogView.findViewById<EditText>(R.id.editPhone)
                            val codeEdit = dialogView.findViewById<EditText>(R.id.editCode)
                            codeEdit!!.visibility = View.VISIBLE

                            if (BuildConfig.DEBUG) {
                                emailEdit!!.visibility = View.VISIBLE
                                phoneEdit!!.visibility = View.VISIBLE

                            }
                            val layoutActions =
                                dialogView.findViewById<LinearLayout>(R.id.listActions)
                            val v: View =
                                LayoutInflater.from(mContext)
                                    .inflate(R.layout.action_item, null)
                            v.setOnClickListener {
                                presenter.actionPutConfirmDelivery(
                                    items.id!!,
                                    codeEdit.text.toString()
                                )
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

                            if (BuildConfig.DEBUG) {
                                val reSend: View =
                                    LayoutInflater.from(mContext).inflate(R.layout.action_item, null)
                                reSend.setOnClickListener {
                                    presenter.actionPutSendConfirmation(
                                        items.id!!,
                                        emailEdit!!.text.toString(),
                                        phoneEdit!!.text.toString()
                                    )
                                   // dialog.dismiss()
                                }
                                reSend.action.text = "Reenviar codigo"
                                reSend.action.setCompoundDrawablesWithIntrinsicBounds(
                                    R.drawable.yes_action,
                                    0,
                                    0,
                                    0
                                )
                                layoutActions.addView(reSend)
                            }
                            val cancel: View =
                                LayoutInflater.from(mContext)
                                    .inflate(R.layout.action_item, null)
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

                        8 -> {
                            val freezeActions = ServiceFactory.reasons.reasons.list
                            val dialog = BottomSheetDialog(mContext)
                            val dialogView =
                                LayoutInflater.from(mContext)
                                    .inflate(R.layout.activity_popup, null)
                            val title =
                                dialogView.findViewById<TextView>(R.id.titleOrderId)
                            title.text = "¿Esta seguro de congelar esta orden?"
                            val layoutActions =
                                dialogView.findViewById<LinearLayout>(R.id.listActions)
                            for (i in freezeActions) {
                                val v: View =
                                    LayoutInflater.from(mContext)
                                        .inflate(R.layout.action_item, null)
                                v.setOnClickListener {
                                    presenter.actionPutFreeze(items.id!!, i.id)
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
                                LayoutInflater.from(mContext)
                                    .inflate(R.layout.action_item, null)
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
                dialog.dismiss()
            }

            v.action.text =
                ServiceFactory.data.actions!!.firstOrNull { it.id == id }?.name

            when (id) {
                4 -> {
                    v.action.setCompoundDrawablesWithIntrinsicBounds(
                        R.drawable.save_icon,
                        0,
                        0,
                        0
                    )
                }
                5 -> {
                    v.action.setCompoundDrawablesWithIntrinsicBounds(
                        R.drawable.release,
                        0,
                        0,
                        0
                    )
                }
                8 -> {
                    v.action.setCompoundDrawablesWithIntrinsicBounds(
                        R.drawable.freeze_icon,
                        0,
                        0,
                        0
                    )
                }
                6 -> {
                    v.action.setCompoundDrawablesWithIntrinsicBounds(
                        R.drawable.car,
                        0,
                        0,
                        0
                    )
                }
                6 -> {
                    v.action.setCompoundDrawablesWithIntrinsicBounds(
                        R.drawable.persons,
                        0,
                        0,
                        0
                    )
                }
                8 -> {
                    v.action.setCompoundDrawablesWithIntrinsicBounds(
                        R.drawable.freeze_icon,
                        0,
                        0,
                        0
                    )
                }
            }
            layoutActions.addView(v)
        }


    }

    fun actionsDetail(
        mContext: Context, presenter: DetailPresenter, id: Int, data: DetailResponse,
        compareArticleList: List<String>,
        articleList: MutableList<String> = mutableListOf<String>()
    ) {
        var observations: String? = null
        when (id) {
            1 -> {

            }
            3 -> {
                presenter.actionTake()
            }
            4 -> {
                val dialog = BottomSheetDialog(mContext)
                val dialogView =
                    LayoutInflater.from(mContext).inflate(R.layout.activity_popup, null)
                val title = dialogView.findViewById<TextView>(R.id.titleOrderId)
                title.text = "¿Esta seguro de guardar esta orden?"

                dialogView.findViewById<EditText>(R.id.editObservations).visibility = View.VISIBLE

                val layoutActions =
                    dialogView.findViewById<LinearLayout>(R.id.listActions)
                val v: View =
                    LayoutInflater.from(mContext).inflate(R.layout.action_item, null)
                v.setOnClickListener {
                    observations =
                        dialogView.findViewById<EditText>(R.id.editObservations).text.toString()
                    dialogView.findViewById<EditText>(R.id.editObservations).text = null
                    presenter.actionPick(
                        data,
                        Detail.adjustvalue,
                        compareArticleList,
                        articleList,
                        observations
                    )
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
                    LayoutInflater.from(mContext).inflate(R.layout.action_item, null)
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
                val dialog = BottomSheetDialog(mContext)
                val dialogView =
                    LayoutInflater.from(mContext).inflate(R.layout.activity_popup, null)
                val title = dialogView.findViewById<TextView>(R.id.titleOrderId)
                title.text = "¿Esta seguro de liberar esta orden?"

                dialogView.findViewById<EditText>(R.id.editObservations).visibility = View.VISIBLE
                val layoutActions =
                    dialogView.findViewById<LinearLayout>(R.id.listActions)
                val v: View =
                    LayoutInflater.from(mContext).inflate(R.layout.action_item, null)
                v.setOnClickListener {
                    observations =
                        dialogView.findViewById<EditText>(R.id.editObservations).text.toString()
                    dialogView.findViewById<EditText>(R.id.editObservations).text = null
                    presenter.actionRelease(observations)
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
                    LayoutInflater.from(mContext).inflate(R.layout.action_item, null)
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
                val dialog = BottomSheetDialog(mContext)
                val dialogView =
                    LayoutInflater.from(mContext).inflate(R.layout.activity_popup, null)
                val title = dialogView.findViewById<TextView>(R.id.titleOrderId)
                val emailEdit = dialogView.findViewById<EditText>(R.id.editEmail)
                val phoneEdit = dialogView.findViewById<EditText>(R.id.editPhone)

                if (BuildConfig.DEBUG) {
                    emailEdit!!.visibility = View.VISIBLE
                    phoneEdit!!.visibility = View.VISIBLE
                }
                title.text =
                    "¿Esta seguro de entregar esta orden a un domiciliario?"

                val layoutActions =
                    dialogView.findViewById<LinearLayout>(R.id.listActions)
                val v: View =
                    LayoutInflater.from(mContext).inflate(R.layout.action_item, null)
                v.setOnClickListener {
                    if (BuildConfig.DEBUG) {
                        presenter.actionPutDeliverCourier(
                            emailEdit!!.text.toString(),
                            phoneEdit.text.toString()
                        )
                    } else {
                        presenter.actionPutDeliverCourier()
                    }
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
                    LayoutInflater.from(mContext).inflate(R.layout.action_item, null)
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
            7 -> {
                val dialog = BottomSheetDialog(mContext)
                val dialogView =
                    LayoutInflater.from(mContext).inflate(R.layout.activity_popup, null)
                val title = dialogView.findViewById<TextView>(R.id.titleOrderId)
                title.text = "¿Esta seguro de entregar la orden al Cliente?"
                val emailEdit = dialogView.findViewById<EditText>(R.id.editEmail)
                val phoneEdit = dialogView.findViewById<EditText>(R.id.editPhone)
                val codeEdit = dialogView.findViewById<EditText>(R.id.editCode)
                codeEdit!!.visibility = View.VISIBLE

                if (BuildConfig.DEBUG) {
                    emailEdit!!.visibility = View.VISIBLE
                    phoneEdit!!.visibility = View.VISIBLE
                }
                val layoutActions =
                    dialogView.findViewById<LinearLayout>(R.id.listActions)
                val v: View =
                    LayoutInflater.from(mContext).inflate(R.layout.action_item, null)
                v.setOnClickListener {
                    presenter.actionPutDeliverCustomer(codeEdit.text.toString())
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
                if (BuildConfig.DEBUG) {
                    val reSend: View =
                        LayoutInflater.from(mContext).inflate(R.layout.action_item, null)
                    reSend.setOnClickListener {
                        presenter.actionPutSendConfirmation(
                            emailEdit!!.text.toString(),
                            phoneEdit!!.text.toString()
                        )
                        // dialog.dismiss()
                    }
                    reSend.action.text = "Reenviar codigo"
                    reSend.action.setCompoundDrawablesWithIntrinsicBounds(
                        R.drawable.yes_action,
                        0,
                        0,
                        0
                    )
                    layoutActions.addView(reSend)
                }
                val cancel: View =
                    LayoutInflater.from(mContext).inflate(R.layout.action_item, null)
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
            8 -> {
                val freezeActions = ServiceFactory.reasons.reasons.list
                val dialog = BottomSheetDialog(mContext)
                val dialogView =
                    LayoutInflater.from(mContext).inflate(R.layout.activity_popup, null)
                val title = dialogView.findViewById<TextView>(R.id.titleOrderId)
                title.text = "¿Esta seguro de congelar esta orden?"
                val layoutActions =
                    dialogView.findViewById<LinearLayout>(R.id.listActions)
                for (i in freezeActions) {
                    val v: View =
                        LayoutInflater.from(mContext)
                            .inflate(R.layout.action_item, null)
                    v.setOnClickListener {
                        presenter.actionPutFreeze(i.id)
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
                    LayoutInflater.from(mContext).inflate(R.layout.action_item, null)
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
}
