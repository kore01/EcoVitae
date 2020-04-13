package com.appp.ecovitae.DataModel.Tips

import android.util.Log
import com.appp.ecovitae.DataModel.Bonus.Bonus
import com.appp.ecovitae.DataModel.Tips.TipModel
import com.google.firebase.database.*
import java.util.*
import kotlin.collections.ArrayList

object TipModel : Observable(){
    private var mValueDataListener: ValueEventListener? = null
    private var mTipList: ArrayList<Tip>? = ArrayList()
    private fun getDatabaseRef(): DatabaseReference?
    {
        return FirebaseDatabase.getInstance().reference.child("Tip")
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
                    val data: ArrayList<Tip> = ArrayList()
                    if (datasnapshot != null)
                    {
                        for (snapshot: DataSnapshot in datasnapshot.children)
                        {
                            try
                            {
                                data.add(
                                    Tip(
                                        snapshot
                                    )
                                )

                            } catch (e: Exception)
                            {
                                e.printStackTrace()
                            }

                        }
                    }
                    mTipList = data
                    Log.i("TipModel", "data updated, there are " + mTipList?.size + " in the list")
                    //Log.i("there is an update ", mTipList!!.size.toString())
                    for(a in mTipList!!)
                    {
                        //Log.i("TipModel", "Subjects: " + a.subject1 + " "+a.number);
                    }
                    //Log.i("there is an update ", mTipList!!.size.toString())
                    TipModel.setChanged()
                    Log.i("there is an update ", mTipList!!.size.toString())
                    TipModel.notifyObservers()
                } catch (e: Exception)
                {
                    e.printStackTrace()

                }
            }

            override fun onCancelled(p0: DatabaseError)
            {
                if (p0 != null)
                {
                    Log.i("TipModel", "line 70 Data update cancelled, err=${p0.message}")
                }
            }

        }
        getDatabaseRef()
            ?.addValueEventListener(mValueDataListener as ValueEventListener)
    }

    fun getDataTip(): ArrayList<Tip>?
    {
        return mTipList
    }
}