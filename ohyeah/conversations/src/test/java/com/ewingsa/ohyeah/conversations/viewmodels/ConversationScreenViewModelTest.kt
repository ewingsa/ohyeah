package com.ewingsa.ohyeah.conversations.viewmodels

import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify

class ConversationScreenViewModelTest {

    private val interactions: ConversationsScreenViewModel.Interactions = mock()

    private lateinit var conversationsScreenViewModel: ConversationsScreenViewModel

    @Before
    fun setup() {
        conversationsScreenViewModel = ConversationsScreenViewModel(interactions)
    }

    @Test
    fun testOnInfoPress() {
        conversationsScreenViewModel.onInfoPress.onClick(mock())

        verify(interactions).onInfoPress()
    }

    @Test
    fun testOnNewPress() {
        conversationsScreenViewModel.onNewPress.onClick(mock())

        verify(interactions).onNewPress()
    }
}
