package com.outcomehealth.challenge

/**
 * Created by Grigory Azaryan on 10/10/20.
 */


/**
 * A generic class that holds a value with its loading status.
 */
sealed class DataResult<out R> {
    data class Success<out T>(val data: T) : DataResult<T>()
    data class Error(val exception: Exception) : DataResult<Nothing>()
    object Loading : DataResult<Nothing>()

    override fun toString(): String {
        return when (this) {
            is Success<*> -> "Success[data=$data]"
            is Error -> "Error[exception=$exception]"
            is Loading -> "Loading"
        }
    }

    /**
     * `true` if [DataResult] is of type [Success] & holds non-null [Success.data].
     */
    val DataResult<*>.succeeded
        get() = this is Success<*> && data != null
}