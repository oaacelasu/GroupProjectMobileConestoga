package com.conestoga.groupproject.view.checkout

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.conestoga.groupproject.R
import com.conestoga.groupproject.data.entities.AppOrder
import com.conestoga.groupproject.view.MainActivity

class ConfirmationActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_confirmation)

        //Hide the action bar
        supportActionBar?.hide()
        val orderData: AppOrder = intent.getSerializableExtra("order") as AppOrder
        val tvConfirmation: TextView = findViewById(R.id.tvConfirmationText)
        tvConfirmation.text = orderData.toString()
        findViewById<View>(R.id.btnReturnToMain).setOnClickListener {
            finish()
            startActivity(
                Intent(
                    this,
                    MainActivity::class.java
                ).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            )
        }
    }
}