package com.wiryadev.binar_movie.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tableTv")
data class TvEntity(
    @PrimaryKey
    @ColumnInfo(name = "tv_id")
    val tvId: Int,

    @ColumnInfo(name = "title")
    val title: String,

    @ColumnInfo(name = "poster_path")
    val posterPath: String,
)
