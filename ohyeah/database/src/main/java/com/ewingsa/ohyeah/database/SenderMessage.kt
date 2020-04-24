package com.ewingsa.ohyeah.database

import androidx.room.Embedded
import androidx.room.Entity

@Entity
data class SenderMessage(
    @Embedded val sender: Sender,
    @Embedded val message: Message
)
