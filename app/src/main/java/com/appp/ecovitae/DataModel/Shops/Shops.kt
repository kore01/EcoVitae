package com.appp.ecovitae.DataModel.Shops

import android.util.Log
import com.google.firebase.database.DataSnapshot
import java.lang.Exception

class Shops (snapshot: DataSnapshot) {

    var id: String? = ""
    var name: String? = ""
    var desc: String? = ""

    //coordinates of shop
    var long: String? = ""
    var lati: String? = ""

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
            //Log.i("infoPunkt", id+" "+desc+" "+long+" "+lati+" "+color)
        } catch (e: Exception)
        {
            e.printStackTrace()
        }
    }
}