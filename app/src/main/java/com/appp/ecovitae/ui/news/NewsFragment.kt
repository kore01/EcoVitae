package com.appp.ecovitae.ui.news

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ImageView
import android.widget.ListView
import android.widget.TextView
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.appp.ecovitae.Adapter.NewsFeedAdapter
import com.appp.ecovitae.GlideApp
import com.appp.ecovitae.Main2Activity
import com.appp.ecovitae.R
import com.google.firebase.storage.FirebaseStorage

class NewsFragment : Fragment() {

    private lateinit var newsViewModel: NewsViewModel

    private var adapter2: NewsFeedAdapter? = null

    lateinit var newsView : ListView
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        newsViewModel =
            ViewModelProviders.of(this).get(NewsViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_news, container, false)
        val title: TextView = root.findViewById(R.id.title)
        val tags: TextView = root.findViewById(R.id.tags)
        val image: ImageView = root.findViewById(R.id.imageView)
        val text: TextView = root.findViewById(R.id.text)
        newsView = root.findViewById(R.id.recyc_view) as ListView

        var info = (activity as Main2Activity).news[(activity as Main2Activity).newsss]
        title.text = info.title
        text.text = info.text
        tags.text = info.tags

        val storage = FirebaseStorage.getInstance()
        val gsReference =
            storage.getReferenceFromUrl(info.image!!.toUri().toString())

        GlideApp.with(context!!)
            .load(gsReference)
            .into(image)


        if ((activity as Main2Activity).news.size > 0) {
            Log.i("main2news", (activity as Main2Activity).news.size.toString())
            adapter2 =
                NewsFeedAdapter((activity as Main2Activity),
                    (activity as Main2Activity).news
                )
            //Log.i("modelarraylist", modelArrayList!!.size.toString() + "fdasfa")
            newsView.adapter = adapter2
        }
        newsView.onItemClickListener =
            AdapterView.OnItemClickListener { parent, view, position, id ->
                // This is your listview's selected item
                val item = parent.getItemAtPosition(position)
                (activity as Main2Activity).newsss = position
            }
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if ((activity as Main2Activity).news.size > 0) {
            Log.i("main2news", (activity as Main2Activity).news.size.toString())
            adapter2 =
                NewsFeedAdapter((activity as Main2Activity),
                    (activity as Main2Activity).news
                )

            Log.i("modelarraylist", adapter2!!.count.toString())
            newsView.adapter = adapter2
        }
    }
}