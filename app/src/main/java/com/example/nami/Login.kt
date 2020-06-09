package com.example.nami


import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.nami.presenters.LoginPresenter
import com.example.nami.presenters.LoginUI
import kotlinx.android.synthetic.main.activity_login.*



class Login : AppCompatActivity(), LoginUI {

    var spinner: ProgressBar? = null
    private val presenter = LoginPresenter(this,this)

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.SplashTheme)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        spinner = findViewById(R.id.progressBar)
        spinner?.visibility = View.GONE
    }

    override fun onResume() {
        super.onResume()
        presenter.actionAutoLogin()
    }


    fun login(v: View) {
        spinner?.visibility = View.VISIBLE
        presenter.actionLogin(edit_user.text.toString(), edit_password.text.toString())
        spinner?.visibility = View.GONE


    }

    override fun showHome() {

        val intent = Intent(this, MainActivity::class.java)
        ContextCompat.startActivity(this, intent, null)

    }


    override fun showError(error: String) {
        runOnUiThread {
            spinner?.visibility = View.GONE
            Toast.makeText(applicationContext, error, Toast.LENGTH_LONG).show()
        }
    }

    override fun showLoad() {
        runOnUiThread {
            spinner?.visibility = View.VISIBLE
        }
    }
}
