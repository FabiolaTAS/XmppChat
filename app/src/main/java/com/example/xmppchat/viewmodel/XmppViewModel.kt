package com.example.xmppchat.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.xmppchat.repository.XmppRepository

class XmppViewModel : ViewModel(){
    val repository = XmppRepository()

    fun connection(email: String, password: String) : MutableLiveData<Boolean> {
        return repository.connect(email, password)
    }
}