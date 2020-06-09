package com.example.nami

import SectionResponse
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import co.zsmb.materialdrawerkt.builders.accountHeader
import com.example.nami.adapter.OrdersAdapter
import com.example.nami.adapter.IndicatorsAdapter
import com.example.nami.models.sections.Behavior
import com.example.nami.presenters.SectionPresenter
import co.zsmb.materialdrawerkt.builders.drawer
import co.zsmb.materialdrawerkt.builders.footer
import co.zsmb.materialdrawerkt.draweritems.badge
import co.zsmb.materialdrawerkt.draweritems.badgeable.primaryItem
import co.zsmb.materialdrawerkt.draweritems.profile.profile
import com.example.nami.presenters.SectionUI


class SectionFragment(
    private val mContext: Context,
    private val legendList: List<Behavior>,
    private val sectionId: Int

) : Fragment(), SectionUI {
    private val presenter = SectionPresenter(this)
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
        drawer {
            accountHeader {
                profile("Samantha", "samantha@gmail.com") {
                    //icon = "http://some.site/samantha.png"
                }
                profile("Laura", "laura@gmail.com") {
                   // icon = R.drawable.profile_laura
                }
            }
            primaryItem("Home")
            primaryItem("Recursos") {
                badge {
                    cornersDp = 0
                    text = ">"
                    colorPressed = 0xFFCC99FF
                }
            }
            primaryItem("Zonas") {
                badge {
                    cornersDp = 0
                    text = ">"
                    colorPressed = 0xFFCC99FF
                }
            }
            primaryItem("Mis gancias") {
                badge {
                    cornersDp = 0
                    text = ">"
                    colorPressed = 0xFFCC99FF
                }
            }

            primaryItem("Cerrar sesión")

            footer {

                primaryItem("Ayuda") {

                    onClick { _ ->
                        val intent = Intent(mContext, helpPage::class.java)
                        startActivity(intent)
                        false

                    }
                }
            }
        }
        reciclerView = v.findViewById(R.id.my_grid_view_list)
        gridView = v.findViewById<GridView>(R.id.gridItems)
        adapter = IndicatorsAdapter(mContext, legendList)
        gridView.adapter = adapter
        itemsRefresh = v.findViewById(R.id.itemsswipetorefresh)

        return v
    }


    override fun showData(data: SectionResponse) {
        activity?.runOnUiThread {
            reciclerView?.adapter = OrdersAdapter(mContext, data.orders,presenter)
            itemsRefresh?.setProgressBackgroundColorSchemeColor(
                ContextCompat.getColor(
                    mContext,
                    R.color.colorPrimary
                )
            )
            itemsRefresh?.setColorSchemeColors(Color.WHITE)
            itemsRefresh?.setOnRefreshListener {
                presenter.actionSection(
                    sectionId
                )

                reciclerView?.adapter = OrdersAdapter(mContext, data.orders,presenter)
                gridView.adapter = adapter
                itemsRefresh?.isRefreshing = false
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