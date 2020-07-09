package com.example.nami.adapter

import android.content.Context
import android.graphics.Typeface
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.nami.Detail
import com.example.nami.R
import com.example.nami.models.auth.LoginResponse
import com.example.nami.models.detailModels.ListElement
import kotlinx.android.synthetic.main.activity_detail.*
import java.text.NumberFormat
import java.util.*

class ItemsDetailAdapter(
    private val mContext: Context,
    private var data: List<ListElement>,
    private val behavior: Int,
    private var compareList: MutableList<String>,
    private val calculateAdjustValue: () -> Unit,
    private val checkBox: CheckBox,
    private val compareArticleList:List<String>
) : RecyclerView.Adapter<ItemsDetailAdapter.ViewHolder>() {

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        var name: TextView = v.findViewById(R.id.name)
        var idProduct: TextView = v.findViewById(R.id.idProduct)
        var cant: TextView = v.findViewById(R.id.cant)
        var cantTotal: TextView = v.findViewById(R.id.cantTotal)
        var minusButton: ImageView? = v.findViewById(R.id.minusButton)
        var moreButton: ImageView? = v.findViewById(R.id.moreButton)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        var v: View = if (behavior == 2) {
            LayoutInflater.from(mContext).inflate(R.layout.article_data_detail, parent, false)
        } else {
            LayoutInflater.from(mContext)
                .inflate(R.layout.article_data_detail_preview, parent, false)
        }
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(
        v: ViewHolder,
        position: Int
    ) {
        val elements = data[position]
        v.name.text = elements.article.name
        v.idProduct.text = "${elements.article.upc}"
        if(behavior!=2){
            v.cant.text = elements.quantityArticle
        }
        else {
            v.cant.text = "${compareList[position]}"
        }
            v.cantTotal.text = elements.quantityArticle
        val priceUnit:Double=elements.valueTotalArticle.toDouble()/elements.quantityArticle.toDouble()
        if (compareList[position].toInt() > 0) {
            v.minusButton?.visibility = View.VISIBLE
            v.minusButton?.setOnClickListener {
                v.moreButton?.visibility = View.VISIBLE
                compareList[position] = (compareList[position].toInt() - 1).toString()
                onBindViewHolder(v, position)
            }
            v.minusButton?.setOnLongClickListener {
                v.moreButton?.visibility = View.VISIBLE
                compareList[position] = "1"
                onBindViewHolder(v, position)
                it.isActivated
            }
        }
        else {
            v.minusButton?.visibility = View.INVISIBLE
        }

        val oldvalue = elements.quantityArticle.toInt()
        if (compareList[position].toInt() < oldvalue) {
            v.cant.setTypeface(Typeface.create(v.cant.typeface, Typeface.NORMAL), Typeface.NORMAL)
            v.moreButton?.visibility = View.VISIBLE
            v.moreButton?.setOnClickListener {
                compareList[position] = (compareList[position].toInt() + 1).toString()
                onBindViewHolder(v, position)

            }
            v.moreButton?.setOnLongClickListener {
                compareList[position] = (elements.quantityArticle.toInt() - 1).toString()
                onBindViewHolder(v, position)
                it.isActivated
            }
        } else {
            v.cant.setTypeface(v.cant.typeface, Typeface.BOLD)
            v.moreButton?.visibility = View.GONE
        }
        calculateAdjustValue()
        checkBox.isChecked = compareArticleList.equals(compareList)
    }
}  