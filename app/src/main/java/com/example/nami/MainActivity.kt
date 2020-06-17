package com.example.nami

import android.content.Context
import android.content.Intent
import android.graphics.Color.RED
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import co.zsmb.materialdrawerkt.builders.accountHeader
import co.zsmb.materialdrawerkt.builders.drawer
import co.zsmb.materialdrawerkt.builders.footer
import co.zsmb.materialdrawerkt.draweritems.badge
import co.zsmb.materialdrawerkt.draweritems.badgeable.primaryItem
import co.zsmb.materialdrawerkt.draweritems.profile.profile

import com.example.nami.adapter.SectionsAdapter
import com.example.nami.models.sections.SectionsResponse
import com.example.nami.presenters.SectionsPresenter
import com.example.nami.presenters.SectionsUI
import com.google.android.material.tabs.TabLayout

class MainActivity : AppCompatActivity(), SectionsUI {
    private val presenter = SectionsPresenter(this)
    var tabLayout: TabLayout? = null
    var viewPager: ViewPager? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        presenter.actionSections()
        tabLayout = findViewById(R.id.tabLayout)
        viewPager = findViewById(R.id.viewPager)

    }

    override fun showSection(data: SectionsResponse) {

        runOnUiThread {
            for (section in data.sections!!) {
                tabLayout!!.addTab(tabLayout!!.newTab().setText(section.name))
            }
            tabLayout?.tabGravity = TabLayout.GRAVITY_FILL

            val adapter = SectionsAdapter(
                this,
                supportFragmentManager,
                tabLayout!!.tabCount,
                data.behaviors!!.toTypedArray(),
                data.sections!!
            )

            viewPager!!.adapter = adapter

            viewPager!!.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabLayout))

            tabLayout!!.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab) {
                    viewPager!!.currentItem = tab.position
                }

                override fun onTabUnselected(tab: TabLayout.Tab) {

                }

                override fun onTabReselected(tab: TabLayout.Tab) {

                }
            })

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
                            val intent = Intent(this@MainActivity, helpPage::class.java)
                            startActivity(intent)
                            false

                        }
                    }
                }
            }

        }
    }

    override fun showError(error: String) {
        runOnUiThread {

            if (error.contains("Error al autenticar el token")) {

                this.getSharedPreferences("localStorage", Context.MODE_PRIVATE).edit().clear().apply()
                finish()

                val intent = Intent(this, Login::class.java)
                startActivity(intent)
            } else {
                Toast.makeText(applicationContext, error, Toast.LENGTH_LONG).show()
            }
        }
    }
}
