package com.appp.ecovitae.DataModel.Punkts

import android.util.Log
import com.google.firebase.database.DataSnapshot
import java.lang.Exception

class Punkts (snapshot: DataSnapshot)
{
    var id: String? = ""
    var desc: String? = ""
    var long: String? = ""
    var lati: String? = ""
    var color: String? = ""
    var whatfor: String? = ""
    var comp: String ?= ""

    init
    {
        try
        {
            val data: HashMap<String, Any> = snapshot.value as HashMap<String, Any>
            id = snapshot.key ?: ""
            desc = data["desc"] as String
            long = data["longitude"] as String
            lati = data["latitude"] as String
            color = data["color"] as String
            whatfor = data["WhatFor"] as String
            comp = data["company"] as String

            Log.i("infoPunkt", id+" "+desc+" "+long+" "+lati+" "+color)
        } catch (e: Exception)
        {
            e.printStackTrace()
        }
    }

}