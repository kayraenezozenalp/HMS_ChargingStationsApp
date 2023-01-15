package com.example.chargestationsapp.ui.main.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SearchViewModel : ViewModel() {
    private val _isSignOut = MutableLiveData(false)
    val isSignOut: LiveData<Boolean>
        get() = _isSignOut

    fun userSignedOut(isSignIn: Boolean) {
        this._isSignOut.value = isSignIn
    }

}