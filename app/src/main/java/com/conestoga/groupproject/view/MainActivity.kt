package com.conestoga.groupproject.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Recycler
import com.conestoga.groupproject.R
import com.conestoga.groupproject.data.entities.AppOrder
import com.conestoga.groupproject.data.entities.Product
import com.conestoga.groupproject.data.entities.User
import com.conestoga.groupproject.databinding.ActivityMainBinding
import com.conestoga.groupproject.databinding.ProductCardViewBinding
import com.conestoga.groupproject.view.adapters.GenericFirebaseAdapter
import com.conestoga.groupproject.view.adapters.WrapContentLinearLayoutManager
import com.conestoga.groupproject.view.checkout.ProductDetail
import com.conestoga.groupproject.view.orders.OrdersActivity
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class MainActivity : AppCompatActivity() {

    private var mAdapter: GenericFirebaseAdapter<Product, ProductCardViewBinding>? = null

    private lateinit var binding: ActivityMainBinding

    private var orders: MutableList<AppOrder> = mutableListOf()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.lifecycleOwner = this

        val query = FirebaseDatabase.getInstance().reference.child("watches")
        val options =
            FirebaseRecyclerOptions.Builder<Product>().setQuery(query, Product::class.java).build()
        binding.user = intent.getSerializableExtra("user") as User

        binding.ivBell.setOnClickListener {
            val intent = Intent(this, OrdersActivity::class.java)
            intent.putExtra("userId", binding.user?.id)
            startActivity(intent)
        }

        listenForOrders(binding.user?.id.toString())

        binding.rvProducts.apply {
            layoutManager = WrapContentLinearLayoutManager(this@MainActivity)
            mAdapter =
                GenericFirebaseAdapter<Product, ProductCardViewBinding>(
                    options,
                    R.layout.product_card_view
                ) { onProductClick(it) }
            adapter = mAdapter
        }
    }


    private fun listenForOrders(userId :String) {
        val query = FirebaseDatabase.getInstance().reference.child("orders/$userId")
        query.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                binding.ivBell.isVisible = snapshot.exists()
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("Orders", error.toString())
            }
        })
    }

    private fun onProductClick(product: Product?) {
        // open product detail activity
        val intent = Intent(this, ProductDetail::class.java)
        intent.putExtra("product", product)
        startActivity(intent)
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
}

private fun Intent.putExtra(s: String, orders: MutableList<AppOrder>) {
    this.putExtra(s, orders)
}
