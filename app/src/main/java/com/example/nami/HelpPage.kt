package com.example.nami

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class helpPage : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        val actionbar = supportActionBar
        actionbar!!.title = "Ayuda"
        actionbar.setBackgroundDrawable(ColorDrawable(Color.RED))
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_help_page)

    }
}
