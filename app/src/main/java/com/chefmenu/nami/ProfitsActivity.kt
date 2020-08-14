package com.chefmenu.nami

import android.os.Bundle
import android.widget.ExpandableListView
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.chefmenu.nami.adapter.ProfitAdapter
import com.chefmenu.nami.adapter.ProfitsPageAdapter
import com.chefmenu.nami.adapter.SectionsAdapter
import com.chefmenu.nami.models.user.ProfitsResponse
import com.chefmenu.nami.presenters.ProfitsPresenter
import com.chefmenu.nami.presenters.ProfitsUI
import com.google.android.material.tabs.TabLayout

class ProfitsActivity : AppCompatActivity(),ProfitsUI {
        private val presenter = ProfitsPresenter(this)
    override fun onCreate(savedInstanceState: Bundle?) {
        presenter.getProfits(4)

        val actionbar = supportActionBar
        actionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        actionbar!!.title = "MIS INGRESOS"
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profits)

    }

    override fun showProfits(data: ProfitsResponse) {
        runOnUiThread {

            var tabLayout :TabLayout = findViewById(R.id.tabLayout)
            tabLayout!!.addTab(tabLayout!!.newTab().setText("Ciclo Actual"))
            tabLayout!!.addTab(tabLayout!!.newTab().setText("Historial de ciclos"))
            tabLayout?.tabGravity = TabLayout.GRAVITY_FILL

            var viewPager:ViewPager = findViewById(R.id.viewPager)
            val adapter = ProfitsPageAdapter(
                this,
                supportFragmentManager,
                tabLayout!!.tabCount,
                data!!
            )
            //Se asigna el adaptador
            viewPager!!.adapter = adapter
            viewPager!!.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabLayout))
            tabLayout!!.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab) {
                    viewPager!!.currentItem = tab.position
                }
                override fun onTabUnselected(tab: TabLayout.Tab) {}
                override fun onTabReselected(tab: TabLayout.Tab) {}
            })
        }
    }

    override fun showError(error: String) {
    }

    override fun showLoad() {

    }
}