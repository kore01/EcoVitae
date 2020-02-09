package com.appp.ecovitae.ui.news

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.appp.ecovitae.GlideApp
import com.appp.ecovitae.Main2Activity
import com.appp.ecovitae.R
import com.google.firebase.storage.FirebaseStorage

class NewsFragment : Fragment() {

    private lateinit var newsViewModel: NewsViewModel

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

        var info = (activity as Main2Activity).news[(activity as Main2Activity).newsss]
        title.text = info.title
        text.text = info.text
        tags.text = info.tags

        val storage = FirebaseStorage.getInstance()
        // Create a reference to a file from a Google Cloud Storage URI
        //var myurl =
        val gsReference =
            storage.getReferenceFromUrl(info.image!!.toUri().toString())

        GlideApp.with(context!!)
            .load(gsReference)
            .into(image)

        return root
    }
}