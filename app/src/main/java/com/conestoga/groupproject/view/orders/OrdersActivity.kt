package com.conestoga.groupproject.view.orders

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.conestoga.groupproject.R
import com.conestoga.groupproject.data.entities.AppOrder
import com.conestoga.groupproject.databinding.ActivityOrdersBinding
import com.conestoga.groupproject.databinding.OrderCardViewBinding
import com.conestoga.groupproject.view.adapters.GenericFirebaseAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.FirebaseDatabase

class OrdersActivity : AppCompatActivity() {

    var mAdapter: GenericFirebaseAdapter<AppOrder, OrderCardViewBinding>? = null
    private lateinit var binding: ActivityOrdersBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_orders)
        binding.lifecycleOwner = this

        // enable action bar
        setSupportActionBar(binding.myToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val userId = intent.getStringExtra("userId")
        val query = FirebaseDatabase.getInstance().reference.child("orders/$userId")
        val options =
            FirebaseRecyclerOptions.Builder<AppOrder>().setQuery(query, AppOrder::class.java)
                .build()

        binding.rvProducts.apply {
            layoutManager = LinearLayoutManager(this@OrdersActivity)
            mAdapter =
                GenericFirebaseAdapter<AppOrder, OrderCardViewBinding>(
                    options,
                    R.layout.order_card_view,
                ) {onOrderClick(it)}

            adapter = mAdapter
        }
    }

    override fun onStart() {
        super.onStart()
        //log the adapter
        mAdapter?.startListening()
    }

    override fun onStop() {
        super.onStop()
        mAdapter?.stopListening()
    }

    override fun onDestroy() {
        super.onDestroy()
        mAdapter = null
    }

    private fun onOrderClick(order: AppOrder?) {
        Log.d("Order", order.toString())
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}