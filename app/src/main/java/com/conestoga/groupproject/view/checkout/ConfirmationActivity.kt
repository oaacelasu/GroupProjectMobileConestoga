package com.conestoga.groupproject.view.checkout

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.conestoga.groupproject.view.MainActivity
import com.conestoga.groupproject.R
import com.conestoga.groupproject.data.entities.AppOrder
import java.util.*

class ConfirmationActivity : AppCompatActivity() {
    protected override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_confirmation)

        Objects.requireNonNull(actionBar)?.hide()
        val orderData: AppOrder = getIntent().getSerializableExtra("order") as AppOrder
        val tvConfirmation: TextView = findViewById<TextView>(R.id.tvConfirmationText)
        tvConfirmation.setText(orderData.toString())
        findViewById<View>(R.id.btnReturnToMain).setOnClickListener(View.OnClickListener { v: View? ->
            finish()
            startActivity(
                Intent(
                    this,
                    MainActivity::class.java
                ).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            )
        })
    }
}