package com.outcomehealth.challenge.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import kotlinx.serialization.Serializable

/**
 * Created by Grigory Azaryan on 10/9/20.
 */

@Serializable
@Parcelize
data class GalleryItem(val title: String, val url: String, var durationSec: Long = 0): Parcelable