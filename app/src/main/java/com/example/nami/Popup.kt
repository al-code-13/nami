package com.example.nami

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_popup.*

class Popup : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_popup)

        val intent: Intent = intent
        val orderId = intent.getStringExtra("orderId")
        val orderState = intent.getStringExtra("state")
    }
}
