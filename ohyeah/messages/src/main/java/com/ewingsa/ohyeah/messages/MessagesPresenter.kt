package com.ewingsa.ohyeah.messages

import android.content.Context
import com.ewingsa.ohyeah.messages.datamodels.MessageItem
import com.ewingsa.ohyeah.messages.viewmodels.MessageViewModel
import com.ewingsa.ohyeah.messages.viewmodels.MessagesToolbarViewModel
import com.ewingsa.ohyeah.viper.ViperContract
import javax.inject.Inject

class MessagesPresenter @Inject constructor(
    private val interactor: MessagesContract.Interactor
) : MessagesContract.Presenter, MessagesToolbarViewModel.Interactions, MessageViewModel.Interactions {

    private var view: MessagesContract.View? = null

    init {
        interactor.setPresenter(this)
    }

    override fun onViewAttached(view: ViperContract.View, router: ViperContract.Router?) {
        this.view = view as? MessagesContract.View?

        interactor.onRouterAttached(router)

        this.view?.setToolbarViewModel(MessagesToolbarViewModel(this))

        interactor.fetchMessages(this.view?.getSenderId())
    }

    override fun getContext(): Context? {
        return view?.getContext()
    }

    override fun setSenderName(name: String) {
        view?.setSenderName(name)
    }

    override fun addMessages(messages: List<MessageItem>) {
        val viewModels = messages.map { MessageViewModel(it, this) }
        view?.addMessages(viewModels)
    }

    override fun scrollToPosition(position: Int) {
        view?.getMessageCount()?.let {
            if (position < it) {
                view?.scrollToPosition(position)
            }
        }
    }

    override fun onBackPress() {
        interactor.goBack()
    }

    override fun onNewPress() {
        view?.getSenderId()?.let {
            interactor.onNewMessage(it)
        }
    }

    override fun onEditMessageRequested(messageId: Long) {
        interactor.onEditMessage(messageId)
    }

    override fun onViewDetached() {
        view = null

        interactor.clear()
    }
}
