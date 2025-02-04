package com.ewingsa.ohyeah.setreminder

class ReminderDataModel(
    var message: String,
    var sender: String,
    var messageId: Long? = null,
    var senderId: Long? = null,
    var year: Int = -1,
    var month: Int = -1,
    var dayOfMonth: Int = -1,
    var hour: Int = -1, // Saved as 24 hour clock
    var minute: Int = -1,
    var amPm: Int = -1,
    var senderPicture: String? = null
)
