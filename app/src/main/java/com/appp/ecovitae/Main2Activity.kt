package com.appp.ecovitae

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.appp.ecovitae.DataModel.Accounts.AccountsModel
import com.appp.ecovitae.DataModel.Accounts.MyAccount
import com.appp.ecovitae.DataModel.Bonus.BonusModel
import com.appp.ecovitae.DataModel.Company.CompanyModel
import com.appp.ecovitae.DataModel.Levels.LevelsModel
import com.appp.ecovitae.DataModel.NewsFeed.NewsFeedModel
import com.appp.ecovitae.DataModel.Punkts.PunktsModel
import com.appp.ecovitae.DataModel.Shops.ShopsModel
import com.appp.ecovitae.DataModel.Tips.TipModel
import com.appp.ecovitae.ui.info.InfoViewModel
import com.appp.ecovitae.ui.tips.TipsViewModel
import com.google.android.gms.location.*
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.ramotion.circlemenu.CircleMenuView
import java.util.*
import kotlin.properties.Delegates

class Main2Activity : AppCompatActivity(), Observer {

    private var lastLocation: Location? = null
    private lateinit var appBarConfiguration: AppBarConfiguration
    var myDialog: Dialog? = null
    var DialYes: Dialog? = null
    var slide: ProgressBar? = null
    private var lfrom: Button? = null
    private var lto: Button? = null
    private lateinit var mapViewModel: InfoViewModel


    var observed = false
    var newsss: Int by Delegates.observable(0) { property, oldValue, newValue ->
        observed = true
        val navController = findNavController(R.id.nav_host_fragment)
        navController.navigate(R.id.nav_news)
        Log.i("newsss", newsss.toString())
        updatelevels()
    }


    var shopsss: String by Delegates.observable(" ") { property, oldValue, newValue ->
        observed = true
        val navController = findNavController(R.id.nav_host_fragment)
        Log.i("shopsss", shopsss.toString())
        navController.navigate(R.id.nav_shop)
        updatelevels()

    }
    var bonusss: String by Delegates.observable(" ") { property, oldValue, newValue ->
        observed = true
        val navController = findNavController(R.id.nav_host_fragment)
        Log.i("bonusss", bonusss.toString())
        navController.navigate(R.id.nav_onebonus)

    }

    var logout: Int by Delegates.observable(0) { property, oldValue, newValue ->

        val navController = findNavController(R.id.nav_host_fragment)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        menu = findViewById(R.id.circle_menu)

        if (FirebaseAuth.getInstance().currentUser != null) {
            navView.menu.findItem(R.id.nav_slideshow).isVisible = false
            navView.menu.findItem(R.id.nav_tools).isVisible = true
            navController.navigate(R.id.nav_home)
            navView.visibility = View.VISIBLE
            toolbar.visibility = View.VISIBLE
            menu!!.visibility = View.VISIBLE
        } else {
            Log.i("Here", "Hfmdka")
            navController.navigate(R.id.nav_slideshow)
            //navView.menu.findItem(R.id.nav_tools).isVisible = false
            //navView.menu.findItem(R.id.nav_slideshow).isVisible = true
            toolbar.visibility = View.GONE
            navView.visibility = View.GONE
            menu!!.visibility = View.GONE
        }
    }

    var updated: Int by Delegates.observable(0) { property, oldValue, newValue ->

        Log.i("updated", "upd?")
        val navController = findNavController(R.id.nav_host_fragment)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        menu = findViewById(R.id.circle_menu)
        updatelevels()

        if (FirebaseAuth.getInstance().currentUser != null) {

            navView.menu.findItem(R.id.nav_slideshow).isVisible = false
            navView.menu.findItem(R.id.nav_tools).isVisible = true
            //navController.navigate(R.id.nav_home)
            navController.navigate(navController.currentDestination!!.id)
            navView.visibility = View.VISIBLE
            toolbar.visibility = View.VISIBLE
            menu!!.visibility = View.VISIBLE
        } else {
            Log.i("Here", "Hfmdka")
            navController.navigate(R.id.nav_slideshow)
            //navView.menu.findItem(R.id.nav_tools).isVisible = false
            //navView.menu.findItem(R.id.nav_slideshow).isVisible = true
            toolbar.visibility = View.GONE
            navView.visibility = View.GONE
            menu!!.visibility = View.GONE
        }

    }

    var users = AccountsModel.getDataAccounts()!!
    var news = NewsFeedModel.getDataNewsfeed()!!
    var companies = CompanyModel.getDataCompany()!!
    var shops = ShopsModel.getData()!!
    var bonuses = BonusModel.getDataBonus()!!
    var tips = TipModel.getDataTip()!!

    var punkts = PunktsModel.getData()!!
    var levels = LevelsModel.getDataLevels()!!
    var menu: CircleMenuView? = null
    var isFABOpen = false
    override fun onCreate(savedInstanceState: Bundle?) {
        Log.i("on create", "create")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)


        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment)
        var header = navView.getHeaderView(0) as LinearLayout
        slide = header.findViewById(R.id.progress)
        lfrom = header.findViewById(R.id.lvfrom)
        lto = header.findViewById(R.id.lvto)
        AccountsModel
        AccountsModel.addObserver(this)
        CompanyModel
        CompanyModel.addObserver(this)
        PunktsModel
        PunktsModel.addObserver(this)
        NewsFeedModel
        NewsFeedModel.addObserver(this)
        LevelsModel
        LevelsModel.addObserver(this)
        ShopsModel
        ShopsModel.addObserver(this)
        BonusModel
        BonusModel.addObserver(this)
        TipModel
        TipModel.addObserver(this)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        menu = findViewById(R.id.circle_menu)
        menu!!.animate().translationXBy(250f)
        menu!!.animate().translationYBy(250f)
        //menu!!.animate().alpha(0F).duration = 1
        menu!!.eventListener = object : CircleMenuView.EventListener() {
            override fun onMenuOpenAnimationStart(view: CircleMenuView) {
                showFABMenu()
            }

            override fun onMenuOpenAnimationEnd(view: CircleMenuView) {
                Log.d("D", "onMenuOpenAnimationEnd")
            }

            override fun onMenuCloseAnimationStart(view: CircleMenuView) {
                Log.d("D", "onMenuCloseAnimationStart")
                closeFABMenu()
            }

            override fun onMenuCloseAnimationEnd(view: CircleMenuView) {

            }

            override fun onButtonClickAnimationStart(view: CircleMenuView, index: Int) {
                Log.d("D", "onButtonClickAnimationStart| index: $index")
                Log.i("click", index.toString())
            }

            override fun onButtonClickAnimationEnd(view: CircleMenuView, index: Int) {
                closeFABMenu()


                if (index == 0) {
                    navController.navigate(R.id.nav_gallery)
                }
                if (index == 1) {
                    Log.i("index", "add")
                    barcodescan()
                }
                if (index == 2) {
                    Log.i("index", "calculator")
                    navController.navigate(R.id.nav_info)
                }
                if (index == 3) {
                    Log.i("index", "profile")
                    navController.navigate(R.id.nav_tools)
                }
                if (index == 4) {
                    navController.navigate(R.id.nav_home)
                }
                Log.d("D", "onButtonClickAnimationEnd| index: $index")
            }


        }


        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        //if(FirebaseAuth.getInstance().currentUser?.email.toString()!=null) {

        Log.i("should be here", "fdaksva")
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home,
                R.id.nav_gallery,
                R.id.nav_slideshow,
                R.id.nav_tools,
                R.id.nav_send,
                R.id.nav_tips,
                R.id.nav_shops,
                R.id.nav_bonus,
                R.id.nav_slideshow,
                R.id.nav_shop,
                R.id.nav_onebonus,
                R.id.nav_news,
                R.id.nav_info
            ), drawerLayout
        )

        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        myDialog = Dialog(this)
        DialYes = Dialog(this)
        val ss = intent.getStringExtra("barcode")
        if (ss != null) {
            Log.i("sss", ss)
            openDialThrow(ss)
        }
        updatelevels()

    }

    var tags = "111111"


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
        if (str.length < 5) return "ERROR "
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

    fun updatepunkts(mMap: GoogleMap) {
        mMap.clear()
        for (i in mapViewModel.punkts) {
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

    lateinit var mMap: GoogleMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    lateinit var mFusedLocationClient: FusedLocationProviderClient
    fun openDialThrow(barc: String) {

        signinacc(FirebaseAuth.getInstance().currentUser?.email.toString())

        myDialog!!.setContentView(R.layout.dial_throw_in)

        val txtclose: TextView = myDialog!!.findViewById(R.id.txtclose)
        txtclose.text = "X"
        txtclose.setOnClickListener {
            myDialog!!.dismiss()
        }
        mapViewModel =
            ViewModelProviders.of(this).get(InfoViewModel::class.java)


        var btnthrow: Button = myDialog!!.findViewById(R.id.throwout)
        btnthrow.setOnClickListener {
            Log.i("Throw button", "click")
            var res = throwin()
            if (res == true) {
                myDialog!!.dismiss()

                Log.i("yeap", "it worked")

            } else {
                Toast.makeText(this, "Моля, приближете се до контейнера.", Toast.LENGTH_LONG).show()
                Log.i("too far", "opa")
            }
        }


        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment

        mapFragment.getMapAsync(OnMapReadyCallback()
        { mmMap ->
            mMap = mmMap
            //  if (checkPermissions()){
            //  mMap = googleMap
            updatepunkts(mMap)
            mMap.setMapStyle(
                MapStyleOptions.loadRawResourceStyle(
                    this, R.raw.google_style
                )
            )

            mMap.isMyLocationEnabled = true
            fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
            fusedLocationClient.lastLocation.addOnSuccessListener(this) { location ->
                // Got last known location. In some rare situations this can be null.
                // 3

                Log.i("location", location.longitude.toString())
                if (location != null) {
                    lastLocation = location
                    val currentLatLng = LatLng(location.latitude, location.longitude)
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 18f))
                }

            }
        })


        var givenstring = barc
        val rootRef = FirebaseDatabase.getInstance().reference
        val ordersRef = rootRef.child("Barcode").orderByKey().startAt(givenstring).limitToFirst(1)
        Log.i("givenstring", givenstring)
        val valueEventListener = object : ValueEventListener {
            @SuppressLint("RestrictedApi")
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                for (ds in dataSnapshot.children) {
                    val packaging = ds.child("packaging").getValue(String::class.java)
                    val name = ds.child("product_name").getValue(String::class.java)
                    Log.i("here it issssss", name + "fdsafas" + "packaging: " + packaging)
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.i("fuck", "opaaaaaa")
                //  Log.d(TAG, databaseError.getMessage()) //Don't ignore errors!
            }
        }
        ordersRef.addListenerForSingleValueEvent(valueEventListener)



        myDialog!!.show()
    }

    fun DoYes(name1: String, desc1: String) {

        DialYes!!.setContentView(R.layout.dial_yes)

        val nam: TextView = DialYes!!.findViewById(R.id.name1)
        nam.text = name1
        Log.i("bonus?", nam.text as String)

        val des: TextView = DialYes!!.findViewById(R.id.desc1)
        des.text = desc1
        Log.i("bonus?", des.text as String)

        val txtclose: TextView = DialYes!!.findViewById(R.id.txtclose)
        txtclose.text = "X"
        txtclose.setOnClickListener {
            DialYes!!.dismiss()
        }

        Log.i("bonus?", name1+" "+desc1)

        DialYes!!.show()
    }

    fun DoNo() {

        myDialog!!.setContentView(R.layout.dial_no)

        val txtclose: TextView = myDialog!!.findViewById(R.id.txtclose)
        txtclose.text = "X"
        txtclose.setOnClickListener {
            myDialog!!.dismiss()
        }
        myDialog!!.show()
    }

    fun throwin(): Boolean {
        Log.i("Throw", "throw in")
        updated++

        for (i in mapViewModel.punkts) {
            if (!checkifmatches(i.whatfor!!)) continue
            val mest = LatLng(i.lati?.toDouble()!!, i.long?.toDouble()!!)


            var loc1 = Location(LocationManager.GPS_PROVIDER)
            var loc2 = Location(LocationManager.GPS_PROVIDER)


            loc1.setLatitude(mest.latitude);
            loc1.setLongitude(mest.longitude);

            loc2.setLatitude(lastLocation!!.latitude)
            loc2.setLongitude(lastLocation!!.longitude)


            var dist = loc1.distanceTo(loc2)

            Log.i("distance: ", dist.toString())
            if (dist > 7) continue

            acc.points = acc.points!! + 1
            acc.coins = acc.coins!! + 5
            acc.bin = acc.bin!! + 1
            updateacc()
            return true
        }
        return false
    }


    override fun onStart() {
        super.onStart()
        Log.i("on start", "starttt")
        updated++
    }

    override fun onRestart() {
        super.onRestart()
        Log.i("on restart", "REstarttt")
        updated++
    }

    override fun onStop() {
        super.onStop()
        Log.i("on stop", "stoppp")
        updated++
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.i("on destroy", "destroyy")
        updated++
    }

    override fun onPause() {
        super.onPause()
        Log.i("on pause", "pause")

    }

    override fun onResume() {
        super.onResume()
        Log.i("on resume", "resume")
        updated++
    }


    fun barcodescan() {
        Log.i("barcode scan", "barcode")

        val intent = Intent(this, Main4Activity::class.java)
        startActivity(intent)
        Log.i("initiated scan", "scan")
    }

    var scannedResult: String = ""


    override fun onSaveInstanceState(outState: Bundle) {
        Log.i("on save", "save")
        Log.i("scannedResult", scannedResult + " fdvadgtr")
        super.onSaveInstanceState(outState)
        outState.putString("scannedResult", scannedResult)
        Log.i("scannedResult", scannedResult + " fdvadgtr")
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {

        Log.i("onrestore", "restore")
        super.onRestoreInstanceState(savedInstanceState)
        savedInstanceState.let {
            scannedResult = it.getString("scannedResult").toString()
        }
    }


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
                    acc.points = acc.points!! + 5
                    acc.coins = acc.coins!! + 5
                    acc.bin = acc.bin!! + 1
                    updateacc()


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

    fun updateacc() {


        var firebaseData = FirebaseDatabase.getInstance().reference
        firebaseData.child("Users").child(acc.id.toString()).child("coins")
            .setValue(acc.coins.toString())
        firebaseData.child("Users").child(acc.id.toString()).child("points")
            .setValue(acc.points.toString())
        firebaseData.child("Users").child(acc.id.toString()).child("bin")
            .setValue(acc.bin.toString())
        firebaseData.child("Users").child(acc.id.toString()).child("rating")
            .setValue(acc.rate.toString())
        firebaseData.child("Users").child(acc.id.toString()).child("company")
            .setValue(acc.comp.toString())
    }


    @SuppressLint("RestrictedApi")
    private fun showFABMenu() {
        menu!!.animate().translationXBy(-250f)
        menu!!.animate().translationYBy(-250f)

        //fab2!!.animate().translationY(-resources.getDimension(R.dimen.standard_105))
        //fab3!!.animate().translationY(-resources.getDimension(R.dimen.standard_155))
    }

    @SuppressLint("RestrictedApi")
    private fun closeFABMenu() {
        menu!!.animate().translationXBy(250f)
        menu!!.animate().translationYBy(250f)

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        //menuInflater.inflate(R.menu.main2, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    var br: Int = 0
    override fun update(o: Observable?, arg: Any?) {
        users = AccountsModel.getDataAccounts()!!
        companies = CompanyModel.getDataCompany()!!
        punkts = PunktsModel.getData()!!
        news = NewsFeedModel.getDataNewsfeed()!!
        levels = LevelsModel.getDataLevels()!!
        shops = ShopsModel.getData()!!
        bonuses = BonusModel.getDataBonus()!!
        tips = TipModel.getDataTip()!!

        Log.i("users", users.size.toString())

        val navView: NavigationView = findViewById(R.id.nav_view)


        if (FirebaseAuth.getInstance().currentUser?.email.toString() != "") signinacc(FirebaseAuth.getInstance().currentUser?.email.toString())


        val navController = findNavController(R.id.nav_host_fragment)

        if (FirebaseAuth.getInstance().currentUser != null) {
            navView.menu.findItem(R.id.nav_slideshow).isVisible = false
            navView.menu.findItem(R.id.nav_tools).isVisible = true
        } else {
            navView.menu.findItem(R.id.nav_tools).isVisible = false
            navView.menu.findItem(R.id.nav_slideshow).isVisible = true
        }

        if (br == 0) {
            updated++
            br++
        }
        updatelevels()

    }

    var acc: MyAccount =
        MyAccount()

    fun signinacc(user: String) {


        //log in in account
        Log.i("signin", "fdnasvhjfkaeijdl")

        var email: String = ""

        var givenstring = user

        val rootRef = FirebaseDatabase.getInstance().reference
        val ordersRef =
            rootRef.child("Users").orderByChild("email").startAt(givenstring).limitToFirst(1)
        Log.i("givenstring", givenstring)
        val valueEventListener = object : ValueEventListener {
            @SuppressLint("RestrictedApi")
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                Log.i("onDataChange", user)
                for (ds in dataSnapshot.children) {
                    Log.i("dssss", ds.key.toString())
                    //Log.i("value", ds.value as String)
                    acc.email = ds.child("email").getValue(String::class.java)
                    acc.id = ds.key
                    acc.coins = ds.child("coins").getValue(String::class.java)?.toInt()
                    acc.points = ds.child("points").getValue(String::class.java)?.toInt()
                    acc.bin = ds.child("bin").getValue(String::class.java)?.toInt()
                    acc.rate = ds.child("rating").getValue(String::class.java)!!.toInt()
                    var num = ds.child("company").getValue(String::class.java)
                    Log.i("companiiiies", num.toString())


                    acc.comp = ds.child("company").getValue(String::class.java)
                    Log.i("companies", acc.comp)

                    Log.i("level", acc.points.toString())
                    acc.level = what_levell(acc.points!!.toInt())
                    Log.i("what level", acc.level.toString())
                    Log.i("email?", acc.email)

                    updated++

                }
            }

            override fun onCancelled(databaseError: DatabaseError) {

            }
        }
        ordersRef.addListenerForSingleValueEvent(valueEventListener)
    }

    fun what_levell(points: Int): Int {
        Log.i("what_Level_points", points.toString())
        Log.i("what_level_numb", acc.points.toString())

        for (i in levels) {
            if (i.points!!.toInt() <= points) continue
            Log.i("Level id is", i.id)
            return i.id!!.toInt() - 1
        }
        return 1
    }

    fun updatelevels() {
        var levell = acc.level!!
        Log.i("what level main???", levell.toString())
        if (levell < 2) {

            slide!!.max = 2
            slide!!.progress = 1

            return
        }

        lfrom!!.text = levell.toString()
        lto!!.text = (levell!! + 1).toString()


        var pointsfrom = levels[levell - 1].points!!.toInt() + 1
        var slidemax = levels[levell].points!!.toInt() - pointsfrom + 1
        var slideprogress = acc.points!! - pointsfrom + 1
        slide!!.max = slidemax
        slide!!.progress = slideprogress
//        updatelevels()


        Log.i("pointsfrom", pointsfrom.toString())
        //slide!!.max = 10
        //slide!!.progress = 6
    }

}
