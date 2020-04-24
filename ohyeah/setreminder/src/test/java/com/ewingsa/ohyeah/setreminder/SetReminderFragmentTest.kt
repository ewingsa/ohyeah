package com.ewingsa.ohyeah.setreminder

import android.content.Intent
import android.net.Uri
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class SetReminderFragmentTest {

    private lateinit var setReminderFragment: SetReminderFragment

    @Before
    fun setup() {
        setReminderFragment = SetReminderFragment()
    }

    @Test
    fun testGetSenderId() {
        setReminderFragment = SetReminderFragment.newInstance(SENDER_ID, null)

        assertEquals(SENDER_ID, setReminderFragment.getSenderId())
    }

    @Test
    fun testGetMessageId() {
        setReminderFragment = SetReminderFragment.newInstance(null, MESSAGE_ID)

        assertEquals(MESSAGE_ID, setReminderFragment.getMessageId())
    }

    @Test
    fun testOnActivityResult() {
        val picturePickerCallback: (Uri) -> Unit = mock()
        val data: Intent = mock()
        val uri: Uri = mock()

        whenever(data.data).thenReturn(uri)

        setReminderFragment.setPicturePickerCallback(picturePickerCallback)

        setReminderFragment.onActivityResult(PICK_IMAGE, RESULT_CODE, data)

        verify(picturePickerCallback).invoke(uri)
    }

    private companion object {
        const val MESSAGE_ID = 1L
        const val PICK_IMAGE = 2
        const val RESULT_CODE = 0
        const val SENDER_ID = 2L
    }
}
