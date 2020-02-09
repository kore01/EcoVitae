package com.appp.ecovitae.MainFiles

import android.app.Dialog
import android.app.ProgressDialog
import android.content.Intent
import android.text.TextUtils
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.appp.ecovitae.DataModel.*
import com.appp.ecovitae.MainActivity
import com.appp.ecovitae.MapsActivity
import com.appp.ecovitae.Profile
import com.appp.ecovitae.R

import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.util.*


open class BaseActivity : Observer, NavigationView.OnNavigationItemSelectedListener,
    AppCompatActivity(), AdapterView.OnItemSelectedListener {

    override fun update(o: Observable?, arg: Any?) {
        users = AccountsModel.getDataAccounts()!!
        companies = CompanyModel.getDataCompany()!!
        signinacc()
        val nav_Menu = navView.menu
        val user = auth.currentUser
        if (user != null) {
            Log.i("Myyyyy", "Myyyyyy")
            nav_Menu.findItem(R.id.nav_profile).isVisible = true
            nav_Menu.findItem(R.id.nav_login).isVisible = false
        } else {
            nav_Menu.findItem(R.id.nav_profile).isVisible = false
            nav_Menu.findItem(R.id.nav_login).isVisible = true
        }
        tobeupdatedinact()
    }

    open fun tobeupdatedinact() {


    }

    //UI elements
    private var etFirstName: EditText? = null
    private var etLastName: EditText? = null
    private var etEmail: EditText? = null
    private var etPassword: EditText? = null
    private var btnCreateAccount: Button? = null
    private var mProgressBar: ProgressDialog? = null
    //Firebase references
    private var mDatabaseReference: DatabaseReference? = null
    private var mDatabase: FirebaseDatabase? = null
    private var mAuth: FirebaseAuth? = null

    val RC_SIGN_IN: Int = 1
    lateinit var mGoogleSignInClient: GoogleSignInClient
    lateinit var mGoogleSignInOptions: GoogleSignInOptions

    private lateinit var firebaseAuth: FirebaseAuth

    var users = AccountsModel.getDataAccounts()!!
    var companies = CompanyModel.getDataCompany()!!

    var myDialog: Dialog? = null
    var myLogDialog: Dialog? = null
    lateinit var toolbar: Toolbar
    lateinit var drawerLayout: DrawerLayout
    lateinit var navView: NavigationView

    private lateinit var auth: FirebaseAuth
    //info for the acc
    var acc: MyAccount = MyAccount()

    //information of previous entersb
    var prefs: Prefs? = null

    fun oncreate() {

//        toolbar = findViewById(R.id.toolbar)
        //      setSupportActionBar(toolbar)

        AccountsModel
        AccountsModel.addObserver(this)

        drawerLayout = findViewById(R.id.drawer_layout)
        navView = findViewById(R.id.nav_view)

//mDatabaseReference!!.keepSynced(true)

        signinacc()
        val nav_Menu = navView.menu

        val toggle = ActionBarDrawerToggle(
            this, drawerLayout, toolbar, 0, 0
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        navView.setNavigationItemSelectedListener(this)
        myDialog = Dialog(this)
        myLogDialog = Dialog(this)
        auth = FirebaseAuth.getInstance()
        //take previous enters
        prefs = Prefs(this)
        val user = auth.currentUser
        if (user != null) {
            Log.i("Myyyyy", "Myyyyyy")
            nav_Menu.findItem(R.id.nav_profile).isVisible = true
            nav_Menu.findItem(R.id.nav_login).isVisible = false
        } else {
            nav_Menu.findItem(R.id.nav_profile).isVisible = false
            nav_Menu.findItem(R.id.nav_login).isVisible = true
        }
    }

    fun onresume() {
        //take previous enters
        prefs = Prefs(this)
        signinacc()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_beginning -> {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                drawerLayout.closeDrawer(GravityCompat.START)
                return true
            }
            R.id.nav_login -> {
                SignUp()
            }
            R.id.nav_profile -> {
                val intent = Intent(this, Profile::class.java)
                startActivity(intent)
                drawerLayout.closeDrawer(GravityCompat.START)
                return true
            }
            R.id.nav_map -> {
                val intent = Intent(this, MapsActivity::class.java)
                startActivity(intent)
                drawerLayout.closeDrawer(GravityCompat.START)
            }
            R.id.nav_update -> {
                Toast.makeText(this, "Update clicked", Toast.LENGTH_SHORT).show()
            }
            R.id.nav_logout -> {
                Toast.makeText(this, "Sign out clicked", Toast.LENGTH_SHORT).show()
            }
        }
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }


    private fun ShowProfile() {
        myDialog!!.setContentView(R.layout.dial_profile)
        val txtclose: TextView = myDialog!!.findViewById(R.id.txtclose)
        txtclose.text = "X"
        val txtclass: TextView = myDialog!!.findViewById(R.id.txtclass)
        signinacc()
        Log.i("USerssss", users.size.toString())
        txtclass.text = acc.email + "fdsfas"

        val btnlogout: Button = myDialog!!.findViewById(R.id.logoutBtn)
        btnlogout.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            finish()
            startActivity(intent)
        }

        txtclose.setOnClickListener {
            myDialog!!.dismiss()
        }
        myDialog!!.show()
    }


    fun SignUp() {
        val txtclose: TextView
        myLogDialog!!.setContentView(R.layout.dial_login)
        txtclose = myLogDialog!!.findViewById(R.id.txtclose)
        txtclose.text = "X"
        txtclose.setOnClickListener {

            myLogDialog!!.dismiss()
        }
        etEmail = myLogDialog!!.findViewById(R.id.et_email)
        etPassword = myLogDialog!!.findViewById(R.id.et_password)
        btnCreateAccount = myLogDialog!!.findViewById(R.id.btn_add)
        mProgressBar = ProgressDialog(this)
        mDatabase = FirebaseDatabase.getInstance()
        mDatabaseReference = mDatabase!!.reference.child("Users")
        mAuth = FirebaseAuth.getInstance()

        val btn: Button = myLogDialog!!.findViewById(R.id.google_sign_in_button)
        btn.setOnClickListener {

            myLogDialog!!.dismiss()
            //signIn()
        }

        val btn2: Button = myLogDialog!!.findViewById(R.id.btn_register)
        btn2.setOnClickListener {

            myLogDialog!!.dismiss()
            //   if(SignIn())
            SignInEmail()
        }
        //  configureGoogleSignIn()
        firebaseAuth = FirebaseAuth.getInstance()
        myLogDialog!!.show()
    }

    /*private fun configureGoogleSignIn() {
        mGoogleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.Web_Id_Client))
            .requestEmail()
            .build()
        mGoogleSignInClient = GoogleSignIn.getClient(this, mGoogleSignInOptions)
    }*/

    private fun signIn() {
        val signInIntent: Intent = mGoogleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                firebaseAuthWithGoogle(account!!)
            } catch (e: ApiException) {
                Toast.makeText(
                    this,
                    R.string.googlenotsuccessful, Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    private fun firebaseAuthWithGoogle(acct: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(acct.idToken, null)
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener {
            Toast.makeText(this, "Входът беше успешен.", Toast.LENGTH_LONG).show()
            if (it.isSuccessful) {
                Toast.makeText(
                    this,
                    R.string.googlesuccessful, Toast.LENGTH_LONG
                ).show()
            } else {
                Toast.makeText(
                    this,
                    R.string.googlenotsuccessful, Toast.LENGTH_LONG
                ).show()
            }
        }
    }


    private fun SignInEmail() {
        var email = etEmail?.text.toString()
        var password = etPassword?.text.toString()
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Sign In", Toast.LENGTH_LONG)
                    // Sign in success, update UI with the signed-in user's information
                    val user = auth.currentUser
                    signinacc()
                } else {
                    Toast.makeText(this, "Sign Up", Toast.LENGTH_LONG)
                    createNewAccount()
                }
            }
    }

    private fun createNewAccount() {
        var email = etEmail?.text.toString()
        var password = etPassword?.text.toString()
        if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)
        ) {
            Log.i("fuck this", "fuck that")
            //mProgressBar!!.setMessage("Регистриране на потребител...")
            //mProgressBar!!.show()
            Log.i("fucked", "fuck")
            Log.i("text ", email + " fdasfdas")

            if (!email.isEmpty() && !password.isEmpty()) {
                mAuth!!.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, OnCompleteListener { task ->
                        if (task.isSuccessful) {
                            acc.email = email
                            makeacc()
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(
                                this, "Входът не беше успешен.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    })
            }
        } else {
            Log.i("????", "jfdajkfds")
            Toast.makeText(this, "Въведете всички детайли.", Toast.LENGTH_SHORT).show()
        }
        Log.i("email", acc.email + " fdsfdasfasd")
        Log.i("what the hell", acc.email + " some text")
    }

    var firebaseData = FirebaseDatabase.getInstance().reference
    fun signinacc() {
        var email: String = ""
        Log.i(" sign in acc", "NO")
        val user = FirebaseAuth.getInstance().currentUser

        if (user != null) {
            email = user.email!!
        } else return
        for (i in users) {
            Log.i("user email ", i.email + " fdjsafandsv")
            if (i.email == email) {
                Log.i("it is here? ", "The fick?")
                acc.email = i.email
                acc.id = i.id
                acc.coins = i.coins!!.toInt()
                acc.points = i.points!!.toInt()
                acc.bin = i.bin!!.toInt()
                Log.i("companiiiies", i.comp)
                var str = companies[i.comp!!.toInt() - 1].name + " "
                Log.i("companiiiis", i.comp)

                acc.comp = str
                Log.i("companies", acc.comp)

                return
            }
        }
    }

    fun updateacc() {
        var email: String = ""
//        Log.i("PROBLEM", "NO")
        val user = FirebaseAuth.getInstance().currentUser


        if (user != null) {
            email = user.email!!
        } else return

        Log.i("USERSSS", users.size.toString())
        for (i in users) {
            if (i.email == email) {
                Log.i("it is here? ", "The fick?")
                acc.email = i.email
                acc.id = i.id
                acc.coins = i.coins!!.toInt()
                acc.points = i.points!!.toInt()
                acc.bin = i.bin!!.toInt()
                var str = companies[i.comp!!.toInt()].name
                acc.comp = str
                return
            }
        }
    }

    private fun makeacc() {
        var newAcc = UploadClass()
        Log.i("Make acccccc", "Did it?")
        val key = firebaseData.child("Users").push().key
        acc.id = key
        acc.coins = 0
        acc.bin = 0
        acc.points = 0
        newAcc.bin = "0"
        newAcc.coins = "0"
        newAcc.points = "0"
        newAcc.email = acc.email

        firebaseData.child("Users").child(key!!).setValue(newAcc)
        //ChangeAcc(this)
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onItemSelected(
        parent: AdapterView<*>?,
        view: View?,
        position: Int,
        id: Long
    ) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
