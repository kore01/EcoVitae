package com.appp.ecovitae.ui.bonus

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ListView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.appp.ecovitae.Adapter.BonusAdapter
import com.appp.ecovitae.Main2Activity
import com.appp.ecovitae.R

class BonusFragment : Fragment() {

    private lateinit var sendViewModel: BonusViewModel
    var bonusView: ListView? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        sendViewModel =
            ViewModelProviders.of(this).get(BonusViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_bonus, container, false)
        bonusView = root.findViewById(R.id.bonus_list)

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.i("the hell", "bonus")
        Log.i("Bonuses size", (activity as Main2Activity).bonuses.size.toString())
        var adapter =
            BonusAdapter((activity as Main2Activity), (activity as Main2Activity).bonuses)
        //Log.i("modelarraylist", modelArrayList!!.size.toString() + "fdasfa")
        bonusView!!.adapter = adapter


        Log.i("BONUS", bonusView!!.count.toString())
        bonusView!!.onItemClickListener =
            AdapterView.OnItemClickListener { parent, view, position, id ->
                // This is your listview's selected item
                // val item = parent.getItemAtPosition(position)
                Log.i("bonus", "clicked")
                (activity as Main2Activity).bonusss =
                    (activity as Main2Activity).bonuses[position].id.toString()
            }

    }
}


