package com.binar.latihan_datastore_login.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.binar.latihan_datastore_login.data.LoginDataStoreManager
import kotlinx.coroutines.launch

class RegisterViewModel(private val pref: LoginDataStoreManager): ViewModel() {

    fun saveUser(name: String, password: String) {
        viewModelScope.launch {
            pref.setUser(name, password)
        }
    }
}