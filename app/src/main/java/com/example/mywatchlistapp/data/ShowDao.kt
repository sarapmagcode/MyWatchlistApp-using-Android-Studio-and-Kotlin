package com.example.mywatchlistapp.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface ShowDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(show: Show)

    @Update
    suspend fun update(show: Show)

    @Delete
    suspend fun delete(show: Show)

    @Query("SELECT * FROM show WHERE id = :id")
    fun getSpecificShow(id: Int): Flow<Show>

    @Query("SELECT * FROM show")
    fun getAllShows(): Flow<List<Show>>
}