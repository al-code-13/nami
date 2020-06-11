package com.example.nami.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.nami.Detail
import com.example.nami.R
import com.example.nami.controllers.services.ServiceFactory
import com.example.nami.models.sections.OrdersList
import com.example.nami.presenters.SectionPresenter
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.android.synthetic.main.action_item.view.*

class OrdersAdapter(
    private val mContext: Context,
    private val mDataSet: List<OrdersList>,
    private val presenter: SectionPresenter
) :
    RecyclerView.Adapter<OrdersAdapter.ViewHolder>() {

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {

        var card: CardView = v.findViewById(R.id.card)
        var names: TextView = v.findViewById(R.id.name)
        var idOrder: TextView = v.findViewById(R.id.idOrder)
        var amount: TextView = v.findViewById(R.id.amount)
        var date: TextView = v.findViewById(R.id.date)
        var time: TextView = v.findViewById(R.id.time)
        var cell: TextView = v.findViewById(R.id.phone)
        var total: TextView = v.findViewById(R.id.total)


    }

    fun <T : RecyclerView.ViewHolder> T.listen(event: (position: Int, type: Int) -> Unit): T {
        itemView.setOnClickListener {
            event.invoke(adapterPosition, itemViewType)
        }
        return this
    }

    @SuppressLint("ResourceAsColor")
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val v: View =
            LayoutInflater.from(mContext).inflate(R.layout.card_view_item_grid, parent, false)

        return ViewHolder(v).listen { pos, _ ->
            val items = mDataSet[pos]
            val legend = ServiceFactory.data.behaviors.firstOrNull { it.id == items.behavior }
            if (legend != null) {
                if (legend.action != null) {
                    verDetalle(items!!)
                } else {
                    val dialog = BottomSheetDialog(mContext)
                    val dialogView =
                        LayoutInflater.from(mContext).inflate(R.layout.activity_popup, null)
                    val title = dialogView.findViewById<TextView>(R.id.titleOrderId)
                    title.text = "Orden #${items.id}"
                    val layoutActions = dialogView.findViewById<LinearLayout>(R.id.listActions)
                    for (id in legend.actions!!) {
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
                                        val layoutActions =
                                            dialogView.findViewById<LinearLayout>(R.id.listActions)
                                        val v: View =
                                            LayoutInflater.from(mContext)
                                                .inflate(R.layout.action_item, null)
                                        v.setOnClickListener {
                                            val observations = null
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
                                        presenter.actionPutDeliverCourier(items.id!!)
                                        dialog.show()

                                    }
                                    7 -> {
                                        presenter.actionPutDeliverCustomer(items.id!!)
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
                            ServiceFactory.data.actions.firstOrNull { it.id == id }?.name

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

                    dialog.window?.setBackgroundDrawable(ColorDrawable(android.R.color.black))
                    dialog.setContentView(dialogView)
                    dialog.show()
                }
            }
        }
    }

    fun verDetalle(items: OrdersList) {
        var datos: Array<String?> = arrayOf(
            items.id.toString(),
            items.name,
            items.lastname,
            items.address,
            items.value,
            items.phoneClient,
            items.date,
            items.origin,
            items.idCodBranch.toString(),
            items.hour,
            items.idState.toString(),
            items.observations,
            items.methodPay!!.name,
            items.pickingOrder.toString(),
            items.detailOrder!!.totalItems.toString(),
            items.behavior.toString()
        )
        val intent: Intent = Intent(mContext, Detail::class.java)
        intent.putExtra("orderId", items.id)
        intent.putExtra("userInfo", datos)
        intent.putExtra("behavior", items.behavior)
        startActivity(mContext, intent, null)
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {

        holder.card.setCardBackgroundColor(Color.parseColor(ServiceFactory.data.behaviors.firstOrNull { it.id == mDataSet[position].behavior }!!.color))

        holder.names.text = mDataSet[position].name

        holder.idOrder.text = mDataSet[position].id.toString()

        holder.amount.text = mDataSet[position].detailOrder!!.totalItems.toString()

        holder.date.text = mDataSet[position].date

        holder.time.text = mDataSet[position].hour!!.substring(0, mDataSet[position].hour!!.length - 13)

        holder.cell.text = mDataSet[position].phoneClient

        holder.total.text = mDataSet[position].value

    }

    override fun getItemCount(): Int {
        return mDataSet.size
    }
}
