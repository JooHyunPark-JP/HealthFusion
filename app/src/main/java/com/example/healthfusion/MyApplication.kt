package com.example.healthfusion

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

//Application class
@HiltAndroidApp
class MyApplication : Application(){

/*    override fun onCreate() {
        super.onCreate()

        // use this code when you need to delete previous (room) database
        deleteDatabase("healthfusion_database")
    }*/
}