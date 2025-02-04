package com.ewingsa.ohyeah.messages.viewmodels

import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify

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
