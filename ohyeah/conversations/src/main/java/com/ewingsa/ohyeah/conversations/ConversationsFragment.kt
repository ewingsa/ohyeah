package com.ewingsa.ohyeah.conversations

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.ewingsa.ohyeah.appinjection.Injectable
import com.ewingsa.ohyeah.conversations.databinding.FragmentConversationsBinding
import com.ewingsa.ohyeah.conversations.viewmodels.ConversationViewModel
import com.ewingsa.ohyeah.conversations.viewmodels.ConversationsScreenViewModel
import com.ewingsa.ohyeah.viper.BaseViperFragment

class ConversationsFragment :
    BaseViperFragment<ConversationsContract.Presenter, ConversationsContract.Router>(),
    ConversationsContract.View,
    Injectable {

    private var binding: FragmentConversationsBinding? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)

        binding = FragmentConversationsBinding.inflate(inflater, container, false).apply { lifecycleOwner = viewLifecycleOwner }

        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.conversationRecyclerView?.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
        }
    }

    override fun onDestroyView() {
        binding = null
        super.onDestroyView()
    }

    override fun setScreenViewModel(screenViewModel: ConversationsScreenViewModel) {
        binding?.setVariable(BR.viewModel, screenViewModel)
    }

    override fun addNoConversationsMessage() {
        binding?.conversationNoReminders?.visibility = View.VISIBLE
    }

    override fun addConversations(conversations: List<ConversationViewModel>) {
        binding?.conversationRecyclerView?.adapter = ConversationsAdapter(conversations)
    }

    companion object {
        @JvmStatic
        fun newInstance(): ConversationsFragment {
            return ConversationsFragment()
        }
    }
}
