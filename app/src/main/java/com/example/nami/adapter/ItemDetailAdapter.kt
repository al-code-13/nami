package com.example.nami.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.nami.R
import com.example.nami.models.detailModels.ListElement

class ItemsDetailAdapter(
    private val mContext: Context,
    private var data: List<ListElement>,
    private val behavior: Int,
    private val compareList: List<String>,
    private var adjustValue:Double
) : RecyclerView.Adapter<ItemsDetailAdapter.ViewHolder>() {

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        var name: TextView = v.findViewById(R.id.name)
        var idProduct: TextView = v.findViewById(R.id.idProduct)
        var cant: TextView = v.findViewById(R.id.cant)

        var minusButton: ImageView? = v.findViewById(R.id.minusButton)
        var moreButton: ImageView? = v.findViewById(R.id.moreButton)
    }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        var v: View
        if (behavior == 2) {
            v = LayoutInflater.from(mContext).inflate(R.layout.article_data_detail, parent, false)
        } else {
            v = LayoutInflater.from(mContext)
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
        v.idProduct.text = "${elements.article.codSofware}"
        v.cant.text = "${elements.quantityArticle}"

        if (elements.quantityArticle.toInt() > 0) {
            v.minusButton?.visibility = View.VISIBLE
            v.minusButton?.setOnClickListener {
                v.moreButton?.visibility = View.VISIBLE
                elements.quantityArticle = (elements.quantityArticle.toInt() - 1).toString()
                adjustValue-=elements.article.value.toInt()
                onBindViewHolder(v, position)
            }

        } else {

            v.minusButton?.visibility = View.GONE
        }
        val oldvalue = compareList[position].toInt()
        if (elements.quantityArticle.toInt() < oldvalue) {
            v.moreButton?.visibility = View.VISIBLE
            v.moreButton?.setOnClickListener {
                elements.quantityArticle = (elements.quantityArticle.toInt() + 1).toString()
                adjustValue+=elements.valueTotalArticle.toDouble()
                onBindViewHolder(v, position)
            }
        } else {
            v.moreButton?.visibility = View.GONE
        }

    }
}