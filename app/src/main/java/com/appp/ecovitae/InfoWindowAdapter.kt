package com.appp.ecovitae
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Marker

class InfoWindowAdapter(context: Context) : GoogleMap.InfoWindowAdapter {
    private val context: Context
    override fun getInfoWindow(marker: Marker): View? {
        return null
    }

    override fun getInfoContents(marker: Marker): View {

        Log.i("marker",marker.title)
        val inflater =
            context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view: View = inflater.inflate(R.layout.map_info4, null)
        val tvTitle = view.findViewById(R.id.title) as TextView
        val tvDesc = view.findViewById(R.id.desc) as TextView
        Log.i("markersnip", marker.snippet)
        tvTitle.text=marker.title
        tvDesc.text=marker.snippet
        val tvbtn = view.findViewById(R.id.filter) as Button
        tvbtn.setOnClickListener {
            Log.i("openDial","open dialog")
            openDial()
        }
        Log.i("markers", tvTitle.text as String)
        Log.i("markers", tvDesc.text as String)

        return view
    }

    fun openDial()
    {
        Log.i("openDial","open dialog")
//        Toast.makeText(this, "btn", Toast.LENGTH_LONG).show()

    }

    init {
        this.context = context.applicationContext
    }
}