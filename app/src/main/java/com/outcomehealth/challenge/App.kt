package com.outcomehealth.challenge

import android.app.Application
import timber.log.Timber

/**
 * Created by Grigory Azaryan on 10/9/20.
 */

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}