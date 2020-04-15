package com.appp.ecovitae.ui.tips

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import androidx.core.view.get
import androidx.core.view.size
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import com.appp.ecovitae.Adapter.NewsFeedAdapter
import com.appp.ecovitae.Adapter.TipAdapter
import com.appp.ecovitae.Main2Activity
import com.appp.ecovitae.R
import com.appp.ecovitae.ui.send.ShopsViewModel
import kotlinx.android.synthetic.main.fragment_profile.view.*
import kotlinx.android.synthetic.main.tip_item.view.*
import java.math.RoundingMode
import kotlin.math.roundToInt

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
        newsView!!.adapter = adapter
        button!!.setOnClickListener {

            var i = 0
            var points = 0
            var maxpoints = 0
            var br = 0
            var maxbr = 0

            while (i < (this.newsView!!.size)) {

                Log.i("tips", i.toString())
                var pa = newsView!![i]
                var t = pa.pointss!!
                var p = t.text.toString().toInt()
                if (newsView!![i].checkBox.isChecked) {
                    points += p
                    br++

                }
                maxpoints += p
                maxbr++
                i++
            }

            var pers:Double = (points.toDouble() / maxpoints * 100).toBigDecimal().setScale(2, RoundingMode.UP).toDouble()
            var strr =
                "Имате $points от $maxpoints точки, което е $pers%. Следвате $br от $maxbr съвета. \n"
            if (pers <= 50) strr += "Можете повече!"
            else if (pers <= 75) strr += "Опитайте се да го увеличите!"
            else if (pers < 100) strr += "Справяте се чудесно!"
            else strr += "Добра работа! Продължавайте в същия дух!"
            resultt!!.text = strr
            resultt!!.visibility = View.VISIBLE


            Log.i("checked", newsView!![1].checkBox.isChecked.toString())

        }






        return root
    }
}