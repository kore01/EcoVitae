package com.appp.ecovitae.ui.send

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class OneBonusViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is bonus Fragment"
    }
    val text: LiveData<String> = _text
}