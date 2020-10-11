package com.outcomehealth.challenge.viewmodel

import androidx.lifecycle.*
import com.outcomehealth.challenge.data.DataResult
import com.outcomehealth.challenge.data.GalleryItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import java.net.URL

/**
 * Created by Grigory Azaryan on 10/9/20.
 */

class GalleryViewModel(private val state: SavedStateHandle) : ViewModel() {
    private val GALLERY_LIST = "gallery_list"

    private val _liveData: MutableLiveData<DataResult<List<GalleryItem>>> by lazy {
        // get persisted data or empty list
        val galleryList: List<GalleryItem> = state.get(GALLERY_LIST) ?: emptyList()

       return@lazy MutableLiveData(DataResult.Success(galleryList))
    }

    fun getGalleryLiveData(): LiveData<DataResult<List<GalleryItem>>> {
        // start loading, if there is no data yet
        if ((state.get(GALLERY_LIST) as? List<GalleryItem>).isNullOrEmpty()) {
            refresh()
        }

        return _liveData
    }

    fun refresh() {
        // notify view that data is refreshing
        _liveData.value = DataResult.Loading

        viewModelScope.launch {
            val apiResponseResult = loadGallery()

            // save fresh data
            if (apiResponseResult is DataResult.Success) {
                state.set(GALLERY_LIST, apiResponseResult.data)
            }

            _liveData.value = apiResponseResult
        }
    }

    // todo refactor
    private suspend fun loadGallery(): DataResult<List<GalleryItem>> = withContext(Dispatchers.Default) {
        try {
            // get text from downloaded file
            val stringResp = URL("https://bit.ly/2MIjM4F").readText()
            // parse using Kotlin Serialization
            val parsed = Json.decodeFromString<List<GalleryItem>>(stringResp)

            return@withContext DataResult.Success(parsed)
        } catch (e: Exception) {
            return@withContext DataResult.Error(e)
        }
    }

}