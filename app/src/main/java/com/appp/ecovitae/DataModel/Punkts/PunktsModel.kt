package com.appp.ecovitae.DataModel.Punkts

import android.util.Log
import com.google.firebase.database.*
import java.util.*
import kotlin.collections.ArrayList

object PunktsModel : Observable()
{
    private var mValueDataListener: ValueEventListener? = null
    private var mPunktsList: ArrayList<Punkts>? = ArrayList()

    private fun getDatabaseRef(): DatabaseReference?
    {
        return FirebaseDatabase.getInstance().reference.child("Punkts")
    }

    init
    {
        if (mValueDataListener != null)
        {

            getDatabaseRef()
                ?.removeEventListener(mValueDataListener!!)

        }
        mValueDataListener = null
        Log.i("PunktModel", "data init line 28")
        mValueDataListener = object : ValueEventListener
        {
            override fun onDataChange(datasnapshot: DataSnapshot)
            {
                try
                {
                    Log.i("PunktModel", "data updated line 34")
                    val data: ArrayList<Punkts> = ArrayList()

                    if (datasnapshot != null)
                    {
                        for (snapshot: DataSnapshot in datasnapshot.children)
                        {
                            try
                            {
                                data.add(
                                    Punkts(
                                        snapshot
                                    )
                                )

                            } catch (e: Exception)
                            {
                                e.printStackTrace()
                            }

                        }
                    }
                    mPunktsList = data
                    Log.i("PunktsModel", "data updated, there are " + mPunktsList?.size + " in the list")
                    for(a in mPunktsList!!)
                    {
                        Log.i("PunktsModel", "Subjects: " + a.lati + " "+a.long + " " + a.whatfor)
                    }
                    PunktsModel.setChanged()
                    PunktsModel.notifyObservers()
                } catch (e: Exception)
                {
                    e.printStackTrace()

                }
            }

            override fun onCancelled(p0: DatabaseError)
            {
                if (p0 != null)
                {
                    Log.i("PunktsModel", "line 70 Data update cancelled, err=${p0.message}")
                }
            }

        }
        getDatabaseRef()
            ?.addValueEventListener(mValueDataListener as ValueEventListener)
    }

    fun getData(): ArrayList<Punkts>?
    {
        return mPunktsList
    }
}