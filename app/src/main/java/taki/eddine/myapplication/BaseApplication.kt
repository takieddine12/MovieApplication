package taki.eddine.myapplication

import android.app.Application
import timber.log.Timber
import timber.log.Timber.DebugTree

class BaseApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        Timber.plant(DebugTree())
    }
}