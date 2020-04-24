package com.ewingsa.ohyeah.conversations

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.LinearLayoutManager
import com.ewingsa.ohyeah.appinjection.Injectable
import com.ewingsa.ohyeah.conversations.viewmodels.ConversationViewModel
import com.ewingsa.ohyeah.conversations.viewmodels.ConversationsScreenViewModel
import com.ewingsa.ohyeah.viper.BaseViperFragment
import kotlinx.android.synthetic.main.fragment_conversations.conversation_no_reminders
import kotlinx.android.synthetic.main.fragment_conversations.conversation_recycler_view

class ConversationsFragment : BaseViperFragment<ConversationsContract.Presenter, ConversationsContract.Router>(),
    ConversationsContract.View, Injectable {

    private var viewDataBinding: ViewDataBinding? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.fragment_conversations, container, false).also {
            viewDataBinding = DataBindingUtil.bind(it)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        conversation_recycler_view.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
        }
    }

    override fun setScreenViewModel(screenViewModel: ConversationsScreenViewModel) {
        viewDataBinding?.setVariable(BR.viewModel, screenViewModel)
    }

    override fun addNoConversationsMessage() {
        conversation_no_reminders.visibility = View.VISIBLE
    }

    override fun addConversations(conversations: List<ConversationViewModel>) {
        conversation_recycler_view.adapter = ConversationsAdapter(conversations)
    }

    companion object {
        @JvmStatic
        fun newInstance(): ConversationsFragment {
            return ConversationsFragment()
        }
    }
}
