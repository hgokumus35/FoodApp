package com.gokalp.foodapp

import android.app.Application
import com.gokalp.foodapp.di.HomeDI
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class FoodApp : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger(Level.ERROR)
            androidContext(this@FoodApp)
            modules(listOf(
                HomeDI.module
            ))
        }
    }
}