package com.chefmenu.nami

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ExpandableListView
import androidx.fragment.app.Fragment
import com.chefmenu.nami.adapter.ProfitAdapter
import com.chefmenu.nami.models.user.History
import kotlinx.android.synthetic.main.fragment_profit.*

class ProfitFragment(
    private val mContext: Context,
    private val data: List<History>? = null
) : Fragment() {
    lateinit var expandedList: ExpandableListView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (data != null) {
            var v = inflater.inflate(R.layout.fragment_profit, container, false)
            expandedList = v.findViewById(R.id.expandible)
            expandedList.setAdapter(ProfitAdapter(mContext, data))
            expandedList.visibility = View.VISIBLE
//            noProfits.visibility = View.GONE
            return v
        } else {
            var v = inflater.inflate(R.layout.fragment_profit, container, false)
            expandedList.visibility = View.GONE
            noProfits.visibility = View.VISIBLE
            return v
        }
    }

}