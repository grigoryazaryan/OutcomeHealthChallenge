package com.outcomehealth.challenge

import kotlinx.serialization.Serializable

/**
 * Created by Grigory Azaryan on 10/9/20.
 */

@Serializable
data class GalleryItem(val title: String, val url: String)