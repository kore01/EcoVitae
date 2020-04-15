package com.appp.ecovitae.ui.profile

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONArrayRequestListener
import com.appp.ecovitae.Main2Activity
import com.appp.ecovitae.R
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.CommonStatusCodes
import com.google.android.gms.safetynet.SafetyNet
import com.google.firebase.auth.FirebaseAuth
import org.json.JSONArray

class ProfileFragment : Fragment() {

    private lateinit var profileViewModel: ProfileViewModel

    private var SITE_KEY = "6Lemy94UAAAAANJydwzCy04m_9IQQn9swHZiYWsJ"

    var code = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        profileViewModel =
            ViewModelProviders.of(this).get(ProfileViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_profile, container, false)
        val coins: TextView = root.findViewById(R.id.coins)
        val points: TextView = root.findViewById(R.id.points)
        val bin: TextView = root.findViewById(R.id.bin)
        val comp: TextView = root.findViewById(R.id.comp)
        val add: Button = root.findViewById(R.id.add)
        val edittext: EditText = root.findViewById(R.id.code)
        val yes: TextView = root.findViewById(R.id.yes)
        val no: TextView = root.findViewById(R.id.nope)

        yes.visibility = View.GONE
        no.visibility = View.GONE

        coins.text = (activity as Main2Activity).acc.coins.toString()
        points.text = (activity as Main2Activity).acc.points.toString()
        bin.text = (activity as Main2Activity).acc.bin.toString()
        comp.text = (activity as Main2Activity).acc.comp.toString()

        var logout: Button = root.findViewById(R.id.logoutBtn)
        logout.setOnClickListener {

            FirebaseAuth.getInstance().signOut()
            (activity as Main2Activity).logout++
            Log.i("clicked", "logout")
        }

        add.setOnClickListener {


            code = edittext.text.toString()
            if (code.length < 1) {
                no.visibility = View.VISIBLE

            } else {
                no.visibility = View.GONE

                //da adna captcha
                SafetyNet.getClient(activity as Main2Activity).verifyWithRecaptcha(SITE_KEY)
                    .addOnSuccessListener(activity as Main2Activity) { response ->
                        if (!response.tokenResult.isEmpty()) {
                            Log.i("response", "responded")
                            handleVerify(response.tokenResult)
                            var compp = findcomp(code)
                            if (compp == "-1") {
                                no.visibility = View.VISIBLE
                            }
                            else {

                                (activity as Main2Activity).acc.comp = compp
                                comp.text = (activity as Main2Activity).acc.comp.toString()
                                yes.visibility = View.VISIBLE
                                no.visibility = View.INVISIBLE
                                (activity as Main2Activity).updateacc()
                            }
                        }
                    }
                    .addOnFailureListener(activity as Main2Activity) { e ->
                        if (e is ApiException) {
                            Log.d(
                                TAG,
                                ("Error message: " + CommonStatusCodes.getStatusCodeString(e.statusCode))
                            )
                        } else {
                            Log.d(TAG, "Unknown type of error: " + e.message)
                        }
                    }

                //
                // SignInEmail()

            }
        }
        return root
    }

    protected fun handleVerify(responseToken: String) {
        //it is google recaptcha siteverify server
        //you can place your server url
        //SignInEmail()

        val url = "https://www.google.com/recaptcha/api/siteverify"
        AndroidNetworking.get(url)
            .addHeaders("token", responseToken)
            .setTag("MY_NETWORK_CALL")
            .setPriority(Priority.LOW)
            .build()
            .getAsJSONArray(object : JSONArrayRequestListener {
                override fun onResponse(response: JSONArray) {
                    Log.i("responded", "yeah")


                    // do anything with response
                }

                override fun onError(error: ANError) {
                    // handle error
                }
            })
    }

    private fun findcomp(str : String): String {


        for (i in (activity as Main2Activity).companies)
        {
            if(i.id == str)
            {

                return i.name.toString()

            }



        }
        return "-1"
    }

}




