package com.example.mywatchlistapp

import android.app.Application
import com.example.mywatchlistapp.data.ShowRoomDatabase

class ShowApplication : Application() {
    val database: ShowRoomDatabase by lazy { ShowRoomDatabase.getDatabase(this) }
}