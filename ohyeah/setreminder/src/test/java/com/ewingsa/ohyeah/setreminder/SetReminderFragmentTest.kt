package com.ewingsa.ohyeah.setreminder

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

    private companion object {
        const val MESSAGE_ID = 1L
        const val SENDER_ID = 2L
    }
}
