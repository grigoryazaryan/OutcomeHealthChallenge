package com.outcomehealth.challenge.util

import android.content.Context
import android.content.pm.ActivityInfo
import android.hardware.SensorManager
import android.view.OrientationEventListener
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import kotlin.math.abs

/**
 * Created by Grigory Azaryan on 10/10/20.
 */

class OrientationChangedListener : LifecycleObserver {
    private var context: Context
    private var orientationChangedListener: (Int) -> Unit

    private var listener: OrientationEventListener

    constructor(lifecycleOwner: LifecycleOwner, context: Context, orientationChangedListener: (orientation: Int) -> Unit) {
        this.context = context
        this.orientationChangedListener = orientationChangedListener
        lifecycleOwner.lifecycle.addObserver(this)

        listener = object : OrientationEventListener(context, SensorManager.SENSOR_DELAY_NORMAL) {
            private var screenOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
            private val LANDSCAPE_DEG = 270
            private val REVERSE_LANDSCAPE_DEG = 90
            private val REVERSE_PORTRAIT = 180
            private val spectrum = 30

            override fun onOrientationChanged(deg: Int) {
                if (abs(deg - LANDSCAPE_DEG) < 45) {
                    if (abs(deg - LANDSCAPE_DEG) < spectrum) {
                        if (screenOrientation != ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
                            orientationChangedListener(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE)
                            screenOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
                        }
                    }
                } else if (abs(deg - REVERSE_LANDSCAPE_DEG) < 45) {
                    if (abs(deg - REVERSE_LANDSCAPE_DEG) < spectrum) {
                        if (screenOrientation != ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE) {
                            orientationChangedListener(ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE)
                            screenOrientation = ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE
                        }
                    }
                } else if (deg != -1) {
                    if (screenOrientation != ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
                        orientationChangedListener(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
                        screenOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
                    }
                }
            }
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun addLocationListener() {
        listener.enable()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun removeLocationListener() {
        listener.disable()
    }
}