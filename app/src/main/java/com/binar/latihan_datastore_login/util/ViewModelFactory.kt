package com.binar.latihan_datastore_login.util

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.binar.latihan_datastore_login.data.LoginDataStoreManager
import com.binar.latihan_datastore_login.ui.LoginViewModel
import com.binar.latihan_datastore_login.ui.RegisterViewModel

class ViewModelFactory(private val pref: LoginDataStoreManager)
    : ViewModelProvider.NewInstanceFactory(){

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RegisterViewModel::class.java)) {
            return RegisterViewModel(pref) as T
        }
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            return LoginViewModel(pref) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }
}