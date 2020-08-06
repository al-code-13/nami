package com.chefmenu.nami

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import co.zsmb.materialdrawerkt.builders.accountHeader
import co.zsmb.materialdrawerkt.builders.drawer
import co.zsmb.materialdrawerkt.builders.footer
import co.zsmb.materialdrawerkt.draweritems.badge
import co.zsmb.materialdrawerkt.draweritems.badgeable.primaryItem
import co.zsmb.materialdrawerkt.draweritems.profile.profile
import co.zsmb.materialdrawerkt.imageloader.drawerImageLoader
import com.chefmenu.nami.adapter.SectionsAdapter
import com.chefmenu.nami.models.sections.SectionsResponse
import com.chefmenu.nami.models.user.Branch
import com.chefmenu.nami.models.user.BranchsResponse
import com.chefmenu.nami.models.user.UserResponse
import com.chefmenu.nami.presenters.SectionsPresenter
import com.chefmenu.nami.presenters.SectionsUI
import com.google.android.material.tabs.TabLayout
import com.mikepenz.materialdrawer.util.DrawerUIUtils
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), SectionsUI {
    //Declaracion presentador de secciones
    private val presenter = SectionsPresenter(this, this)

    //Declaracion layout de las pestañas
    var tabLayout: TabLayout? = null

    //Declaracion del contenido de las pestañas
    var viewPager: ViewPager? = null

    lateinit var adapter: SectionsAdapter

    companion object {
        val DETAIL_RESULT = 338
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.i("codigoResultante", resultCode.toString())
        if (resultCode == DETAIL_RESULT) {
            val refresh: Boolean? = data?.getBooleanExtra("datosp", false)
            Log.i("a ver que recibimos", refresh.toString())
            if (refresh == true) {
                refreshSections()
            }
        }
    }

    private fun refreshSections() {
        adapter.notifyDataSetChanged(viewPager!!.currentItem)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //solicitud del primer servicio (secciones)
        presenter.actionSections()
        presenter.actionBranchs()
        tabLayout = findViewById(R.id.tabLayout)
        viewPager = findViewById(R.id.viewPager)
        drawerImageLoader {
            placeholder { ctx, _ ->
                DrawerUIUtils.getPlaceHolder(ctx)
            }
            set { imageView, uri, placeholder, _ ->
                Picasso.with(imageView.context)
                    .load(uri)
                    .placeholder(placeholder)
                    .into(imageView)
            }
            cancel { imageView ->
                Picasso.with(imageView.context)
                    .cancelRequest(imageView)
            }
        }
    }

    override fun showBranchs(
        data: BranchsResponse,
        userData: UserResponse,
        selectedBranch: Branch?
    ) {
        runOnUiThread {
            if (data.branchs!!.size <= 0 || data.branchs == null) {
                val dialog = AlertDialog.Builder(this)
                dialog.setTitle("No tienes ninguna sucursal asignada, comunicate con tu administrador. gracias")
                dialog.setCancelable(false)
                dialog.setPositiveButton("Ok") { _, _ ->
                    finish()
                    presenter.actionLogOut()
                }
                dialog.show()
            } else {
                if (selectedBranch != null) {
                    val index: Int = data.branchs!!.indexOf(selectedBranch)
                    data.branchs!!.removeAt(index)
                    data.branchs!!.add(0, selectedBranch)
                    toolbar3.title = "${selectedBranch.name}"
                    //Se creal el sidemenu
                    drawer {
                        this.showOnFirstLaunch = true
                        this.closeOnClick = true
                        this.keepStickyItemsVisible = true
                        this.toolbar = toolbar3
                        accountHeader {
                            for (i in data.branchs!!) {
                                profile(
                                    "${i.city!!.name}",
                                    "${i!!.name}"
                                ) {
                                    this.onClick { _, _, _ ->
                                        presenter.actionRefreshSections(i.id!!)
                                        toolbar3.title = "${i.name}"
                                        true
                                    }
                                    iconUrl = "${i.establishment!!.logo}"
                                    Log.i("iconUrl", "${i.establishment!!.logo}")
                                }
                                this.onlyMainProfileImageVisible = true
                                this.onlySmallProfileImagesVisible = false

                            }
                        }
                        primaryItem("Inicio")
                        primaryItem("Perfil") {
                            enabled = true
                            this.onClick {_->
                                val intent = Intent(this@MainActivity, ProfileActivity::class.java)
                                startActivity(intent)
                                false
                            }
                        }
                        primaryItem("Recursos") {
                            enabled = false
                            badge {
                                cornersDp = 0
                                text = ">"
                                colorPressed = 0xFFCC99FF
                            }
                        }

                        primaryItem("Zonas") {
                            enabled = false
                            badge {
                                cornersDp = 0
                                text = ">"
                                colorPressed = 0xFFCC99FF
                            }
                        }
                        primaryItem("Mis ganancias") {
                            enabled = false
                            badge {
                                cornersDp = 0
                                text = ">"
                                colorPressed = 0xFFCC99FF
                            }
                        }
                        primaryItem("Cerrar sesión") {
                            onClick { _ ->
                                finish()
                                presenter.actionLogOut()
                                true
                            }
                        }

                        footer {
                            primaryItem("Ayuda") {
                                enabled = false
                                onClick { _ ->
                                    val intent = Intent(this@MainActivity, HelpPage::class.java)
                                    startActivity(intent)
                                    false

                                }
                            }
                        }
                    }
                } else {
                    presenter.actionRefreshSections(data.branchs?.get(0)!!.id!!)
                    toolbar3.title = "${data.branchs?.get(0)!!.name}"
                    //Se creal el sidemenu
                    drawer {
                        this.showOnFirstLaunch = true
                        this.closeOnClick = true
                        this.keepStickyItemsVisible = true
                        this.toolbar = toolbar3
                        accountHeader {
                            for (i in data.branchs!!) {
                                profile(
                                    "${i.city!!.name}",
                                    "${i!!.name}"

                                ) {
                                    this.onClick { _, _, _ ->
                                        presenter.actionRefreshSections(i.id!!)
                                        toolbar3.title = "${i.name}"
                                        true
                                    }
                                    iconUrl = "${i.establishment!!.logo}"
                                    Log.i("iconUrl", "${i.establishment!!.logo}")
                                }
                                this.onlyMainProfileImageVisible = true
                                this.onlySmallProfileImagesVisible = false
                            }
                        }
                        primaryItem("Inicio")
                        primaryItem("Perfil") {
                            enabled = true
                            this.onClick {_->
                                val intent = Intent(this@MainActivity, ProfileActivity::class.java)
                                startActivity(intent)
                                false

                            }
                        }
                        primaryItem("Recursos") {
                            enabled = false
                            badge {
                                cornersDp = 0
                                text = ">"
                                colorPressed = 0xFFCC99FF
                            }
                        }

                        primaryItem("Zonas") {
                            enabled = false
                            badge {
                                cornersDp = 0
                                text = ">"
                                colorPressed = 0xFFCC99FF
                            }
                        }
                        primaryItem("Mis ganancias") {
                            enabled = false
                            badge {
                                cornersDp = 0
                                text = ">"
                                colorPressed = 0xFFCC99FF
                            }
                        }
                        primaryItem("Cerrar sesión") {
                            onClick { _ ->
                                finish()
                                presenter.actionLogOut()
                                true
                            }
                        }

                        footer {
                            primaryItem("Ayuda") {
                                enabled = false
                                onClick { _ ->
                                    val intent = Intent(this@MainActivity, HelpPage::class.java)
                                    startActivity(intent)
                                    false

                                }
                            }
                        }
                    }
                }
            }
        }
    }


    //Funcion cuando responde el servicio
    override fun showSection(data: SectionsResponse) {
        //Se corre en el hilo principal
        runOnUiThread {
            //por cada seccion se genera una pestaña
            tabLayout!!.removeAllTabs()
            for (section in data.sections!!) {
                tabLayout!!.addTab(tabLayout!!.newTab().setText(section.name))
            }
            tabLayout?.tabGravity = TabLayout.GRAVITY_FILL

            //Se llama el adaptador de las secciones
            adapter = SectionsAdapter(
                this,
                supportFragmentManager,
                tabLayout!!.tabCount,
                data.behaviors!!.toTypedArray(),
                data.sections!!
            ) { refreshSections() }
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

    override fun onBackPressed() {
        finishAffinity()
    }

    override fun showError(error: String) {
        Log.i("erordeltoken", error)
        runOnUiThread {
            if (error.contains("token")) {
                presenter.actionLogOut()
            } else {
                Toast.makeText(applicationContext, error, Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun exit() {
        finish()
    }

}
