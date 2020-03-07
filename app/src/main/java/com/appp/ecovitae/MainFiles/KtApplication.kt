package com.appp.ecovitae.MainFiles

import androidx.multidex.MultiDexApplication
import com.androidnetworking.AndroidNetworking
import com.google.firebase.FirebaseApp
import com.google.firebase.database.FirebaseDatabase

class KtApplication : MultiDexApplication() {
    override fun onCreate() {
        super.onCreate()
        AndroidNetworking.initialize(applicationContext)
        if (!FirebaseApp.getApps(this).isEmpty()) {
            FirebaseDatabase.getInstance().setPersistenceEnabled(true)
        }
    }
}