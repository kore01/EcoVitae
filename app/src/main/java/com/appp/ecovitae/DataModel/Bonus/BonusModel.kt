package com.appp.ecovitae.DataModel.Bonus

import android.util.Log
import com.google.firebase.database.*
import java.util.*
import kotlin.collections.ArrayList

object BonusModel : Observable(){
    private var mValueDataListener: ValueEventListener? = null
    private var mBonusList: ArrayList<Bonus>? = ArrayList()

    private fun getDatabaseRef(): DatabaseReference?
    {
        return FirebaseDatabase.getInstance().reference.child("Bonus")
    }

    init
    {
        // FirebaseApp.initializeApp(this);
        if (mValueDataListener != null)
        {

            getDatabaseRef()
                ?.removeEventListener(mValueDataListener!!)

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
                    val data: ArrayList<Bonus> = ArrayList()
                    if (datasnapshot != null)
                    {
                        for (snapshot: DataSnapshot in datasnapshot.children)
                        {
                            try
                            {
                                data.add(
                                    Bonus(
                                        snapshot
                                    )
                                )

                            } catch (e: Exception)
                            {
                                e.printStackTrace()
                            }

                        }
                    }
                    mBonusList = data
                    Log.i("BonusModel", "data updated, there are " + mBonusList?.size + " in the list")
                    //Log.i("there is an update ", mBonusList!!.size.toString())
                    for(a in mBonusList!!)
                    {
                        //Log.i("BonusModel", "Subjects: " + a.subject1 + " "+a.number);
                    }
                    //Log.i("there is an update ", mBonusList!!.size.toString())
                    BonusModel.setChanged()
                    Log.i("there is an update ", mBonusList!!.size.toString())
                    BonusModel.notifyObservers()
                } catch (e: Exception)
                {
                    e.printStackTrace()

                }
            }

            override fun onCancelled(p0: DatabaseError)
            {
                if (p0 != null)
                {
                    Log.i("BonusModel", "line 70 Data update cancelled, err=${p0.message}")
                }
            }

        }
        getDatabaseRef()
            ?.addValueEventListener(mValueDataListener as ValueEventListener)
    }

    fun getDataBonus(): ArrayList<Bonus>?
    {
        return mBonusList
    }
}

