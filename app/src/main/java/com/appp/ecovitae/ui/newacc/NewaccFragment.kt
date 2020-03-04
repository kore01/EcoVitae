package com.appp.ecovitae.ui.newacc

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.appp.ecovitae.DataModel.MyAccount
import com.appp.ecovitae.DataModel.UploadClass
import com.appp.ecovitae.Main2Activity
import com.appp.ecovitae.R
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.synnapps.carouselview.CarouselView
import com.synnapps.carouselview.ImageListener

class NewaccFragment : Fragment() {

    private lateinit var newaccModel: NewaccModel


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
    private lateinit var firebaseAuth: FirebaseAuth

    private lateinit var carouselView: CarouselView

    var sampleImages = intArrayOf(
        R.drawable.ic_add,
        R.drawable.ic_calculator,
        R.drawable.ic_filter,
        R.drawable.ic_home,
        R.drawable.ic_map
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        newaccModel =
            ViewModelProviders.of(this).get(NewaccModel::class.java)
        val root = inflater.inflate(R.layout.fragment_newacc, container, false)



        carouselView = root.findViewById(R.id.carouselView)
        carouselView.pageCount = sampleImages.size
        carouselView.setImageListener(imageListener)

        val txtclose: TextView

        etEmail = root.findViewById(R.id.et_email)
        etPassword = root.findViewById(R.id.et_password)
        btnCreateAccount = root.findViewById(R.id.btn_add)
        mProgressBar = ProgressDialog(context)
        mDatabase = FirebaseDatabase.getInstance()
        mDatabaseReference = mDatabase!!.reference.child("Users")
        mAuth = FirebaseAuth.getInstance()


        val btn2: Button = root.findViewById(R.id.btn_register)
        btn2.setOnClickListener {
            //   if(SignIn())
            SignInEmail()
        }
        //  configureGoogleSignIn()
        firebaseAuth = FirebaseAuth.getInstance()

        return root
    }

    private lateinit var auth: FirebaseAuth

    var imageListener =
        ImageListener { position, imageView -> imageView.setImageResource(sampleImages[position]) }


    private fun SignInEmail() {
        Log.i("signinemail", "yeap")

        auth = FirebaseAuth.getInstance()
        var email = etEmail?.text.toString()
        var password = etPassword?.text.toString()

        if (email.length == 0 || password.length == 0) return
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(activity!!) { task ->
                if (task.isSuccessful) {

                    Log.i("successful", "sign in")
                    Toast.makeText(context, "Sign In", Toast.LENGTH_LONG)
                    // Sign in success, update UI with the signed-in user's information
                    val user = auth.currentUser
                    signinacc(user!!.email.toString())
                    (activity as Main2Activity).logout++
                    Log.i("intent", "intentttt")

                } else {
                    Log.i("successful", "sign up")
                    Toast.makeText(context, "Sign Up", Toast.LENGTH_LONG)
                    createNewAccount()
                    Log.i("intent", "intentttt")


                }
            }
    }

    var acc: MyAccount = MyAccount()

    private fun createNewAccount() {
        var email = etEmail?.text.toString()
        var password = etPassword?.text.toString()
        if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)
        ) {
            //mProgressBar!!.setMessage("Регистриране на потребител...")
            //mProgressBar!!.show()

            if (!email.isEmpty() && !password.isEmpty()) {
                mAuth!!.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(activity!!, OnCompleteListener { task ->
                        if (task.isSuccessful) {
                            acc.email = email
                            makeacc()
                            (activity as Main2Activity).logout++

                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(
                                context, "Входът не беше успешен.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    })
            }
        } else {
            Log.i("????", "jfdajkfds")
            Toast.makeText(context, "Въведете всички детайли.", Toast.LENGTH_SHORT).show()
        }
        Log.i("email", acc.email + " fdsfdasfasd")
        Log.i("what the hell", acc.email + " some text")
    }

    var firebaseData = FirebaseDatabase.getInstance().reference
    private fun makeacc() {
        var newAcc = UploadClass()
        Log.i("Make acccccc", "Did it?")
        val key = firebaseData.child("Users").push().key
        acc.id = key
        acc.coins = 0
        acc.bin = 0
        acc.points = 0
        acc.rate = 0
        acc.comp = ""
        newAcc.bin = "0"
        newAcc.coins = "0"
        newAcc.points = "0"
        newAcc.rating = "0"
        newAcc.email = acc.email

        newAcc.company = acc.comp

        firebaseData.child("Users").child(key!!).setValue(newAcc)
        //ChangeAcc(this)
    }

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
                    val packaging = ds.child("packaging").getValue(String::class.java)
                    acc.email = ds.child("email").getValue(String::class.java)
                    acc.id = ds.key
                    acc.coins = ds.child("coins").getValue(String::class.java)?.toInt()
                    acc.rate = ds.child("rating").getValue(String::class.java)?.toInt()
                    acc.points = ds.child("points").getValue(String::class.java)?.toInt()
                    acc.bin = ds.child("bin").getValue(String::class.java)?.toInt()
                    //Log.i("companiiiies", i.comp)

                    var num = ds.child("company").getValue(String::class.java)
                    Log.i("companiiiies", num.toString())
                    var str: String = ""
                    if (num == "") {
                        Log.i("hhhhhh", "tttttt")
                        str = ""
                    } else str = (activity as Main2Activity).companies[num!!.toInt() - 1].name + " "
                    //Log.i("companiiiis", i.comp)

                    acc.comp = str
                    Log.i("companies", acc.comp)
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

}