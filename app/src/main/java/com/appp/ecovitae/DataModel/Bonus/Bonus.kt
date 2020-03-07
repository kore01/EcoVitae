package com.appp.ecovitae.DataModel.Bonus

import android.util.Log
import com.google.firebase.database.DataSnapshot
import java.lang.Exception

class Bonus (snapshot: DataSnapshot) {

    var id: String? = ""
    var name: String? = ""
    var desc: String? = ""
    var partnerid: String? = ""


    //coordinates of shop
    var price: String ?= ""
    var numbtak: String ?= ""
    var numbus: String ?= ""
    var partner: String ?= ""
    var image: String ?= ""


    init
    {
        try
        {
            val data: HashMap<String, Any> = snapshot.value as HashMap<String, Any>
            id = snapshot.key ?: ""
            desc = data["desc"] as String
            name = data["name"] as String
            partnerid = data ["partnerid"] as String
            image = data["image"] as String
            price = data["price"] as String
            numbtak = data["numbtak"] as String
            numbus = data["numbus"] as String
            partner = data["partner"] as String
            //Log.i("infoPunkt", id+" "+desc+" "+long+" "+lati+" "+color)
        } catch (e: Exception)
        {
            e.printStackTrace()
        }
    }
}