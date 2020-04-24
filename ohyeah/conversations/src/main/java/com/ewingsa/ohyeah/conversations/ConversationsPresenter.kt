package com.ewingsa.ohyeah.conversations

import android.R.string.no
import android.R.string.yes
import android.content.Context
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.ewingsa.ohyeah.conversations.datamodels.ConversationItem
import com.ewingsa.ohyeah.conversations.viewmodels.ConversationViewModel
import com.ewingsa.ohyeah.conversations.viewmodels.ConversationsScreenViewModel
import com.ewingsa.ohyeah.viper.ViperContract
import javax.inject.Inject

class ConversationsPresenter @Inject constructor(
    private val interactor: ConversationsContract.Interactor
) : ConversationsContract.Presenter, ConversationsScreenViewModel.Interactions, ConversationViewModel.Interactions {

    private var view: ConversationsContract.View? = null

    init {
        interactor.setPresenter(this)
    }

    override fun onViewAttached(view: ViperContract.View, router: ViperContract.Router?) {
        this.view = view as? ConversationsContract.View

        interactor.onRouterAttached(router)

        this.view?.setScreenViewModel(ConversationsScreenViewModel(this))

        interactor.fetchConversations()
    }

    override fun addNoConversationsMessage() {
        view?.addNoConversationsMessage()
    }

    override fun getContext(): Context? {
        return view?.getContext()
    }

    override fun addConversations(conversations: List<ConversationItem>) {
        val viewModels = conversations.map { ConversationViewModel(it, this) }
        view?.addConversations(viewModels)
    }

    override fun onInfoPress() {
        interactor.goToInfo()
    }

    override fun onNewPress() {
        interactor.goToSetReminder()
    }

    override fun onConversationSelected(senderId: Long) {
        interactor.goToConversation(senderId)
    }

    override fun onDeleteConversationRequested(senderId: Long) {
        getContext()?.let {
            AlertDialog.Builder(it)
                .setTitle(it.getString(R.string.conversation_delete_topic))
                .setMessage(it.getString(R.string.conversation_delete_topic_message))
                .setIcon(R.drawable.ic_ohyeah)
                .setPositiveButton(yes) { _, whichButton ->
                    if (whichButton == -1) {
                        interactor.deleteConversation(senderId)
                        Toast.makeText(it, it.getString(R.string.conversation_deleted), Toast.LENGTH_SHORT).show()
                    }
                }
                .setNegativeButton(no, null).show()
        }
    }

    override fun onViewDetached() {
        view = null

        interactor.clear()
    }
}
