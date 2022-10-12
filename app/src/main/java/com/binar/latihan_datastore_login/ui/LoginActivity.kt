package com.binar.latihan_datastore_login.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.binar.latihan_datastore_login.data.LoginDataStoreManager
import com.binar.latihan_datastore_login.databinding.ActivityLoginBinding
import com.binar.latihan_datastore_login.util.ViewModelFactory
import kotlinx.coroutines.delay

class LoginActivity : AppCompatActivity() {

    private var _binding: ActivityLoginBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: LoginViewModel
    private lateinit var pref: LoginDataStoreManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupViewModel()
        setOnClickListener()
        checkUserLoginStatus()
    }

    private fun setupViewModel() {
        pref = LoginDataStoreManager(this)
        viewModel = ViewModelProvider(this, ViewModelFactory(pref))[LoginViewModel::class.java]
    }

    private fun setOnClickListener() {
        binding.tvRegisterHere.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
        binding.btnLogin.setOnClickListener {
            if (validateInput()) {
                val username = binding.etUsername.text.toString()
                val password = binding.etPassword.text.toString()

                viewModel.getUser().observe(this) {
                    if (it.username == username && it.password == password) {
                        viewModel.setUserLogin(true)

                        startActivity(Intent(this, MainActivity::class.java).apply {
                            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
                        })
                    } else {
                        Toast.makeText(this, "Wrong username or password", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun checkUserLoginStatus() {
        viewModel.getUserLogin().observe(this) {
            Log.d("loginstate", it.toString())
            if (it) {
                startActivity(Intent(this, MainActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
                })
                finish()
            }
        }
    }

    private fun validateInput(): Boolean {
        var isValid = true
        val username = binding.etUsername.text.toString()
        val password = binding.etPassword.text.toString()

        if (username.isEmpty()) {
            isValid = false
            binding.etUsername.error = "Username or password must not be empty"
        }
        if (password.isEmpty()) {
            isValid = false
            binding.etPassword.error = "Password must not be empty"
        }
        return isValid
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}