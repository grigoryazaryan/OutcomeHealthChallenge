package com.outcomehealth.challenge.util

import android.content.Context
import android.os.Environment
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.database.ExoDatabaseProvider
import com.google.android.exoplayer2.source.DefaultMediaSourceFactory
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory
import com.google.android.exoplayer2.upstream.cache.CacheDataSource
import com.google.android.exoplayer2.upstream.cache.NoOpCacheEvictor
import com.google.android.exoplayer2.upstream.cache.SimpleCache

/**
 * Created by Grigory Azaryan on 10/11/20.
 */

class ExoPlayerHelper {

    companion object {

        // singleton
        private var cache: SimpleCache? = null

        fun createSimplePlayerWithCaching(context: Context): SimpleExoPlayer = with(context) {

            val cacheDir = getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS) ?: filesDir

            cache = cache ?: SimpleCache(cacheDir, NoOpCacheEvictor(), ExoDatabaseProvider(this))

            val cacheDataSourceFactory = CacheDataSource.Factory()
                .setCache(cache!!)
                .setUpstreamDataSourceFactory(DefaultHttpDataSourceFactory(packageName))


            return@with SimpleExoPlayer.Builder(this)
                .setMediaSourceFactory(DefaultMediaSourceFactory(cacheDataSourceFactory))
                .build()
        }
    }
}