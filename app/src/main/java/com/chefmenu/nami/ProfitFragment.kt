package com.chefmenu.nami

import android.app.ActionBar
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ExpandableListView
import com.chefmenu.nami.adapter.ProfitAdapter
import com.chefmenu.nami.models.user.History
import com.chefmenu.nami.models.user.ProfitsResponse
import com.chefmenu.nami.presenters.ProfitsPresenter
import com.chefmenu.nami.presenters.ProfitsUI

class ProfitFragment (
    private val mContext: Context,
    private val data:List <History>
): Fragment() {
    lateinit var expandedList: ExpandableListView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var v=inflater.inflate(R.layout.fragment_profit, container, false)
        expandedList = v.findViewById(R.id.expandible)
        expandedList.setAdapter(ProfitAdapter(mContext, data))
        return v
    }

}