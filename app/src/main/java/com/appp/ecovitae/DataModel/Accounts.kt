package com.appp.ecovitae.DataModel

import android.util.Log
import com.google.firebase.database.DataSnapshot
import java.lang.Exception

class Accounts (snapshot: DataSnapshot)
{
    var id: String? = ""
    var points: String ?= ""
    var coins: String ?= ""
    var email: String ?= ""
    var bin: String ?= ""
    var comp: String ?= ""
    var rate: String ?= ""



    init
    {
        try
        {
            val data: HashMap<String, Any> = snapshot.value as HashMap<String, Any>
            id = snapshot.key ?: ""
            points = data["points"] as String
            coins = data["coins"] as String
            email = data["email"] as String
            bin = data["bin"] as String
            comp = data["company"] as String
            rate = data["rating"] as String
            //long = data["longitude"] as String
            //lati = data["latitude"] as String

            Log.i("infotooo", id+" "+email)
        } catch (e: Exception)
        {
            e.printStackTrace()
        }
    }

}