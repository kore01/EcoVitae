package com.appp.ecovitae.DataModel.NewsFeed

import android.util.Log
import com.google.firebase.database.*
import java.util.*
import kotlin.collections.ArrayList

object NewsFeedModel : Observable(){
    private var mValueDataListener: ValueEventListener? = null
    private var mNewsfeedList: ArrayList<NewsFeed>? = ArrayList()

    private fun getDatabaseRef(): DatabaseReference?
    {
        return FirebaseDatabase.getInstance().reference.child("Newsfeed")
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
                    val data: ArrayList<NewsFeed> = ArrayList()
                    if (datasnapshot != null)
                    {
                        for (snapshot: DataSnapshot in datasnapshot.children)
                        {
                            try
                            {
                                data.add(
                                    NewsFeed(
                                        snapshot
                                    )
                                )

                            } catch (e: Exception)
                            {
                                e.printStackTrace()
                            }

                        }
                    }
                    mNewsfeedList = data
                    Log.i("NewsfeedModel", "data updated, there are " + mNewsfeedList?.size + " in the list")
                    //Log.i("there is an update ", mNewsfeedList!!.size.toString())
                    for(a in mNewsfeedList!!)
                    {
                        //Log.i("NewsfeedModel", "Subjects: " + a.subject1 + " "+a.number);
                    }
                    //Log.i("there is an update ", mNewsfeedList!!.size.toString())
                    NewsFeedModel.setChanged()
                    Log.i("there is an update ", mNewsfeedList!!.size.toString())
                    NewsFeedModel.notifyObservers()
                } catch (e: Exception)
                {
                    e.printStackTrace()

                }
            }

            override fun onCancelled(p0: DatabaseError)
            {
                if (p0 != null)
                {
                    Log.i("NewsfeedModel", "line 70 Data update cancelled, err=${p0.message}")
                }
            }

        }
        getDatabaseRef()
            ?.addValueEventListener(mValueDataListener as ValueEventListener)
    }

    fun getDataNewsfeed(): ArrayList<NewsFeed>?
    {
        return mNewsfeedList
    }
}

