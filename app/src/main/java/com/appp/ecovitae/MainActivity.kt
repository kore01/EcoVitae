package com.appp.ecovitae

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import com.appp.ecovitae.MainFiles.BaseActivity

class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        oncreate()
        updateacc()

        val txtValue = findViewById<TextView>(R.id.bin)
        Log.i("Acc.bin", acc.bin.toString() + "fdsafda")
        if (acc.bin != null) txtValue.text = acc.bin.toString()
    }

    override fun tobeupdatedinact() {
        super.tobeupdatedinact()
        val txtValue = findViewById<TextView>(R.id.textView)
        if (acc.bin != null) txtValue.text = acc.bin.toString()


    }

    override fun onStart() {
        super.onStart()
        onresume()

    }
}
