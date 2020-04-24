package com.ewingsa.ohyeah.messages.viewmodels

import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import org.junit.Before
import org.junit.Test

class MessagesToolbarViewModelTest {

    private val interactions: MessagesToolbarViewModel.Interactions = mock()

    private lateinit var messagesToolbarViewModel: MessagesToolbarViewModel

    @Before
    fun setup() {
        messagesToolbarViewModel = MessagesToolbarViewModel(interactions)
    }

    @Test
    fun testOnBackPress() {
        messagesToolbarViewModel.onBackPress.onClick(mock())

        verify(interactions).onBackPress()
    }

    @Test
    fun testOnNewPress() {
        messagesToolbarViewModel.onNewPress.onClick(mock())

        verify(interactions).onNewPress()
    }
}
