package com.chefmenu.nami.adapter

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.chefmenu.nami.ProfitFragment
import com.chefmenu.nami.models.user.History
import com.chefmenu.nami.models.user.ProfitsResponse


@Suppress("DEPRECATION")
class ProfitsPageAdapter(
    private val myContext: Context,
    fm: FragmentManager,
    private var totalTabs: Int,
    private val dataService: ProfitsResponse
) :
    FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
        val data = mutableListOf<History>()
        return if (dataService.history!!.isEmpty() || dataService.history == null) {
            ProfitFragment(myContext,null)

        } else {
            if (position == 0) {
                data.add(dataService.history[0])
                ProfitFragment(myContext, data)
            } else {
                for (i in dataService.history) {
                    data.add(i)
                }
                ProfitFragment(myContext, data)
            }
        }
    }

    override fun getCount(): Int {
        return totalTabs
    }
}