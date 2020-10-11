package com.outcomehealth.challenge

import android.app.Dialog
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.outcomehealth.challenge.adapter.GalleryRecyclerAdapter
import com.outcomehealth.challenge.data.DataResult
import com.outcomehealth.challenge.data.GalleryItem
import com.outcomehealth.challenge.helper.OrientationChangedListener
import com.outcomehealth.challenge.viewmodel.GalleryViewModel
import kotlinx.android.synthetic.main.activity_main.*
import timber.log.Timber


class MainActivity : AppCompatActivity() {

    private val viewModel by viewModels<GalleryViewModel>()

    private var galleryRecyclerAdapter: GalleryRecyclerAdapter = GalleryRecyclerAdapter()

    private lateinit var player: SimpleExoPlayer

    private lateinit var fullscreenDialog: Dialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        // refresh data on swipe
        swipe_refresh.setOnRefreshListener { viewModel.refresh() }

        // setting up recyclerview
        galleryRecyclerAdapter = GalleryRecyclerAdapter()
        galleryRecyclerAdapter.setItemClickListener { item, position, _ ->
            player.seekTo(position, 0)
            player.play()
        }

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
                    setPlaylist(value.data)
                }
                is DataResult.Error -> {
                    swipe_refresh.isRefreshing = false
                    Toast.makeText(this, R.string.something_went_wrong, Toast.LENGTH_SHORT).show()
                }
            }
        }

        setupPlayer()

        OrientationChangedListener(this, this) { orientation ->
            requestedOrientation = orientation

            if (orientation == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE || orientation == ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE){
                openFullscreen()
            } else {
                closeFullscreen()
            }
        }

        fullscreenDialog = Dialog(this, android.R.style.Theme_Black_NoTitleBar_Fullscreen)
        fullscreenDialog.setCancelable(false)

    }

    private fun openFullscreen(){
        fullscreenDialog.show()
        (video_view.parent as? ViewGroup)?.removeView(video_view)
        fullscreenDialog.addContentView(video_view, ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT))
    }

    private fun closeFullscreen(){
        (video_view.parent as? ViewGroup)?.removeView(video_view)
        video_view_container.addView(video_view, 0, ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT))
        fullscreenDialog.dismiss()
    }

    private fun setupPlayer() {
        // todo caching https://alexandroid.net/exoplayer-buffering-and-cache-code-sample/
//        val cacheDataSourceFactory = CacheDataSource.Factory()
//            .setCache(SimpleCache())
//            .setUpstreamDataSourceFactory(httpDataSourceFactory)

        player = SimpleExoPlayer.Builder(this)
//            .setMediaSourceFactory(DefaultMediaSourceFactory(cacheDataSourceFactory))
            .build()

        player.addListener(object : Player.EventListener {
            override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
                mediaItem?.let {
                    val playingItem = it.playbackProperties?.tag as? GalleryItem
                    video_title.text = playingItem?.title
                }
            }
        })
        video_view.player = player
    }

    private fun setPlaylist(items: List<GalleryItem>) {
        for (item in items) {
            val mediaItem: MediaItem = MediaItem.Builder()
                .setUri(item.url)
                .setTag(item)
                .build()

            player.addMediaItem(mediaItem)
        }

        player.prepare()
        player.play()
    }
}