package com.chefmenu.nami


import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.ScrollView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.chefmenu.nami.presenters.LoginPresenter
import com.chefmenu.nami.presenters.LoginUI
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import kotlinx.android.synthetic.main.activity_login.*


class Login : AppCompatActivity(), LoginUI {

    var spinner: ProgressBar? = null
    private val presenter = LoginPresenter(this, this)
    var containerLogin: ScrollView? = null
    var firebaseVersion: Long = BuildConfig.VERSION_CODE.toLong()
    var storeUrl: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.SplashTheme)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        val numVersion = BuildConfig.VERSION_NAME

        if (BuildConfig.DEBUG) {
            version.text = "Versión $numVersion Alpha Debug\n Services Development"
        } else {
            version.text = "Versión $numVersion Alpha Debug\n Services Pre-Production"
        }


        val remoteConfig = Firebase.remoteConfig
        val configSettings = remoteConfigSettings {
            minimumFetchIntervalInSeconds = 60
        }
        remoteConfig.setConfigSettingsAsync(configSettings)
        remoteConfig.setDefaultsAsync(
            mapOf(
                "current_version" to BuildConfig.VERSION_CODE,
                "googleplay_url" to storeUrl
            )
        )
        remoteConfig.fetchAndActivate().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                firebaseVersion = remoteConfig.getLong("current_version")
                storeUrl = remoteConfig.getString("googleplay_url")
                if (BuildConfig.VERSION_CODE.toLong() < firebaseVersion && storeUrl != "") {
                    val builder: AlertDialog.Builder = AlertDialog.Builder(this)
                    val dialog: AlertDialog = builder.setTitle("Pick")
                        .setMessage("Hemos actualizado rydder para mejorar tu experiencia. Por favor actualiza a la última versión para continuar. Gracias.")
                        .setPositiveButton("Actualizar") { _, _ ->
                            //throw RuntimeException("Forzando primer error")
                              val uri: Uri =
                                  Uri.parse(storeUrl)
                              val intent = Intent(Intent.ACTION_VIEW, uri)
                              startActivity(intent)
                              finish()
                        }
                        .setCancelable(false)
                        .create()
                    dialog.show()
                    dialog.getButton(AlertDialog.BUTTON_POSITIVE)
                        .setTextColor(ContextCompat.getColor(this, R.color.secondaryColor))
                }
                Log.i("firebaseVersion", firebaseVersion.toString())
            }
        }
        Log.i("firebaseVersion", firebaseVersion.toString())
        Log.i("localVersion", BuildConfig.VERSION_CODE.toString())

        spinner = findViewById(R.id.progressBar)
        containerLogin = findViewById(R.id.containerLogin)
        spinner?.visibility = View.GONE
    }

    override fun onResume() {
        super.onResume()
        presenter.actionAutoLogin()
    }


    fun login(v: View) {
        spinner?.visibility = View.VISIBLE
        containerLogin?.visibility = View.GONE
        presenter.actionLogin(edit_user.text.toString(), edit_password.text.toString())
    }

    override fun showHome() {
        val intent = Intent(this, MainActivity::class.java)
        ContextCompat.startActivity(this, intent, null)

    }


    override fun showError(error: String) {
        runOnUiThread {
            containerLogin?.visibility = View.VISIBLE
            spinner?.visibility = View.GONE
            Toast.makeText(applicationContext, error, Toast.LENGTH_LONG).show()
        }
    }

    override fun showLoad() {
        runOnUiThread {
            containerLogin?.visibility = View.GONE
            spinner?.visibility = View.VISIBLE
        }
    }
}