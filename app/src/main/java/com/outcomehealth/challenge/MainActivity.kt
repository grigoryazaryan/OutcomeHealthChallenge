package com.outcomehealth.challenge

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val viewModel by viewModels<GalleryViewModel>()

    private var galleryRecyclerAdapter: GalleryRecyclerAdapter = GalleryRecyclerAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // refresh data on swipe
        swipe_refresh.setOnRefreshListener { viewModel.refresh() }

        // setting up recyclerview
        galleryRecyclerAdapter = GalleryRecyclerAdapter()
        val layoutManager = GridLayoutManager(this, 2)
        recycler.layoutManager = layoutManager
        recycler.adapter = galleryRecyclerAdapter

        // subscribing to data
        viewModel.getGalleryLiveData().observe(this) { value ->
            when (value) {
                is DataResult.Loading -> {
                    swipe_refresh.isRefreshing = true
                }
                is DataResult.Success -> {
                    swipe_refresh.isRefreshing = false
                    galleryRecyclerAdapter.setData(value.data)

                }
                is DataResult.Error -> {
                    swipe_refresh.isRefreshing = false
                    Toast.makeText(this, R.string.something_went_wrong, Toast.LENGTH_SHORT).show()
                }
            }
        }

    }
}