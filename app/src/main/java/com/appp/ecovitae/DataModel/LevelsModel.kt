package com.appp.ecovitae.DataModel

import android.util.Log
import com.google.firebase.database.*
import java.util.*
import kotlin.collections.ArrayList

object LevelsModel  : Observable(){
    private var mValueDataListener: ValueEventListener? = null
    private var mAccountsList: ArrayList<Levels>? = ArrayList()

    private fun getDatabaseRef(): DatabaseReference?
    {
        return FirebaseDatabase.getInstance().reference.child("Levels")
    }

    init
    {
        // FirebaseApp.initializeApp(this);
        if (mValueDataListener != null)
        {

            getDatabaseRef()?.removeEventListener(mValueDataListener!!)

        }
        mValueDataListener = null
        // Log.i("AccountModel", "data init line 28");
        mValueDataListener = object : ValueEventListener
        {
            override fun onDataChange(datasnapshot: DataSnapshot)
            {
                try
                {
                    Log.i("AccountModel", "data updated line 34")
                    val data: ArrayList<Levels> = ArrayList()
                    if (datasnapshot != null)
                    {
                        for (snapshot: DataSnapshot in datasnapshot.children)
                        {
                            try
                            {
                                data.add(Levels(snapshot))

                            } catch (e: Exception)
                            {
                                e.printStackTrace()
                            }

                        }
                    }
                    mAccountsList = data
                    //Log.i("AccountsModel", "data updated, there are " + mAccountsList?.size + " in the list")
                    //Log.i("there is an update ", mAccountsList!!.size.toString())
                    //Log.i("there is an update ", mAccountsList!!.size.toString())
                    LevelsModel.setChanged()
                    Log.i("there is an update ", mAccountsList!!.size.toString())
                    LevelsModel.notifyObservers()
                } catch (e: Exception)
                {
                    e.printStackTrace()

                }
            }

            override fun onCancelled(p0: DatabaseError)
            {
                if (p0 != null)
                {
                    Log.i("AccountsModel", "line 70 Data update cancelled, err=${p0.message}")
                }
            }

        }
        getDatabaseRef()?.addValueEventListener(mValueDataListener as ValueEventListener)
    }

    fun getDataLevels(): ArrayList<Levels>?
    {
        return mAccountsList
    }
}

