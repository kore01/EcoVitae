package com.appp.ecovitae.Adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.core.net.toUri
import com.appp.ecovitae.CheckBoxList
import com.appp.ecovitae.DataModel.Bonus.Bonus
import com.appp.ecovitae.DataModel.Tips.Tip
import com.appp.ecovitae.GlideApp
import com.appp.ecovitae.R
import com.google.firebase.storage.FirebaseStorage

class TipAdapter(private val context: Context, modelArrayList: ArrayList<Tip>) :
    BaseAdapter() {

    private var modelArrayList: ArrayList<Tip>

    init {
        this.modelArrayList = modelArrayList
    }

    override fun getViewTypeCount(): Int {
        return count
    }

    override fun getItemViewType(position: Int): Int {

        return position
    }

    override fun getCount(): Int {
        return modelArrayList.size
    }

    override fun getItem(position: Int): Any {
        return modelArrayList[position]
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView
        val holder: ViewHolder
        Log.i("opa? ", "opa")
        if (convertView == null) {


            holder = ViewHolder()
            val inflater =
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            convertView = inflater.inflate(R.layout.tip_item, null, true)

            //holder.shop = convertView!!.findViewById(R.id.shop) as TextView
            holder.text = convertView.findViewById(R.id.text) as TextView
            holder.check = convertView.findViewById(R.id.checkBox) as CheckBox
            holder.pointss = convertView.findViewById(R.id.pointss) as TextView

            convertView.tag = holder
        } else {
            // the getTag returns the viewHolder object set as a tag to the view
            holder = convertView.tag as ViewHolder
        }

        holder.text!!.text = modelArrayList[position].desc

        holder.pointss!!.text = modelArrayList[position].points!!

        return convertView!!
    }

    private inner class ViewHolder {

        var pointss: TextView? = null
        var text: TextView? = null
        var check: CheckBox? = null
    }


    companion object {
        lateinit var public_modelArrayList: ArrayList<CheckBoxList>
    }





}