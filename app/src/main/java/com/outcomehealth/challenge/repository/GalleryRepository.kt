package com.outcomehealth.challenge.repository

import com.outcomehealth.challenge.data.DataResult
import com.outcomehealth.challenge.data.GalleryItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import timber.log.Timber
import java.net.URL

/**
 * Created by Grigory Azaryan on 10/11/20.
 */

class GalleryRepository {

    /**
     * Returns [DataResult] containing list of Gallery items
     */
    suspend fun loadGallery(): DataResult<List<GalleryItem>> = withContext(Dispatchers.Default) {
        try {
            // get text from downloaded file
            val stringResp = URL("https://bit.ly/2MIjM4F").readText()
            // parse using Kotlin Serialization
            val parsed = Json.decodeFromString<List<GalleryItem>>(stringResp)

            return@withContext DataResult.Success(parsed)
        } catch (e: Exception) {
            Timber.e(e)
            return@withContext DataResult.Error(e)
        }
    }

}