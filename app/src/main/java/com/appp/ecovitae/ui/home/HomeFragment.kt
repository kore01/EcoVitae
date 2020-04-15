package com.appp.ecovitae.ui.home

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.appp.ecovitae.Adapter.NewsFeedAdapter
import com.appp.ecovitae.Main2Activity
import com.appp.ecovitae.R
import com.google.zxing.integration.android.IntentIntegrator
import com.google.zxing.integration.android.IntentResult

class HomeFragment : Fragment() {

    private var lfrom: Button? = null
    private var lto: Button? = null
    private var pointstonext: TextView? = null
    private var equalss: TextView? = null
    private var slide: ProgressBar? = null
    private lateinit var homeViewModel: HomeViewModel
    private var adapter: NewsFeedAdapter? = null
    var newsView: ListView? = null
    var daytip: TextView? = null
    var rnds = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
            ViewModelProviders.of(this).get(HomeViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_home, container, false)
        newsView = root.findViewById(R.id.newsfeed)

        lfrom = root.findViewById(R.id.lvfrom)
        pointstonext = root.findViewById(R.id.nextlevel)
        equalss = root.findViewById(R.id.equals)
        lto = root.findViewById(R.id.lvto)
        slide = root.findViewById(R.id.progress)
        daytip = root.findViewById(R.id.tip)
        homeViewModel.text.observe(this, Observer {
            Log.i("fdas", "fdasfdasfdshgdf")
        })



        if ((activity as Main2Activity).tips.size != 0) {
            rnds = (1..(activity as Main2Activity).tips.size).random()
            rnds--
            daytip!!.text = (activity as Main2Activity).tips[rnds].desc
        }

        var levell = (activity as Main2Activity).acc.level
        updatelevel()





        if ((activity as Main2Activity).news.size > 0) {
            Log.i("main2news", (activity as Main2Activity).news.size.toString())
            adapter =
                NewsFeedAdapter((activity as Main2Activity), (activity as Main2Activity).news)
            //Log.i("modelarraylist", modelArrayList!!.size.toString() + "fdasfa")
            newsView!!.adapter = adapter
        }
        newsView!!.onItemClickListener =
            AdapterView.OnItemClickListener { parent, view, position, id ->
                // This is your listview's selected item
                val item = parent.getItemAtPosition(position)
                (activity as Main2Activity).newsss = position

            }

        return root
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {


        var result: IntentResult? =
            IntentIntegrator.parseActivityResult(requestCode, resultCode, data)

        if (result != null) {

            if (result.contents != null) {
                //  scannedResult = result.contents

            } else {

            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }


    var levell = 0
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.i("ctrate view", "view")
        if ((activity as Main2Activity).news.size > 0) {
            Log.i("main2news", (activity as Main2Activity).news.size.toString())
            adapter =
                NewsFeedAdapter((activity as Main2Activity), (activity as Main2Activity).news)

            Log.i("modelarraylist", adapter!!.count.toString())
            newsView!!.adapter = adapter

        }

        if ((activity as Main2Activity).tips.size != 0) {
            rnds = (1..(activity as Main2Activity).tips.size).random()
            rnds--
            daytip!!.text = (activity as Main2Activity).tips[rnds].desc
        }
        updatelevel()
    }

    fun updatelevel() {

        (activity as Main2Activity).updatelevels()

        levell = (activity as Main2Activity).acc.level!!
        Log.i("what level???", levell.toString())
        Log.i("what level???", (activity as Main2Activity).acc.points.toString())
        if (levell < 2) {
            slide!!.max = 2
            slide!!.progress = 1
            pointstonext!!.text = "  Точки до следващото ниво: " + (1).toString()
            equalss!!.text = "Все още не сте спестили енергия!"
            return
        }

        lfrom!!.text = levell.toString()
        lto!!.text = (levell!! + 1).toString()


        var pointsfrom = (activity as Main2Activity).levels[levell - 1].points!!.toInt() + 1
        var slidemax = (activity as Main2Activity).levels[levell].points!!.toInt() - pointsfrom + 1
        var slideprogress = (activity as Main2Activity).acc.points!! - pointsfrom + 1
        slide!!.max = slidemax
        slide!!.progress = slideprogress

        pointstonext!!.text = "Точки до следващото ниво: " + (slidemax - slideprogress).toString()
        equalss!!.text =
            "Това се равнява на енергията за захранването на компютър за " + ((activity as Main2Activity).acc.bin!! * 25).toString() + " минути."


        Log.i("pointsfrom", pointsfrom.toString())
        Log.i("my points", (activity as Main2Activity).acc.points.toString())
        Log.i(
            "slide.max",
            ((activity as Main2Activity).levels[levell].points!!.toInt() - pointsfrom).toString()
        )
        Log.i("slide.progress", ((activity as Main2Activity).acc.points!! - pointsfrom).toString())
        //slide!!.max = 10
        //slide!!.progress = 6
    }
}