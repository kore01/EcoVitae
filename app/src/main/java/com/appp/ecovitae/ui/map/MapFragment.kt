package com.appp.ecovitae.ui.map

import android.Manifest
import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.*
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.appp.ecovitae.Adapter.CustomAdapter
import com.appp.ecovitae.Adapter.InfoWindowAdapter
import com.appp.ecovitae.CheckBoxList
import com.appp.ecovitae.DataModel.Accounts.MyAccount
import com.appp.ecovitae.DataModel.InfoWindowData
import com.appp.ecovitae.Main2Activity
import com.appp.ecovitae.R
import com.appp.ecovitae.R.*
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.database.FirebaseDatabase
import java.util.ArrayList


class MapFragment : Fragment(), OnMapReadyCallback {

    private lateinit var mapViewModel: InfoViewModel
    lateinit var mMap: GoogleMap
    lateinit var mFusedLocationClient: FusedLocationProviderClient

    private var lv: ListView? = null
    private var modelArrayList: ArrayList<CheckBoxList>? = null
    private var customAdapter: CustomAdapter? = null
    private val trashlist = arrayOf("Хартия", "Метал", "Пластмаса", "Стъкло")
    private var myacc: MyAccount? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mapViewModel =
            ViewModelProviders.of(this).get(InfoViewModel::class.java)
        val root = inflater.inflate(layout.fragment_map, container, false)
        var upd: Button = root.findViewById(R.id.update)
        var sel: Button = root.findViewById(R.id.select)
        var add: ImageButton = root.findViewById(R.id.add)
        var desel: Button = root.findViewById(R.id.deselect)
        var filtbtn: ImageButton = root.findViewById(R.id.filter)
        var men: LinearLayout = root.findViewById(R.id.ll)
        lv = root.findViewById(R.id.lv)

        //filtbtn.visibility = View.GONE
        myDialog = Dialog(context!!)

        Log.i("fdasfas", "cat")
        val mapFragment =
            childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?

        mapFragment?.getMapAsync(this)
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(context!!)
        // onMapReady(mMap)
        //show all punkts at the beginning

        //
        myacc = (activity as Main2Activity).acc
        Log.i("account", myacc!!.comp.toString() + "fdaskfjc")


        if (myacc!!.comp.toString() == "") {
            add.visibility = View.GONE
            filtbtn.visibility = View.VISIBLE
        }

        upd.setOnClickListener {
            filtbtn.visibility = View.VISIBLE
            val animation = AnimationUtils.loadAnimation(context!!, R.anim.fade_out)
            men.startAnimation(animation)
            men.visibility = View.GONE
            add.visibility = View.VISIBLE
            if (myacc!!.comp.toString() == "") {
                add.visibility = View.GONE
            }
            filterr()
        }
        sel.setOnClickListener {
            filtbtn.visibility = View.VISIBLE
            val animation = AnimationUtils.loadAnimation(context!!, R.anim.fade_out)
            men.startAnimation(animation)
            men.visibility = View.GONE
            add.visibility = View.VISIBLE
            if (myacc!!.comp.toString() == "") {
                add.visibility = View.GONE
            }
            selectall()
        }
        desel.setOnClickListener {
            filtbtn.visibility = View.VISIBLE
            val animation = AnimationUtils.loadAnimation(context!!, R.anim.fade_out)
            men.startAnimation(animation)
            men.visibility = View.GONE
            add.visibility = View.VISIBLE
            if (myacc!!.comp.toString() == "") {
                add.visibility = View.GONE
            }
            deselectall()
        }
        men.visibility = View.INVISIBLE
        filtbtn.setOnClickListener {
            filtbtn.visibility = View.GONE
            add.visibility = View.GONE
            men.visibility = View.VISIBLE
            if (myacc!!.comp.toString() == "") {
                add.visibility = View.GONE
            }
            val animation = AnimationUtils.loadAnimation(context!!, R.anim.fade_in)
            men.startAnimation(animation)
        }


        add.setOnClickListener {
            Log.i("Here", "yeap, toast shown")
            //Toast.makeText(this, "Add point here", Toast.LENGTH_LONG).show()
            Add_Point()
        }
        modelArrayList = getModel(true)
        customAdapter =
            CustomAdapter(context!!, modelArrayList!!)
        Log.i("modelarraylist", modelArrayList!!.size.toString() + "fdasfa")
        lv!!.adapter = customAdapter

        return root
    }


    private fun Add_Point() {
        myDialog!!.setContentView(R.layout.dial_add_point)
        myDialog!!.getWindow()!!.setBackgroundDrawableResource(android.R.color.transparent);

        val txtclose: TextView = myDialog!!.findViewById(R.id.txtclose)
        txtclose.text = "X"
        txtclose.setOnClickListener {
            myDialog!!.dismiss()
        }


        val btn = myDialog!!.findViewById<Button>(R.id.btn_add)
        btn.setOnClickListener {
            if (check_all_is_filled()) {
                myDialog!!.dismiss()
            } else {
                Toast.makeText(context, "Please, fill in all gaps", Toast.LENGTH_LONG).show()
            }
            myDialog!!.dismiss()
        }
        myDialog!!.show()
    }

    var firebaseData = FirebaseDatabase.getInstance().reference

    private fun check_all_is_filled(): Boolean {
        val desc: EditText = myDialog!!.findViewById(R.id.desc)
        if (desc.text.equals("")) {
            return false
        }
        val cb1: CheckBox = myDialog!!.findViewById(R.id.cB1)
        val cb2: CheckBox = myDialog!!.findViewById(R.id.cB2)
        val cb3: CheckBox = myDialog!!.findViewById(R.id.cB3)
        val cb4: CheckBox = myDialog!!.findViewById(R.id.cB4)

        var whatfor = ""
        Log.i("thfnas", cb1.isChecked.toString())
        if (cb1.isChecked) whatfor += '1'
        else whatfor += '0'
        if (cb2.isChecked) whatfor += '1'
        else whatfor += '0'
        if (cb3.isChecked) whatfor += '1'
        else whatfor += '0'
        if (cb4.isChecked) whatfor += '1'
        else whatfor += '0'
        whatfor += '0'

        if (whatfor.equals("00000"))
            return false

        Log.i("try now", whatfor)
        //if (radio == null) return false
        //       Log.i("colour", radio!!.text.toString() + "fdas")
        val rb: RadioGroup = myDialog!!.findViewById(R.id.rb)

        val rb1: RadioButton = myDialog!!.findViewById(R.id.radioButton2)
        val rb2: RadioButton = myDialog!!.findViewById(R.id.radioButton3)
        val rb3: RadioButton = myDialog!!.findViewById(R.id.radioButton4)
        val checked = rb.checkedRadioButtonId

        var color = ""

        if (checked == rb1.id) {
            color = "blue"
        }

        if (checked == rb2.id) {
            color = "green"
        }
        if (checked == rb3.id) {
            color = "yellow"
        }
        Log.i("colour", color)

        Log.i("fdasfda", rb1.isChecked.toString() + " gyhjkojhgvh")
        Log.i("fdasfda", rb2.isChecked.toString() + " gyhjkojhgvh")
        Log.i("fdasfda", rb3.isChecked.toString() + " gyhjkojhgvh")

        getLastLocation()
        if (myloc == null) return false


        val pon: CheckBox = myDialog!!.findViewById(R.id.pon)
        val vt: CheckBox = myDialog!!.findViewById(R.id.vt)
        val sr: CheckBox = myDialog!!.findViewById(R.id.sr)
        val cht: CheckBox = myDialog!!.findViewById(R.id.cht)
        val pet: CheckBox = myDialog!!.findViewById(R.id.PT)
        val sb: CheckBox = myDialog!!.findViewById(R.id.SB)
        val ned: CheckBox = myDialog!!.findViewById(R.id.ND)

        var days = ""
        Log.i("thfnas", cb1.isChecked.toString())
        if (pon.isChecked) days += "понеделник, "
        if (vt.isChecked) days += "вторник, "
        if (sr.isChecked) days += "сряда, "
        if (cht.isChecked) days += "четвъртък, "
        if (pet.isChecked) days += "петък, "
        if (sb.isChecked) days += "събота, "
        if (ned.isChecked) days += "неделя, "

        if (days.equals(""))
            return false

        days=days.dropLast(2)


        val key = firebaseData.child("Punkts").push().key
        firebaseData.child("Punkts").child(key!!).child("WhatFor").setValue(whatfor)
        firebaseData.child("Punkts").child(key).child("desc").setValue(desc.text.toString())
        firebaseData.child("Punkts").child(key).child("longitude")
            .setValue(myloc!!.longitude.toString())
        firebaseData.child("Punkts").child(key).child("latitude")
            .setValue(myloc!!.latitude.toString())
        firebaseData.child("Punkts").child(key).child("color").setValue(color)
        firebaseData.child("Punkts").child(key).child("company").setValue(myacc!!.comp)
        firebaseData.child("Punkts").child(key).child("days").setValue(days)

        updatepunkts()
        //if(myDialog)
        return true

    }

    private fun getModel(isSelect: Boolean): ArrayList<CheckBoxList> {
        val list = ArrayList<CheckBoxList>()

        for (i in 0 until trashlist.size) {
            val model = CheckBoxList()
            model.setSelecteds(isSelect)
            model.setAnimals(trashlist[i])
            list.add(model)
        }
        return list
    }

    private fun filterr() {

        Log.i("updateeeed", "yeap")
        getnewtags()
        updatepunkts()
    }

    private fun selectall() {
        Log.i("select", modelArrayList!!.size.toString())
        for (i in modelArrayList!!) {
            Log.i("hereEEE", i.trash + " " + i.isSelected)
            i.isSelected = true
            Log.i("hereEEE", i.trash + " " + i.isSelected)
        }
        customAdapter =
            CustomAdapter(context!!, modelArrayList!!)
        Log.i("modelarraylist", modelArrayList!!.size.toString() + "fdasfa")
        lv!!.adapter = customAdapter

        tags = "111111"
        updatepunkts()
    }

    private fun deselectall() {
        for (i in this.modelArrayList!!) {
            i.isSelected = false
            Log.i("hereEEE", i.trash + " " + i.isSelected)
        }
        customAdapter =
            CustomAdapter(context!!, modelArrayList!!)
        Log.i("modelarraylist", modelArrayList!!.size.toString() + "fdasfa")
        lv!!.adapter = customAdapter
        tags = "000000"
        updatepunkts()
    }


    private fun getnewtags() {
        Log.i("hereEE", "entered")
        tags = ""
        for (i in this.modelArrayList!!) {
            if (i.isSelected == true) tags += "1"
            else tags += "0"
            Log.i("hereEEE", i.trash + " " + i.isSelected)
        }
        while (tags.length < 6) tags += "0"
    }

    var tags = "111111"
    var myDialog: Dialog? = null
    fun updatepunkts() {
        mMap.clear()
        for (i in mapViewModel.punkts) {
            if (!checkifmatches(i.whatfor!!)) continue
            Log.i("HEREEE", i.lati + i.long)
            val mest = LatLng(i.lati?.toDouble()!!, i.long?.toDouble()!!)
            var name = whatfor(i.whatfor.toString())

            val markerOptions = MarkerOptions()
            markerOptions.position(mest)
                .title("Location Details")
                .snippet("I am custom Location Marker.")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))

            val info = InfoWindowData(
                name, i.desc!!,
                i.comp!!,
                i.days!!
            )


            if (i.color == "blue") {
                markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
            }
            if (i.color == "yellow") {
                markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW))
            }
            if (i.color == "green") {
                markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
            }
            if (i.color == "orange") {
                markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
            }

            if (i.color == "purple") {
                markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA))
            }


            val customInfoWindow = InfoWindowAdapter(context!!)

            mMap!!.setInfoWindowAdapter(customInfoWindow)

            val marker = mMap!!.addMarker(markerOptions)
            marker.tag = info
            marker.showInfoWindow()

        }
    }

    private fun checkifmatches(ch: String): Boolean {
        var chsiz = ch.length - 1
        for (i in 0..chsiz) {
            if (ch[i] == '1' && tags[i] == '1') {
                return true
            }
        }
        return false
    }


    fun whatfor(str: String): String {
        //what for are the bins?
        if (str.length < 5) return "HHVHVB "
        var ans = ""
        if (str[0] == '1') {
            if (ans.isNotEmpty()) ans += ", "
            ans += "хартия"
        }
        if (str[1] == '1') {
            if (ans.isNotEmpty()) ans += ", "
            ans += "метал"
        }
        if (str[2] == '1') {
            if (ans.isNotEmpty()) ans += ", "
            ans += "пластмаса"
        }
        if (str[3] == '1') {
            if (ans.isNotEmpty()) ans += ", "
            ans += "стъкло"
        }
        ans = "Кош за " + ans
        return ans
    }

    override fun onMapReady(googleMap: GoogleMap) {


        if (checkPermissions()) {
            mMap = googleMap
            updatepunkts()
            mMap.setMapStyle(
                MapStyleOptions.loadRawResourceStyle(
                    context!!, raw.google_style
                )
            )

            mMap.isMyLocationEnabled = true

            getLastLocation()
            Log.i("select?", "select")
            selectall()
            Log.i("select?", "select")
        } else {
            PleaseStartGPS()
            requestPermissions()
        }
        // var markerInfoWindowAdapter = InfoWindowAdapter(getApplicationContext());
        //googleMap.setInfoWindowAdapter(markerInfoWindowAdapter);

    }

    var myloc: LatLng? = null
    private fun getLastLocation() {
        if (checkPermissions()) {
            if (isLocationEnabled()) {

                mFusedLocationClient.lastLocation.addOnCompleteListener { task ->
                    val location: Location? = task.result
                    if (location == null) {
                        requestNewLocationData()
                    } else {
                        val mest = LatLng(location.latitude, location.longitude)
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(mest))
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mest, 17F))
                        myloc = mest
                    }
                }
            } else {
                Toast.makeText(context!!, "Turn on location", Toast.LENGTH_LONG).show()
                PleaseStartGPS()
            }
        } else {
            PleaseStartGPS()

            requestPermissions()
        }
    }

    @SuppressLint("MissingPermission")
    private fun requestNewLocationData() {
        var mLocationRequest = LocationRequest()
        mLocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        mLocationRequest.interval = 0
        mLocationRequest.fastestInterval = 0
        mLocationRequest.numUpdates = 1

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(context!!)
        mFusedLocationClient.requestLocationUpdates(
            mLocationRequest, mLocationCallback,
            Looper.myLooper()
        )
    }

    private fun PleaseStartGPS() {
        Log.i("Check here", "checkkk")

        myDialog!!.setContentView(R.layout.dial_search_for)

        val btn = myDialog!!.findViewById<Button>(R.id.extra_act)
        btn.setOnClickListener {
            val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
            startActivity(intent)

            onMapReady(mMap)
            getLastLocation()
            onMapReady(mMap)
            myDialog!!.dismiss()
        }
        myDialog!!.show()
    }

    private val mLocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            var mLastLocation: Location = locationResult.lastLocation
        }
    }

    private fun checkPermissions(): Boolean {
        if (ActivityCompat.checkSelfPermission(
                context!!,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                context!!,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            return true
        }
        return false
    }

    val PERMISSION_ID = 102

    private fun requestPermissions() {
        ActivityCompat.requestPermissions(
            activity!!,
            arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            ),
            PERMISSION_ID
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (requestCode == PERMISSION_ID) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                // Granted. Start getting the location information
            }
        }
    }

    @SuppressLint("ServiceCast")
    private fun isLocationEnabled(): Boolean {

        var sensorManager =
            activity!!.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        //var locationManager: LocationManager =
        //  getSystemService(activity!!.LOCATION_SERVICE) as LocationManager
        return sensorManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || sensorManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }


}