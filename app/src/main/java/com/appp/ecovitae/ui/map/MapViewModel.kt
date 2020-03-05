package com.appp.ecovitae.ui.map

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.appp.ecovitae.DataModel.Punkts.PunktsModel
import java.util.*

class MapViewModel : ViewModel(), Observer {

    var punkts = PunktsModel.getData()!!
    private val _text = MutableLiveData<String>().apply {
        value = "This is map Fragment"
    }
    val text: LiveData<String> = _text
    override fun update(o: Observable?, arg: Any?) {
        punkts = PunktsModel.getData()!!
    }





}