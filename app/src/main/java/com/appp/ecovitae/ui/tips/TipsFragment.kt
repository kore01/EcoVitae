package com.appp.ecovitae.ui.tips

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import com.appp.ecovitae.Adapter.NewsFeedAdapter
import com.appp.ecovitae.Adapter.TipAdapter
import com.appp.ecovitae.Main2Activity
import com.appp.ecovitae.R
import com.appp.ecovitae.ui.send.ShopsViewModel

class TipsFragment : Fragment() {

    private lateinit var sendViewModel: ShopsViewModel
    private var adapter: TipAdapter? = null
    var newsView: ListView? = null
    var button: Button? = null
    var resultt: TextView? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        sendViewModel =
            ViewModelProviders.of(this).get(ShopsViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_tips, container, false)
        adapter =
            TipAdapter((activity as Main2Activity), (activity as Main2Activity).tips)

        newsView = root.findViewById(R.id.recyc_view)
        button = root.findViewById(R.id.calcul)
        resultt = root.findViewById(R.id.result)
        resultt!!.visibility = View.GONE

        button!!.setOnClickListener {
            resultt!!.visibility = View.VISIBLE


        }

        newsView!!.adapter = adapter

        return root
    }
}