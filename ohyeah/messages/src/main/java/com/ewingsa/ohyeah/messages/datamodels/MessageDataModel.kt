package com.ewingsa.ohyeah.messages.datamodels

import android.graphics.Bitmap

class MessageDataModel(
    val id: Long,
    val message: String,
    val displayTime: String,
    val displayImage: Bitmap?
) : MessageItem
