package com.example.picturetocard.ui.collection

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.picturetocard.PictureToCard
import com.example.picturetocard.R
import com.example.picturetocard.ui.CollectionManager
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class CollectionFragment : CollectionManager() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_collection, container, false)

        val database = (requireActivity().application as PictureToCard).database

        GlobalScope.launch {
            fillCollection(database, view, R.id.recyclerCollection)
        }

        return view
    }

}
