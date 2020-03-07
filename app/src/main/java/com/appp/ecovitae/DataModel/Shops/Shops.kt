package com.appp.ecovitae.DataModel.Shops

import android.util.Log
import com.google.firebase.database.DataSnapshot
import java.lang.Exception

class Shops (snapshot: DataSnapshot) {

    var id: String? = ""
    var name: String? = ""
    var desc: String? = ""
    var adress: String? = ""

    //coordinates of shop
    var long: String? = ""
    var lati: String? = ""

    var image: String ?= ""
    init
    {
        try
        {
            val data: HashMap<String, Any> = snapshot.value as HashMap<String, Any>
            id = snapshot.key ?: ""
            desc = data["desc"] as String
            long = data["longitude"] as String
            lati = data["latitude"] as String
            name = data["name"] as String
            adress = data["adress"] as String
            image = data["image"] as String
            //Log.i("infoPunkt", id+" "+desc+" "+long+" "+lati+" "+color)
        } catch (e: Exception)
        {
            e.printStackTrace()
        }
    }
}