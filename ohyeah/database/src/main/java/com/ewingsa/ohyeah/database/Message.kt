package com.ewingsa.ohyeah.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Message(
    @PrimaryKey(autoGenerate = true) val messageId: Long = 0,
    @ColumnInfo(name = "message") var message: String,
    @ColumnInfo(name = "sender_id") var senderId: Long,
    @ColumnInfo(name = "timestamp") var timestamp: Long, // in milliseconds
    @ColumnInfo(name = "read") var read: Boolean = false
)
