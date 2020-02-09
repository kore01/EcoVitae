package com.appp.ecovitae.DataModel

import android.content.Context
import android.content.SharedPreferences

class Prefs(context: Context) {
    val PREFS_FILENAME = "com.appp.ecovitae.DataModel.prefs"
    val AccID = "accid"
    var prefs: SharedPreferences = context.getSharedPreferences(PREFS_FILENAME, 0)

    var Acc_ID: String?
        get() = prefs.getString(AccID, "")
        set(value) = prefs.edit().putString(AccID, value).apply()
    var points: Int?
        get() = prefs.getInt( "points", 0)
        set(value) = prefs.edit().putInt("points", value!!).apply()
    var coins: Int?
        get() = prefs.getInt("coins", 0)
        set(value) = prefs.edit().putInt("coins", value!!).apply()
    var email: String?
        get() = prefs.getString("email", "")
        set(value) = prefs.edit().putString("email", value).apply()
}