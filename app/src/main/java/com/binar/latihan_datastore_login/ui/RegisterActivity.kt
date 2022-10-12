package com.binar.latihan_datastore_login.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.binar.latihan_datastore_login.data.LoginDataStoreManager
import com.binar.latihan_datastore_login.databinding.ActivityRegisterBinding
import com.binar.latihan_datastore_login.util.ViewModelFactory

class RegisterActivity : AppCompatActivity() {

    private var _binding: ActivityRegisterBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: RegisterViewModel
    private lateinit var pref: LoginDataStoreManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupViewModel()
        setOnClickListener()
    }

    private fun setupViewModel() {
        pref = LoginDataStoreManager(this)
        viewModel = ViewModelProvider(this, ViewModelFactory(pref))[RegisterViewModel::class.java]
    }

    private fun setOnClickListener() {
        binding.btnRegister.setOnClickListener {
            if (validateInput()) {
                val username = binding.etUsername.text.toString()
                val password = binding.etPassword.text.toString()
                viewModel.saveUser(username, password)

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
            binding.etUsername.error = "Password must not be empty"
        }
        return isValid
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}