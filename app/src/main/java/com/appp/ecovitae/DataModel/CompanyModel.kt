package com.appp.ecovitae.DataModel

import android.util.Log
import com.google.firebase.database.*
import java.util.*
import kotlin.collections.ArrayList

object CompanyModel : Observable(){
    private var mValueDataListener: ValueEventListener? = null
    private var mCompanyList: ArrayList<Company>? = ArrayList()

    private fun getDatabaseRef(): DatabaseReference?
    {
        return FirebaseDatabase.getInstance().reference.child("Company")
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
                    val data: ArrayList<Company> = ArrayList()
                    if (datasnapshot != null)
                    {
                        for (snapshot: DataSnapshot in datasnapshot.children)
                        {
                            try
                            {
                                data.add(Company(snapshot))

                            } catch (e: Exception)
                            {
                                e.printStackTrace()
                            }

                        }
                    }
                    mCompanyList = data
                    Log.i("CompanyModel", "data updated, there are " + mCompanyList?.size + " in the list")
                    //Log.i("there is an update ", mCompanyList!!.size.toString())
                    for(a in mCompanyList!!)
                    {
                        //Log.i("CompanyModel", "Subjects: " + a.subject1 + " "+a.number);
                    }
                    //Log.i("there is an update ", mCompanyList!!.size.toString())
                    CompanyModel.setChanged()
                    Log.i("there is an update ", mCompanyList!!.size.toString())
                    CompanyModel.notifyObservers()
                } catch (e: Exception)
                {
                    e.printStackTrace()

                }
            }

            override fun onCancelled(p0: DatabaseError)
            {
                if (p0 != null)
                {
                    Log.i("CompanyModel", "line 70 Data update cancelled, err=${p0.message}")
                }
            }

        }
        getDatabaseRef()?.addValueEventListener(mValueDataListener as ValueEventListener)
    }

    fun getDataCompany(): ArrayList<Company>?
    {
        return mCompanyList
    }
}

