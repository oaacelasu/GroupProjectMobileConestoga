package com.conestoga.groupproject.view.auth

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.conestoga.groupproject.R
import com.conestoga.groupproject.data.entities.User
import com.conestoga.groupproject.databinding.ActivityAuthBinding
import com.conestoga.groupproject.view.MainActivity
import java.util.*

class AuthActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAuthBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthBinding.inflate(layoutInflater)
        binding.lifecycleOwner = this
        binding.btnGuest.setOnClickListener { loginAsGuest() }
        // TODO : handle login button click

        setContentView(binding.root)
    }

    private fun handleLogin(email: String, password: String) {
        // TODO: handle login with firebase auth and navigate to main activity with user data
    }

    private fun loginAsGuest() {
        intent = Intent(this, MainActivity::class.java)
        intent.putExtra("user", User("guest", "Guest User", "guest@gmail.com"))
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }
}