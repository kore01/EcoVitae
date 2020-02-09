package com.appp.ecovitae

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import com.google.zxing.integration.android.IntentIntegrator
import com.google.zxing.integration.android.IntentResult

import kotlinx.android.synthetic.main.activity_main4.*

class Main4Activity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main4)
        setSupportActionBar(toolbar)

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }
        
        var btn = findViewById<Button>(R.id.btn)
        btn.setOnClickListener {

            IntentIntegrator(this)
                .setOrientationLocked(false)
                .setBeepEnabled(false)
                .initiateScan()
        }



    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        //super.onActivityResult(requestCode, resultCode, data)

        Log.i("on activity result", "result")
        var result: IntentResult? =
            IntentIntegrator.parseActivityResult(requestCode, resultCode, data)

        if (result != null) {

            if (result.contents != null) {
            //    scannedResult = result.contents

            } else {

            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }


}
