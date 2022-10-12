package com.binar.latihan_datastore_login.data

data class UserPreferences(
    val username: String,
    val password: String,
    val loginStatus: Boolean
)