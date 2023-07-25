package com.example.mywatchlistapp.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Show::class], version = 3, exportSchema = false)
abstract class ShowRoomDatabase : RoomDatabase() {

    abstract fun showDao(): ShowDao

    companion object {
        @Volatile
        private var INSTANCE: ShowRoomDatabase? = null

        fun getDatabase(context: Context): ShowRoomDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context,
                    ShowRoomDatabase::class.java,
                    "show_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()

                INSTANCE = instance
                instance
            }
        }
    }
}