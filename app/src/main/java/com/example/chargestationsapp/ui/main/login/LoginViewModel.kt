package com.example.chargestationsapp.ui.main.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class LoginViewModel : ViewModel() {
    private val _isSignIn = MutableLiveData(false)
    val isSignIn: LiveData<Boolean>
        get() = _isSignIn

    fun userSignedIn(isSignIn: Boolean) {
        this._isSignIn.value = isSignIn
    }

}