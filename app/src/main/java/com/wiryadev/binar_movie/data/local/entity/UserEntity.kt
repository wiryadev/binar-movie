package com.wiryadev.binar_movie.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "tableUser",
    indices = [
        Index(
            value = [
                "username"
            ],
            unique = true,
        ),
    ]
)
data class UserEntity(
    @PrimaryKey
    @ColumnInfo(name = "email")
    val email: String,

    @ColumnInfo(name = "username")
    val username: String,

    @ColumnInfo(name = "password")
    val password: String,

    @ColumnInfo(name = "full_name")
    val fullName: String? = null,

    @ColumnInfo(name = "date")
    val date: String? = null,

    @ColumnInfo(name = "address")
    val address: String? = null,
)