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

            IntentIntegrator(this)
                .setOrientationLocked(false)
                .setBeepEnabled(false)
                .initiateScan()
    }


    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        Log.i("scaannnnn", "scan")
        val result: IntentResult? =
            IntentIntegrator.parseActivityResult(requestCode, resultCode, data)

        if (result != null) {

            if (result.contents != null) {

                Log.i("scannn", result.contents)

                val intent = Intent(this, Main2Activity::class.java)
                intent.putExtra("barcode", result.contents)
                startActivity(intent)

            } else {

            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }


}
