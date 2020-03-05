package com.appp.ecovitae.DataModel.Shops

import android.util.Log
import com.google.firebase.database.*
import java.util.*
import kotlin.collections.ArrayList

object ShopsModel : Observable()
{
    private var mValueDataListener: ValueEventListener? = null
    private var mShopsList: ArrayList<Shops>? = ArrayList()

    private fun getDatabaseRef(): DatabaseReference?
    {
        return FirebaseDatabase.getInstance().reference.child("Shops")
    }

    init
    {
        if (mValueDataListener != null)
        {

            getDatabaseRef()
                ?.removeEventListener(mValueDataListener!!)

        }
        mValueDataListener = null
        Log.i("ShopModel", "data init line 28")
        mValueDataListener = object : ValueEventListener
        {
            override fun onDataChange(datasnapshot: DataSnapshot)
            {
                try
                {
                    Log.i("ShopModel", "data updated line 34")
                    val data: ArrayList<Shops> = ArrayList()

                    if (datasnapshot != null)
                    {
                        for (snapshot: DataSnapshot in datasnapshot.children)
                        {
                            try
                            {
                                data.add(
                                    Shops(
                                        snapshot
                                    )
                                )

                            } catch (e: Exception)
                            {
                                e.printStackTrace()
                            }

                        }
                    }
                    mShopsList = data
                    Log.i("ShopsModel", "data updated, there are " + mShopsList?.size + " in the list")
                    ShopsModel.setChanged()
                    ShopsModel.notifyObservers()
                } catch (e: Exception)
                {
                    e.printStackTrace()

                }
            }

            override fun onCancelled(p0: DatabaseError)
            {
                if (p0 != null)
                {
                    Log.i("ShopsModel", "line 70 Data update cancelled, err=${p0.message}")
                }
            }

        }
        getDatabaseRef()
            ?.addValueEventListener(mValueDataListener as ValueEventListener)
    }

    fun getData(): ArrayList<Shops>?
    {
        return mShopsList
    }
}