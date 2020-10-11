package com.outcomehealth.challenge

import androidx.multidex.MultiDexApplication
import timber.log.Timber

/**
 * Created by Grigory Azaryan on 10/9/20.
 */

class App : MultiDexApplication() {

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}