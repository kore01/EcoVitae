package com.appp.ecovitae.ui.send

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ListView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.appp.ecovitae.Adapter.ShopsAdapter
import com.appp.ecovitae.Main2Activity
import com.appp.ecovitae.R

class ShopsFragment : Fragment() {

    private lateinit var sendViewModel: ShopsViewModel

    private var adapter: ShopsAdapter? = null
    var shopsView: ListView? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        sendViewModel =
            ViewModelProviders.of(this).get(ShopsViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_shops, container, false)
        shopsView = root.findViewById(R.id.shops_list)

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter =
            ShopsAdapter((activity as Main2Activity), (activity as Main2Activity).shops)
        //Log.i("modelarraylist", modelArrayList!!.size.toString() + "fdasfa")
        shopsView!!.adapter = adapter

        shopsView!!.onItemClickListener =
            AdapterView.OnItemClickListener { parent, view, position, id ->
                // This is your listview's selected item
                // val item = parent.getItemAtPosition(position)
                (activity as Main2Activity).shopsss =
                    (activity as Main2Activity).shops[position].id.toString()
            }
    }
}