package com.binar.latihan_datastore_login.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.binar.latihan_datastore_login.data.LoginDataStoreManager
import com.binar.latihan_datastore_login.data.UserPreferences
import kotlinx.coroutines.launch

class LoginViewModel(private val pref: LoginDataStoreManager): ViewModel() {

    fun getUser(): LiveData<UserPreferences> {
        return pref.getUser().asLiveData()
    }

    fun setUserLogin(isLogin: Boolean) {
        viewModelScope.launch {
            pref.setUserLogin(isLogin)
        }
    }

    fun setProfileImage(uri: String) {
        viewModelScope.launch {
            pref.setProfileImage(uri)
        }
    }

    fun getUserLogin(): LiveData<Boolean> {
        return pref.getUserLogin().asLiveData()
    }

    fun getUserProfileImage(): LiveData<String> {
        return pref.getUserProfileImage().asLiveData()
    }
}