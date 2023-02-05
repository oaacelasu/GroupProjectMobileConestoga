package com.conestoga.groupproject.view.checkout

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.conestoga.groupproject.data.entities.Product
import com.conestoga.groupproject.databinding.ActivityProductDetailBinding

class ProductDetail : AppCompatActivity() {
    private var product: Product? = null

    private lateinit var binding: ActivityProductDetailBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductDetailBinding.inflate(layoutInflater)

        // enable action bar
        setSupportActionBar(binding.myToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // bind views
        binding.lifecycleOwner = this
        product = intent.getSerializableExtra("product") as Product
        binding.product = product

        // set button Action
        binding.btnBuyNow.setOnClickListener {
            val intent = Intent(this, CheckoutActivity::class.java)
            intent.putExtra("product", product)
            startActivity(intent)
        }
        setContentView(binding.root)
    }


    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}