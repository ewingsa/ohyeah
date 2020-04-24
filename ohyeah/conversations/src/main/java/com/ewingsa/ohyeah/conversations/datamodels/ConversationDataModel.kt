package com.ewingsa.ohyeah.conversations.datamodels

import android.graphics.Bitmap

class ConversationDataModel(
    val senderId: Long,
    val senderName: String,
    val previewText: String,
    val displayTime: String,
    val displayImage: Bitmap?,
    val numberUnread: String?
) : ConversationItem
