package com.appp.ecovitae.Adapter

import android.app.Activity
import android.content.Context
import android.view.View
import com.appp.ecovitae.DataModel.InfoWindowData
import com.appp.ecovitae.R
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Marker
import kotlinx.android.synthetic.main.marker_view.view.*

class InfoWindowAdapter (val context: Context) : GoogleMap.InfoWindowAdapter {

    override fun getInfoContents(p0: Marker?): View {

        var mInfoView = (context as Activity).layoutInflater.inflate(R.layout.marker_view, null)
        var mInfoWindow: InfoWindowData? = p0?.tag as InfoWindowData?



        mInfoView.whatfor.text = mInfoWindow?.mPunktWhatfor
        mInfoView.desc.text = mInfoWindow?.mPunktDesc
        mInfoView.comp.text = mInfoWindow?.mPunktComp
        mInfoView.time.text = mInfoWindow?.mPunktTime
     //   mInfoView.txtOpenningHoursValue.text = mInfoWindow?.mLocationHours
      //  mInfoView.txtBranchMarkerValue.text = mInfoWindow?.mLocationBranch

        return mInfoView
    }

    override fun getInfoWindow(p0: Marker?): View? {
        return null
    }
}