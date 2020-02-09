package com.appp.ecovitae

import android.os.Bundle
import com.appp.ecovitae.MainFiles.BaseActivity

class Profile : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        oncreate()
        updateacc()
    }
}
