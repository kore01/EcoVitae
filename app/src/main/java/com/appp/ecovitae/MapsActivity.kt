package com.appp.ecovitae

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
import android.widget.*
import androidx.core.app.ActivityCompat
import com.appp.ecovitae.Adapter.CustomAdapter
import com.appp.ecovitae.DataModel.Punkts.PunktsModel
import com.appp.ecovitae.MainFiles.BaseActivity
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.FirebaseApp
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.zxing.integration.android.IntentIntegrator
import com.google.zxing.integration.android.IntentResult
import java.util.*
import kotlin.math.asin
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt


class MapsActivity : BaseActivity(), OnMapReadyCallback, Observer {

    private lateinit var mMap: GoogleMap
    var punkts = PunktsModel.getData()!!
    fun updatepunkts() {
        mMap.clear()
        for (i in punkts) {
            if (!checkifmatches(i.whatfor!!)) continue
            Log.i("HEREEE", i.lati + i.long)
            val mest = LatLng(i.lati?.toDouble()!!, i.long?.toDouble()!!)
            var name = whatfor(i.whatfor.toString())
            if (i.color == "blue") {
                mMap.addMarker(
                    MarkerOptions().position(mest).title(name).snippet(i.desc)
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
                )
            }
            if (i.color == "yellow") {
                mMap.addMarker(
                    MarkerOptions().position(mest).title(name).snippet(i.desc)
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW))
                )
            }
            if (i.color == "green") {
                mMap.addMarker(
                    MarkerOptions().position(mest).title(name).snippet(i.desc)
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
                )
            }
            if (i.color == "orange") {
                mMap.addMarker(
                    MarkerOptions().position(mest).title(name).snippet(i.desc)
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
                )
            }
        }
    }

    override fun update(o: Observable?, arg: Any?) {
        punkts = PunktsModel.getData()!!
        updatepunkts()
        Log.i("updated", "Update")
        //onMapReady(mMap)
        getLastLocation()
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

    private var lv: ListView? = null
    private var modelArrayList: ArrayList<CheckBoxList>? = null
    private var customAdapter: CustomAdapter? = null
    private val trashlist = arrayOf("Хартия", "Метал", "Пластмаса", "Стъкло")


    var tags = "111111"
    lateinit var mFusedLocationClient: FusedLocationProviderClient
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        //requestPermissions()
        oncreate()


        FirebaseApp.initializeApp(this)
        myDialog = Dialog(this)
        PunktsModel
        PunktsModel.addObserver(this)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        getLastLocation()


        val btn_click_me = findViewById<Button>(R.id.button)
// set on-click listener
        btn_click_me.setOnClickListener {
            distance()
        }

        val btnScan = findViewById<Button>(R.id.button3)
        btnScan.setOnClickListener {
            run {
                IntentIntegrator(this).initiateScan()
            }
        }

        val btn_add_point = findViewById<Button>(R.id.addpoint)
        btn_add_point.setOnClickListener {
            Log.i("Here", "yeap, toast shown")
            Toast.makeText(this, "Add point here", Toast.LENGTH_LONG).show()
            Add_Point()
        }

        lv = findViewById<ListView>(R.id.lv)
        modelArrayList = getModel(false)
        customAdapter =
            CustomAdapter(this, modelArrayList!!)
        Log.i("modelarraylist", modelArrayList!!.size.toString() + "fdasfa")
        lv!!.adapter = customAdapter
        //show all punkts at the beginning
        Log.i("select?", "select")
        selectall()
        Log.i("select?", "select")
        val btn = findViewById<Button>(R.id.update)
        btn.setOnClickListener {
            Log.i("fdsafas", "vfdijfkealsd")
            filterr()
        }


    }

    var radio: RadioButton? = null

    private fun Add_Point() {
        myDialog!!.setContentView(R.layout.dial_add_point)

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
                Toast.makeText(this, "Please, fill in all gaps", Toast.LENGTH_LONG).show()
            }



            myDialog!!.dismiss()
        }

        /* val key = firebaseData.child("Punkts").push().key
         firebaseData.child("Punkts").child(key!!).child("WhatFor").setValue("00000")
         firebaseData.child("Punkts").child(key!!).child("desc").setValue("00000")
         firebaseData.child("Punkts").child(key!!).child("longitude").setValue("00000")
         firebaseData.child("Punkts").child(key!!).child("latitude").setValue("00000")
         firebaseData.child("Punkts").child(key!!).child("color").setValue("0e0000")
 */        myDialog!!.show()

    }

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

        Log.i("fdasfda", checked.toString() + "gyhjkojhgvh")
        Log.i("fdasfda", rb1.id.toString() + "gyhjkojhgvh")
        Log.i("fdasfda", rb2.id.toString() + "gyhjkojhgvh")
        Log.i("fdasfda", rb3.id.toString() + "gyhjkojhgvh")

        if (checked == rb1.id) {
            color = "blue"
        }

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


        val key = firebaseData.child("Punkts").push().key
        firebaseData.child("Punkts").child(key!!).child("WhatFor").setValue(whatfor)
        firebaseData.child("Punkts").child(key).child("desc").setValue(desc.text.toString())
        firebaseData.child("Punkts").child(key).child("longitude")
            .setValue(myloc!!.longitude.toString())
        firebaseData.child("Punkts").child(key).child("latitude")
            .setValue(myloc!!.latitude.toString())
        firebaseData.child("Punkts").child(key).child("color").setValue(color)
        //if(myDialog)
        return true

    }

    private fun filterr() {
        Log.i("updateeeed", "yeap")
        getnewtags()
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

    private fun selectall() {
        Log.i("select", "select all")

        for (i in this.modelArrayList!!) {
            Log.i("hereEEE", i.trash + " " + i.isSelected)
            i.isSelected = true

            Log.i("hereEEE", i.trash + " " + i.isSelected)
        }
        updatepunkts()
    }

    private fun deselectall() {
        for (i in this.modelArrayList!!) {
            i.isSelected = false
            Log.i("hereEEE", i.trash + " " + i.isSelected)
        }
        updatepunkts()
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

    private fun distance() {
        if (checkPermissions()) {
            if (isLocationEnabled()) {
                mFusedLocationClient.lastLocation.addOnCompleteListener(this) { task ->
                    val location: Location? = task.result
                    if (location == null) {
                        requestNewLocationData()
                    } else {
                        Log.i("Oppss", "ops")
                        val mest = LatLng(location.latitude, location.longitude)
                        Log.i(
                            "my location",
                            mest.latitude.toString() + " " + mest.longitude.toString()
                        )
                        var mina = 105.5
                        for (i in punkts) {
                            Log.i("HEREEE", i.lati + " " + i.long)
                            val mest2 = LatLng(i.lati?.toDouble()!!, i.long?.toDouble()!!)
                            Log.i(
                                "distance",
                                distance(mest, mest2).toString()
                            )
                            var dist = distance(mest, mest2)
                            if (mina > dist) mina = dist
                        }
                        Log.i("show mina", mina.toString())
                        if (mina < 15) {
                            AddOneBin()

                            Toast.makeText(this, "It was thrown away", Toast.LENGTH_LONG).show()
                        }
                    }
                }
            } else {
                Toast.makeText(this, "Turn on location", Toast.LENGTH_LONG).show()
                PleaseStartGPS()
            }
        } else {
            PleaseStartGPS()
            requestPermissions()
        }


    }

    fun AddOneBin() {
        acc.bin = acc.bin!! + 1
        Log.i("Acc.id ", acc.id)
        firebaseData.child("Users").child(acc.id!!.toString()).child("bin")
            .setValue(acc.bin.toString())
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        val result: IntentResult? =
            IntentIntegrator.parseActivityResult(requestCode, resultCode, data)

        if (result != null) {

            val txtValue = findViewById<TextView>(R.id.textView)
            if (result.contents != null) {
                txtValue.text = result.contents
                barcodenumb(result.contents.toString())
                Log.i("scannn", result.contents)
            } else {
                txtValue.text = "scan failed"
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    private fun distance(StartP: LatLng, EndP: LatLng): Double {
        val lat1 = StartP.latitude
        val lat2 = EndP.latitude
        val lon1 = StartP.longitude
        val lon2 = EndP.longitude
        val dLat = Math.toRadians(lat2 - lat1)
        val dLon = Math.toRadians(lon2 - lon1)
        val a = sin(dLat / 2) * sin(dLat / 2) +
                cos(Math.toRadians(lat1)) * cos(Math.toRadians(lat2)) *
                sin(dLon / 2) * sin(dLon / 2)
        val c = 2 * asin(sqrt(a))
        return 6366000 * c
    }


    var myloc: LatLng? = null
    @SuppressLint("MissingPermission")
    private fun getLastLocation() {
        if (checkPermissions()) {
            if (isLocationEnabled()) {

                mFusedLocationClient.lastLocation.addOnCompleteListener(this) { task ->
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
                Toast.makeText(this, "Turn on location", Toast.LENGTH_LONG).show()
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

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
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
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                this,
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
            this,
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

    private fun isLocationEnabled(): Boolean {
        var locationManager: LocationManager =
            getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    fun barcodenumb(barcodenum: String) {
        var givenstring = barcodenum

        val rootRef = FirebaseDatabase.getInstance().reference
        val ordersRef = rootRef.child("Barcode").orderByKey().startAt(givenstring).limitToFirst(1)
        Log.i("givenstring", givenstring)
        val valueEventListener = object : ValueEventListener {
            @SuppressLint("RestrictedApi")
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                for (ds in dataSnapshot.children) {
                    Log.i("dssss", ds.key.toString())
                    //Log.i("value", ds.value as String)
                    val packaging = ds.child("packaging").getValue(String::class.java)
                    val name = ds.child("product_name").getValue(String::class.java)

                    Log.i("here it issssss", name + "fdsafas")
                    //Log.d(TAG, username)
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.i("fuck", "opaaaaaa")
                //  Log.d(TAG, databaseError.getMessage()) //Don't ignore errors!
            }
        }
        ordersRef.addListenerForSingleValueEvent(valueEventListener)


    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        mMap.setMapStyle(
            MapStyleOptions.loadRawResourceStyle(
                this, R.raw.google_style
            )
        )

        // Add a marker in Sydney and move the camera
        Log.i("punkts", punkts.size.toString())
        updatepunkts()
        mMap.isMyLocationEnabled = true

        getLastLocation()
        var markerInfoWindowAdapter = InfoWindowAdapter(applicationContext)
        googleMap.setInfoWindowAdapter(markerInfoWindowAdapter)
    }

    fun whatfor(str: String): String {
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

    override fun onStart() {
        super.onStart()
        FirebaseApp.initializeApp(this)

        PunktsModel
        PunktsModel.addObserver(this)
    }
}
