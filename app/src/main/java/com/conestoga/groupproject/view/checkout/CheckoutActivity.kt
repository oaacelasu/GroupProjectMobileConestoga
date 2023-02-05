package com.conestoga.groupproject.view.checkout

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.conestoga.groupproject.data.entities.AppOrder
import com.conestoga.groupproject.data.entities.OrderItem
import com.conestoga.groupproject.data.entities.Product
import com.conestoga.groupproject.databinding.ActivityCheckoutBinding
import com.google.firebase.database.FirebaseDatabase

class CheckoutActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCheckoutBinding
    private var product: Product? = null
    private var quantity: Int = 1

    private val textWatcher: TextWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            validateForm()
        }

        override fun afterTextChanged(s: Editable) {}
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCheckoutBinding.inflate(layoutInflater)
        binding.lifecycleOwner = this


        product = intent.getSerializableExtra("product") as Product
        binding.product = product
        binding.quantity = quantity

        // enable action bar
        setSupportActionBar(binding.myToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Checkout"


        // set listeners
        addListeners()
        // validate form on start
        validateForm()
        setContentView(binding.root)
    }

    private fun addListeners() {
        with(binding) {
// add text change listeners to all fields to validate form
            etAddress.addTextChangedListener(textWatcher)
            etCity.addTextChangedListener(textWatcher)
            etState.addTextChangedListener(textWatcher)
            etZip.addTextChangedListener(textWatcher)
            etCardNumber.addTextChangedListener(textWatcher)
            etCardName.addTextChangedListener(textWatcher)
            etCardExp.addTextChangedListener(textWatcher)
            etCardCvv.addTextChangedListener(textWatcher)

            // add click listeners to quantity buttons
            ivMinus.setOnClickListener {
                if (quantity > 1) {
                    quantity--
                }
            }
            ivPlus.setOnClickListener {
                quantity++
            }

            btnPlaceOrder.setOnClickListener {
                if (etAddress.text.toString().isEmpty()) {
                    Toast.makeText(this@CheckoutActivity, "Address is required!", Toast.LENGTH_LONG)
                        .show()
                    return@setOnClickListener
                }

                val order = AppOrder(
                    listOf(
                        OrderItem(
                            product,
                            quantity
                        )
                    ),
                    quantity * product!!.price
                )

                addOrder(order, "guest")
                finish()
                val intent = Intent(this@CheckoutActivity, ConfirmationActivity::class.java)
                intent.putExtra("order", order)
                startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
            }
        }
    }

    // validate form and enable/disable confirm button accordingly to prevent empty orders
    private fun validateForm() {
        with(binding) {
            val isValid = etAddress.text.toString().isNotEmpty() && etCity.text.toString()
                .isNotEmpty() && etState.text.toString()
                .isNotEmpty() && etZip.text.toString().isNotEmpty() && etCardNumber.text.toString()
                .isNotEmpty() && etCardName.text
                .toString().isNotEmpty() && etCardExp.text.toString()
                .isNotEmpty() && etCardCvv.text.toString()
                .isNotEmpty()
            btnPlaceOrder.isEnabled = isValid
            btnPlaceOrder.alpha = if (isValid) 1f else 0.5f
        }

    }

    private fun addOrder(order: AppOrder, userId: String) {
        FirebaseDatabase.getInstance().reference.child("orders/$userId").push().setValue(order)
    }


    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}