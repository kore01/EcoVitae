package com.appp.ecovitae.DataModel

import android.util.Log
import com.google.firebase.database.DataSnapshot
import java.lang.Exception

class Company(snapshot: DataSnapshot) {
    var id: String? = ""
    var name: String? = ""

    init {
        try {
            val data: HashMap<String, Any> = snapshot.value as HashMap<String, Any>
            id = snapshot.key ?: ""
            name = data["name"] as String
            //long = data["longitude"] as String
            //lati = data["latitude"] as String
            Log.i("company info", id + " " + name)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

}