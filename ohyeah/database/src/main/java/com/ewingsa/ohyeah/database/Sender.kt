package com.ewingsa.ohyeah.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Sender(
    @PrimaryKey(autoGenerate = true) val senderId: Long = 0,
    @ColumnInfo(name = "name") var name: String,
    @ColumnInfo(name = "photo_uri") var photoUri: String?
)
