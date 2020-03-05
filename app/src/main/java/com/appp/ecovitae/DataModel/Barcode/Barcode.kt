package com.appp.ecovitae.DataModel.Barcode

import com.google.firebase.database.DataSnapshot
import java.lang.Exception

class Barcode (snapshot: DataSnapshot)
{
    var id: String? = ""
    var material: String? = ""
    var points: String? = ""
    var name: String? = ""


    init
    {
        try
        {
            val data: HashMap<String, Any> = snapshot.value as HashMap<String, Any>
            id = snapshot.key ?: ""
            material = data["WhatFor"] as String
            points = data["points"] as String
            name=data["name"] as String

//            Log.i("infoPunkt", id+" "+desc+" "+long+" "+lati+" "+color)
        } catch (e: Exception)
        {
            e.printStackTrace()
        }
    }
}