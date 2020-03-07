package com.appp.ecovitae.ui.send

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.appp.ecovitae.DataModel.Bonus.Bonus
import com.appp.ecovitae.DataModel.Shops.Shops
import com.appp.ecovitae.GlideApp
import com.appp.ecovitae.Main2Activity
import com.appp.ecovitae.R
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.storage.FirebaseStorage

class OneBonusFragment : Fragment(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var sendViewModel: ShopsViewModel
    private lateinit var bonus : Bonus
    private lateinit var shop : Shops

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        sendViewModel =
            ViewModelProviders.of(this).get(ShopsViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_onebonus, container, false)


        bonus = (activity as Main2Activity).bonuses.filter { bonus -> bonus.id == (activity as Main2Activity).bonusss}.get(0)

        shop = (activity as Main2Activity).shops.filter { shop -> shop.id == bonus.partnerid}.get(0)

        val title: TextView = root.findViewById(R.id.title)
        val desc: TextView = root.findViewById(R.id.desc_bonus)
        val picture: ImageView = root.findViewById(R.id.picture)

        val storage = FirebaseStorage.getInstance()
        val gsReference =
            storage.getReferenceFromUrl(bonus.image!!.toUri().toString())

        GlideApp.with(context!!)
            .load(gsReference)
            .into(picture)


        title.text = bonus.name
        desc.text = bonus.desc


        val name_partner: TextView = root.findViewById(R.id.partner)
        val address: TextView = root.findViewById(R.id.address)
        val desc_partner: TextView = root.findViewById(R.id.desc)
        val logo: ImageView = root.findViewById(R.id.logo)

        address.text = shop.adress
        name_partner.text = shop.name
        desc_partner.text = shop.desc


        val gsReference2 =
            storage.getReferenceFromUrl(shop.image!!.toUri().toString())

        GlideApp.with(context!!)
            .load(gsReference2)
            .into(logo)


        val mapFragment =
            childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?

        if(shop.lati=="") mapFragment!!.setMenuVisibility(false)

        mapFragment?.getMapAsync(this)

        val check_out: Button = root.findViewById(R.id.btn_check_out)
        check_out.setOnClickListener {
            (activity as Main2Activity).shopsss = shop.id.toString()
        }



        return root
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        if(shop.lati=="") return
        // Add a marker in Sydney and move the camera
        val sydney = LatLng(shop.lati!!.toDouble(), shop.long!!.toDouble())
        mMap.addMarker(MarkerOptions().position(sydney).title(shop.name))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 17F))
    }
}