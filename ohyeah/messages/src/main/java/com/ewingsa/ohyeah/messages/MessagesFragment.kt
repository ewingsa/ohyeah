package com.ewingsa.ohyeah.messages

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.ewingsa.ohyeah.appinjection.Injectable
import com.ewingsa.ohyeah.messages.databinding.FragmentMessagesBinding
import com.ewingsa.ohyeah.messages.viewmodels.MessageViewModel
import com.ewingsa.ohyeah.messages.viewmodels.MessagesToolbarViewModel
import com.ewingsa.ohyeah.viper.BaseViperFragment

class MessagesFragment :
    BaseViperFragment<MessagesContract.Presenter, MessagesContract.Router>(),
    MessagesContract.View,
    Injectable {

    private var senderId: Long? = null

    private var binding: FragmentMessagesBinding? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)

        binding = FragmentMessagesBinding.inflate(inflater, container, false).apply { lifecycleOwner = viewLifecycleOwner }

        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.messageRecyclerView?.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context).apply { reverseLayout = true }
        }
    }

    override fun onDestroyView() {
        binding = null
        super.onDestroyView()
    }

    override fun getSenderId() = senderId

    override fun setToolbarViewModel(toolbarViewModel: MessagesToolbarViewModel) {
        binding?.setVariable(BR.viewModel, toolbarViewModel)
    }

    override fun setSenderName(title: String) {
        binding?.messageSenderText?.text = title
    }

    override fun addMessages(messages: List<MessageViewModel>) {
        binding?.messageRecyclerView?.adapter = MessagesAdapter(messages)
    }

    override fun getMessageCount(): Int? {
        return binding?.messageRecyclerView?.adapter?.itemCount
    }

    override fun scrollToPosition(position: Int) {
        binding?.messageRecyclerView?.layoutManager?.scrollToPosition(position)
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
