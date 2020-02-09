package com.appp.ecovitae.DataModel

import android.util.Log
import com.google.firebase.database.DataSnapshot
import java.lang.Exception

class NewsFeed (snapshot: DataSnapshot)
{
    var id: String? = ""
    var title: String ?= ""
    var text: String ?= ""
    var image: String ?= ""
    var tags: String ?= ""



    init
    {
        try
        {
            val data: HashMap<String, Any> = snapshot.value as HashMap<String, Any>
            id = snapshot.key ?: ""
            title = data["title"] as String
            image = data["image"] as String
            tags = data["tags"] as String
            text = data["text"] as String
            //long = data["longitude"] as String
            //lati = data["latitude"] as String

            Log.i("infotooo newsfeed", id+" "+text)
        } catch (e: Exception)
        {
            e.printStackTrace()
        }
    }

}