package com.appp.ecovitae.DataModel

import com.google.firebase.database.DataSnapshot
import java.lang.Exception

class Levels (snapshot: DataSnapshot)
{
    var id: String? = ""
    var points: String ?= ""

    init
    {
        try
        {
            val data: HashMap<String, Any> = snapshot.value as HashMap<String, Any>
            id = snapshot.key ?: ""
            points = data["points"] as String
        } catch (e: Exception)
        {
            e.printStackTrace()
        }
    }

}