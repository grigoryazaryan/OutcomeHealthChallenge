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
import com.outcomehealth.challenge.util.ExoPlayerHelper
import com.outcomehealth.challenge.util.OrientationChangedListener
import com.outcomehealth.challenge.viewmodel.GalleryViewModel
import kotlinx.android.synthetic.main.activity_main.*
import timber.log.Timber


class MainActivity : AppCompatActivity() {

    private val viewModel by viewModels<GalleryViewModel>()

    private var galleryRecyclerAdapter: GalleryRecyclerAdapter = GalleryRecyclerAdapter(this)

    private lateinit var player: SimpleExoPlayer

    private lateinit var fullscreenDialog: Dialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        // refresh data on swipe
        swipe_refresh.setOnRefreshListener { viewModel.refresh() }

        setupGalleryList()

        setupPlayer()

        initFullscreenDialog()

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

        // subscribe to device orientation changes
        OrientationChangedListener(this, this) { orientation ->
            // change activity's orientation and fullscreen dialog's respectively
            requestedOrientation = orientation

            if (orientation == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE || orientation == ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE) {
                openFullscreen()
            } else {
                closeFullscreen()
            }
        }

    }

    override fun onPause() {
        super.onPause()
        player.pause()
    }

    private fun setupGalleryList() {
//        galleryRecyclerAdapter = GalleryRecyclerAdapter(this)
        galleryRecyclerAdapter.setItemClickListener { item, position, _ ->
            player.seekTo(position, 0)
            player.play()
        }

        val layoutManager = GridLayoutManager(this, 2)
        recycler.layoutManager = layoutManager
        recycler.adapter = galleryRecyclerAdapter
    }

    private fun setupPlayer() {

        player = ExoPlayerHelper.createSimplePlayerWithCaching(this)

        player.addListener(
            object : Player.EventListener {
                override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
                    val playingItem = mediaItem?.playbackProperties?.tag as? GalleryItem
                    playingItem?.let {
                        onMediaItemChanged(it)
                    }
                }
            })

        video_view.player = player
    }

    private fun onMediaItemChanged(playingItem: GalleryItem) {
        video_title.text = playingItem.title
        galleryRecyclerAdapter.selectItem(playingItem)
    }

    private fun setPlaylist(items: List<GalleryItem>) {
        val mediaItems = mutableListOf<MediaItem>()

        for (item in items) {
            val mediaItem: MediaItem = MediaItem.Builder()
                .setUri(item.url)
                .setTag(item)
                .build()

            mediaItems.add(mediaItem)
        }

        player.setMediaItems(mediaItems)

        player.prepare()
        player.play()
    }

    private fun initFullscreenDialog() {
        fullscreenDialog = Dialog(this, android.R.style.Theme_Black_NoTitleBar_Fullscreen)
        fullscreenDialog.setCancelable(false)
    }

    private fun openFullscreen() {
        fullscreenDialog.show()
        // detach video view from the main layout
        (video_view.parent as? ViewGroup)?.removeView(video_view)
        // attach it to the dialog
        fullscreenDialog.addContentView(video_view, ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT))
    }

    private fun closeFullscreen() {
        // detach video view from the dialog
        (video_view.parent as? ViewGroup)?.removeView(video_view)
        // attach it to the main layout
        video_view_container.addView(video_view, 0, ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT))
        fullscreenDialog.dismiss()
    }
}