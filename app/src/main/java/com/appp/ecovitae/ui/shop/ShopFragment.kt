package com.appp.ecovitae.ui.send

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ImageView
import android.widget.ListView
import android.widget.TextView
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.appp.ecovitae.Adapter.BonusAdapter
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


class ShopFragment : Fragment(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var sendViewModel: ShopsViewModel
    lateinit var coordin: LatLng
    lateinit var shops: Shops
    var bonusView: ListView? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        sendViewModel =
            ViewModelProviders.of(this).get(ShopsViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_shop, container, false)

        //shopsView = root.findViewById(R.id.shops_list)
        shops = (activity as Main2Activity).shops.filter { shop -> shop.id == (activity as Main2Activity).shopsss}.get(0)

        val title: TextView = root.findViewById(R.id.title)
        val desc: TextView = root.findViewById(R.id.desc)
        val image: ImageView = root.findViewById(R.id.logo)
        val address: TextView = root.findViewById(R.id.address)

        title.text = shops.name
        desc.text = shops.desc
        address.text = shops.adress

        val storage = FirebaseStorage.getInstance()
        val gsReference =
            storage.getReferenceFromUrl(shops.image!!.toUri().toString())

        GlideApp.with(context!!)
            .load(gsReference)
            .into(image)


        val mapFragment =
            childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?

        if(shops.lati=="") mapFragment!!.setMenuVisibility(false)

        mapFragment?.getMapAsync(this)


        bonusView = root.findViewById(R.id.bonus_list)



        return root
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        if(shops.lati=="") return
        // Add a marker in Sydney and move the camera
        val sydney = LatLng(shops.lati!!.toDouble(), shops.long!!.toDouble())
        mMap.addMarker(MarkerOptions().position(sydney).title(shops.name))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 17F))
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.i("the hell", "bonus")
        Log.i("Bonuses size", (activity as Main2Activity).bonuses.size.toString())
        var adapter =
            BonusAdapter((activity as Main2Activity),
                (activity as Main2Activity).bonuses.filter{ bonus -> bonus.partnerid == shops.id  } as ArrayList<Bonus>)


        //Log.i("modelarraylist", modelArrayList!!.size.toString() + "fdasfa")
        bonusView!!.adapter = adapter

        bonusView!!.onItemClickListener =
            AdapterView.OnItemClickListener { parent, view, position, id ->
                // This is your listview's selected item
                // val item = parent.getItemAtPosition(position)
                (activity as Main2Activity).bonusss=
                    (activity as Main2Activity).bonuses[position].id.toString()
            }
    }

}