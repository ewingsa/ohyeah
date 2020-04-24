package com.ewingsa.ohyeah.messages

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.LinearLayoutManager
import com.ewingsa.ohyeah.appinjection.Injectable
import com.ewingsa.ohyeah.messages.viewmodels.MessageViewModel
import com.ewingsa.ohyeah.messages.viewmodels.MessagesToolbarViewModel
import com.ewingsa.ohyeah.viper.BaseViperFragment
import kotlinx.android.synthetic.main.fragment_messages.message_recycler_view
import kotlinx.android.synthetic.main.fragment_messages.message_sender_text

class MessagesFragment : BaseViperFragment<MessagesContract.Presenter, MessagesContract.Router>(),
    MessagesContract.View, Injectable {

    private var senderId: Long? = null

    private var viewDataBinding: ViewDataBinding? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.fragment_messages, container, false).also {
            viewDataBinding = DataBindingUtil.bind(it)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        message_recycler_view.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context).apply { reverseLayout = true }
        }
    }

    override fun getSenderId() = senderId

    override fun setToolbarViewModel(toolbarViewModel: MessagesToolbarViewModel) {
        viewDataBinding?.setVariable(BR.viewModel, toolbarViewModel)
    }

    override fun setSenderName(title: String) {
        message_sender_text.text = title
    }

    override fun addMessages(messages: List<MessageViewModel>) {
        message_recycler_view.adapter = MessagesAdapter(messages)
    }

    override fun getMessageCount(): Int? {
        return message_recycler_view.adapter?.itemCount
    }

    override fun scrollToPosition(position: Int) {
        message_recycler_view.layoutManager?.scrollToPosition(position)
    }

    companion object {
        @JvmStatic
        fun newInstance(senderId: Long): MessagesFragment {
            return MessagesFragment().apply {
                this.senderId = senderId
            }
        }
    }
}
