package com.example.nami

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.nami.models.user.UserResponse
import com.example.nami.presenters.ProfilePresenter
import com.example.nami.presenters.ProfileUI
import kotlinx.android.synthetic.main.activity_profile.*

class ProfileActivity : AppCompatActivity(), ProfileUI {

    private val presenter = ProfilePresenter(this, this)
    override fun onCreate(savedInstanceState: Bundle?) {
        val actionbar = supportActionBar
        actionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        actionbar!!.title = "Perfil"
        //actionbar!!.setBackgroundDrawable(ColorDrawable(Color.parseColor("#01bd8a")))
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        val phone = edit_phone
        presenter.getUser()
        button1.setOnClickListener {
            presenter.actionUpdateProfile(phone.text.toString())
        }
    }

    override fun showProfile(data: UserResponse) {
        runOnUiThread {

            containerProfile?.visibility = View.VISIBLE
            button1?.visibility = View.VISIBLE
            progressContainer?.visibility = View.GONE
            name.text = data.user!!.name
            lastName.text = data.user!!.lastname
            edit_phone.hint = data.user!!.phone
        }
    }


    override fun showSuccess(message: String) {
        runOnUiThread {
            containerProfile?.visibility = View.VISIBLE
            button1?.visibility = View.VISIBLE
            progressContainer?.visibility = View.GONE
            Toast.makeText(applicationContext, message, Toast.LENGTH_LONG).show()
            val intent = Intent(this, MainActivity::class.java)
            ContextCompat.startActivity(this, intent, null)
        }
    }

    override fun showError(error: String) {
        runOnUiThread {
            containerProfile?.visibility = View.VISIBLE
            button1?.visibility = View.VISIBLE
            progressContainer?.visibility = View.GONE
            Toast.makeText(applicationContext, error, Toast.LENGTH_LONG).show()
        }
    }

    override fun showLoad() {

        runOnUiThread {
            containerProfile?.visibility = View.GONE
            button1?.visibility = View.GONE
            progressContainer?.visibility = View.VISIBLE
        }
    }
}