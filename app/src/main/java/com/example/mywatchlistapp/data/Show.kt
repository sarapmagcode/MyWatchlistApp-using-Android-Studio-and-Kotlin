package com.example.mywatchlistapp.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import org.jetbrains.annotations.NotNull

@Entity(tableName = "show")
data class Show(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    @ColumnInfo(name = "title")
    @NotNull
    val title: String,

    @ColumnInfo(name = "rating")
    @NotNull
    val rating: Float,

    @ColumnInfo(name = "recommend")
    @NotNull
    val recommend: String,

    @ColumnInfo(name = "comment")
    val comment: String,

    @ColumnInfo(name = "timestamp")
    val timestamp: String
)