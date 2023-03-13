package com.conestoga.groupproject.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import com.conestoga.groupproject.R
import com.conestoga.groupproject.data.entities.AppOrder
import com.conestoga.groupproject.data.entities.Product
import com.conestoga.groupproject.databinding.ActivityMainBinding
import com.conestoga.groupproject.databinding.ProductCardViewBinding
import com.conestoga.groupproject.view.adapters.GenericFirebaseAdapter
import com.conestoga.groupproject.view.adapters.WrapContentLinearLayoutManager
import com.conestoga.groupproject.view.auth.SignInActivity
import com.conestoga.groupproject.view.checkout.ProductDetail
import com.conestoga.groupproject.view.orders.OrdersActivity
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class MainActivity : AppCompatActivity() {

    private var mAdapter: GenericFirebaseAdapter<Product, ProductCardViewBinding>? = null

    private lateinit var binding: ActivityMainBinding

    private var orders: MutableList<AppOrder> = mutableListOf()

    // alert dialog reference
    private var alertDialog: AlertDialog? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.lifecycleOwner = this

        val query = FirebaseDatabase.getInstance().reference.child("watches")
        val options =
            FirebaseRecyclerOptions.Builder<Product>().setQuery(query, Product::class.java).build()
        binding.user = FirebaseAuth.getInstance().currentUser

        binding.ivBell.setOnClickListener {
            val intent = Intent(this, OrdersActivity::class.java)
            intent.putExtra("userId", binding.user?.uid)
            startActivity(intent)
        }

        listenForOrders(binding.user?.uid.toString())

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

    // bind profile alert dialog
    private fun bindProfileDialog() {
        // Create a new AlertDialog Builder
        val builder = AlertDialog.Builder(this)

        // Inflate the logout_dialog layout
        val dialogView = layoutInflater.inflate(R.layout.fragment_profile_dialog, null)

        // Set the title and subtitle
        dialogView.findViewById<TextView>(R.id.dialog_name).text =  binding.user?.displayName
        dialogView.findViewById<TextView>(R.id.dialog_email).text =  binding.user?.email


        // Set the Back button click listener
        dialogView.findViewById<Button>(R.id.back_button).setOnClickListener {
            alertDialog?.dismiss() // Dismiss the dialog
        }

        // Set the Logout button click listener
        dialogView.findViewById<Button>(R.id.logout_button).setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            val intent = Intent(this, SignInActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }

        // Set the custom view for the AlertDialog
        builder.setView(dialogView)

        // Create the AlertDialog
        alertDialog = builder.create()

    }


    private fun listenForOrders(userId: String) {
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
        bindProfileDialog()
        binding.ivAvatar.setOnClickListener {
            // Show the dialog
            alertDialog?.show()
        }
    }

    override fun onStop() {
        super.onStop()
        mAdapter?.stopListening()
        alertDialog?.cancel()
        alertDialog = null

    }

    override fun onDestroy() {
        super.onDestroy()
        mAdapter = null
        alertDialog?.cancel()
        alertDialog = null
    }
}