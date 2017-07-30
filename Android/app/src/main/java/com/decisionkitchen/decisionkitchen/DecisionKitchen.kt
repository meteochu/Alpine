package com.decisionkitchen.decisionkitchen

import android.app.Application
import com.facebook.drawee.backends.pipeline.Fresco

/**
 * Created by kevin on 2017-07-29.
 */
public class DecisionKitchen: Application() {
    override fun onCreate() {
        super.onCreate()
        Fresco.initialize(this)
    }
}