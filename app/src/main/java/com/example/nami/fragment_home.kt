package com.example.nami

import android.content.Context
import android.content.res.Configuration
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.nami.adapter.OrdersAdapter
import com.example.nami.adapter.IndicatorsAdapter
import com.example.nami.models.sections.Behavior
import com.example.nami.presenters.SectionPresenter

import com.example.nami.models.sections.SectionResponse
import com.example.nami.presenters.BasePresenter
import com.example.nami.presenters.SectionUI
import kotlinx.android.synthetic.main.fragment_home.*


class SectionFragment(
    private val mContext: Context,
    private val legendList: List<Behavior>,
    private val sectionId: Int

) : Fragment(), SectionUI {
    private val presenter = SectionPresenter(this,mContext)
    private var reciclerView: AutofitRecyclerView? = null
    private var adapter: IndicatorsAdapter? = null
    private var itemsRefresh: SwipeRefreshLayout? = null
    private lateinit var gridView: GridView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        presenter.actionSection(
            sectionId
        )
        val v: View
        val orientation = activity?.resources?.configuration?.orientation
        v = if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            inflater.inflate(R.layout.fragment_home, container, false)
        } else {
            inflater.inflate(R.layout.home_fragment_landscape, container, false)
        }
        reciclerView = v.findViewById(R.id.my_grid_view_list)
        gridView = v.findViewById<GridView>(R.id.gridItems)
        adapter = IndicatorsAdapter(mContext, legendList)
        gridView.adapter = adapter
        if(legendList.size<=0){
            gridView.visibility=View.GONE
        }
        itemsRefresh = v.findViewById(R.id.itemsswipetorefresh)

        return v
    }


    override fun showData(data: SectionResponse) {
        activity?.runOnUiThread {
            if(data.orders!!.size<=0){
                val nothing=layoutInflater.inflate(R.layout.action_item,null)
                nothing.findViewById<TextView>(R.id.action).text="¯\\_(ツ)_/¯"
                itemsswipetorefresh.addView(nothing)
                Log.i("¯\\_(ツ)_/¯","¯\\_(ツ)_/¯")
                reciclerView?.visibility=View.GONE
            }
            else{



            reciclerView?.adapter = OrdersAdapter(mContext, data.orders!!,presenter)
            itemsRefresh?.setProgressBackgroundColorSchemeColor(
                ContextCompat.getColor(
                    mContext,
                    R.color.colorPrimary
                )
            )
            itemsRefresh?.setColorSchemeColors(Color.WHITE)
            itemsRefresh?.setOnRefreshListener {
                presenter.actionRefreshSection(
                    sectionId
                )
                reciclerView?.adapter = OrdersAdapter(mContext, data.orders!!,presenter)
                gridView.adapter = adapter
                itemsRefresh?.isRefreshing = false
            }
            }
        }
    }

    override fun showError(error: String) {
        activity?.runOnUiThread {
            Toast.makeText(mContext, error, Toast.LENGTH_LONG).show()
        }
    }

    override fun actionSuccess(message: String) {
        activity?.runOnUiThread {
            Toast.makeText(mContext, message, Toast.LENGTH_LONG).show()
        }
    }
}