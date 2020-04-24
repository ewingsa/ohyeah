package com.ewingsa.ohyeah.database

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity

@Entity
data class PreviewSenderMessage(
    @Embedded val sender: Sender,
    @Embedded val message: Message,
    @ColumnInfo(name = "unread") var unread: Int? = null
)
