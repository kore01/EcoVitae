package com.appp.ecovitae.ui.profile

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.appp.ecovitae.Main2Activity
import com.appp.ecovitae.R
import com.google.firebase.auth.FirebaseAuth

class ProfileFragment : Fragment() {

    private lateinit var profileViewModel: ProfileViewModel

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

        coins.text = (activity as Main2Activity).acc.coins.toString()
        points.text = (activity as Main2Activity).acc.points.toString()
        bin.text = (activity as Main2Activity).acc.bin.toString()

        var logout: Button = root.findViewById(R.id.logoutBtn)
        logout.setOnClickListener {

            FirebaseAuth.getInstance().signOut()
            (activity as Main2Activity).logout++
             Log.i("clicked", "logout")
        }
        return root
    }
}