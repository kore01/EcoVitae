package com.appp.ecovitae.DataModel.Tips

import com.google.firebase.database.DataSnapshot
import java.lang.Exception

class Tip (snapshot: DataSnapshot) {

    var id: String? = ""
    var desc: String? = ""
    var type: String? = ""
    var points: String? = ""


    init
    {
        try
        {
            val data: HashMap<String, Any> = snapshot.value as HashMap<String, Any>
            id = snapshot.key ?: ""
            desc = data["desc"] as String
            type = data["type"] as String
            points = data ["points"] as String
            //Log.i("infoPunkt", id+" "+desc+" "+long+" "+lati+" "+color)
        } catch (e: Exception)
        {
            e.printStackTrace()
        }
    }
}