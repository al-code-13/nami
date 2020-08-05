package com.example.nami

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.nami.adapter.IndicatorsAdapter
import com.example.nami.adapter.OrdersAdapter
import com.example.nami.models.sections.Behavior
import com.example.nami.models.sections.SectionResponse
import com.example.nami.presenters.SectionPresenter
import com.example.nami.presenters.SectionUI
import java.text.SimpleDateFormat
import java.util.*


class SectionFragment(
    private val mContext: Context,
    private val legendList: List<Behavior>,
    private val sectionId: Int,
    private val filter: String? = null,
    private val refreshAdapter:()->Unit
) : Fragment(), SectionUI {
    lateinit var spinner: Spinner
    lateinit var loading: LinearLayout
    private val presenter = SectionPresenter(this, mContext)
    private var reciclerView: AutofitRecyclerView? = null
    private var adapter: IndicatorsAdapter? = null
    private var itemsRefresh: SwipeRefreshLayout? = null
    private lateinit var gridView: GridView
    private var selectedDay: String? = null
    val sdf = SimpleDateFormat("yyyy-MM-dd")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v: View
        val orientation = activity?.resources?.configuration?.orientation
        v = if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            inflater.inflate(R.layout.fragment_home, container, false)
        } else {
            inflater.inflate(R.layout.home_fragment_landscape, container, false)
        }
        loading = v.findViewById(R.id.loading)
        Log.i("SeCreofragmento",sectionId.toString())
        if (filter != null) {
            var limiter: List<String> = filter.split("-")
            var days = mutableListOf<String>()
            var initDate: Calendar = Calendar.getInstance()
            initDate.add(Calendar.DAY_OF_YEAR, -limiter[1].toInt())

            for (xd in 1..limiter[1].toInt()) {
                Log.i("fecha en el bucle", initDate.time.toString())
                initDate.add(Calendar.DAY_OF_YEAR, 1)
                days.add(sdf.format(initDate.time).toString())
            }


            spinner = v.findViewById(R.id.spinnerView) as Spinner
            spinner.adapter =
                ArrayAdapter<String>(
                    mContext,
                    R.layout.support_simple_spinner_dropdown_item,
                    days.reversed()
                )

            spinner.onItemSelectedListener = object :
                AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {
                    Toast.makeText(mContext, "Seleccione algun dia", Toast.LENGTH_SHORT).show()
                }

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    loading.visibility = View.VISIBLE
                    reciclerView?.visibility = View.GONE
                    selectedDay = days.reversed()[position]
                    presenter.actionSection(sectionId,selectedDay,selectedDay)
                }

            }
            spinner.visibility = View.VISIBLE
        }
        else {
            presenter.actionSection(
                sectionId, null, null
            )
        }
        reciclerView = v.findViewById(R.id.my_grid_view_list)
        gridView = v.findViewById<GridView>(R.id.gridItems)
        adapter = IndicatorsAdapter(mContext, legendList)
        gridView.adapter = adapter
        if (legendList.isEmpty()) {
            gridView.visibility = View.GONE
        }
        itemsRefresh?.setProgressBackgroundColorSchemeColor(
            ContextCompat.getColor(
                mContext,
                R.color.colorPrimary
            )
        )
        itemsRefresh = v.findViewById(R.id.itemsswipetorefresh)
        //itemsRefresh?.setColorSchemeColors(Color.WHITE)
        itemsRefresh?.setOnRefreshListener {
            refresh(selectedDay, selectedDay)
        }
        loading.visibility = View.VISIBLE
        reciclerView?.visibility = View.GONE
        return v
    }

    override fun showData(data: SectionResponse) {
        activity?.runOnUiThread {
            if (data.orders!!.size <= 0) {
                loading.visibility = View.GONE
                reciclerView?.visibility = View.GONE
            } else {
                Log.i("aquidesaparece", "el loadingf")
                loading.visibility = View.GONE
                reciclerView?.visibility = View.VISIBLE
                reciclerView?.adapter = OrdersAdapter(mContext, data.orders!!, presenter, sectionId)
            }
        }
    }

    fun forceRefresh(){
        refresh(selectedDay,selectedDay)
    }
    fun refresh(initialDate: String? = null, endlDate: String? = null) {
        loading.visibility = View.VISIBLE
        reciclerView?.visibility = View.GONE
        presenter.actionRefreshSection(
            sectionId,
            initialDate,
            endlDate
        )
        itemsRefresh?.isRefreshing = false
    }

    override fun showError(error: String) {
        activity?.runOnUiThread {
            Toast.makeText(mContext, error, Toast.LENGTH_LONG).show()
            if (error.contains("token")) {
                presenter.actionLogOut()
            } else {
                Toast.makeText(mContext, error, Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun actionSuccess(message: String) {
        activity?.runOnUiThread {
            Toast.makeText(mContext, message, Toast.LENGTH_LONG).show()
            refreshAdapter()
            //refresh(selectedDay, selectedDay)
        }
    }
}